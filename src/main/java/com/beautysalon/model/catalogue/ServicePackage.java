package com.beautysalon.model.catalogue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServicePackage {
    private String id;
    private String name;
    private String description;
    private List<String> serviceIds = new ArrayList<>();
    private BigDecimal totalPrice;
    private BigDecimal discountPercent;
    private ServiceStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ServicePackage() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<String> getServiceIds() { return serviceIds; }
    public void setServiceIds(List<String> serviceIds) { this.serviceIds = serviceIds; }
    public String getServiceIdsAsString() { return String.join(",", serviceIds); }
    public void setServiceIdsFromString(String s) {
        if (s == null || s.isBlank()) { serviceIds = new ArrayList<>(); return; }
        serviceIds = new ArrayList<>(Arrays.asList(s.split(",")));
    }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    public BigDecimal getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(BigDecimal discountPercent) { this.discountPercent = discountPercent; }
    public ServiceStatus getStatus() { return status; }
    public void setStatus(ServiceStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public boolean isActive() { return ServiceStatus.ACTIVE.equals(status); }
}
