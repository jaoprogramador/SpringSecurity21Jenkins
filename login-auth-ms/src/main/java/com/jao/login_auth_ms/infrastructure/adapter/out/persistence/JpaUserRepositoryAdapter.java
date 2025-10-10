package com.jao.login_auth_ms.infrastructure.adapter.out.persistence;


import com.jao.login_auth_ms.domain.model.User;
import com.jao.login_auth_ms.domain.port.out.UserRepositoryPort;
import com.jao.login_auth_ms.infrastructure.adapter.out.persistence.entity.UserJpaEntity;
import com.jao.login_auth_ms.infrastructure.adapter.out.persistence.repository.UserJpaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker; 
// @Component actúa como el Adaptador de Salida
@Component
@RequiredArgsConstructor
@Slf4j
public class JpaUserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository jpaRepository;
    private final ModelMapper modelMapper;
 // Nombre del disyuntor definido en application.properties
    private static final String POSTGRES_CB = "postgresDb"; 

 // Método Fallback: se llama cuando el Circuit Breaker está Abierto o la llamada falla/supera el timeout
    // Devuelve un Optional vacío, asumiendo que el dominio puede manejar un usuario no encontrado.
    private Optional<User> fallbackFindByEmail(String email, Throwable t) {
        // Registrar el evento (SLF4J)
        log.error("Circuit Breaker POSTGRES_CB activado o fallo de DB para el email {}: {}", email, t.getMessage());
        // En un microservicio de login, si la DB falla, negamos el acceso (devuelve Optional vacío)
        return Optional.empty(); 
    }

    
    @Override
    @CircuitBreaker(name = POSTGRES_CB, fallbackMethod = "fallbackFindByEmail")
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(entity -> modelMapper.map(entity, User.class)); // Mapeo Entity -> Domain Model
    }

    @Override
    @CircuitBreaker(name = POSTGRES_CB, fallbackMethod = "fallbackSave")
    public User save(User user) {
        // Mapeo Domain Model -> Entity
        UserJpaEntity entity = modelMapper.map(user, UserJpaEntity.class);
        UserJpaEntity savedEntity = jpaRepository.save(entity);
        
        // Mapeo Entity -> Domain Model
        return modelMapper.map(savedEntity, User.class);
    }
    
 // Fallback para operaciones de escritura: Si la DB falla, la operación NO puede continuar.
    // Lanza una excepción para que el dominio la maneje (ej. 503 Service Unavailable).
    private User fallbackSave(User user, Throwable t) {
        log.error("Circuit Breaker POSTGRES_CB activado: Fallo en la escritura de usuario {}: {}", user.getEmail(), t.getMessage());
        throw new RuntimeException("Servicio de Persistencia de Usuarios No Disponible. Intente más tarde.");
    }
    
    // ---------------------- Métodos CRUD Restantes ----------------------
    
    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findById(id)
                .map(entity -> modelMapper.map(entity, User.class));
    }

    @Override
    public List<User> findAll() {
        // Uso de Streams y lambdas para mapear la lista
        return jpaRepository.findAll().stream()
                .map(entity -> modelMapper.map(entity, User.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}

