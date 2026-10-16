package com.beautysalon.model.staff;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StaffMember {
    private String id;
    private String userId;
    private String fullName;
    private String email;
    private String phone;
    private List<String> specialties = new ArrayList<>();
    private String bio;
    private StaffStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public StaffMember() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public List<String> getSpecialties() { return specialties; }
    public void setSpecialties(List<String> specialties) { this.specialties = specialties; }
    public String getSpecialtiesAsString() { return String.join(",", specialties); }
    public void setSpecialtiesFromString(String s) {
        if (s == null || s.isBlank()) { specialties = new ArrayList<>(); return; }
        specialties = new ArrayList<>(Arrays.asList(s.split(",")));
    }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public StaffStatus getStatus() { return status; }
    public void setStatus(StaffStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public boolean isActive() { return StaffStatus.ACTIVE.equals(status); }
}
