package com.jao.loginAuth.infrastructure.adapter.out.persistence.repository;

import com.jao.loginAuth.infrastructure.adapter.out.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Spring Data JPA generará la implementación en tiempo de ejecución
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {
    
    // Método necesario para buscar un usuario por su email durante el login
    Optional<UserJpaEntity> findByEmail(String email);
}

