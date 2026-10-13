package com.beautysalon.repository.catalogue;

import com.beautysalon.model.catalogue.*;
import com.beautysalon.util.DateTimeUtil;
import com.beautysalon.util.FileStorageUtil;
import com.beautysalon.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.*;

@Repository
public class PackageRepository {
    private final Path dataDir;
    private final IdGenerator idGenerator;
    private static final int FIELDS = 9;

    @Autowired
    public PackageRepository(Path dataDirectory, IdGenerator idGenerator) {
        this.dataDir = dataDirectory;
        this.idGenerator = idGenerator;
    }

    private Path file() { return dataDir.resolve("packages.txt"); }

    private ServicePackage fromParts(String[] p) {
        ServicePackage pkg = new ServicePackage();
        pkg.setId(p[0]);
        pkg.setName(p[1]);
        pkg.setDescription(p[2]);
        pkg.setServiceIdsFromString(p[3]);
        pkg.setTotalPrice(new BigDecimal(p[4]));
        pkg.setDiscountPercent(new BigDecimal(p[5]));
        pkg.setStatus(ServiceStatus.fromString(p[6]));
        pkg.setCreatedAt(DateTimeUtil.parseDateTime(p[7]));
        pkg.setUpdatedAt(DateTimeUtil.parseDateTime(p[8]));
        return pkg;
    }

    private String toLine(ServicePackage pkg) {
        return FileStorageUtil.joinLine(
            pkg.getId(), pkg.getName(), pkg.getDescription(), pkg.getServiceIdsAsString(),
            pkg.getTotalPrice().toPlainString(), pkg.getDiscountPercent().toPlainString(),
            pkg.getStatus().name(),
            DateTimeUtil.formatDateTime(pkg.getCreatedAt()),
            DateTimeUtil.formatDateTime(pkg.getUpdatedAt())
        );
    }

    public List<ServicePackage> findAll() {
        return FileStorageUtil.readAll(file(), this::fromParts, FIELDS);
    }

    public List<ServicePackage> findAllActive() {
        return findAll().stream().filter(ServicePackage::isActive).toList();
    }

    public Optional<ServicePackage> findById(String id) {
        return findAll().stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public ServicePackage save(ServicePackage pkg) throws IOException {
        if (pkg.getId() == null || pkg.getId().isBlank()) pkg.setId(idGenerator.generate());
        if (pkg.getCreatedAt() == null) pkg.setCreatedAt(DateTimeUtil.now());
        pkg.setUpdatedAt(DateTimeUtil.now());
        FileStorageUtil.appendLine(file(), toLine(pkg));
        return pkg;
    }

    public ServicePackage update(ServicePackage pkg) throws IOException {
        pkg.setUpdatedAt(DateTimeUtil.now());
        String updatedLine = toLine(pkg);
        FileStorageUtil.updateLines(file(), line -> {
            String[] parts = FileStorageUtil.splitLine(line);
            if (parts.length > 0 && pkg.getId().equals(parts[0])) return updatedLine;
            return line;
        });
        return pkg;
    }

    public void deleteById(String id) throws IOException {
        FileStorageUtil.deleteById(file(), id);
    }
}
