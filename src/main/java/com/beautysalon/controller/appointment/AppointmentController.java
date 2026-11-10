package com.beautysalon.controller.appointment;

// Import the Appointment model class.
import com.beautysalon.model.appointment.Appointment;

// Import the User model class.
import com.beautysalon.model.user.User;

// Import services required for appointment management.
import com.beautysalon.service.appointment.AppointmentService;
import com.beautysalon.service.catalogue.PackageService;
import com.beautysalon.service.catalogue.SalonServiceService;
import com.beautysalon.service.staff.StaffService;
import com.beautysalon.service.user.UserService;

// Import utility class to handle date and time.
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

// This class handles customer appointment operations.
@Controller
@RequestMapping("/customer")
public class AppointmentController {

    // Service objects used to manage appointments, services, packages, staff and users.
    private final AppointmentService appointmentService;
    private final SalonServiceService serviceService;
    private final PackageService packageService;
    private final StaffService staffService;
    private final UserService userService;

    // Constructor injection to initialize all required services.
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

    // Display all appointments belonging to the logged-in customer.
    @GetMapping("/appointments")
    public String myAppointments(Authentication auth, Model model) {
        // Get the logged-in user's details using the username.
        User user = userService.findByUsername(auth.getName()).orElse(null);
        if (user != null) {
            // Add the customer's appointments to the model.
            model.addAttribute("appointments", appointmentService.findByCustomerId(user.getId()));
        }
        return "appointment/my-appointments";
    }

    // Display details of a selected appointment.
    @GetMapping("/appointments/{id}")
    public String viewAppointment(@PathVariable String id, Authentication auth, Model model) {
        // Get the logged-in user and selected appointment.
        User user = userService.findByUsername(auth.getName()).orElse(null);
        Appointment appt = appointmentService.findById(id).orElse(null);

        // Check whether the appointment belongs to the logged-in customer.
        if (appt == null || user == null || !appt.getCustomerId().equals(user.getId())) {
            return "redirect:/customer/appointments";
        }

        // Add appointment details to the model.
        model.addAttribute("appointment", appt);

        // Find the assigned staff member and add their details to the model.
        staffService.findById(appt.getStaffId()).ifPresent(s -> model.addAttribute("staff", s));
        return "appointment/appointment-detail";
    }

    // Display the appointment booking form.
    @GetMapping("/appointments/book")
    public String bookForm(Model model,
                            @RequestParam(required = false) String serviceId,
                            @RequestParam(required = false) String packageId,
                            @RequestParam(required = false) String staffId,
                            @RequestParam(required = false) String date) {

        // Load all active services, packages and staff members.
        model.addAttribute("services", serviceService.findAllActive());
        model.addAttribute("packages", packageService.findAllActive());
        model.addAttribute("staff", staffService.findAllActive());

        // Check whether the customer selected a package.
        if (packageId != null && !packageId.isBlank()) {

            // Find the selected package and get its details.
            packageService.findById(packageId).ifPresent(pkg -> {
                model.addAttribute("selectedPackage", pkg);

                // Calculate the total duration of all services in the package.
                int totalDuration = 0;
                for (String sId : pkg.getServiceIds()) {
                    var sOpt = serviceService.findById(sId);
                    if (sOpt.isPresent()) totalDuration += sOpt.get().getDurationMinutes();
                }

                // Set the default duration to 60 minutes if no duration is found.
                if (totalDuration == 0) totalDuration = 60;
                model.addAttribute("packageDuration", totalDuration);

                // Check whether staff and date are selected.
                if (staffId != null && date != null) {

                    // Convert the date string into a LocalDate object.
                    LocalDate localDate = DateTimeUtil.parseDate(date);
                    if (localDate != null) {

                        // Get available time slots for the selected staff and package duration.
                        List<LocalTime> slots = appointmentService.getAvailableSlots(
                                staffId, localDate, totalDuration);

                        // Add available slots and selected details to the model.
                        model.addAttribute("availableSlots", slots);
                        model.addAttribute("selectedStaffId", staffId);
                        model.addAttribute("selectedDate", date);
                    }
                }
            });

        // If no package is selected, check whether a service is selected.
        } else if (serviceId != null && !serviceId.isBlank()) {

            // Find the selected service and get its details.
            serviceService.findById(serviceId).ifPresent(s -> {
                model.addAttribute("selectedService", s);

                // Check whether staff and date are selected.
                if (staffId != null && date != null) {

                    // Convert the date string into a LocalDate object.
                    LocalDate localDate = DateTimeUtil.parseDate(date);
                    if (localDate != null) {

                        // Get available time slots based on the service duration.
                        List<LocalTime> slots = appointmentService.getAvailableSlots(
                                staffId, localDate, s.getDurationMinutes());

                        // Add available slots and selected details to the model.
                        model.addAttribute("availableSlots", slots);
                        model.addAttribute("selectedStaffId", staffId);
                        model.addAttribute("selectedDate", date);
                    }
                }
            });
        }
        return "appointment/book";
    }

    // Handle the customer's appointment booking request.
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
            // Get the currently logged-in user.
            User user = userService.findByUsername(auth.getName())
                    .orElseThrow(() -> new RuntimeException("User not found."));

            // Convert the date and start time into LocalDate and LocalTime objects.
            LocalDate localDate = DateTimeUtil.parseDate(date);
            LocalTime localStartTime = DateTimeUtil.parseTime(startTime);

            Appointment appt;

            // Book a package if the customer selected a package.
            if (packageId != null && !packageId.isBlank()) {
                appt = appointmentService.bookPackage(user.getId(), packageId, staffId, localDate, localStartTime);

            // Otherwise, book an individual service if selected.
            } else if (serviceId != null && !serviceId.isBlank()) {
                appt = appointmentService.book(user.getId(), serviceId, staffId, localDate, localStartTime);

            // Show an error if neither a service nor a package is selected.
            } else {
                throw new IllegalArgumentException("Please select either a service or a package.");
            }

            // Add customer notes if notes are provided.
            if (!notes.isBlank()) {
                appt.setNotes(notes);
            }

            // Display a success message after booking.
            redirectAttributes.addFlashAttribute("successMessage", "Appointment booked successfully!");

            // Redirect to the newly created appointment details page.
            return "redirect:/customer/appointments/" + appt.getId();

        } catch (Exception e) {
            // Display an error message if the booking fails.
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

            // Prepare the booking page URL with the selected details.
            String redirectUrl = "/customer/appointments/book?";
            if (packageId != null && !packageId.isBlank()) {
                redirectUrl += "packageId=" + packageId;
            } else {
                redirectUrl += "serviceId=" + (serviceId != null ? serviceId : "");
            }

            // Keep the selected staff and date when redirecting.
            redirectUrl += "&staffId=" + staffId + "&date=" + date;
            return "redirect:" + redirectUrl;
        }
    }

    // Handle appointment cancellation by the customer.
    @PostMapping("/appointments/{id}/cancel")
    public String cancelAppointment(@PathVariable String id, Authentication auth,
                                     RedirectAttributes redirectAttributes) {
        try {
            // Get the currently logged-in customer.
            User user = userService.findByUsername(auth.getName()).orElseThrow(() -> new RuntimeException("User not found."));

            // Cancel the appointment belonging to this customer.
            appointmentService.cancel(id, user.getId(), false);

            // Display a success message after cancellation.
            redirectAttributes.addFlashAttribute("successMessage", "Appointment cancelled successfully.");
        } catch (Exception e) {
            // Display an error message if cancellation fails.
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/customer/appointments";
    }
}
