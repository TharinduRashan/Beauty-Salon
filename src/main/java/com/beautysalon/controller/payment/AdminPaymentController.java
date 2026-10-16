package com.beautysalon.controller.payment;

import com.beautysalon.service.payment.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPaymentController {

    private final PaymentService paymentService;

    @Autowired
    public AdminPaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/payments")
    public String paymentReports(Model model) {
        model.addAttribute("payments", paymentService.findAll());
        return "payment/admin/reports";
    }

    @PostMapping("/payments/{id}/refund")
    public String refund(@PathVariable String id, @RequestParam String reason,
                          RedirectAttributes redirectAttributes) {
        try {
            paymentService.refund(id, reason);
            redirectAttributes.addFlashAttribute("successMessage", "Payment refunded.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/payments";
    }
}
