package com.beautysalon.controller.appointment;

// Import the AppointmentStatus enum to manage appointment statuses.
import com.beautysalon.model.appointment.AppointmentStatus;

// Import the service used to handle appointment operations.
import com.beautysalon.service.appointment.AppointmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// This class handles appointment management functions for the admin.
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminAppointmentController {

    // Service object used to perform appointment-related operations.
    private final AppointmentService appointmentService;

    // Constructor injection to initialize the AppointmentService.
    @Autowired
    public AdminAppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // Display all appointments in the admin dashboard.
    @GetMapping("/appointments")
    public String listAppointments(Model model) {
        model.addAttribute("appointments", appointmentService.findAllEnriched());
        return "appointment/admin/manage";
    }

    // Update the status of a selected appointment.
    @PostMapping("/appointments/{id}/status")
    public String updateStatus(@PathVariable String id, @RequestParam String status,
                                RedirectAttributes redirectAttributes) {
        try {
            
            appointmentService.updateStatus(id, AppointmentStatus.fromString(status));

           
            redirectAttributes.addFlashAttribute("successMessage", "Appointment status updated.");
        } catch (Exception e) {
            
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/appointments";
    }

    // Cancel a selected appointment by its ID.
    @PostMapping("/appointments/{id}/cancel")
    public String cancelAppointment(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            appointmentService.cancel(id, null, true);

            
            redirectAttributes.addFlashAttribute("successMessage", "Appointment cancelled.");
        } catch (Exception e) {
       
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/appointments";
    }
}
