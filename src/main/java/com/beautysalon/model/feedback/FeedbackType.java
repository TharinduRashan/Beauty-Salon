package com.beautysalon.model.feedback;
public enum FeedbackType {
    REVIEW, COMPLAINT, GENERAL;
    public static FeedbackType fromString(String s) {
        try { return FeedbackType.valueOf(s.toUpperCase()); }
        catch (Exception e) { return GENERAL; }
    }
}
