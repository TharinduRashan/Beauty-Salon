package com.beautysalon.model.catalogue;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SalonService {
    private String id;
    private String name;
    private String description;
    private String category;
    private int durationMinutes;
    private BigDecimal price;
    private ServiceStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SalonService() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public ServiceStatus getStatus() { return status; }
    public void setStatus(ServiceStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public boolean isActive() { return ServiceStatus.ACTIVE.equals(status); }
    public String getDurationFormatted() {
        if (durationMinutes < 60) return durationMinutes + " min";
        int h = durationMinutes / 60, m = durationMinutes % 60;
        return m == 0 ? h + " hr" : h + " hr " + m + " min";
    }
}
