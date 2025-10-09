package com.jao.login_auth_ms.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    
    private Long id;
    private String username;
    private String email;
    
    // Campos de Seguridad
    private int failedLoginAttempts;
    private boolean isAccountNonLocked;
    private LocalDateTime lockTime;
    private String role;
}

