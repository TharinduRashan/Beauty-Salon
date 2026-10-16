package com.beautysalon.controller.staff;

import com.beautysalon.service.staff.StaffService;
import com.beautysalon.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/staff")
@PreAuthorize("hasAnyRole('STAFF','ADMIN')")
public class StaffController {

    private final StaffService staffService;
    private final UserService userService;

    @Autowired
    public StaffController(StaffService staffService, UserService userService) {
        this.staffService = staffService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        userService.findByUsername(auth.getName()).ifPresent(u -> {
            model.addAttribute("user", u);
            staffService.findByUserId(u.getId()).ifPresent(s -> model.addAttribute("staff", s));
        });
        return "dashboard/staff";
    }

    @GetMapping("/schedule")
    public String mySchedule(Authentication auth, Model model) {
        userService.findByUsername(auth.getName()).ifPresent(u -> {
            staffService.findByUserId(u.getId()).ifPresent(s -> {
                model.addAttribute("staff", s);
                model.addAttribute("schedules", staffService.findScheduleByStaffId(s.getId()));
            });
        });
        return "staff/my-schedule";
    }
}
