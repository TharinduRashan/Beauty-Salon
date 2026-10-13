package com.beautysalon.controller.catalogue;

import com.beautysalon.service.catalogue.SalonServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
public class ServiceController {

    private final SalonServiceService serviceService;

    @Autowired
    public ServiceController(SalonServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping("/services")
    public String listServices(Model model) {
        model.addAttribute("services", serviceService.findAllActive());
        return "catalogue/services";
    }

    @GetMapping("/services/{id}")
    public String serviceDetail(@PathVariable String id, Model model) {
        serviceService.findById(id).ifPresent(s -> model.addAttribute("service", s));
        return "catalogue/service-detail";
    }

    // Admin CRUD
    @GetMapping("/admin/services")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public String adminListServices(Model model) {
        model.addAttribute("services", serviceService.findAll());
        return "catalogue/admin/service-list";
    }

    @GetMapping("/admin/services/new")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public String newServiceForm() {
        return "catalogue/admin/service-form";
    }

    @PostMapping("/admin/services/new")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public String createService(@RequestParam String name, @RequestParam String description,
                                 @RequestParam String category, @RequestParam int durationMinutes,
                                 @RequestParam BigDecimal price, RedirectAttributes redirectAttributes) {
        try {
            serviceService.create(name, description, category, durationMinutes, price);
            redirectAttributes.addFlashAttribute("successMessage", "Service created successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/services";
    }

    @GetMapping("/admin/services/{id}/edit")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public String editServiceForm(@PathVariable String id, Model model) {
        serviceService.findById(id).ifPresent(s -> model.addAttribute("service", s));
        return "catalogue/admin/service-form";
    }

    @PostMapping("/admin/services/{id}/edit")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public String updateService(@PathVariable String id, @RequestParam String name,
                                 @RequestParam String description, @RequestParam String category,
                                 @RequestParam int durationMinutes, @RequestParam BigDecimal price,
                                 RedirectAttributes redirectAttributes) {
        try {
            serviceService.update(id, name, description, category, durationMinutes, price);
            redirectAttributes.addFlashAttribute("successMessage", "Service updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/services";
    }

    @PostMapping("/admin/services/{id}/toggle")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public String toggleStatus(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            serviceService.toggleStatus(id);
            redirectAttributes.addFlashAttribute("successMessage", "Service status updated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/services";
    }
}
