package com.beautysalon.model.catalogue;

public enum ServiceStatus {
    ACTIVE, INACTIVE;
    public static ServiceStatus fromString(String s) {
        try { return ServiceStatus.valueOf(s.toUpperCase()); }
        catch (Exception e) { return ACTIVE; }
    }
}
