package com.beautysalon.model.feedback;
public enum FeedbackStatus {
    PENDING, APPROVED, REJECTED;
    public static FeedbackStatus fromString(String s) {
        try { return FeedbackStatus.valueOf(s.toUpperCase()); }
        catch (Exception e) { return PENDING; }
    }
}
