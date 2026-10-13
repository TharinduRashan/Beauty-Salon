package com.beautysalon.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
    public NotFoundException(String entityType, String id) {
        super(entityType + " not found: " + id);
    }
}
