package com.beautysalon.service.appointment;

import com.beautysalon.exception.ConflictException;
import com.beautysalon.exception.NotFoundException;
import com.beautysalon.exception.ValidationException;
import com.beautysalon.model.appointment.Appointment;
import com.beautysalon.model.appointment.AppointmentStatus;
import com.beautysalon.model.catalogue.SalonService;
import com.beautysalon.model.catalogue.ServicePackage;
import com.beautysalon.model.staff.StaffMember;
import com.beautysalon.model.staff.StaffSchedule;
import com.beautysalon.model.user.User;
import com.beautysalon.repository.appointment.AppointmentRepository;
import com.beautysalon.repository.catalogue.PackageRepository;
import com.beautysalon.repository.catalogue.ServiceRepository;
import com.beautysalon.repository.staff.StaffRepository;
import com.beautysalon.repository.user.UserRepository;
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

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ServiceRepository serviceRepository;
    private final StaffRepository staffRepository;
    private final UserRepository userRepository;
    private final StaffService staffService;
    private final PackageRepository packageRepository;

    @Value("${salon.cancellation-hours-before:24}")
    private int cancellationHoursBefore = 24;

    @Value("${salon.advance-booking-days:90}")
    private int advanceBookingDays = 90;

    @Value("${salon.closing-time:20:00}")
    private String closingTimeStr = "20:00";

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

    public AppointmentService(AppointmentRepository appointmentRepository,
                              ServiceRepository serviceRepository,
                              StaffRepository staffRepository,
                              UserRepository userRepository,
                              StaffService staffService) {
        this(appointmentRepository, serviceRepository, staffRepository, userRepository, staffService, null);
    }

    public List<Appointment> findAll() { return appointmentRepository.findAll(); }

    public Optional<Appointment> findById(String id) { return appointmentRepository.findById(id); }

    public List<Appointment> findByCustomerId(String customerId) {
        List<Appointment> appointments = appointmentRepository.findByCustomerId(customerId);
        enrichAppointments(appointments);
        return appointments;
    }

    public List<Appointment> findAllEnriched() {
        List<Appointment> appointments = appointmentRepository.findAll();
        enrichAppointments(appointments);
        return appointments;
    }

    private void enrichAppointments(List<Appointment> appointments) {
        Map<String, String> userNames = new HashMap<>();
        Map<String, String> staffNames = new HashMap<>();
        for (Appointment a : appointments) {
            String custName = userNames.computeIfAbsent(a.getCustomerId(),
                    id -> userRepository.findById(id).map(User::getFullName).orElse("Unknown"));
            String sName = staffNames.computeIfAbsent(a.getStaffId(),
                    id -> staffRepository.findById(id).map(StaffMember::getFullName).orElse("Unknown"));
            a.setCustomerName(custName);
            a.setStaffName(sName);
        }
    }

    public Appointment book(String customerId, String serviceId, String staffId,
                             LocalDate date, LocalTime startTime) throws IOException {
        userRepository.findById(customerId).orElseThrow(() -> new NotFoundException("Customer", customerId));
        SalonService service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new NotFoundException("Service", serviceId));
        if (!service.isActive()) throw new ValidationException("Service is not currently available.");
        StaffMember staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new NotFoundException("Staff member", staffId));
        if (!staff.isActive()) throw new ValidationException("Staff member is not currently available.");

        LocalDate today = DateTimeUtil.today();
        if (date.isBefore(today)) throw new ValidationException("Appointment date cannot be in the past.");
        if (date.isAfter(today.plusDays(advanceBookingDays)))
            throw new ValidationException("Cannot book more than " + advanceBookingDays + " days in advance.");

        LocalTime endTime = startTime.plusMinutes(service.getDurationMinutes());
        LocalTime closingTime = DateTimeUtil.parseTime(closingTimeStr);
        if (closingTime != null && endTime.isAfter(closingTime))
            throw new ValidationException("Appointment would end after salon closing time.");

        if (!staffService.isAvailable(staffId, date, startTime, endTime))
            throw new ConflictException("Staff member is not available at the selected time.");

        checkOverlap(staffId, date, startTime, endTime, null);

        Appointment appt = new Appointment();
        appt.setCustomerId(customerId);
        appt.setStaffId(staffId);
        appt.setServiceId(serviceId);
        appt.setServiceNameSnapshot(service.getName());
        appt.setPriceSnapshot(service.getPrice());
        appt.setDurationMinutes(service.getDurationMinutes());
        appt.setAppointmentDate(date);
        appt.setStartTime(startTime);
        appt.setEndTime(endTime);
        appt.setStatus(AppointmentStatus.CONFIRMED);
        return appointmentRepository.save(appt);
    }

    public Appointment bookPackage(String customerId, String packageId, String staffId,
                                   LocalDate date, LocalTime startTime) throws IOException {
        userRepository.findById(customerId).orElseThrow(() -> new NotFoundException("Customer", customerId));
        if (packageRepository == null) throw new IllegalStateException("Package repository not configured.");
        ServicePackage pkg = packageRepository.findById(packageId)
                .orElseThrow(() -> new NotFoundException("Package", packageId));
        if (!pkg.isActive()) throw new ValidationException("Package is not currently available.");
        StaffMember staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new NotFoundException("Staff member", staffId));
        if (!staff.isActive()) throw new ValidationException("Staff member is not currently available.");

        LocalDate today = DateTimeUtil.today();
        if (date.isBefore(today)) throw new ValidationException("Appointment date cannot be in the past.");
        if (date.isAfter(today.plusDays(advanceBookingDays)))
            throw new ValidationException("Cannot book more than " + advanceBookingDays + " days in advance.");

        int totalDuration = 0;
        for (String svcId : pkg.getServiceIds()) {
            var svcOpt = serviceRepository.findById(svcId);
            if (svcOpt.isPresent()) {
                totalDuration += svcOpt.get().getDurationMinutes();
            }
        }
        if (totalDuration == 0) totalDuration = 60; // Default minimum

        LocalTime endTime = startTime.plusMinutes(totalDuration);
        LocalTime closingTime = DateTimeUtil.parseTime(closingTimeStr);
        if (closingTime != null && endTime.isAfter(closingTime))
            throw new ValidationException("Package appointment would end after salon closing time.");

        if (!staffService.isAvailable(staffId, date, startTime, endTime))
            throw new ConflictException("Staff member is not available at the selected time.");

        checkOverlap(staffId, date, startTime, endTime, null);

        Appointment appt = new Appointment();
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
        appt.setStatus(AppointmentStatus.CONFIRMED);
        return appointmentRepository.save(appt);
    }

    private void checkOverlap(String staffId, LocalDate date, LocalTime start, LocalTime end, String excludeId) {
        List<Appointment> existing = appointmentRepository.findByStaffIdAndDate(staffId, date);
        for (Appointment a : existing) {
            if (excludeId != null && excludeId.equals(a.getId())) continue;
            if (start.isBefore(a.getEndTime()) && end.isAfter(a.getStartTime())) {
                throw new ConflictException("Time slot conflicts with an existing appointment from "
                        + a.getFormattedStartTime() + " to " + a.getFormattedEndTime() + ".");
            }
        }
    }

    public Appointment cancel(String appointmentId, String requestingUserId, boolean isAdmin) throws IOException {
        Appointment appt = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Appointment", appointmentId));
        if (!isAdmin && !appt.getCustomerId().equals(requestingUserId))
            throw new ValidationException("You can only cancel your own appointments.");
        if (appt.getStatus() == AppointmentStatus.COMPLETED)
            throw new ValidationException("Completed appointments cannot be cancelled.");
        if (appt.getStatus() == AppointmentStatus.CANCELLED)
            throw new ValidationException("Appointment is already cancelled.");
        if (!isAdmin) {
            LocalDateTime apptDateTime = LocalDateTime.of(appt.getAppointmentDate(), appt.getStartTime());
            long hoursUntil = ChronoUnit.HOURS.between(DateTimeUtil.now(), apptDateTime);
            if (hoursUntil < cancellationHoursBefore)
                throw new ValidationException("Cancellations must be made at least " + cancellationHoursBefore + " hours before the appointment.");
        }
        appt.setStatus(AppointmentStatus.CANCELLED);
        return appointmentRepository.update(appt);
    }

    public Appointment updateStatus(String appointmentId, AppointmentStatus newStatus) throws IOException {
        Appointment appt = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Appointment", appointmentId));
        appt.setStatus(newStatus);
        return appointmentRepository.update(appt);
    }

    public List<LocalTime> getAvailableSlots(String staffId, LocalDate date, int durationMinutes) {
        List<LocalTime> slots = new ArrayList<>();
        StaffSchedule daySchedule = staffService.findRegularSchedule(staffId).stream()
                .filter(s -> date.getDayOfWeek().equals(s.getDayOfWeek()))
                .findFirst().orElse(null);
        if (daySchedule == null) return slots;
        boolean onLeave = staffService.findLeave(staffId).stream().anyMatch(leave -> {
            if (leave.getLeaveDate() == null) return false;
            LocalDate leaveEnd = leave.getLeaveDateEnd() != null ? leave.getLeaveDateEnd() : leave.getLeaveDate();
            return !date.isBefore(leave.getLeaveDate()) && !date.isAfter(leaveEnd);
        });
        if (onLeave) return slots;
        List<Appointment> existing = appointmentRepository.findByStaffIdAndDate(staffId, date);
        LocalTime current = daySchedule.getStartTime();
        LocalTime workEnd = daySchedule.getEndTime();
        LocalTime closingTime = DateTimeUtil.parseTime(closingTimeStr);
        if (closingTime != null && workEnd.isAfter(closingTime)) workEnd = closingTime;
        while (!current.plusMinutes(durationMinutes).isAfter(workEnd)) {
            LocalTime slotEnd = current.plusMinutes(durationMinutes);
            boolean inBreak = daySchedule.getBreakStart() != null && daySchedule.getBreakEnd() != null
                    && current.isBefore(daySchedule.getBreakEnd()) && slotEnd.isAfter(daySchedule.getBreakStart());
            if (!inBreak) {
                LocalTime fc = current;
                LocalTime fs = slotEnd;
                boolean overlap = existing.stream().anyMatch(a -> fc.isBefore(a.getEndTime()) && fs.isAfter(a.getStartTime()));
                if (!overlap) slots.add(current);
            }
            current = current.plusMinutes(15);
        }
        return slots;
    }
}
