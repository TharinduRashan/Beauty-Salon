package com.beautysalon.service.catalogue;

import com.beautysalon.exception.NotFoundException;
import com.beautysalon.exception.ValidationException;
import com.beautysalon.model.catalogue.SalonService;
import com.beautysalon.model.catalogue.ServicePackage;
import com.beautysalon.model.catalogue.ServiceStatus;
import com.beautysalon.repository.catalogue.PackageRepository;
import com.beautysalon.repository.catalogue.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PackageService {

    private final PackageRepository packageRepository;
    private final ServiceRepository serviceRepository;

    @Autowired
    public PackageService(PackageRepository packageRepository, ServiceRepository serviceRepository) {
        this.packageRepository = packageRepository;
        this.serviceRepository = serviceRepository;
    }

    public List<ServicePackage> findAll() { return packageRepository.findAll(); }
    public List<ServicePackage> findAllActive() { return packageRepository.findAllActive(); }
    public Optional<ServicePackage> findById(String id) { return packageRepository.findById(id); }

    public ServicePackage create(String name, String description, List<String> serviceIds,
                                  BigDecimal totalPrice, BigDecimal discountPercent) throws IOException {
        validate(name, serviceIds, totalPrice, discountPercent);
        ServicePackage pkg = new ServicePackage();
        pkg.setName(name);
        pkg.setDescription(description);
        pkg.setServiceIds(serviceIds);
        pkg.setTotalPrice(totalPrice);
        pkg.setDiscountPercent(discountPercent);
        pkg.setStatus(ServiceStatus.ACTIVE);
        return packageRepository.save(pkg);
    }

    public ServicePackage update(String id, String name, String description, List<String> serviceIds,
                                  BigDecimal totalPrice, BigDecimal discountPercent) throws IOException {
        ServicePackage pkg = packageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Package", id));
        validate(name, serviceIds, totalPrice, discountPercent);
        pkg.setName(name);
        pkg.setDescription(description);
        pkg.setServiceIds(serviceIds);
        pkg.setTotalPrice(totalPrice);
        pkg.setDiscountPercent(discountPercent);
        return packageRepository.update(pkg);
    }

    public ServicePackage toggleStatus(String id) throws IOException {
        ServicePackage pkg = packageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Package", id));
        pkg.setStatus(pkg.isActive() ? ServiceStatus.INACTIVE : ServiceStatus.ACTIVE);
        return packageRepository.update(pkg);
    }

    public void delete(String id) throws IOException {
        packageRepository.findById(id).orElseThrow(() -> new NotFoundException("Package", id));
        packageRepository.deleteById(id);
    }

    private void validate(String name, List<String> serviceIds, BigDecimal price, BigDecimal discount) {
        if (name == null || name.length() < 2) throw new ValidationException("Package name must be at least 2 characters.");
        if (serviceIds == null || serviceIds.isEmpty()) throw new ValidationException("Package must include at least one service.");
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) throw new ValidationException("Package price must be positive.");
        if (discount == null || discount.compareTo(BigDecimal.ZERO) < 0 || discount.compareTo(new BigDecimal("100")) > 0)
            throw new ValidationException("Discount must be 0-100.");
    }

    public void seedPackagesIfAbsent() throws IOException {
        if (packageRepository.findAll().isEmpty()) {
            List<SalonService> all = serviceRepository.findAll();
            if (!all.isEmpty()) {
                List<String> bridalServiceIds = all.stream()
                        .filter(s -> s.getName().contains("Facial") || s.getName().contains("Manicure") || s.getName().contains("Threading"))
                        .map(SalonService::getId).toList();
                if (!bridalServiceIds.isEmpty()) {
                    create("Bridal Glow Sanctuary", "Comprehensive radiance package including Deep Cleansing Facial, Manicure, and Eyebrow Threading.",
                            bridalServiceIds, new BigDecimal("6500.00"), new BigDecimal("15.00"));
                }
                List<String> hairNailsIds = all.stream()
                        .filter(s -> s.getName().contains("Haircut") || s.getName().contains("Manicure") || s.getName().contains("Pedicure"))
                        .map(SalonService::getId).toList();
                if (!hairNailsIds.isEmpty()) {
                    create("Signature Hair & Nails Indulgence", "The complete salon makeover package with precision cut, manicure, and pedicure care.",
                            hairNailsIds, new BigDecimal("5800.00"), new BigDecimal("10.00"));
                }
                List<String> massageFacialIds = all.stream()
                        .filter(s -> s.getName().contains("Massage") || s.getName().contains("Facial"))
                        .map(SalonService::getId).toList();
                if (!massageFacialIds.isEmpty()) {
                    create("Total Rejuvenation Retreat", "Harmonizing full body essential oil massage coupled with our luxury facial.",
                            massageFacialIds, new BigDecimal("8900.00"), new BigDecimal("20.00"));
                }
            }
        }
    }
}
