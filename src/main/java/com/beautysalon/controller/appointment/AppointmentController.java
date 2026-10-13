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

@Controller
@RequestMapping("/customer")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final SalonServiceService serviceService;
    private final PackageService packageService;
    private final StaffService staffService;
    private final UserService userService;

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

    @GetMapping("/appointments")
    public String myAppointments(Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName()).orElse(null);
        if (user != null) {
            model.addAttribute("appointments", appointmentService.findByCustomerId(user.getId()));
        }
        return "appointment/my-appointments";
    }

    @GetMapping("/appointments/{id}")
    public String viewAppointment(@PathVariable String id, Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName()).orElse(null);
        Appointment appt = appointmentService.findById(id).orElse(null);
        if (appt == null || user == null || !appt.getCustomerId().equals(user.getId())) {
            return "redirect:/customer/appointments";
        }
        model.addAttribute("appointment", appt);
        staffService.findById(appt.getStaffId()).ifPresent(s -> model.addAttribute("staff", s));
        return "appointment/appointment-detail";
    }

    @GetMapping("/appointments/book")
    public String bookForm(Model model,
                            @RequestParam(required = false) String serviceId,
                            @RequestParam(required = false) String packageId,
                            @RequestParam(required = false) String staffId,
                            @RequestParam(required = false) String date) {
        model.addAttribute("services", serviceService.findAllActive());
        model.addAttribute("packages", packageService.findAllActive());
        model.addAttribute("staff", staffService.findAllActive());

        if (packageId != null && !packageId.isBlank()) {
            packageService.findById(packageId).ifPresent(pkg -> {
                model.addAttribute("selectedPackage", pkg);
                int totalDuration = 0;
                for (String sId : pkg.getServiceIds()) {
                    var sOpt = serviceService.findById(sId);
                    if (sOpt.isPresent()) totalDuration += sOpt.get().getDurationMinutes();
                }
                if (totalDuration == 0) totalDuration = 60;
                model.addAttribute("packageDuration", totalDuration);

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
            redirectAttributes.addFlashAttribute("successMessage", "Appointment booked successfully!");
            return "redirect:/customer/appointments/" + appt.getId();
        } catch (Exception e) {
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
