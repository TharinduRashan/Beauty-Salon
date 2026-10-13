package com.beautysalon.config;

import com.beautysalon.service.catalogue.SalonServiceService;
import com.beautysalon.service.staff.StaffService;
import com.beautysalon.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UserService userService;
    private final SalonServiceService serviceService;
    private final StaffService staffService;
    private final com.beautysalon.service.catalogue.PackageService packageService;
    private final com.beautysalon.service.feedback.FeedbackService feedbackService;

    @Autowired
    public DataSeeder(UserService userService,
                      SalonServiceService serviceService,
                      StaffService staffService,
                      com.beautysalon.service.catalogue.PackageService packageService,
                      com.beautysalon.service.feedback.FeedbackService feedbackService) {
        this.userService = userService;
        this.serviceService = serviceService;
        this.staffService = staffService;
        this.packageService = packageService;
        this.feedbackService = feedbackService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Seeding initial data...");
        userService.seedAdminIfAbsent();
        serviceService.seedServicesIfAbsent();
        packageService.seedPackagesIfAbsent();
        staffService.seedStaffIfAbsent();
        feedbackService.seedFeedbackIfAbsent();
        log.info("Data seeding complete.");
    }
}
