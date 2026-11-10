package com.beautysalon.service.appointment;

// Import custom exception classes.
import com.beautysalon.exception.ConflictException;
import com.beautysalon.exception.NotFoundException;
import com.beautysalon.exception.ValidationException;

// Import model classes.
import com.beautysalon.model.appointment.Appointment;
import com.beautysalon.model.appointment.AppointmentStatus;
import com.beautysalon.model.catalogue.SalonService;
import com.beautysalon.model.catalogue.ServicePackage;
import com.beautysalon.model.staff.StaffMember;
import com.beautysalon.model.staff.StaffSchedule;
import com.beautysalon.model.user.User;

// Import repository classes.
import com.beautysalon.repository.appointment.AppointmentRepository;
import com.beautysalon.repository.catalogue.PackageRepository;
import com.beautysalon.repository.catalogue.ServiceRepository;
import com.beautysalon.repository.staff.StaffRepository;
import com.beautysalon.repository.user.UserRepository;

// Import staff service and date-time utility.
import com.beautysalon.service.staff.StaffService;
import com.beautysalon.util.DateTimeUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

// This class handles the main business logic of appointments.
@Service
public class AppointmentService {

    // Repository objects used to access different data.
    private final AppointmentRepository appointmentRepository;
    private final ServiceRepository serviceRepository;
    private final StaffRepository staffRepository;
    private final UserRepository userRepository;
    private final StaffService staffService;
    private final PackageRepository packageRepository;

    // Get the minimum cancellation time from application properties.
    @Value("${salon.cancellation-hours-before:24}")
    private int cancellationHoursBefore = 24;

    // Get the maximum number of days allowed for advance booking.
    @Value("${salon.advance-booking-days:90}")
    private int advanceBookingDays = 90;

    // Get the salon closing time from application properties.
    @Value("${salon.closing-time:20:00}")
    private String closingTimeStr = "20:00";

    // Constructor injection to initialize all required dependencies.
    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository,
                               ServiceRepository serviceRepository,
                               StaffRepository staffRepository,
                               UserRepository userRepository,
                               StaffService staffService,
                               @Autowired(required = false) PackageRepository packageRepository) {
        this.appointmentRepository = appointmentRepository;
        this.serviceRepository = serviceRepository;
        this.staffRepository = staffRepository;
        this.userRepository = userRepository;
        this.staffService = staffService;
        this.packageRepository = packageRepository;
    }

    // Alternative constructor without PackageRepository.
    public AppointmentService(AppointmentRepository appointmentRepository,
                              ServiceRepository serviceRepository,
                              StaffRepository staffRepository,
                              UserRepository userRepository,
                              StaffService staffService) {
        this(appointmentRepository, serviceRepository, staffRepository, userRepository, staffService, null);
    }

    // Get all appointments.
    public List<Appointment> findAll() { return appointmentRepository.findAll(); }

    // Find an appointment using its ID.
    public Optional<Appointment> findById(String id) { return appointmentRepository.findById(id); }

    // Find appointments belonging to a specific customer.
    public List<Appointment> findByCustomerId(String customerId) {
        List<Appointment> appointments = appointmentRepository.findByCustomerId(customerId);

        // Add customer and staff names to appointment objects.
        enrichAppointments(appointments);
        return appointments;
    }

    // Get all appointments with additional customer and staff details.
    public List<Appointment> findAllEnriched() {
        List<Appointment> appointments = appointmentRepository.findAll();
        enrichAppointments(appointments);
        return appointments;
    }

    // Add customer names and staff names to appointment objects.
    private void enrichAppointments(List<Appointment> appointments) {
        Map<String, String> userNames = new HashMap<>();
        Map<String, String> staffNames = new HashMap<>();

        // Loop through all appointments.
        for (Appointment a : appointments) {

            // Find the customer name and store it in the map.
            String custName = userNames.computeIfAbsent(a.getCustomerId(),
                    id -> userRepository.findById(id).map(User::getFullName).orElse("Unknown"));

            // Find the staff name and store it in the map.
            String sName = staffNames.computeIfAbsent(a.getStaffId(),
                    id -> staffRepository.findById(id).map(StaffMember::getFullName).orElse("Unknown"));

            // Set customer and staff names in the appointment object.
            a.setCustomerName(custName);
            a.setStaffName(sName);
        }
    }

    // Book an appointment for a single salon service.
    public Appointment book(String customerId, String serviceId, String staffId,
                             LocalDate date, LocalTime startTime) throws IOException {

        // Check whether the customer exists.
        userRepository.findById(customerId).orElseThrow(() -> new NotFoundException("Customer", customerId));

        // Find the selected service.
        SalonService service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new NotFoundException("Service", serviceId));

        // Check whether the service is active.
        if (!service.isActive()) throw new ValidationException("Service is not currently available.");

        // Find the selected staff member.
        StaffMember staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new NotFoundException("Staff member", staffId));

        // Check whether the staff member is active.
        if (!staff.isActive()) throw new ValidationException("Staff member is not currently available.");

        // Get today's date.
        LocalDate today = DateTimeUtil.today();

        // Prevent booking appointments in the past.
        if (date.isBefore(today)) throw new ValidationException("Appointment date cannot be in the past.");

        // Check the maximum advance booking period.
        if (date.isAfter(today.plusDays(advanceBookingDays)))
            throw new ValidationException("Cannot book more than " + advanceBookingDays + " days in advance.");

        // Calculate the appointment ending time using service duration.
        LocalTime endTime = startTime.plusMinutes(service.getDurationMinutes());

        // Get the salon closing time.
        LocalTime closingTime = DateTimeUtil.parseTime(closingTimeStr);

        // Prevent appointments from ending after salon closing time.
        if (closingTime != null && endTime.isAfter(closingTime))
            throw new ValidationException("Appointment would end after salon closing time.");

        // Check whether the staff member is available at the selected time.
        if (!staffService.isAvailable(staffId, date, startTime, endTime))
            throw new ConflictException("Staff member is not available at the selected time.");

        // Check whether the selected time overlaps with another appointment.
        checkOverlap(staffId, date, startTime, endTime, null);

        // Create a new appointment object.
        Appointment appt = new Appointment();

        // Set appointment details.
        appt.setCustomerId(customerId);
        appt.setStaffId(staffId);
        appt.setServiceId(serviceId);
        appt.setServiceNameSnapshot(service.getName());
        appt.setPriceSnapshot(service.getPrice());
        appt.setDurationMinutes(service.getDurationMinutes());
        appt.setAppointmentDate(date);
        appt.setStartTime(startTime);
        appt.setEndTime(endTime);

        // Set the appointment status as CONFIRMED.
        appt.setStatus(AppointmentStatus.CONFIRMED);

        // Save the appointment in the repository.
        return appointmentRepository.save(appt);
    }

    // Book an appointment for a selected package.
    public Appointment bookPackage(String customerId, String packageId, String staffId,
                                   LocalDate date, LocalTime startTime) throws IOException {

        // Check whether the customer exists.
        userRepository.findById(customerId).orElseThrow(() -> new NotFoundException("Customer", customerId));

        // Check whether the package repository is available.
        if (packageRepository == null) throw new IllegalStateException("Package repository not configured.");

        // Find the selected package.
        ServicePackage pkg = packageRepository.findById(packageId)
                .orElseThrow(() -> new NotFoundException("Package", packageId));

        // Check whether the package is active.
        if (!pkg.isActive()) throw new ValidationException("Package is not currently available.");

        // Find the selected staff member.
        StaffMember staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new NotFoundException("Staff member", staffId));

        // Check whether the staff member is active.
        if (!staff.isActive()) throw new ValidationException("Staff member is not currently available.");

        // Get today's date.
        LocalDate today = DateTimeUtil.today();

        // Prevent booking appointments in the past.
        if (date.isBefore(today)) throw new ValidationException("Appointment date cannot be in the past.");

        // Check the maximum advance booking period.
        if (date.isAfter(today.plusDays(advanceBookingDays)))
            throw new ValidationException("Cannot book more than " + advanceBookingDays + " days in advance.");

        // Calculate the total duration of all services in the package.
        int totalDuration = 0;
        for (String svcId : pkg.getServiceIds()) {

            // Find each service and add its duration.
            var svcOpt = serviceRepository.findById(svcId);
            if (svcOpt.isPresent()) {
                totalDuration += svcOpt.get().getDurationMinutes();
            }
        }

        // Use 60 minutes as the default duration if no duration is found.
        if (totalDuration == 0) totalDuration = 60; // Default minimum

        // Calculate the appointment ending time.
        LocalTime endTime = startTime.plusMinutes(totalDuration);

        // Get the salon closing time.
        LocalTime closingTime = DateTimeUtil.parseTime(closingTimeStr);

        // Check whether the package appointment ends after closing time.
        if (closingTime != null && endTime.isAfter(closingTime))
            throw new ValidationException("Package appointment would end after salon closing time.");

        // Check whether the staff member is available.
        if (!staffService.isAvailable(staffId, date, startTime, endTime))
            throw new ConflictException("Staff member is not available at the selected time.");

        // Check whether the selected time overlaps with another appointment.
        checkOverlap(staffId, date, startTime, endTime, null);

        // Create a new appointment object.
        Appointment appt = new Appointment();

        // Set appointment details for the selected package.
        appt.setCustomerId(customerId);
        appt.setStaffId(staffId);
        appt.setPackageId(packageId);
        appt.setServiceId(pkg.getServiceIds().isEmpty() ? "" : pkg.getServiceIds().get(0));
        appt.setServiceNameSnapshot("Package: " + pkg.getName());
        appt.setPriceSnapshot(pkg.getTotalPrice());
        appt.setDurationMinutes(totalDuration);
        appt.setAppointmentDate(date);
        appt.setStartTime(startTime);
        appt.setEndTime(endTime);

        // Set the appointment status as CONFIRMED.
        appt.setStatus(AppointmentStatus.CONFIRMED);

        // Save the package appointment.
        return appointmentRepository.save(appt);
    }

    // Check whether a new appointment overlaps with existing appointments.
    private void checkOverlap(String staffId, LocalDate date, LocalTime start, LocalTime end, String excludeId) {

        // Get all appointments for the selected staff member and date.
        List<Appointment> existing = appointmentRepository.findByStaffIdAndDate(staffId, date);

        // Check each existing appointment.
        for (Appointment a : existing) {

            // Skip the appointment with the given ID if necessary.
            if (excludeId != null && excludeId.equals(a.getId())) continue;

            // Check whether the new appointment overlaps with an existing appointment.
            if (start.isBefore(a.getEndTime()) && end.isAfter(a.getStartTime())) {
                throw new ConflictException("Time slot conflicts with an existing appointment from "
                        + a.getFormattedStartTime() + " to " + a.getFormattedEndTime() + ".");
            }
        }
    }

    // Cancel an appointment.
    public Appointment cancel(String appointmentId, String requestingUserId, boolean isAdmin) throws IOException {

        // Find the appointment using its ID.
        Appointment appt = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Appointment", appointmentId));

        // Check whether the customer is allowed to cancel this appointment.
        if (!isAdmin && !appt.getCustomerId().equals(requestingUserId))
            throw new ValidationException("You can only cancel your own appointments.");

        // Prevent cancellation of completed appointments.
        if (appt.getStatus() == AppointmentStatus.COMPLETED)
            throw new ValidationException("Completed appointments cannot be cancelled.");

        // Prevent cancellation if the appointment is already cancelled.
        if (appt.getStatus() == AppointmentStatus.CANCELLED)
            throw new ValidationException("Appointment is already cancelled.");

        // Apply cancellation time rules only to customers, not admins.
        if (!isAdmin) {

            // Combine appointment date and starting time.
            LocalDateTime apptDateTime = LocalDateTime.of(appt.getAppointmentDate(), appt.getStartTime());

            // Calculate the number of hours remaining until the appointment.
            long hoursUntil = ChronoUnit.HOURS.between(DateTimeUtil.now(), apptDateTime);

            // Check whether the customer is cancelling before the required time limit.
            if (hoursUntil < cancellationHoursBefore)
                throw new ValidationException("Cancellations must be made at least " + cancellationHoursBefore + " hours before the appointment.");
        }

        // Change the appointment status to CANCELLED.
        appt.setStatus(AppointmentStatus.CANCELLED);

        // Update the appointment in the repository.
        return appointmentRepository.update(appt);
    }

    // Update the status of an existing appointment.
    public Appointment updateStatus(String appointmentId, AppointmentStatus newStatus) throws IOException {

        // Find the appointment using its ID.
        Appointment appt = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Appointment", appointmentId));

        // Set the new appointment status.
        appt.setStatus(newStatus);

        // Save the updated appointment.
        return appointmentRepository.update(appt);
    }

    // Get available time slots for a staff member on a selected date.
    public List<LocalTime> getAvailableSlots(String staffId, LocalDate date, int durationMinutes) {

        // Create a list to store available time slots.
        List<LocalTime> slots = new ArrayList<>();

        // Find the regular working schedule for the selected day.
        StaffSchedule daySchedule = staffService.findRegularSchedule(staffId).stream()
                .filter(s -> date.getDayOfWeek().equals(s.getDayOfWeek()))
                .findFirst().orElse(null);

        // Return an empty list if no schedule is found.
        if (daySchedule == null) return slots;

        // Check whether the staff member is on leave on the selected date.
        boolean onLeave = staffService.findLeave(staffId).stream().anyMatch(leave -> {

            // Ignore leave records without a start date.
            if (leave.getLeaveDate() == null) return false;

            // Get the leave ending date.
            LocalDate leaveEnd = leave.getLeaveDateEnd() != null ? leave.getLeaveDateEnd() : leave.getLeaveDate();

            // Check whether the selected date falls within the leave period.
            return !date.isBefore(leave.getLeaveDate()) && !date.isAfter(leaveEnd);
        });

        // Return an empty list if the staff member is on leave.
        if (onLeave) return slots;

        // Get existing appointments for this staff member and date.
        List<Appointment> existing = appointmentRepository.findByStaffIdAndDate(staffId, date);

        // Get the working start time and end time.
        LocalTime current = daySchedule.getStartTime();
        LocalTime workEnd = daySchedule.getEndTime();

        // Get the salon closing time.
        LocalTime closingTime = DateTimeUtil.parseTime(closingTimeStr);

        // Make sure the working end time does not exceed salon closing time.
        if (closingTime != null && workEnd.isAfter(closingTime)) workEnd = closingTime;

        // Generate time slots while the appointment can finish within working hours.
        while (!current.plusMinutes(durationMinutes).isAfter(workEnd)) {

            // Calculate the ending time of the current slot.
            LocalTime slotEnd = current.plusMinutes(durationMinutes);

            // Check whether the current slot overlaps with the staff break time.
            boolean inBreak = daySchedule.getBreakStart() != null && daySchedule.getBreakEnd() != null
                    && current.isBefore(daySchedule.getBreakEnd()) && slotEnd.isAfter(daySchedule.getBreakStart());

            // Continue checking only if the slot is not inside a break.
            if (!inBreak) {

                // Store the current slot start and end times.
                LocalTime fc = current;
                LocalTime fs = slotEnd;

                // Check whether the slot overlaps with an existing appointment.
                boolean overlap = existing.stream().anyMatch(a -> fc.isBefore(a.getEndTime()) && fs.isAfter(a.getStartTime()));

                // Add the slot only if there is no overlapping appointment.
                if (!overlap) slots.add(current);
            }

            // Move to the next time slot by 15 minutes.
            current = current.plusMinutes(15);
        }

        // Return the list of available time slots.
        return slots;
    }
}
