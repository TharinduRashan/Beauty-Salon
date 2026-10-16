package com.beautysalon.model.payment;
public enum PaymentMethod {
    CASH, CARD, ONLINE;
    public static PaymentMethod fromString(String s) {
        try { return PaymentMethod.valueOf(s.toUpperCase()); }
        catch (Exception e) { return CASH; }
    }
}
