package com.beautysalon.repository.staff;

import com.beautysalon.model.staff.*;
import com.beautysalon.util.DateTimeUtil;
import com.beautysalon.util.FileStorageUtil;
import com.beautysalon.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

@Repository
public class StaffRepository {
    private final Path dataDir;
    private final IdGenerator idGenerator;
    private static final int FIELDS = 10;

    @Autowired
    public StaffRepository(Path dataDirectory, IdGenerator idGenerator) {
        this.dataDir = dataDirectory;
        this.idGenerator = idGenerator;
    }

    private Path file() { return dataDir.resolve("staff.txt"); }

    private StaffMember fromParts(String[] p) {
        StaffMember s = new StaffMember();
        s.setId(p[0]);
        s.setUserId(p[1]);
        s.setFullName(p[2]);
        s.setEmail(p[3]);
        s.setPhone(p[4]);
        s.setSpecialtiesFromString(p[5]);
        s.setBio(p[6]);
        s.setStatus(StaffStatus.fromString(p[7]));
        s.setCreatedAt(DateTimeUtil.parseDateTime(p[8]));
        s.setUpdatedAt(DateTimeUtil.parseDateTime(p[9]));
        return s;
    }

    private String toLine(StaffMember s) {
        return FileStorageUtil.joinLine(
            s.getId(), s.getUserId() != null ? s.getUserId() : "",
            s.getFullName(), s.getEmail(), s.getPhone() != null ? s.getPhone() : "",
            s.getSpecialtiesAsString(), s.getBio() != null ? s.getBio() : "",
            s.getStatus().name(),
            DateTimeUtil.formatDateTime(s.getCreatedAt()),
            DateTimeUtil.formatDateTime(s.getUpdatedAt())
        );
    }

    public List<StaffMember> findAll() {
        return FileStorageUtil.readAll(file(), this::fromParts, FIELDS);
    }

    public List<StaffMember> findAllActive() {
        return findAll().stream().filter(StaffMember::isActive).toList();
    }

    public Optional<StaffMember> findById(String id) {
        return findAll().stream().filter(s -> s.getId().equals(id)).findFirst();
    }

    public Optional<StaffMember> findByUserId(String userId) {
        return findAll().stream().filter(s -> userId.equals(s.getUserId())).findFirst();
    }

    public StaffMember save(StaffMember s) throws IOException {
        if (s.getId() == null || s.getId().isBlank()) s.setId(idGenerator.generate());
        if (s.getCreatedAt() == null) s.setCreatedAt(DateTimeUtil.now());
        s.setUpdatedAt(DateTimeUtil.now());
        FileStorageUtil.appendLine(file(), toLine(s));
        return s;
    }

    public StaffMember update(StaffMember s) throws IOException {
        s.setUpdatedAt(DateTimeUtil.now());
        String updatedLine = toLine(s);
        FileStorageUtil.updateLines(file(), line -> {
            String[] parts = FileStorageUtil.splitLine(line);
            if (parts.length > 0 && s.getId().equals(parts[0])) return updatedLine;
            return line;
        });
        return s;
    }

    public void deleteById(String id) throws IOException {
        FileStorageUtil.deleteById(file(), id);
    }
}
