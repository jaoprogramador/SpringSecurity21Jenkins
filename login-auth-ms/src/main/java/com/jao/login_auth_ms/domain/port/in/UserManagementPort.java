package com.jao.login_auth_ms.domain.port.in;

import java.util.List;
import java.util.Optional;

import com.jao.login_auth_ms.domain.model.User;

// Interface para el caso de uso de administración de usuarios (CRUD)
public interface UserManagementPort {
    
    User save(User user); // POST y PUT
    Optional<User> findById(Long id); // GET by ID
    List<User> findAll(); // GET All
    void deleteById(Long id); // DELETE
    User updatePartial(Long id, User userDetails); // PATCH
}
