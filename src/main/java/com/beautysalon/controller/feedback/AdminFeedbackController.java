package com.beautysalon.controller.feedback;

import com.beautysalon.model.feedback.FeedbackStatus;
import com.beautysalon.service.feedback.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminFeedbackController {

    private final FeedbackService feedbackService;

    @Autowired
    public AdminFeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping("/feedback")
    public String listFeedback(Model model) {
        model.addAttribute("feedbacks", feedbackService.findAll());
        return "feedback/admin/moderate";
    }

    @PostMapping("/feedback/{id}/approve")
    public String approve(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            feedbackService.moderate(id, FeedbackStatus.APPROVED);
            redirectAttributes.addFlashAttribute("successMessage", "Feedback approved.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/feedback";
    }

    @PostMapping("/feedback/{id}/reject")
    public String reject(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            feedbackService.moderate(id, FeedbackStatus.REJECTED);
            redirectAttributes.addFlashAttribute("successMessage", "Feedback rejected.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/feedback";
    }

    @PostMapping("/feedback/{id}/delete")
    public String delete(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            feedbackService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Feedback deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/feedback";
    }
}
