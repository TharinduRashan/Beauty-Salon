package com.beautysalon.model.catalogue;

import java.util.Locale;

public enum ServiceStatus {
    ACTIVE,
    INACTIVE;

    /**
     * Converts a string into a ServiceStatus value.
     * Returns ACTIVE when the provided value is invalid.
     */
    public static ServiceStatus fromString(String status) {

        if (status == null || status.isBlank()) {
            return ACTIVE;
        }

        try {
            return ServiceStatus.valueOf(
                    status.trim().toUpperCase(Locale.ROOT)
            );

        } catch (IllegalArgumentException exception) {
            return ACTIVE;
        }
    }

    /**
     * Checks whether the current status is ACTIVE.
     */
    public boolean isActive() {
        return this == ACTIVE;
    }

    /**
     * Returns the opposite service status.
     */
    public ServiceStatus toggle() {
        return this == ACTIVE ? INACTIVE : ACTIVE;
    }
}
