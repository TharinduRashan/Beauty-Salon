package com.beautysalon.model.payment;
public enum PaymentStatus {
    PENDING, PAID, REFUNDED, PARTIALLY_REFUNDED;
    public static PaymentStatus fromString(String s) {
        try { return PaymentStatus.valueOf(s.toUpperCase()); }
        catch (Exception e) { return PENDING; }
    }
}
