package com.beautysalon.model.appointment;

import com.beautysalon.util.DateTimeUtil;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Appointment {
    private String id;
    private String customerId;
    private String staffId;
    private String serviceId;
    private String packageId;
    private String serviceNameSnapshot;
    private BigDecimal priceSnapshot;
    private int durationMinutes;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private AppointmentStatus status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Transient fields for display
    private String customerName;
    private String staffName;

    public Appointment() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getStaffId() { return staffId; }
    public void setStaffId(String staffId) { this.staffId = staffId; }
    public String getServiceId() { return serviceId; }
    public void setServiceId(String serviceId) { this.serviceId = serviceId; }
    public String getPackageId() { return packageId; }
    public void setPackageId(String packageId) { this.packageId = packageId; }
    public String getServiceNameSnapshot() { return serviceNameSnapshot; }
    public void setServiceNameSnapshot(String serviceNameSnapshot) { this.serviceNameSnapshot = serviceNameSnapshot; }
    public BigDecimal getPriceSnapshot() { return priceSnapshot; }
    public void setPriceSnapshot(BigDecimal priceSnapshot) { this.priceSnapshot = priceSnapshot; }
    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getStaffName() { return staffName; }
    public void setStaffName(String staffName) { this.staffName = staffName; }
    public String getFormattedDate() { return DateTimeUtil.formatDate(appointmentDate); }
    public String getFormattedStartTime() { return DateTimeUtil.formatTime(startTime); }
    public String getFormattedEndTime() { return DateTimeUtil.formatTime(endTime); }
    public boolean isCancellable() {
        return status == AppointmentStatus.PENDING || status == AppointmentStatus.CONFIRMED;
    }
    public boolean isUpcoming() {
        return appointmentDate != null && !appointmentDate.isBefore(DateTimeUtil.today());
    }
}
