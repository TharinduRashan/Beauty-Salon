package com.beautysalon.controller.appointment;

import com.beautysalon.model.appointment.AppointmentStatus;
import com.beautysalon.service.appointment.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminAppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AdminAppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/appointments")
    public String listAppointments(Model model) {
        model.addAttribute("appointments", appointmentService.findAllEnriched());
        return "appointment/admin/manage";
    }

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
