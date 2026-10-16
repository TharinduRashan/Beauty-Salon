package com.beautysalon.model.feedback;

import com.beautysalon.util.DateTimeUtil;
import java.time.LocalDateTime;

public class Feedback {
    private String id;
    private String customerId;
    private String appointmentId;
    private FeedbackType type;
    private Integer rating;
    private String title;
    private String content;
    private FeedbackStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Transient
    private String customerName;
    private String serviceNameSnapshot;

    public Feedback() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }
    public FeedbackType getType() { return type; }
    public void setType(FeedbackType type) { this.type = type; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public FeedbackStatus getStatus() { return status; }
    public void setStatus(FeedbackStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getServiceNameSnapshot() { return serviceNameSnapshot; }
    public void setServiceNameSnapshot(String s) { this.serviceNameSnapshot = s; }
    public String getFormattedCreatedAt() { return DateTimeUtil.formatDateTime(createdAt); }
    public boolean isApproved() { return FeedbackStatus.APPROVED.equals(status); }
    public boolean isPending() { return FeedbackStatus.PENDING.equals(status); }
    public String getRatingStars() {
        if (rating == null) return "";
        return "★".repeat(rating) + "☆".repeat(5 - rating);
    }
}
