package com.beautysalon.controller.payment;

import com.beautysalon.model.payment.PaymentMethod;
import com.beautysalon.model.user.User;
import com.beautysalon.service.payment.PaymentService;
import com.beautysalon.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/customer")
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;

    @Autowired
    public PaymentController(PaymentService paymentService, UserService userService) {
        this.paymentService = paymentService;
        this.userService = userService;
    }

    @GetMapping("/payments")
    public String myPayments(Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName()).orElse(null);
        if (user != null) model.addAttribute("payments", paymentService.findByCustomerId(user.getId()));
        return "payment/my-payments";
    }

    @GetMapping("/payments/checkout/{appointmentId}")
    public String checkout(@PathVariable String appointmentId, Model model,
                            RedirectAttributes redirectAttributes) {
        try {
            var payment = paymentService.getOrCreatePayment(appointmentId);
            if (payment.isPaid()) return "redirect:/customer/payments/receipt/" + payment.getId();
            model.addAttribute("payment", payment);
            model.addAttribute("methods", PaymentMethod.values());
            return "payment/checkout";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/customer/appointments";
        }
    }

    @PostMapping("/payments/checkout/{paymentId}/process")
    public String processPayment(@PathVariable String paymentId,
                                  @RequestParam String paymentMethod,
                                  RedirectAttributes redirectAttributes) {
        try {
            paymentService.processPayment(paymentId, PaymentMethod.fromString(paymentMethod));
            redirectAttributes.addFlashAttribute("successMessage", "Payment processed successfully!");
            return "redirect:/customer/payments/receipt/" + paymentId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/customer/payments";
        }
    }

    @GetMapping("/payments/receipt/{paymentId}")
    public String receipt(@PathVariable String paymentId, Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName()).orElse(null);
        paymentService.findById(paymentId).ifPresent(p -> {
            if (user != null && (user.getId().equals(p.getCustomerId()) || user.getRole().name().equals("ADMIN"))) {
                model.addAttribute("payment", p);
            }
        });
        return "payment/receipt";
    }
}
