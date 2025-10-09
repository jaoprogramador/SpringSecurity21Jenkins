package com.jao.login_auth_ms.domain.service;

import com.jao.login_auth_ms.domain.model.User;
import com.jao.login_auth_ms.domain.port.in.UserManagementPort;
import com.jao.login_auth_ms.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementPort {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public User save(User user) {
        // Lógica crucial: Hashear la contraseña antes de persistir
        if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
            // El DTO de entrada (UserCreateRequest) tiene un campo 'password'.
            // Al mapear a User, usamos ese campo para el hashing.
            String rawPassword = user.getPasswordHash(); 
            user.setPasswordHash(passwordEncoder.encode(rawPassword));
        }
        return userRepositoryPort.save(user);
    }
    
    // ... Implementar los otros métodos de CRUD que faltan ...
    
    @Override
    public Optional<User> findById(Long id) {
        return userRepositoryPort.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepositoryPort.findAll();
    }

    @Override
    public void deleteById(Long id) {
        userRepositoryPort.deleteById(id);
    }
    
    @Override
    public User updatePartial(Long id, User userDetails) {
        // Lógica de PATCH: Obtener el existente, aplicar cambios, guardar.
        User existingUser = userRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado para la actualización."));

        if (userDetails.getUsername() != null) {
            existingUser.setUsername(userDetails.getUsername());
        }
        if (userDetails.getEmail() != null) {
            existingUser.setEmail(userDetails.getEmail());
        }
        if (userDetails.getPasswordHash() != null && !userDetails.getPasswordHash().isEmpty()) {
            existingUser.setPasswordHash(passwordEncoder.encode(userDetails.getPasswordHash())); // Hashear si cambia
        }

        return userRepositoryPort.save(existingUser);
    }
}

