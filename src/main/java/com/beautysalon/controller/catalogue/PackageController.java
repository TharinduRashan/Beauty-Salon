package com.beautysalon.controller.catalogue;

import com.beautysalon.service.catalogue.PackageService;
import com.beautysalon.service.catalogue.SalonServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Arrays;

@Controller
public class PackageController {

    private final PackageService packageService;
    private final SalonServiceService serviceService;

    @Autowired
    public PackageController(PackageService packageService, SalonServiceService serviceService) {
        this.packageService = packageService;
        this.serviceService = serviceService;
    }

    @GetMapping("/packages")
    public String listPackages(Model model) {
        model.addAttribute("packages", packageService.findAllActive());
        model.addAttribute("services", serviceService.findAllActive());
        return "catalogue/packages";
    }

    @GetMapping("/admin/packages")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public String adminListPackages(Model model) {
        model.addAttribute("packages", packageService.findAll());
        model.addAttribute("services", serviceService.findAll());
        return "catalogue/admin/package-list";
    }

    @GetMapping("/admin/packages/new")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public String newPackageForm(Model model) {
        model.addAttribute("services", serviceService.findAllActive());
        return "catalogue/admin/package-form";
    }

    @PostMapping("/admin/packages/new")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public String createPackage(@RequestParam String name, @RequestParam String description,
                                 @RequestParam String[] serviceIds, @RequestParam BigDecimal totalPrice,
                                 @RequestParam(defaultValue = "0") BigDecimal discountPercent,
                                 RedirectAttributes redirectAttributes) {
        try {
            packageService.create(name, description, Arrays.asList(serviceIds), totalPrice, discountPercent);
            redirectAttributes.addFlashAttribute("successMessage", "Package created successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/packages";
    }

    @GetMapping("/admin/packages/{id}/edit")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public String editPackageForm(@PathVariable String id, Model model) {
        packageService.findById(id).ifPresent(p -> model.addAttribute("pkg", p));
        model.addAttribute("services", serviceService.findAllActive());
        return "catalogue/admin/package-form";
    }

    @PostMapping("/admin/packages/{id}/edit")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public String updatePackage(@PathVariable String id, @RequestParam String name,
                                 @RequestParam String description, @RequestParam String[] serviceIds,
                                 @RequestParam BigDecimal totalPrice,
                                 @RequestParam(defaultValue = "0") BigDecimal discountPercent,
                                 RedirectAttributes redirectAttributes) {
        try {
            packageService.update(id, name, description, Arrays.asList(serviceIds), totalPrice, discountPercent);
            redirectAttributes.addFlashAttribute("successMessage", "Package updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/packages";
    }

    @PostMapping("/admin/packages/{id}/toggle")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public String togglePackage(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            packageService.toggleStatus(id);
            redirectAttributes.addFlashAttribute("successMessage", "Package status updated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/packages";
    }
}
