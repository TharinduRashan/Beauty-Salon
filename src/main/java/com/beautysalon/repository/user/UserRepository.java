package com.beautysalon.repository.user;

import com.beautysalon.model.user.*;
import com.beautysalon.util.DateTimeUtil;
import com.beautysalon.util.FileStorageUtil;
import com.beautysalon.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class UserRepository {
    private final Path dataDir;
    private final IdGenerator idGenerator;
    private static final int FIELDS = 10;

    @Autowired
    public UserRepository(Path dataDirectory, IdGenerator idGenerator) {
        this.dataDir = dataDirectory;
        this.idGenerator = idGenerator;
    }

    private Path file() { return dataDir.resolve("users.txt"); }

    private User fromParts(String[] p) {
        User u = new User();
        u.setId(p[0]);
        u.setUsername(p[1]);
        u.setEmail(p[2]);
        u.setPasswordHash(p[3]);
        u.setFullName(p[4]);
        u.setPhone(p[5]);
        u.setRole(UserRole.fromString(p[6]));
        u.setStatus(UserStatus.fromString(p[7]));
        u.setCreatedAt(DateTimeUtil.parseDateTime(p[8]));
        u.setUpdatedAt(DateTimeUtil.parseDateTime(p[9]));
        return u;
    }

    private String toLine(User u) {
        return FileStorageUtil.joinLine(
            u.getId(), u.getUsername(), u.getEmail(), u.getPasswordHash(),
            u.getFullName(), u.getPhone() != null ? u.getPhone() : "",
            u.getRole().name(), u.getStatus().name(),
            DateTimeUtil.formatDateTime(u.getCreatedAt()),
            DateTimeUtil.formatDateTime(u.getUpdatedAt())
        );
    }

    public List<User> findAll() {
        return FileStorageUtil.readAll(file(), this::fromParts, FIELDS);
    }

    public Optional<User> findById(String id) {
        return findAll().stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    public Optional<User> findByUsername(String username) {
        return findAll().stream().filter(u -> u.getUsername().equalsIgnoreCase(username)).findFirst();
    }

    public Optional<User> findByEmail(String email) {
        return findAll().stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    public User save(User user) throws IOException {
        if (user.getId() == null || user.getId().isBlank()) {
            user.setId(idGenerator.generate());
        }
        LocalDateTime now = DateTimeUtil.now();
        if (user.getCreatedAt() == null) user.setCreatedAt(now);
        user.setUpdatedAt(now);
        FileStorageUtil.appendLine(file(), toLine(user));
        return user;
    }

    public User update(User user) throws IOException {
        user.setUpdatedAt(DateTimeUtil.now());
        String updatedLine = toLine(user);
        FileStorageUtil.updateLines(file(), line -> {
            String[] parts = FileStorageUtil.splitLine(line);
            if (parts.length > 0 && user.getId().equals(parts[0])) return updatedLine;
            return line;
        });
        return user;
    }

    public void deleteById(String id) throws IOException {
        FileStorageUtil.deleteById(file(), id);
    }

    public List<User> findByRole(UserRole role) {
        return findAll().stream().filter(u -> u.getRole() == role).toList();
    }
}
