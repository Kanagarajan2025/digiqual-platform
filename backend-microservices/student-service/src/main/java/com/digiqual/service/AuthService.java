package com.digiqual.service;

import com.digiqual.dto.LoginRequest;
import com.digiqual.dto.LoginResponse;
import com.digiqual.entity.User;
import com.digiqual.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }
    
    public LoginResponse login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        
        if (userOptional.isEmpty()) {
            return new LoginResponse(false, "Invalid email or password");
        }
        
        User user = userOptional.get();
        
        // Check if user is active
        if (!user.isActive()) {
            return new LoginResponse(false, "Your account has been suspended. Please contact admin.");
        }
        
        // Check role match
        if (!user.getRole().toString().equals(request.getRole())) {
            return new LoginResponse(false, "Invalid role for this account");
        }
        
        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new LoginResponse(false, "Invalid email or password");
        }
        
        // Generate JWT token
        String token = jwtService.generateToken(user.getEmail(), user.getRole().toString());
        
        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        
        return new LoginResponse(
            true,
            "Login successful",
            token,
            user.getEmail(),
            user.getRole().toString(),
            user.getId().toString()
        );
    }
}
