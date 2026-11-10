package com.beautysalon.controller.appointment;

import com.beautysalon.model.appointment.Appointment;
import com.beautysalon.model.user.User;
import com.beautysalon.service.appointment.AppointmentService;
import com.beautysalon.service.catalogue.PackageService;
import com.beautysalon.service.catalogue.SalonServiceService;
import com.beautysalon.service.staff.StaffService;
import com.beautysalon.service.user.UserService;
import com.beautysalon.util.DateTimeUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

// Handles appointment operations for customers.
@Controller
@RequestMapping("/customer")
public class AppointmentController {

    // Required services for appointment management.
    private final AppointmentService appointmentService;
    private final SalonServiceService serviceService;
    private final PackageService packageService;
    private final StaffService staffService;
    private final UserService userService;

    // Initialize services using constructor injection.
    @Autowired
    public AppointmentController(AppointmentService appointmentService,
                                  SalonServiceService serviceService,
                                  PackageService packageService,
                                  StaffService staffService,
                                  UserService userService) {
        this.appointmentService = appointmentService;
        this.serviceService = serviceService;
        this.packageService = packageService;
        this.staffService = staffService;
        this.userService = userService;
    }

    // Display appointments of the logged-in customer.
    @GetMapping("/appointments")
    public String myAppointments(Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName()).orElse(null);
        if (user != null) {
            model.addAttribute("appointments", appointmentService.findByCustomerId(user.getId()));
        }
        return "appointment/my-appointments";
    }

    // Display details of a selected appointment.
    @GetMapping("/appointments/{id}")
    public String viewAppointment(@PathVariable String id, Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName()).orElse(null);
        Appointment appt = appointmentService.findById(id).orElse(null);

        // Allow access only if the appointment belongs to the logged-in customer.
        if (appt == null || user == null || !appt.getCustomerId().equals(user.getId())) {
            return "redirect:/customer/appointments";
        }

        model.addAttribute("appointment", appt);
        staffService.findById(appt.getStaffId()).ifPresent(s -> model.addAttribute("staff", s));
        return "appointment/appointment-detail";
    }

    // Display the appointment booking form with available options.
    @GetMapping("/appointments/book")
    public String bookForm(Model model,
                            @RequestParam(required = false) String serviceId,
                            @RequestParam(required = false) String packageId,
                            @RequestParam(required = false) String staffId,
                            @RequestParam(required = false) String date) {

        model.addAttribute("services", serviceService.findAllActive());
        model.addAttribute("packages", packageService.findAllActive());
        model.addAttribute("staff", staffService.findAllActive());

        // Check whether the customer selected a package.
        if (packageId != null && !packageId.isBlank()) {

            packageService.findById(packageId).ifPresent(pkg -> {
                model.addAttribute("selectedPackage", pkg);

                // Calculate the total duration of all services in the package.
                int totalDuration = 0;
                for (String sId : pkg.getServiceIds()) {
                    var sOpt = serviceService.findById(sId);
                    if (sOpt.isPresent()) totalDuration += sOpt.get().getDurationMinutes();
                }

                if (totalDuration == 0) totalDuration = 60;
                model.addAttribute("packageDuration", totalDuration);

                // Get available time slots for the selected staff and date.
                if (staffId != null && date != null) {

                    LocalDate localDate = DateTimeUtil.parseDate(date);
                    if (localDate != null) {

                        List<LocalTime> slots = appointmentService.getAvailableSlots(
                                staffId, localDate, totalDuration);

                        model.addAttribute("availableSlots", slots);
                        model.addAttribute("selectedStaffId", staffId);
                        model.addAttribute("selectedDate", date);
                    }
                }
            });

        // If no package is selected, check whether a service is selected.
        } else if (serviceId != null && !serviceId.isBlank()) {

            serviceService.findById(serviceId).ifPresent(s -> {
                model.addAttribute("selectedService", s);

                if (staffId != null && date != null) {

                    LocalDate localDate = DateTimeUtil.parseDate(date);
                    if (localDate != null) {

                        List<LocalTime> slots = appointmentService.getAvailableSlots(
                                staffId, localDate, s.getDurationMinutes());

                        model.addAttribute("availableSlots", slots);
                        model.addAttribute("selectedStaffId", staffId);
                        model.addAttribute("selectedDate", date);
                    }
                }
            });
        }
        return "appointment/book";
    }

    // Process the customer's appointment booking request.
    @PostMapping("/appointments/book")
    public String bookAppointment(Authentication auth,
                                   @RequestParam(required = false) String serviceId,
                                   @RequestParam(required = false) String packageId,
                                   @RequestParam String staffId,
                                   @RequestParam String date,
                                   @RequestParam String startTime,
                                   @RequestParam(defaultValue = "") String notes,
                                   RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByUsername(auth.getName())
                    .orElseThrow(() -> new RuntimeException("User not found."));

            LocalDate localDate = DateTimeUtil.parseDate(date);
            LocalTime localStartTime = DateTimeUtil.parseTime(startTime);

            Appointment appt;

            // Book either a package or an individual service.
            if (packageId != null && !packageId.isBlank()) {
                appt = appointmentService.bookPackage(user.getId(), packageId, staffId, localDate, localStartTime);

            } else if (serviceId != null && !serviceId.isBlank()) {
                appt = appointmentService.book(user.getId(), serviceId, staffId, localDate, localStartTime);

            } else {
                throw new IllegalArgumentException("Please select either a service or a package.");
            }

            if (!notes.isBlank()) {
                appt.setNotes(notes);
            }

            // Show a success message and redirect to appointment details.
            redirectAttributes.addFlashAttribute("successMessage", "Appointment booked successfully!");

            return "redirect:/customer/appointments/" + appt.getId();

        } catch (Exception e) {
            // Show an error message if the booking fails.
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

            String redirectUrl = "/customer/appointments/book?";
            if (packageId != null && !packageId.isBlank()) {
                redirectUrl += "packageId=" + packageId;
            } else {
                redirectUrl += "serviceId=" + (serviceId != null ? serviceId : "");
            }

            redirectUrl += "&staffId=" + staffId + "&date=" + date;
            return "redirect:" + redirectUrl;
        }
    }

    // Cancel an appointment requested by the customer.
    @PostMapping("/appointments/{id}/cancel")
    public String cancelAppointment(@PathVariable String id, Authentication auth,
                                     RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByUsername(auth.getName()).orElseThrow(() -> new RuntimeException("User not found."));

            appointmentService.cancel(id, user.getId(), false);

            redirectAttributes.addFlashAttribute("successMessage", "Appointment cancelled successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/customer/appointments";
    }
}
