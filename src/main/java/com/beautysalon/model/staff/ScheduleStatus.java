package com.beautysalon.model.staff;
public enum ScheduleStatus {
    ACTIVE, CANCELLED;
    public static ScheduleStatus fromString(String s) {
        try { return ScheduleStatus.valueOf(s.toUpperCase()); }
        catch (Exception e) { return ACTIVE; }
    }
}
