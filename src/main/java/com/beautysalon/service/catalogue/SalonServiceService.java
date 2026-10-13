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

    private final ServiceRepository serviceRepository;

    @Autowired
    public SalonServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public List<SalonService> findAll() {
        return serviceRepository.findAll();
    }

    public List<SalonService> findAllActive() {
        return serviceRepository.findAllActive();
    }

    public Optional<SalonService> findById(String id) {
        return serviceRepository.findById(id);
    }

    public SalonService create(String name, String description, String category,
                               int durationMinutes, BigDecimal price) throws IOException {
        validateService(name, description, category, durationMinutes, price);
        SalonService service = new SalonService();
        service.setName(name);
        service.setDescription(description);
        service.setCategory(category);
        service.setDurationMinutes(durationMinutes);
        service.setPrice(price);
        service.setStatus(ServiceStatus.ACTIVE);
        return serviceRepository.save(service);
    }

    public SalonService update(String id, String name, String description, String category,
                               int durationMinutes, BigDecimal price) throws IOException {
        SalonService service = serviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service", id));
        validateService(name, description, category, durationMinutes, price);
        service.setName(name);
        service.setDescription(description);
        service.setCategory(category);
        service.setDurationMinutes(durationMinutes);
        service.setPrice(price);
        return serviceRepository.update(service);
    }

    public SalonService toggleStatus(String id) throws IOException {
        SalonService service = serviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service", id));
        service.setStatus(service.isActive() ? ServiceStatus.INACTIVE : ServiceStatus.ACTIVE);
        return serviceRepository.update(service);
    }

    public void delete(String id) throws IOException {
        serviceRepository.findById(id).orElseThrow(() -> new NotFoundException("Service", id));
        serviceRepository.deleteById(id);
    }

    private void validateService(String name, String description, String category,
                                  int durationMinutes, BigDecimal price) {
        if (name == null || name.length() < 2 || name.length() > 100)
            throw new ValidationException("Service name must be 2-100 characters.");
        if (category == null || category.isBlank())
            throw new ValidationException("Category is required.");
        if (durationMinutes < 15 || durationMinutes > 480)
            throw new ValidationException("Duration must be 15-480 minutes.");
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0)
            throw new ValidationException("Price must be non-negative.");
    }

    public void seedServicesIfAbsent() throws IOException {
        if (serviceRepository.findAll().isEmpty()) {
            create("Classic Haircut", "Professional haircut and styling for all hair types.", "Hair", 45, new BigDecimal("2500.00"));
            create("Hair Coloring", "Full hair color treatment with premium products.", "Hair", 120, new BigDecimal("6500.00"));
            create("Classic Manicure", "Complete nail care for hands including shaping, cuticle care, and polish.", "Nails", 60, new BigDecimal("1800.00"));
            create("Luxury Facial", "Deep cleansing facial with hydration and anti-aging treatment.", "Skin", 75, new BigDecimal("4500.00"));
            create("Classic Pedicure", "Foot care treatment including soak, exfoliation, and polish.", "Nails", 75, new BigDecimal("2200.00"));
            create("Eyebrow Threading", "Precise eyebrow shaping using the traditional threading technique.", "Beauty", 20, new BigDecimal("800.00"));
            create("Body Massage", "Full body relaxation massage with essential oils.", "Body", 90, new BigDecimal("5500.00"));
        }
    }
}
