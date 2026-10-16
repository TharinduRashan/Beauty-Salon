package com.beautysalon.model.staff;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class StaffSchedule {
    private String id;
    private String staffId;
    private DayOfWeek dayOfWeek;  // for REGULAR type
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime breakStart;
    private LocalTime breakEnd;
    private LocalDate leaveDate;  // for LEAVE type - specific date
    private LocalDate leaveDateEnd; // for LEAVE type - end date
    private ScheduleType type;
    private ScheduleStatus status;

    public StaffSchedule() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getStaffId() { return staffId; }
    public void setStaffId(String staffId) { this.staffId = staffId; }
    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public LocalTime getBreakStart() { return breakStart; }
    public void setBreakStart(LocalTime breakStart) { this.breakStart = breakStart; }
    public LocalTime getBreakEnd() { return breakEnd; }
    public void setBreakEnd(LocalTime breakEnd) { this.breakEnd = breakEnd; }
    public LocalDate getLeaveDate() { return leaveDate; }
    public void setLeaveDate(LocalDate leaveDate) { this.leaveDate = leaveDate; }
    public LocalDate getLeaveDateEnd() { return leaveDateEnd; }
    public void setLeaveDateEnd(LocalDate leaveDateEnd) { this.leaveDateEnd = leaveDateEnd; }
    public ScheduleType getType() { return type; }
    public void setType(ScheduleType type) { this.type = type; }
    public ScheduleStatus getStatus() { return status; }
    public void setStatus(ScheduleStatus status) { this.status = status; }
    public boolean isActive() { return ScheduleStatus.ACTIVE.equals(status); }
}
