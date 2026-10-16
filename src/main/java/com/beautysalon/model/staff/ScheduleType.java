package com.beautysalon.model.staff;
public enum ScheduleType {
    REGULAR, LEAVE, BREAK_EXTRA;
    public static ScheduleType fromString(String s) {
        try { return ScheduleType.valueOf(s.toUpperCase()); }
        catch (Exception e) { return REGULAR; }
    }
}
