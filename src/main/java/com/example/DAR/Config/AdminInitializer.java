package com.example.DAR.Config;

import com.example.DAR.Model.User;
import com.example.DAR.Repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class AdminInitializer {
    @Value("${adminPass}")
    private String adminPass;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostConstruct
    public void initAdmin() {

        if (userRepository.findUserByUsername("admin") == null) {
            User admin = new User();

            admin.setName("Main Admin");
            admin.setEmail("admin@dar.com");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode(adminPass));
            admin.setPhoneNumber("966500000000");
            admin.setRole("ADMIN");
            admin.setCreateAt(LocalDate.now());
            admin.setAiCounter(0);
            admin.setSmartAlertsEnabled(true);
            admin.setSmartAlertIntroSent(false);

            userRepository.save(admin);
        }
    }
}
