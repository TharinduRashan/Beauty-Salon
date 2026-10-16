package com.beautysalon.service.feedback;

import com.beautysalon.exception.ConflictException;
import com.beautysalon.exception.NotFoundException;
import com.beautysalon.exception.ValidationException;
import com.beautysalon.model.appointment.AppointmentStatus;
import com.beautysalon.model.feedback.*;
import com.beautysalon.repository.appointment.AppointmentRepository;
import com.beautysalon.repository.feedback.FeedbackRepository;
import com.beautysalon.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository,
                            AppointmentRepository appointmentRepository,
                            UserRepository userRepository) {
        this.feedbackRepository = feedbackRepository;
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    public List<Feedback> findAll() { return feedbackRepository.findAll().stream().map(this::enrich).toList(); }
    public List<Feedback> findApproved() { return feedbackRepository.findApproved().stream().map(this::enrich).toList(); }
    public Optional<Feedback> findById(String id) { return feedbackRepository.findById(id).map(this::enrich); }
    public List<Feedback> findByCustomerId(String customerId) {
        return feedbackRepository.findByCustomerId(customerId).stream().map(this::enrich).toList();
    }

    public Feedback submitReview(String customerId, String appointmentId, int rating,
                                  String title, String content) throws IOException {
        var appt = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Appointment", appointmentId));
        if (!appt.getCustomerId().equals(customerId))
            throw new ValidationException("You can only review your own appointments.");
        if (appt.getStatus() != AppointmentStatus.COMPLETED)
            throw new ValidationException("Reviews can only be submitted for completed appointments.");
        if (feedbackRepository.existsByAppointmentIdAndCustomerId(appointmentId, customerId))
            throw new ConflictException("You have already submitted a review for this appointment.");
        validateContent(title, content);
        if (rating < 1 || rating > 5) throw new ValidationException("Rating must be between 1 and 5.");

        Feedback f = new Feedback();
        f.setCustomerId(customerId);
        f.setAppointmentId(appointmentId);
        f.setType(FeedbackType.REVIEW);
        f.setRating(rating);
        f.setTitle(title);
        f.setContent(content);
        f.setStatus(FeedbackStatus.PENDING);
        return enrich(feedbackRepository.save(f));
    }

    public Feedback submitGeneralFeedback(String customerId, FeedbackType type,
                                           String title, String content) throws IOException {
        validateContent(title, content);
        Feedback f = new Feedback();
        f.setCustomerId(customerId);
        f.setType(type != null ? type : FeedbackType.GENERAL);
        f.setTitle(title);
        f.setContent(content);
        f.setStatus(FeedbackStatus.PENDING);
        return enrich(feedbackRepository.save(f));
    }

    public Feedback moderate(String feedbackId, FeedbackStatus newStatus) throws IOException {
        Feedback f = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new NotFoundException("Feedback", feedbackId));
        f.setStatus(newStatus);
        return enrich(feedbackRepository.update(f));
    }

    public void delete(String feedbackId) throws IOException {
        feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new NotFoundException("Feedback", feedbackId));
        feedbackRepository.deleteById(feedbackId);
    }

    private void validateContent(String title, String content) {
        if (title == null || title.isBlank() || title.length() > 100)
            throw new ValidationException("Title is required and must be under 100 characters.");
        if (content == null || content.length() < 10 || content.length() > 1000)
            throw new ValidationException("Content must be 10-1000 characters.");
    }

    private Feedback enrich(Feedback f) {
        userRepository.findById(f.getCustomerId()).ifPresent(u -> f.setCustomerName(u.getFullName()));
        if (f.getAppointmentId() != null) {
            appointmentRepository.findById(f.getAppointmentId())
                    .ifPresent(a -> f.setServiceNameSnapshot(a.getServiceNameSnapshot()));
        }
        return f;
    }

    public void seedFeedbackIfAbsent() throws IOException {
        if (feedbackRepository.findAll().isEmpty()) {
            var admin = userRepository.findByUsername("admin").orElse(null);
            String adminId = admin != null ? admin.getId() : "seed-admin";
            Feedback f1 = new Feedback();
            f1.setCustomerId(adminId);
            f1.setType(FeedbackType.REVIEW);
            f1.setRating(5);
            f1.setTitle("Exquisite Luxury & Craftsmanship");
            f1.setContent("The therapists are attentive, the ambiance is serene, and the hair styling exceeded all my expectations!");
            f1.setStatus(FeedbackStatus.APPROVED);
            feedbackRepository.save(f1);

            Feedback f2 = new Feedback();
            f2.setCustomerId(adminId);
            f2.setType(FeedbackType.REVIEW);
            f2.setRating(5);
            f2.setTitle("Mindful Sanctuary and Glowing Skin");
            f2.setContent("The Luxury Facial was heavenly. My skin has never felt softer or looked brighter. Highly recommend Amaya and the team.");
            f2.setStatus(FeedbackStatus.APPROVED);
            feedbackRepository.save(f2);
        }
    }
}
