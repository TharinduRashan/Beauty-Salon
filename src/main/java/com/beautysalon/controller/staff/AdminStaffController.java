package com.beautysalon.controller.staff;

import com.beautysalon.service.staff.StaffService;
import com.beautysalon.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/staff")
@PreAuthorize("hasRole('ADMIN')")
public class AdminStaffController {

    private final StaffService staffService;

    @Autowired
    public AdminStaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping
    public String listStaff(Model model) {
        model.addAttribute("staffList", staffService.findAll());
        return "staff/admin/list";
    }

    @GetMapping("/new")
    public String newStaffForm() { return "staff/admin/form"; }

    @PostMapping("/new")
    public String createStaff(@RequestParam String fullName, @RequestParam String email,
                               @RequestParam(defaultValue = "") String phone,
                               @RequestParam(defaultValue = "") String specialtiesStr,
                               @RequestParam(defaultValue = "") String bio,
                               RedirectAttributes redirectAttributes) {
        try {
            List<String> specialties = specialtiesStr.isBlank() ? List.of() :
                    Arrays.asList(specialtiesStr.split(","));
            staffService.create(fullName, email, phone, specialties, bio);
            redirectAttributes.addFlashAttribute("successMessage", "Staff member added.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/staff";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable String id, Model model) {
        staffService.findById(id).ifPresent(s -> model.addAttribute("staff", s));
        return "staff/admin/form";
    }

    @PostMapping("/{id}/edit")
    public String updateStaff(@PathVariable String id, @RequestParam String fullName,
                               @RequestParam String email, @RequestParam(defaultValue = "") String phone,
                               @RequestParam(defaultValue = "") String specialtiesStr,
                               @RequestParam(defaultValue = "") String bio,
                               RedirectAttributes redirectAttributes) {
        try {
            List<String> specialties = specialtiesStr.isBlank() ? List.of() :
                    Arrays.asList(specialtiesStr.split(","));
            staffService.update(id, fullName, email, phone, specialties, bio);
            redirectAttributes.addFlashAttribute("successMessage", "Staff member updated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/staff";
    }

    @PostMapping("/{id}/toggle")
    public String toggleStaff(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            staffService.toggleStatus(id);
            redirectAttributes.addFlashAttribute("successMessage", "Staff status updated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/staff";
    }

    @GetMapping("/{id}/schedule")
    public String scheduleForm(@PathVariable String id, Model model) {
        staffService.findById(id).ifPresent(s -> model.addAttribute("staff", s));
        model.addAttribute("schedules", staffService.findScheduleByStaffId(id));
        model.addAttribute("daysOfWeek", DayOfWeek.values());
        return "staff/admin/schedule";
    }

    @PostMapping("/{id}/schedule/add")
    public String addSchedule(@PathVariable String id,
                               @RequestParam String dayOfWeek,
                               @RequestParam String startTime,
                               @RequestParam String endTime,
                               @RequestParam(defaultValue = "") String breakStart,
                               @RequestParam(defaultValue = "") String breakEnd,
                               RedirectAttributes redirectAttributes) {
        try {
            staffService.addRegularSchedule(id, DayOfWeek.valueOf(dayOfWeek),
                    DateTimeUtil.parseTime(startTime), DateTimeUtil.parseTime(endTime),
                    breakStart.isBlank() ? null : DateTimeUtil.parseTime(breakStart),
                    breakEnd.isBlank() ? null : DateTimeUtil.parseTime(breakEnd));
            redirectAttributes.addFlashAttribute("successMessage", "Schedule added.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/staff/" + id + "/schedule";
    }

    @PostMapping("/{id}/leave/add")
    public String addLeave(@PathVariable String id,
                            @RequestParam String leaveFrom,
                            @RequestParam String leaveTo,
                            @RequestParam(defaultValue = "") String reason,
                            RedirectAttributes redirectAttributes) {
        try {
            staffService.addLeave(id, DateTimeUtil.parseDate(leaveFrom),
                    DateTimeUtil.parseDate(leaveTo), reason);
            redirectAttributes.addFlashAttribute("successMessage", "Leave recorded.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/staff/" + id + "/schedule";
    }

    @PostMapping("/schedule/{scheduleId}/cancel")
    public String cancelSchedule(@PathVariable String scheduleId,
                                  @RequestParam String staffId,
                                  RedirectAttributes redirectAttributes) {
        try {
            staffService.cancelSchedule(scheduleId);
            redirectAttributes.addFlashAttribute("successMessage", "Schedule entry cancelled.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/staff/" + staffId + "/schedule";
    }
}
