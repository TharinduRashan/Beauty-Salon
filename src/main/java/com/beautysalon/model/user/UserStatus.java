package com.beautysalon.model.user;

public enum UserStatus {
    ACTIVE, INACTIVE, SUSPENDED;

    public static UserStatus fromString(String s) {
        try { return UserStatus.valueOf(s.toUpperCase()); }
        catch (Exception e) { return ACTIVE; }
    }
}
