package com.jao.loginAuth.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String passwordHash; // Ya incluye el salt
    
    // CAMPOS DE SEGURIDAD
    private int failedLoginAttempts = 0;
    private boolean isAccountNonLocked = true;
    private LocalDateTime lockTime;
    private String role; // Rol simple para Spring Security
}

