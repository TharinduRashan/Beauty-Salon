package com.beautysalon.service.staff;

import com.beautysalon.exception.NotFoundException;
import com.beautysalon.exception.ValidationException;
import com.beautysalon.model.staff.*;
import com.beautysalon.repository.staff.ScheduleRepository;
import com.beautysalon.repository.staff.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class StaffService {

    private final StaffRepository staffRepository;
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public StaffService(StaffRepository staffRepository, ScheduleRepository scheduleRepository) {
        this.staffRepository = staffRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public List<StaffMember> findAll() { return staffRepository.findAll(); }
    public List<StaffMember> findAllActive() { return staffRepository.findAllActive(); }
    public Optional<StaffMember> findById(String id) { return staffRepository.findById(id); }
    public Optional<StaffMember> findByUserId(String userId) { return staffRepository.findByUserId(userId); }

    public StaffMember create(String fullName, String email, String phone,
                               List<String> specialties, String bio) throws IOException {
        validateStaff(fullName, email);
        StaffMember s = new StaffMember();
        s.setFullName(fullName);
        s.setEmail(email);
        s.setPhone(phone);
        s.setSpecialties(specialties);
        s.setBio(bio);
        s.setStatus(StaffStatus.ACTIVE);
        return staffRepository.save(s);
    }

    public StaffMember update(String id, String fullName, String email, String phone,
                               List<String> specialties, String bio) throws IOException {
        StaffMember s = staffRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Staff", id));
        validateStaff(fullName, email);
        s.setFullName(fullName);
        s.setEmail(email);
        s.setPhone(phone);
        s.setSpecialties(specialties);
        s.setBio(bio);
        return staffRepository.update(s);
    }

    public StaffMember toggleStatus(String id) throws IOException {
        StaffMember s = staffRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Staff", id));
        s.setStatus(s.isActive() ? StaffStatus.INACTIVE : StaffStatus.ACTIVE);
        return staffRepository.update(s);
    }

    public void delete(String id) throws IOException {
        staffRepository.findById(id).orElseThrow(() -> new NotFoundException("Staff", id));
        scheduleRepository.deleteByStaffId(id);
        staffRepository.deleteById(id);
    }

    private void validateStaff(String fullName, String email) {
        if (fullName == null || fullName.length() < 2)
            throw new ValidationException("Full name must be at least 2 characters.");
        if (email == null || !email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$"))
            throw new ValidationException("Invalid email.");
    }

    // ── Schedule Management ──

    public List<StaffSchedule> findScheduleByStaffId(String staffId) {
        return scheduleRepository.findByStaffId(staffId);
    }

    public List<StaffSchedule> findRegularSchedule(String staffId) {
        return scheduleRepository.findRegularScheduleByStaffId(staffId);
    }

    public List<StaffSchedule> findLeave(String staffId) {
        return scheduleRepository.findLeaveByStaffId(staffId);
    }

    public StaffSchedule addRegularSchedule(String staffId, DayOfWeek day, LocalTime start,
                                             LocalTime end, LocalTime breakStart,
                                             LocalTime breakEnd) throws IOException {
        staffRepository.findById(staffId)
                .orElseThrow(() -> new NotFoundException("Staff", staffId));
        if (start == null || end == null || !end.isAfter(start))
            throw new ValidationException("End time must be after start time.");
        StaffSchedule sched = new StaffSchedule();
        sched.setStaffId(staffId);
        sched.setDayOfWeek(day);
        sched.setStartTime(start);
        sched.setEndTime(end);
        sched.setBreakStart(breakStart);
        sched.setBreakEnd(breakEnd);
        sched.setType(ScheduleType.REGULAR);
        sched.setStatus(ScheduleStatus.ACTIVE);
        return scheduleRepository.save(sched);
    }

    public StaffSchedule addLeave(String staffId, LocalDate from, LocalDate to,
                                   String reason) throws IOException {
        staffRepository.findById(staffId)
                .orElseThrow(() -> new NotFoundException("Staff", staffId));
        if (from == null || to == null || to.isBefore(from))
            throw new ValidationException("Leave end date must be on or after start date.");
        StaffSchedule leave = new StaffSchedule();
        leave.setStaffId(staffId);
        leave.setLeaveDate(from);
        leave.setLeaveDateEnd(to);
        leave.setType(ScheduleType.LEAVE);
        leave.setStatus(ScheduleStatus.ACTIVE);
        return scheduleRepository.save(leave);
    }

    public void cancelSchedule(String scheduleId) throws IOException {
        StaffSchedule sched = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NotFoundException("Schedule", scheduleId));
        sched.setStatus(ScheduleStatus.CANCELLED);
        scheduleRepository.update(sched);
    }

    /**
     * Checks if a staff member is available for a given date and time range.
     * Returns true if the staff works that day, is within working hours,
     * not on break, and not on leave.
     */
    public boolean isAvailable(String staffId, LocalDate date, LocalTime start, LocalTime end) {
        DayOfWeek day = date.getDayOfWeek();
        List<StaffSchedule> regular = scheduleRepository.findRegularScheduleByStaffId(staffId);
        StaffSchedule daySchedule = regular.stream()
                .filter(s -> day.equals(s.getDayOfWeek()))
                .findFirst().orElse(null);
        if (daySchedule == null) return false;
        if (start.isBefore(daySchedule.getStartTime()) || end.isAfter(daySchedule.getEndTime())) return false;
        if (daySchedule.getBreakStart() != null && daySchedule.getBreakEnd() != null) {
            if (start.isBefore(daySchedule.getBreakEnd()) && end.isAfter(daySchedule.getBreakStart())) return false;
        }
        List<StaffSchedule> leaves = scheduleRepository.findLeaveByStaffId(staffId);
        for (StaffSchedule leave : leaves) {
            if (leave.getLeaveDate() == null) continue;
            LocalDate leaveEnd = leave.getLeaveDateEnd() != null ? leave.getLeaveDateEnd() : leave.getLeaveDate();
            if (!date.isBefore(leave.getLeaveDate()) && !date.isAfter(leaveEnd)) return false;
        }
        return true;
    }

    public void seedStaffIfAbsent() throws IOException {
        if (staffRepository.findAll().isEmpty()) {
            StaffMember s1 = create("Amaya Perera", "amaya@glamourbeauty.lk", "+94771234567",
                    List.of("Hair", "Beauty"), "Expert hairstylist with 8 years of experience.");
            StaffMember s2 = create("Nilufar Rashid", "nilufar@glamourbeauty.lk", "+94782345678",
                    List.of("Nails", "Skin"), "Certified nail technician and esthetician.");
            StaffMember s3 = create("Kaveesha Silva", "kaveesha@glamourbeauty.lk", "+94793456789",
                    List.of("Body", "Skin", "Beauty"), "Massage therapist and skincare specialist.");

            DayOfWeek[] workDays = {
                    DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                    DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY
            };
            for (StaffMember staff : List.of(s1, s2, s3)) {
                for (DayOfWeek day : workDays) {
                    addRegularSchedule(staff.getId(), day,
                            LocalTime.of(9, 0), LocalTime.of(18, 0),
                            LocalTime.of(12, 0), LocalTime.of(13, 0));
                }
            }
        }
    }
}
