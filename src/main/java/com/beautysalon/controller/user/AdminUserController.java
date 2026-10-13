package com.beautysalon.controller.user;

import com.beautysalon.model.user.User;
import com.beautysalon.model.user.UserRole;
import com.beautysalon.model.user.UserStatus;
import com.beautysalon.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserService userService;

    @Autowired
    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("totalUsers", users.size());
        model.addAttribute("activeUsers", users.stream().filter(User::isActive).count());
        return "dashboard/admin";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/admin/list";
    }

    @PostMapping("/users/{id}/status")
    public String updateStatus(@PathVariable String id, @RequestParam String status,
                                RedirectAttributes redirectAttributes) {
        try {
            userService.updateStatus(id, UserStatus.fromString(status));
            redirectAttributes.addFlashAttribute("successMessage", "User status updated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/role")
    public String updateRole(@PathVariable String id, @RequestParam String role,
                              RedirectAttributes redirectAttributes) {
        try {
            userService.updateRole(id, UserRole.fromString(role));
            redirectAttributes.addFlashAttribute("successMessage", "User role updated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "User deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/users";
    }
}
