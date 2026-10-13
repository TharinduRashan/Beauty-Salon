package com.beautysalon.model.appointment;
public enum AppointmentStatus {
    PENDING, CONFIRMED, COMPLETED, CANCELLED, NO_SHOW;
    public static AppointmentStatus fromString(String s) {
        try { return AppointmentStatus.valueOf(s.toUpperCase()); }
        catch (Exception e) { return PENDING; }
    }
}
