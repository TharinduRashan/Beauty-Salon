package com.beautysalon.service.user;

import com.beautysalon.exception.ConflictException;
import com.beautysalon.exception.NotFoundException;
import com.beautysalon.exception.ValidationException;
import com.beautysalon.model.user.*;
import com.beautysalon.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String username, String email, String rawPassword, String fullName, String phone) throws IOException {
        if (username == null || username.length() < 3) throw new ValidationException("Username must be at least 3 characters.");
        if (!username.matches("[a-zA-Z0-9_]+")) throw new ValidationException("Username may only contain letters, digits, and underscores.");
        if (email == null || !email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) throw new ValidationException("Invalid email address.");
        if (rawPassword == null || rawPassword.length() < 8) throw new ValidationException("Password must be at least 8 characters.");
        if (fullName == null || fullName.length() < 2) throw new ValidationException("Full name must be at least 2 characters.");
        if (userRepository.existsByUsername(username)) throw new ConflictException("Username '" + username + "' is already taken.");
        if (userRepository.existsByEmail(email)) throw new ConflictException("Email '" + email + "' is already registered.");

        User user = new User();
        user.setUsername(username);
        user.setEmail(email.toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setRole(UserRole.CUSTOMER);
        user.setStatus(UserStatus.ACTIVE);
        return userRepository.save(user);
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User updateProfile(String userId, String fullName, String phone, String email) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));
        if (fullName != null && fullName.length() >= 2) user.setFullName(fullName);
        if (phone != null) user.setPhone(phone);
        if (email != null && email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
            Optional<User> existing = userRepository.findByEmail(email);
            if (existing.isPresent() && !existing.get().getId().equals(userId)) {
                throw new ConflictException("Email is already in use.");
            }
            user.setEmail(email.toLowerCase());
        }
        return userRepository.update(user);
    }

    public void changePassword(String userId, String currentPassword, String newPassword) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));
        if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            throw new ValidationException("Current password is incorrect.");
        }
        if (newPassword == null || newPassword.length() < 8) {
            throw new ValidationException("New password must be at least 8 characters.");
        }
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.update(user);
    }

    public User updateStatus(String userId, UserStatus newStatus) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));
        user.setStatus(newStatus);
        return userRepository.update(user);
    }

    public User updateRole(String userId, UserRole newRole) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));
        user.setRole(newRole);
        return userRepository.update(user);
    }

    public void deleteUser(String userId) throws IOException {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User", userId));
        userRepository.deleteById(userId);
    }

    public void seedAdminIfAbsent() throws IOException {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@glamourbeauty.lk");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setFullName("System Administrator");
            admin.setPhone("+94112345678");
            admin.setRole(UserRole.ADMIN);
            admin.setStatus(UserStatus.ACTIVE);
            userRepository.save(admin);
        }
    }
}
