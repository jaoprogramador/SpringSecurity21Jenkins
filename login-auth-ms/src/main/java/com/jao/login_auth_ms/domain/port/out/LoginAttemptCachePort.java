package com.jao.login_auth_ms.domain.port.out;


import java.util.Optional;

import com.jao.login_auth_ms.domain.model.LoginAttempt;

// Contrato para la caché de intentos fallidos (Implementado por Redis Adapter)
public interface LoginAttemptCachePort {
    
    Optional<LoginAttempt> getAttempt(String key);
    void saveAttempt(LoginAttempt attempt);
    void removeAttempt(String key);
}

