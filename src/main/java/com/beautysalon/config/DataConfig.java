package com.beautysalon.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class DataConfig {

    private static final Logger log = LoggerFactory.getLogger(DataConfig.class);

    @Value("${app.data.dir:./data}")
    private String dataDir;

    @Bean
    public Path dataDirectory() throws IOException {
        Path dir = Paths.get(dataDir).toAbsolutePath();
        Files.createDirectories(dir);
        log.info("Data directory: {}", dir);

        // Ensure all data files exist
        String[] files = {
            "users.txt", "services.txt", "packages.txt",
            "staff.txt", "staff_schedules.txt", "appointments.txt",
            "payments.txt", "feedback.txt"
        };
        for (String file : files) {
            Path filePath = dir.resolve(file);
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
                log.info("Created data file: {}", filePath);
            }
        }
        return dir;
    }
}
