package com.beautysalon.model.user;

public enum UserRole {
    ADMIN, STAFF, CUSTOMER;

    public static UserRole fromString(String s) {
        try { return UserRole.valueOf(s.toUpperCase()); }
        catch (Exception e) { return CUSTOMER; }
    }
}
