package com.beautysalon.model.appointment;

// Import utility class to format dates and times.
import com.beautysalon.util.DateTimeUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

// This class represents an appointment in the salon system.
public class Appointment {

    // Store the unique appointment ID.
    private String id;

    // Store the ID of the customer who booked the appointment.
    private String customerId;

    // Store the ID of the assigned staff member.
    private String staffId;

    // Store the ID of the selected service.
    private String serviceId;

    // Store the ID of the selected package.
    private String packageId;

    // Store the service name at the time of booking.
    private String serviceNameSnapshot;

    // Store the service price at the time of booking.
    private BigDecimal priceSnapshot;

    // Store the total service duration in minutes.
    private int durationMinutes;

    // Store the appointment date.
    private LocalDate appointmentDate;

    // Store the appointment starting time.
    private LocalTime startTime;

    // Store the appointment ending time.
    private LocalTime endTime;

    // Store the current appointment status.
    private AppointmentStatus status;

    // Store additional notes provided by the customer.
    private String notes;

    // Store the date and time when the appointment was created.
    private LocalDateTime createdAt;

    // Store the date and time when the appointment was last updated.
    private LocalDateTime updatedAt;

    // Transient fields for display
    // These fields are used to display customer and staff names.
    private String customerName;
    private String staffName;

    // Default constructor.
    public Appointment() {}

    // Getter to return the appointment ID.
    public String getId() { return id; }

    // Setter to assign the appointment ID.
    public void setId(String id) { this.id = id; }

    // Getter to return the customer ID.
    public String getCustomerId() { return customerId; }

    // Setter to assign the customer ID.
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    // Getter to return the staff ID.
    public String getStaffId() { return staffId; }

    // Setter to assign the staff ID.
    public void setStaffId(String staffId) { this.staffId = staffId; }

    // Getter to return the service ID.
    public String getServiceId() { return serviceId; }

    // Setter to assign the service ID.
    public void setServiceId(String serviceId) { this.serviceId = serviceId; }

    // Getter to return the package ID.
    public String getPackageId() { return packageId; }

    // Setter to assign the package ID.
    public void setPackageId(String packageId) { this.packageId = packageId; }

    // Getter to return the service name snapshot.
    public String getServiceNameSnapshot() { return serviceNameSnapshot; }

    // Setter to assign the service name snapshot.
    public void setServiceNameSnapshot(String serviceNameSnapshot) { this.serviceNameSnapshot = serviceNameSnapshot; }

    // Getter to return the saved service price.
    public BigDecimal getPriceSnapshot() { return priceSnapshot; }

    // Setter to assign the saved service price.
    public void setPriceSnapshot(BigDecimal priceSnapshot) { this.priceSnapshot = priceSnapshot; }

    // Getter to return the appointment duration.
    public int getDurationMinutes() { return durationMinutes; }

    // Setter to assign the appointment duration.
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    // Getter to return the appointment date.
    public LocalDate getAppointmentDate() { return appointmentDate; }

    // Setter to assign the appointment date.
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }

    // Getter to return the starting time.
    public LocalTime getStartTime() { return startTime; }

    // Setter to assign the starting time.
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    // Getter to return the ending time.
    public LocalTime getEndTime() { return endTime; }

    // Setter to assign the ending time.
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    // Getter to return the appointment status.
    public AppointmentStatus getStatus() { return status; }

    // Setter to assign the appointment status.
    public void setStatus(AppointmentStatus status) { this.status = status; }

    // Getter to return customer notes.
    public String getNotes() { return notes; }

    // Setter to assign customer notes.
    public void setNotes(String notes) { this.notes = notes; }

    // Getter to return the appointment creation date and time.
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setter to assign the creation date and time.
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Getter to return the last updated date and time.
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setter to assign the updated date and time.
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Getter to return the customer name.
    public String getCustomerName() { return customerName; }

    // Setter to assign the customer name.
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    // Getter to return the staff name.
    public String getStaffName() { return staffName; }

    // Setter to assign the staff name.
    public void setStaffName(String staffName) { this.staffName = staffName; }

    // Return the appointment date in a formatted way.
    public String getFormattedDate() { return DateTimeUtil.formatDate(appointmentDate); }

    // Return the starting time in a formatted way.
    public String getFormattedStartTime() { return DateTimeUtil.formatTime(startTime); }

    // Return the ending time in a formatted way.
    public String getFormattedEndTime() { return DateTimeUtil.formatTime(endTime); }

    // Check whether the appointment can be cancelled.
    public boolean isCancellable() {
        return status == AppointmentStatus.PENDING || status == AppointmentStatus.CONFIRMED;
    }

    // Check whether the appointment date is today or in the future.
    public boolean isUpcoming() {
        return appointmentDate != null && !appointmentDate.isBefore(DateTimeUtil.today());
    }
}
