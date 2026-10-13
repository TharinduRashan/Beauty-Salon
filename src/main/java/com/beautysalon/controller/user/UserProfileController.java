package com.beautysalon.controller.user;

import com.beautysalon.exception.ValidationException;
import com.beautysalon.model.user.User;
import com.beautysalon.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/customer")
public class UserProfileController {

    private final UserService userService;

    @Autowired
    public UserProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        userService.findByUsername(auth.getName()).ifPresent(u -> model.addAttribute("user", u));
        return "dashboard/customer";
    }

    @GetMapping("/profile")
    public String profile(Authentication auth, Model model) {
        userService.findByUsername(auth.getName()).ifPresent(u -> model.addAttribute("user", u));
        return "user/profile";
    }

    @GetMapping("/profile/edit")
    public String editProfileForm(Authentication auth, Model model) {
        userService.findByUsername(auth.getName()).ifPresent(u -> model.addAttribute("user", u));
        return "user/edit-profile";
    }

    @PostMapping("/profile/edit")
    public String editProfile(Authentication auth,
                               @RequestParam String fullName,
                               @RequestParam String email,
                               @RequestParam(defaultValue = "") String phone,
                               RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByUsername(auth.getName())
                    .orElseThrow(() -> new ValidationException("User not found."));
            userService.updateProfile(user.getId(), fullName, phone, email);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/customer/profile";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(Authentication auth,
                                  @RequestParam String currentPassword,
                                  @RequestParam String newPassword,
                                  @RequestParam String confirmPassword,
                                  RedirectAttributes redirectAttributes) {
        try {
            if (!newPassword.equals(confirmPassword)) throw new ValidationException("Passwords do not match.");
            User user = userService.findByUsername(auth.getName())
                    .orElseThrow(() -> new ValidationException("User not found."));
            userService.changePassword(user.getId(), currentPassword, newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/customer/profile";
    }
}
