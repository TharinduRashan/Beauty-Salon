package com.beautysalon.repository.feedback;

import com.beautysalon.model.feedback.*;
import com.beautysalon.util.DateTimeUtil;
import com.beautysalon.util.FileStorageUtil;
import com.beautysalon.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

@Repository
public class FeedbackRepository {
    private final Path dataDir;
    private final IdGenerator idGenerator;
    private static final int FIELDS = 10;

    @Autowired
    public FeedbackRepository(Path dataDirectory, IdGenerator idGenerator) {
        this.dataDir = dataDirectory;
        this.idGenerator = idGenerator;
    }

    private Path file() { return dataDir.resolve("feedback.txt"); }

    private Feedback fromParts(String[] p) {
        Feedback f = new Feedback();
        f.setId(p[0]);
        f.setCustomerId(p[1]);
        f.setAppointmentId(p[2].isBlank() ? null : p[2]);
        f.setType(FeedbackType.fromString(p[3]));
        f.setRating(p[4].isBlank() ? null : Integer.parseInt(p[4]));
        f.setTitle(p[5]);
        f.setContent(p[6]);
        f.setStatus(FeedbackStatus.fromString(p[7]));
        f.setCreatedAt(DateTimeUtil.parseDateTime(p[8]));
        f.setUpdatedAt(DateTimeUtil.parseDateTime(p[9]));
        return f;
    }

    private String toLine(Feedback f) {
        return FileStorageUtil.joinLine(
            f.getId(), f.getCustomerId(),
            f.getAppointmentId() != null ? f.getAppointmentId() : "",
            f.getType().name(),
            f.getRating() != null ? String.valueOf(f.getRating()) : "",
            f.getTitle(), f.getContent(), f.getStatus().name(),
            DateTimeUtil.formatDateTime(f.getCreatedAt()),
            DateTimeUtil.formatDateTime(f.getUpdatedAt())
        );
    }

    public List<Feedback> findAll() {
        return FileStorageUtil.readAll(file(), this::fromParts, FIELDS);
    }

    public Optional<Feedback> findById(String id) {
        return findAll().stream().filter(f -> f.getId().equals(id)).findFirst();
    }

    public List<Feedback> findByCustomerId(String customerId) {
        return findAll().stream().filter(f -> customerId.equals(f.getCustomerId())).toList();
    }

    public List<Feedback> findApproved() {
        return findAll().stream().filter(Feedback::isApproved).toList();
    }

    public boolean existsByAppointmentIdAndCustomerId(String appointmentId, String customerId) {
        return findAll().stream()
                .anyMatch(f -> appointmentId.equals(f.getAppointmentId()) && customerId.equals(f.getCustomerId()));
    }

    public Feedback save(Feedback f) throws IOException {
        if (f.getId() == null || f.getId().isBlank()) f.setId(idGenerator.generate());
        if (f.getCreatedAt() == null) f.setCreatedAt(DateTimeUtil.now());
        f.setUpdatedAt(DateTimeUtil.now());
        FileStorageUtil.appendLine(file(), toLine(f));
        return f;
    }

    public Feedback update(Feedback f) throws IOException {
        f.setUpdatedAt(DateTimeUtil.now());
        String updatedLine = toLine(f);
        FileStorageUtil.updateLines(file(), line -> {
            String[] parts = FileStorageUtil.splitLine(line);
            if (parts.length > 0 && f.getId().equals(parts[0])) return updatedLine;
            return line;
        });
        return f;
    }

    public void deleteById(String id) throws IOException {
        FileStorageUtil.deleteById(file(), id);
    }
}
