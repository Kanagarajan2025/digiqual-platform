package com.digiqual.config;

import com.digiqual.entity.User;
import com.digiqual.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    
    @Bean
    public CommandLineRunner initializeData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Only create demo users if they don't exist
            if (!userRepository.existsByEmail("admin@digiqual.com")) {
                User admin = new User();
                admin.setEmail("admin@digiqual.com");
                admin.setPassword(passwordEncoder.encode("Admin@123"));
                admin.setFullName("Super Admin");
                admin.setRole(User.UserRole.ADMIN);
                admin.setActive(true);
                admin.setEmailVerified(true);
                userRepository.save(admin);
                System.out.println("✓ Admin user created");
            }
            
            if (!userRepository.existsByEmail("partner@digiqual.com")) {
                User partner = new User();
                partner.setEmail("partner@digiqual.com");
                partner.setPassword(passwordEncoder.encode("Partner@123"));
                partner.setFullName("Partner Institute");
                partner.setRole(User.UserRole.PARTNER);
                partner.setActive(true);
                partner.setEmailVerified(true);
                userRepository.save(partner);
                System.out.println("✓ Partner user created");
            }
            
            if (!userRepository.existsByEmail("student@digiqual.com")) {
                User student = new User();
                student.setEmail("student@digiqual.com");
                student.setPassword(passwordEncoder.encode("Student@123"));
                student.setFullName("Student User");
                student.setRole(User.UserRole.STUDENT);
                student.setActive(true);
                student.setEmailVerified(true);
                userRepository.save(student);
                System.out.println("✓ Student user created");
            }
        };
    }
}
