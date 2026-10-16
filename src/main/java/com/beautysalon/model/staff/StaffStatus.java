package com.beautysalon.model.staff;
public enum StaffStatus {
    ACTIVE, INACTIVE;
    public static StaffStatus fromString(String s) {
        try { return StaffStatus.valueOf(s.toUpperCase()); }
        catch (Exception e) { return ACTIVE; }
    }
}
