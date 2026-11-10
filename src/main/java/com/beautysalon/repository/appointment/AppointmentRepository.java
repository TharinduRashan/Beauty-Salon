package com.beautysalon.repository.appointment;

// Import the Appointment model class.
import com.beautysalon.model.appointment.Appointment;

// Import the appointment status enum.
import com.beautysalon.model.appointment.AppointmentStatus;

// Import utility classes for date and file handling.
import com.beautysalon.util.DateTimeUtil;
import com.beautysalon.util.FileStorageUtil;
import com.beautysalon.util.IdGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

// This class handles appointment data storage and retrieval.
@Repository
public class AppointmentRepository {

    // Store the directory path where appointment data is saved.
    private final Path dataDir;

    // Object used to generate unique appointment IDs.
    private final IdGenerator idGenerator;

    // Define the number of fields in an appointment record.
    private static final int FIELDS = 15;

    // Constructor injection to initialize the data directory and ID generator.
    @Autowired
    public AppointmentRepository(Path dataDirectory, IdGenerator idGenerator) {
        this.dataDir = dataDirectory;
        this.idGenerator = idGenerator;
    }

    // Get the file path of the appointments text file.
    private Path file() { return dataDir.resolve("appointments.txt"); }

    // Convert an array of strings into an Appointment object.
    private Appointment fromParts(String[] p) {
        Appointment a = new Appointment();

        // Assign values from the file to the appointment object.
        a.setId(p[0]);
        a.setCustomerId(p[1]);
        a.setStaffId(p[2]);
        a.setServiceId(p[3]);

        // Convert an empty package ID into null.
        a.setPackageId(p[4].isBlank() ? null : p[4]);

        // Set the service name and price details.
        a.setServiceNameSnapshot(p[5]);
        a.setPriceSnapshot(p[6].isBlank() ? BigDecimal.ZERO : new BigDecimal(p[6]));

        // Convert the duration string into an integer.
        a.setDurationMinutes(p[7].isBlank() ? 0 : Integer.parseInt(p[7]));

        // Convert date and time strings into Java date and time objects.
        a.setAppointmentDate(DateTimeUtil.parseDate(p[8]));
        a.setStartTime(DateTimeUtil.parseTime(p[9]));
        a.setEndTime(DateTimeUtil.parseTime(p[10]));

        // Convert the status string into an AppointmentStatus value.
        a.setStatus(AppointmentStatus.fromString(p[11]));

        // Convert empty notes into null.
        a.setNotes(p[12].isBlank() ? null : p[12]);

        // Set the appointment creation and update dates.
        a.setCreatedAt(DateTimeUtil.parseDateTime(p[13]));
        a.setUpdatedAt(DateTimeUtil.parseDateTime(p[14]));

        return a;
    }

    // Convert an Appointment object into a single text line.
    private String toLine(Appointment a) {
        return FileStorageUtil.joinLine(

            // Convert appointment values into strings before saving.
            a.getId() != null ? a.getId() : "",
            a.getCustomerId() != null ? a.getCustomerId() : "",
            a.getStaffId() != null ? a.getStaffId() : "",
            a.getServiceId() != null ? a.getServiceId() : "",
            a.getPackageId() != null ? a.getPackageId() : "",
            a.getServiceNameSnapshot() != null ? a.getServiceNameSnapshot() : "",
            a.getPriceSnapshot() != null ? a.getPriceSnapshot().toPlainString() : "0.00",
            String.valueOf(a.getDurationMinutes()),

            // Format date and time values before saving.
            DateTimeUtil.formatDate(a.getAppointmentDate()),
            DateTimeUtil.formatTime(a.getStartTime()),
            DateTimeUtil.formatTime(a.getEndTime()),

            // Save the appointment status.
            a.getStatus() != null ? a.getStatus().name() : AppointmentStatus.PENDING.name(),

            // Save notes and date information.
            a.getNotes() != null ? a.getNotes() : "",
            DateTimeUtil.formatDateTime(a.getCreatedAt()),
            DateTimeUtil.formatDateTime(a.getUpdatedAt())
        );
    }

    // Get all appointments from the text file.
    public List<Appointment> findAll() {
        return FileStorageUtil.readAll(file(), this::fromParts, FIELDS);
    }

    // Find an appointment using its unique ID.
    public Optional<Appointment> findById(String id) {
        return findAll().stream().filter(a -> a.getId().equals(id)).findFirst();
    }

    // Find all appointments belonging to a specific customer.
    public List<Appointment> findByCustomerId(String customerId) {
        return findAll().stream().filter(a -> customerId.equals(a.getCustomerId())).toList();
    }

    // Find all appointments assigned to a specific staff member.
    public List<Appointment> findByStaffId(String staffId) {
        return findAll().stream().filter(a -> staffId.equals(a.getStaffId())).toList();
    }

    // Find appointments assigned to a staff member on a specific date.
    public List<Appointment> findByStaffIdAndDate(String staffId, LocalDate date) {
        return findAll().stream()
                .filter(a -> staffId.equals(a.getStaffId())
                        && date != null && date.equals(a.getAppointmentDate())

                        // Ignore cancelled appointments.
                        && a.getStatus() != AppointmentStatus.CANCELLED)
                .toList();
    }

    // Save a new appointment to the text file.
    public Appointment save(Appointment a) throws IOException {

        // Generate a new ID if the appointment does not have an ID.
        if (a.getId() == null || a.getId().isBlank()) a.setId(idGenerator.generate());

        // Set the creation date if it is not already available.
        if (a.getCreatedAt() == null) a.setCreatedAt(DateTimeUtil.now());

        // Set the latest update date and time.
        a.setUpdatedAt(DateTimeUtil.now());

        // Convert the appointment into a text line and append it to the file.
        FileStorageUtil.appendLine(file(), toLine(a));

        return a;
    }

    // Update an existing appointment in the text file.
    public Appointment update(Appointment a) throws IOException {

        // Update the last modified date and time.
        a.setUpdatedAt(DateTimeUtil.now());

        // Convert the updated appointment into a text line.
        String updatedLine = toLine(a);

        // Find the matching appointment record and replace its old data.
        FileStorageUtil.updateLines(file(), line -> {
            String[] parts = FileStorageUtil.splitLine(line);

            // Compare appointment IDs to find the correct record.
            if (parts.length > 0 && a.getId().equals(parts[0])) return updatedLine;
            return line;
        });
        return a;
    }

    // Delete an appointment from the text file using its ID.
    public void deleteById(String id) throws IOException {
        FileStorageUtil.deleteById(file(), id);
    }
}
