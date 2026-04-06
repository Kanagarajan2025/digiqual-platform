package com.digiqual.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;
    
    @Column(nullable = false)
    private String fullName;
    
    @Column(nullable = false)
    private boolean active = true;
    
    @Column(nullable = false)
    private boolean emailVerified = false;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    
    // Relations can be added later: Partner details, Student details
    
    public enum UserRole {
        STUDENT,
        PARTNER,
        ADMIN
    }
}
