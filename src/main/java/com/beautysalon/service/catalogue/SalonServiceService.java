package com.beautysalon.service.catalogue;

import com.beautysalon.exception.NotFoundException;
import com.beautysalon.exception.ValidationException;
import com.beautysalon.model.catalogue.SalonService;
import com.beautysalon.model.catalogue.ServiceStatus;
import com.beautysalon.repository.catalogue.ServiceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class SalonServiceService {

    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 100;
    private static final int MAX_DESCRIPTION_LENGTH = 500;

    private static final int MIN_DURATION = 15;
    private static final int MAX_DURATION = 480;

    private final ServiceRepository serviceRepository;

    @Autowired
    public SalonServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    // Retrieve all salon services
    public List<SalonService> findAll() {
        return serviceRepository.findAll();
    }

    // Retrieve only active salon services
    public List<SalonService> findAllActive() {
        return serviceRepository.findAllActive();
    }

    // Find a service using its unique ID
    public Optional<SalonService> findById(String id) {
        return serviceRepository.findById(id);
    }

    // Create a new salon service
    public SalonService create(String name, String description, String category,
                               int durationMinutes, BigDecimal price) throws IOException {

        validateService(name, description, category, durationMinutes, price);

        SalonService service = new SalonService();

        service.setName(name.trim());
        service.setDescription(description == null ? "" : description.trim());
        service.setCategory(category.trim());
        service.setDurationMinutes(durationMinutes);
        service.setPrice(price);
        service.setStatus(ServiceStatus.ACTIVE);

        return serviceRepository.save(service);
    }

    // Update an existing salon service
    public SalonService update(String id, String name, String description, String category,
                               int durationMinutes, BigDecimal price) throws IOException {

        SalonService service = getServiceOrThrow(id);

        validateService(name, description, category, durationMinutes, price);

        service.setName(name.trim());
        service.setDescription(description == null ? "" : description.trim());
        service.setCategory(category.trim());
        service.setDurationMinutes(durationMinutes);
        service.setPrice(price);

        return serviceRepository.update(service);
    }

    // Activate or deactivate a salon service
    public SalonService toggleStatus(String id) throws IOException {

        SalonService service = getServiceOrThrow(id);

        if (service.isActive()) {
            service.setStatus(ServiceStatus.INACTIVE);
        } else {
            service.setStatus(ServiceStatus.ACTIVE);
        }

        return serviceRepository.update(service);
    }

    // Delete a salon service
    public void delete(String id) throws IOException {

        getServiceOrThrow(id);

        serviceRepository.deleteById(id);
    }

    // Find a service or throw an exception if it does not exist
    private SalonService getServiceOrThrow(String id) {

        return serviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service", id));
    }

    // Validate all service details before saving
    private void validateService(String name, String description, String category,
                                 int durationMinutes, BigDecimal price) {

        if (name == null || name.isBlank()) {
            throw new ValidationException("Service name is required.");
        }

        String trimmedName = name.trim();

        if (trimmedName.length() < MIN_NAME_LENGTH ||
                trimmedName.length() > MAX_NAME_LENGTH) {
            throw new ValidationException(
                    "Service name must be between 2 and 100 characters."
            );
        }

        if (description != null &&
                description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new ValidationException(
                    "Description cannot exceed 500 characters."
            );
        }

        if (category == null || category.isBlank()) {
            throw new ValidationException("Category is required.");
        }

        if (durationMinutes < MIN_DURATION ||
                durationMinutes > MAX_DURATION) {
            throw new ValidationException(
                    "Duration must be between 15 and 480 minutes."
            );
        }

        if (price == null || price.signum() < 0) {
            throw new ValidationException(
                    "Price must be a valid non-negative amount."
            );
        }
    }

    // Insert default salon services when the repository is empty
    public void seedServicesIfAbsent() throws IOException {

        if (!serviceRepository.findAll().isEmpty()) {
            return;
        }

        create("Classic Haircut",
                "Professional haircut and styling for all hair types.",
                "Hair", 45, new BigDecimal("2500.00"));

        create("Hair Coloring",
                "Full hair color treatment with premium products.",
                "Hair", 120, new BigDecimal("6500.00"));

        create("Classic Manicure",
                "Complete nail care including shaping, cuticle care, and polish.",
                "Nails", 60, new BigDecimal("1800.00"));

        create("Luxury Facial",
                "Deep cleansing facial with hydration and anti-aging treatment.",
                "Skin", 75, new BigDecimal("4500.00"));

        create("Classic Pedicure",
                "Foot care treatment including soak, exfoliation, and polish.",
                "Nails", 75, new BigDecimal("2200.00"));

        create("Eyebrow Threading",
                "Precise eyebrow shaping using the traditional threading technique.",
                "Beauty", 20, new BigDecimal("800.00"));

        create("Body Massage",
                "Full body relaxation massage with essential oils.",
                "Body", 90, new BigDecimal("5500.00"));
    }
}
