package com.beautysalon.controller.feedback;

import com.beautysalon.model.feedback.FeedbackType;
import com.beautysalon.model.user.User;
import com.beautysalon.service.appointment.AppointmentService;
import com.beautysalon.service.feedback.FeedbackService;
import com.beautysalon.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final UserService userService;
    private final AppointmentService appointmentService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService, UserService userService,
                               AppointmentService appointmentService) {
        this.feedbackService = feedbackService;
        this.userService = userService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/reviews")
    public String publicReviews(Model model) {
        model.addAttribute("reviews", feedbackService.findApproved());
        return "feedback/reviews";
    }

    @GetMapping("/customer/feedback")
    public String myFeedback(Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName()).orElse(null);
        if (user != null) model.addAttribute("feedbacks", feedbackService.findByCustomerId(user.getId()));
        return "feedback/my-feedback";
    }

    @GetMapping("/customer/feedback/submit")
    public String submitForm(Authentication auth, Model model,
                              @RequestParam(required = false) String appointmentId) {
        User user = userService.findByUsername(auth.getName()).orElse(null);
        if (user != null) {
            var completedAppts = appointmentService.findByCustomerId(user.getId())
                    .stream().filter(a -> a.getStatus().name().equals("COMPLETED")).toList();
            model.addAttribute("completedAppointments", completedAppts);
        }
        model.addAttribute("feedbackTypes", FeedbackType.values());
        model.addAttribute("selectedAppointmentId", appointmentId);
        return "feedback/submit";
    }

    @PostMapping("/customer/feedback/submit")
    public String submitFeedback(Authentication auth,
                                  @RequestParam String type,
                                  @RequestParam(required = false) String appointmentId,
                                  @RequestParam(required = false) Integer rating,
                                  @RequestParam String title,
                                  @RequestParam String content,
                                  RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByUsername(auth.getName())
                    .orElseThrow(() -> new RuntimeException("User not found."));
            FeedbackType fType = FeedbackType.fromString(type);
            if (fType == FeedbackType.REVIEW && appointmentId != null && !appointmentId.isBlank()) {
                feedbackService.submitReview(user.getId(), appointmentId, rating != null ? rating : 5, title, content);
            } else {
                feedbackService.submitGeneralFeedback(user.getId(), fType, title, content);
            }
            redirectAttributes.addFlashAttribute("successMessage", "Feedback submitted! It will be reviewed shortly.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/customer/feedback";
    }
}
