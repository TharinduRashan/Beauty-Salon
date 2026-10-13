package com.beautysalon.controller.user;

import com.beautysalon.exception.ConflictException;
import com.beautysalon.exception.ValidationException;
import com.beautysalon.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                             @RequestParam(required = false) String logout,
                             Model model) {
        if (error != null) model.addAttribute("errorMessage", "Invalid username or password.");
        if (logout != null) model.addAttribute("successMessage", "You have been logged out successfully.");
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(defaultValue = "") String confirmPassword,
            @RequestParam String fullName,
            @RequestParam(defaultValue = "") String phone,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            if (!password.equals(confirmPassword)) {
                model.addAttribute("errorMessage", "Passwords do not match.");
                model.addAttribute("username", username);
                model.addAttribute("email", email);
                model.addAttribute("fullName", fullName);
                model.addAttribute("phone", phone);
                return "auth/register";
            }
            userService.register(username, email, password, fullName, phone);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Registration successful! Please log in.");
            return "redirect:/login";
        } catch (ConflictException | ValidationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            model.addAttribute("fullName", fullName);
            model.addAttribute("phone", phone);
            return "auth/register";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Registration failed. Please try again.");
            return "auth/register";
        }
    }
}
