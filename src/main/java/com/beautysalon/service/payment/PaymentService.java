package com.beautysalon.service.payment;

import com.beautysalon.exception.ConflictException;
import com.beautysalon.exception.NotFoundException;
import com.beautysalon.exception.ValidationException;
import com.beautysalon.model.appointment.Appointment;
import com.beautysalon.model.appointment.AppointmentStatus;
import com.beautysalon.model.payment.*;
import com.beautysalon.repository.appointment.AppointmentRepository;
import com.beautysalon.repository.payment.PaymentRepository;
import com.beautysalon.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.10"); // 10% VAT (simulated)

    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository,
                           AppointmentRepository appointmentRepository,
                           UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    public Optional<Payment> findById(String id) {
        return paymentRepository.findById(id).map(this::enrich);
    }

    public Optional<Payment> findByAppointmentId(String appointmentId) {
        return paymentRepository.findByAppointmentId(appointmentId).map(this::enrich);
    }

    public List<Payment> findByCustomerId(String customerId) {
        return paymentRepository.findByCustomerId(customerId).stream()
                .map(this::enrich).toList();
    }

    public List<Payment> findAll() {
        return paymentRepository.findAll().stream().map(this::enrich).toList();
    }

    /**
     * Gets or creates a PENDING payment record for an appointment.
     * Prevents duplicate payment creation.
     */
    public Payment getOrCreatePayment(String appointmentId) throws IOException {
        // Check for existing payment
        Optional<Payment> existing = paymentRepository.findByAppointmentId(appointmentId);
        if (existing.isPresent()) {
            return enrich(existing.get());
        }
        // Create new payment
        Appointment appt = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Appointment", appointmentId));
        if (appt.getStatus() != AppointmentStatus.CONFIRMED && appt.getStatus() != AppointmentStatus.COMPLETED)
            throw new ValidationException("Payment can only be created for CONFIRMED or COMPLETED appointments.");

        BigDecimal amount = appt.getPriceSnapshot() != null ? appt.getPriceSnapshot() : BigDecimal.ZERO;
        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal tax = amount.subtract(discount).multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = amount.subtract(discount).add(tax);

        Payment payment = new Payment();
        payment.setAppointmentId(appointmentId);
        payment.setCustomerId(appt.getCustomerId());
        payment.setAmount(amount);
        payment.setDiscount(discount);
        payment.setTax(tax);
        payment.setTotal(total);
        payment.setPaymentMethod(PaymentMethod.CASH); // default
        payment.setPaymentStatus(PaymentStatus.PENDING);
        return enrich(paymentRepository.save(payment));
    }

    public Payment processPayment(String paymentId, PaymentMethod method) throws IOException {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment", paymentId));
        if (payment.getPaymentStatus() == PaymentStatus.PAID)
            throw new ConflictException("Payment is already completed.");
        payment.setPaymentMethod(method);
        payment.setPaymentStatus(PaymentStatus.PAID);
        // Generate simulated transaction reference
        payment.setTransactionRef("SIM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        return enrich(paymentRepository.update(payment));
    }

    public Payment refund(String paymentId, String reason) throws IOException {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment", paymentId));
        if (payment.getPaymentStatus() != PaymentStatus.PAID)
            throw new ValidationException("Only PAID payments can be refunded.");
        payment.setPaymentStatus(PaymentStatus.REFUNDED);
        payment.setNotes("Refund: " + reason);
        return enrich(paymentRepository.update(payment));
    }

    private Payment enrich(Payment payment) {
        appointmentRepository.findById(payment.getAppointmentId()).ifPresent(a -> {
            payment.setServiceNameSnapshot(a.getServiceNameSnapshot());
            payment.setAppointmentDateStr(a.getFormattedDate());
        });
        userRepository.findById(payment.getCustomerId()).ifPresent(u ->
                payment.setCustomerName(u.getFullName()));
        return payment;
    }
}
