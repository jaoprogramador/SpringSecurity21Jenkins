package com.jao.login_auth_ms.domain.port.out;


import java.util.List;
import java.util.Optional;

import com.jao.login_auth_ms.domain.model.User;

// Contrato para la persistencia de usuarios (Implementado por JPA Adapter)
public interface UserRepositoryPort {
    
    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    List<User> findAll();
    void deleteById(Long id);
}
