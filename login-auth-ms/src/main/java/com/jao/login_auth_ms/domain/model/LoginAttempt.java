package com.jao.login_auth_ms.domain.model;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class LoginAttempt {

    private final String key; // Email o IP
    private int attempts;
    
    // Un método de dominio:
    public boolean isExceeded(int maxAttempts) {
        // Uso de lambda para una verificación sencilla y clara.
        return maxAttempts < attempts;
    }
    
    public void incrementAttempt() {
        this.attempts++;
    }
}

