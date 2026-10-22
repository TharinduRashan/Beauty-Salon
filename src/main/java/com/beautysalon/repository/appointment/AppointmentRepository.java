package com.beautysalon.repository.appointment;

import com.beautysalon.model.appointment.Appointment;
import com.beautysalon.model.appointment.AppointmentStatus;
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

@Repository
public class AppointmentRepository {
    private final Path dataDir;
    private final IdGenerator idGenerator;
    private static final int FIELDS = 15;

    @Autowired
    public AppointmentRepository(Path dataDirectory, IdGenerator idGenerator) {
        this.dataDir = dataDirectory;
        this.idGenerator = idGenerator;
    }

    private Path file() { return dataDir.resolve("appointments.txt"); }

    private Appointment fromParts(String[] p) {
        Appointment a = new Appointment();
        a.setId(p[0]);
        a.setCustomerId(p[1]);
        a.setStaffId(p[2]);
        a.setServiceId(p[3]);
        a.setPackageId(p[4].isBlank() ? null : p[4]);
        a.setServiceNameSnapshot(p[5]);
        a.setPriceSnapshot(p[6].isBlank() ? BigDecimal.ZERO : new BigDecimal(p[6]));
        a.setDurationMinutes(p[7].isBlank() ? 0 : Integer.parseInt(p[7]));
        a.setAppointmentDate(DateTimeUtil.parseDate(p[8]));
        a.setStartTime(DateTimeUtil.parseTime(p[9]));
        a.setEndTime(DateTimeUtil.parseTime(p[10]));
        a.setStatus(AppointmentStatus.fromString(p[11]));
        a.setNotes(p[12].isBlank() ? null : p[12]);
        a.setCreatedAt(DateTimeUtil.parseDateTime(p[13]));
        a.setUpdatedAt(DateTimeUtil.parseDateTime(p[14]));
        return a;
    }

    private String toLine(Appointment a) {
        return FileStorageUtil.joinLine(
            a.getId() != null ? a.getId() : "",
            a.getCustomerId() != null ? a.getCustomerId() : "",
            a.getStaffId() != null ? a.getStaffId() : "",
            a.getServiceId() != null ? a.getServiceId() : "",
            a.getPackageId() != null ? a.getPackageId() : "",
            a.getServiceNameSnapshot() != null ? a.getServiceNameSnapshot() : "",
            a.getPriceSnapshot() != null ? a.getPriceSnapshot().toPlainString() : "0.00",
            String.valueOf(a.getDurationMinutes()),
            DateTimeUtil.formatDate(a.getAppointmentDate()),
            DateTimeUtil.formatTime(a.getStartTime()),
            DateTimeUtil.formatTime(a.getEndTime()),
            a.getStatus() != null ? a.getStatus().name() : AppointmentStatus.PENDING.name(),
            a.getNotes() != null ? a.getNotes() : "",
            DateTimeUtil.formatDateTime(a.getCreatedAt()),
            DateTimeUtil.formatDateTime(a.getUpdatedAt())
        );
    }

    public List<Appointment> findAll() {
        return FileStorageUtil.readAll(file(), this::fromParts, FIELDS);
    }

    public Optional<Appointment> findById(String id) {
        return findAll().stream().filter(a -> a.getId().equals(id)).findFirst();
    }

    public List<Appointment> findByCustomerId(String customerId) {
        return findAll().stream().filter(a -> customerId.equals(a.getCustomerId())).toList();
    }

    public List<Appointment> findByStaffId(String staffId) {
        return findAll().stream().filter(a -> staffId.equals(a.getStaffId())).toList();
    }

    public List<Appointment> findByStaffIdAndDate(String staffId, LocalDate date) {
        return findAll().stream()
                .filter(a -> staffId.equals(a.getStaffId())
                        && date != null && date.equals(a.getAppointmentDate())
                        && a.getStatus() != AppointmentStatus.CANCELLED)
                .toList();
    }

    public Appointment save(Appointment a) throws IOException {
        if (a.getId() == null || a.getId().isBlank()) a.setId(idGenerator.generate());
        if (a.getCreatedAt() == null) a.setCreatedAt(DateTimeUtil.now());
        a.setUpdatedAt(DateTimeUtil.now());
        FileStorageUtil.appendLine(file(), toLine(a));
        return a;
    }

    public Appointment update(Appointment a) throws IOException {
        a.setUpdatedAt(DateTimeUtil.now());
        String updatedLine = toLine(a);
        FileStorageUtil.updateLines(file(), line -> {
            String[] parts = FileStorageUtil.splitLine(line);
            if (parts.length > 0 && a.getId().equals(parts[0])) return updatedLine;
            return line;
        });
        return a;
    }

    public void deleteById(String id) throws IOException {
        FileStorageUtil.deleteById(file(), id);
    }
}
