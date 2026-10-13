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
public class ServiceRepository {
    private final Path dataDir;
    private final IdGenerator idGenerator;
    private static final int FIELDS = 9;

    @Autowired
    public ServiceRepository(Path dataDirectory, IdGenerator idGenerator) {
        this.dataDir = dataDirectory;
        this.idGenerator = idGenerator;
    }

    private Path file() { return dataDir.resolve("services.txt"); }

    private SalonService fromParts(String[] p) {
        SalonService s = new SalonService();
        s.setId(p[0]);
        s.setName(p[1]);
        s.setDescription(p[2]);
        s.setCategory(p[3]);
        s.setDurationMinutes(Integer.parseInt(p[4]));
        s.setPrice(new BigDecimal(p[5]));
        s.setStatus(ServiceStatus.fromString(p[6]));
        s.setCreatedAt(DateTimeUtil.parseDateTime(p[7]));
        s.setUpdatedAt(DateTimeUtil.parseDateTime(p[8]));
        return s;
    }

    private String toLine(SalonService s) {
        return FileStorageUtil.joinLine(
            s.getId(), s.getName(), s.getDescription(), s.getCategory(),
            String.valueOf(s.getDurationMinutes()), s.getPrice().toPlainString(),
            s.getStatus().name(),
            DateTimeUtil.formatDateTime(s.getCreatedAt()),
            DateTimeUtil.formatDateTime(s.getUpdatedAt())
        );
    }

    public List<SalonService> findAll() {
        return FileStorageUtil.readAll(file(), this::fromParts, FIELDS);
    }

    public List<SalonService> findAllActive() {
        return findAll().stream().filter(SalonService::isActive).toList();
    }

    public Optional<SalonService> findById(String id) {
        return findAll().stream().filter(s -> s.getId().equals(id)).findFirst();
    }

    public SalonService save(SalonService service) throws IOException {
        if (service.getId() == null || service.getId().isBlank()) {
            service.setId(idGenerator.generate());
        }
        if (service.getCreatedAt() == null) service.setCreatedAt(DateTimeUtil.now());
        service.setUpdatedAt(DateTimeUtil.now());
        FileStorageUtil.appendLine(file(), toLine(service));
        return service;
    }

    public SalonService update(SalonService service) throws IOException {
        service.setUpdatedAt(DateTimeUtil.now());
        String updatedLine = toLine(service);
        FileStorageUtil.updateLines(file(), line -> {
            String[] parts = FileStorageUtil.splitLine(line);
            if (parts.length > 0 && service.getId().equals(parts[0])) return updatedLine;
            return line;
        });
        return service;
    }

    public void deleteById(String id) throws IOException {
        FileStorageUtil.deleteById(file(), id);
    }
}
