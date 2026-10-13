package com.beautysalon.controller;

import com.beautysalon.service.catalogue.SalonServiceService;
import com.beautysalon.service.feedback.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final SalonServiceService serviceService;
    private final FeedbackService feedbackService;

    @Value("${salon.name:Glamour Beauty Salon}")
    private String salonName;

    @Autowired
    public HomeController(SalonServiceService serviceService, FeedbackService feedbackService) {
        this.serviceService = serviceService;
        this.feedbackService = feedbackService;
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("salonName", salonName);
        model.addAttribute("featuredServices", serviceService.findAllActive().stream().limit(4).toList());
        model.addAttribute("approvedReviews", feedbackService.findApproved().stream().limit(3).toList());
        return "home/index";
    }

    @GetMapping({"/membership", "/rewards"})
    public String membership(Model model) {
        model.addAttribute("salonName", salonName);
        return "home/membership";
    }

    @GetMapping("/error/403")
    public String accessDenied(Model model) {
        model.addAttribute("errorTitle", "Access Denied");
        model.addAttribute("errorMessage", "You do not have permission to view this page.");
        return "error/403";
    }

    @GetMapping("/error/404")
    public String notFound(Model model) {
        model.addAttribute("errorTitle", "Page Not Found");
        model.addAttribute("errorMessage", "The page you're looking for doesn't exist.");
        return "error/404";
    }
}
