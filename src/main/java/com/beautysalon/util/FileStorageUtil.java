package com.beautysalon.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

/**
 * Thread-safe utility for reading and writing pipe-delimited text files.
 * Uses ReentrantReadWriteLock per file path and temp-file atomic replace.
 */
public class FileStorageUtil {

    private static final Logger log = LoggerFactory.getLogger(FileStorageUtil.class);
    private static final String DELIMITER = "|";
    private static final String ESCAPED_PIPE = "\\pipe";
    private static final String ESCAPED_NEWLINE = "\\newline";
    private static final Map<String, ReentrantReadWriteLock> LOCKS = new HashMap<>();

    // ── Escaping ────────────────────────────────────────────────────────────

    public static String escape(String value) {
        if (value == null) return "";
        return value
                .replace("\\", "\\\\")  // escape backslash first
                .replace("|", ESCAPED_PIPE)
                .replace("\n", ESCAPED_NEWLINE)
                .replace("\r", "");
    }

    public static String unescape(String value) {
        if (value == null) return "";
        return value
                .replace(ESCAPED_NEWLINE, "\n")
                .replace(ESCAPED_PIPE, "|")
                .replace("\\\\", "\\");
    }

    public static String[] splitLine(String line) {
        // Split on unescaped pipe
        String[] parts = line.split("\\|", -1);
        String[] result = new String[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = unescape(parts[i]);
        }
        return result;
    }

    public static String joinLine(String... fields) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) sb.append(DELIMITER);
            sb.append(escape(fields[i]));
        }
        return sb.toString();
    }

    // ── Lock management ─────────────────────────────────────────────────────

    private static synchronized ReentrantReadWriteLock getLock(Path path) {
        return LOCKS.computeIfAbsent(path.toAbsolutePath().toString(),
                k -> new ReentrantReadWriteLock());
    }

    // ── Read ────────────────────────────────────────────────────────────────

    public static List<String> readLines(Path filePath) throws IOException {
        ensureFileExists(filePath);
        ReentrantReadWriteLock lock = getLock(filePath);
        lock.readLock().lock();
        try {
            return Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } finally {
            lock.readLock().unlock();
        }
    }

    public static <T> List<T> readAll(Path filePath, Function<String[], T> mapper, int expectedFields) {
        try {
            List<String> lines = readLines(filePath);
            List<T> result = new ArrayList<>();
            for (String line : lines) {
                if (line.isBlank() || line.startsWith("#")) continue;
                String[] parts = splitLine(line);
                if (parts.length < expectedFields) {
                    log.warn("Skipping malformed record in {}: {}", filePath.getFileName(), line);
                    continue;
                }
                try {
                    T obj = mapper.apply(parts);
                    if (obj != null) result.add(obj);
                } catch (Exception e) {
                    log.warn("Error parsing record in {}: {} | Error: {}",
                            filePath.getFileName(), line, e.getMessage());
                }
            }
            return result;
        } catch (IOException e) {
            log.error("Failed to read file {}: {}", filePath, e.getMessage());
            return new ArrayList<>();
        }
    }

    // ── Write ───────────────────────────────────────────────────────────────

    public static void appendLine(Path filePath, String line) throws IOException {
        ensureFileExists(filePath);
        ReentrantReadWriteLock lock = getLock(filePath);
        lock.writeLock().lock();
        try {
            Files.writeString(filePath, line + System.lineSeparator(),
                    StandardCharsets.UTF_8, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Replaces all lines, filtered by predicate, with updated records using atomic rename.
     * @param filePath the data file
     * @param lineTransformer transforms each line; return null to delete the line
     */
    public static void updateLines(Path filePath,
                                   Function<String, String> lineTransformer) throws IOException {
        ensureFileExists(filePath);
        ReentrantReadWriteLock lock = getLock(filePath);
        lock.writeLock().lock();
        try {
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            Path temp = filePath.resolveSibling(filePath.getFileName() + ".tmp");
            try (BufferedWriter writer = Files.newBufferedWriter(temp, StandardCharsets.UTF_8)) {
                for (String line : lines) {
                    if (line.isBlank() || line.startsWith("#")) {
                        writer.write(line);
                        writer.newLine();
                        continue;
                    }
                    String transformed = lineTransformer.apply(line);
                    if (transformed != null) {
                        writer.write(transformed);
                        writer.newLine();
                    }
                }
            }
            Files.move(temp, filePath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Deletes all lines matching the predicate (by ID field at position 0).
     */
    public static void deleteById(Path filePath, String id) throws IOException {
        updateLines(filePath, line -> {
            if (line.isBlank() || line.startsWith("#")) return line;
            String[] parts = splitLine(line);
            if (parts.length > 0 && id.equals(parts[0])) return null;
            return line;
        });
    }

    // ── Utilities ───────────────────────────────────────────────────────────

    public static void ensureFileExists(Path filePath) throws IOException {
        if (!Files.exists(filePath)) {
            Files.createDirectories(filePath.getParent());
            Files.createFile(filePath);
        }
    }

    public static boolean existsById(Path filePath, String id, int idFieldIndex) {
        try {
            List<String> lines = readLines(filePath);
            for (String line : lines) {
                if (line.isBlank() || line.startsWith("#")) continue;
                String[] parts = splitLine(line);
                if (parts.length > idFieldIndex && id.equals(parts[idFieldIndex])) return true;
            }
        } catch (IOException e) {
            log.error("existsById error: {}", e.getMessage());
        }
        return false;
    }

    public static boolean existsByField(Path filePath, int fieldIndex, String value) {
        try {
            List<String> lines = readLines(filePath);
            for (String line : lines) {
                if (line.isBlank() || line.startsWith("#")) continue;
                String[] parts = splitLine(line);
                if (parts.length > fieldIndex && value.equalsIgnoreCase(parts[fieldIndex].trim())) return true;
            }
        } catch (IOException e) {
            log.error("existsByField error: {}", e.getMessage());
        }
        return false;
    }
}
