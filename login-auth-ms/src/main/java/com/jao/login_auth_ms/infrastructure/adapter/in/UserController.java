package com.jao.login_auth_ms.infrastructure.adapter.in;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jao.login_auth_ms.application.dto.UserCreateRequest;
import com.jao.login_auth_ms.application.dto.UserResponse;
import com.jao.login_auth_ms.domain.model.User;
import com.jao.login_auth_ms.domain.port.in.UserManagementPort;

import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserManagementPort userManagementPort;
    private final ModelMapper modelMapper;

    // POST: Crear nuevo usuario (Crea la cuenta con la clave ya hasheada)
    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserCreateRequest request) {
        // Mapeo DTO de Entrada -> Modelo de Dominio
        User domainUser = modelMapper.map(request, User.class);
        
        // La lógica de hashear la clave debe ocurrir antes de persistir, generalmente en el Domain Service.
        // Aquí asumimos que el Domain Service UserManagementPort maneja el hasheo
        User savedUser = userManagementPort.save(domainUser);
        
        // Mapeo Modelo de Dominio -> DTO de Salida
        UserResponse response = modelMapper.map(savedUser, UserResponse.class);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET: Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        List<User> users = userManagementPort.findAll();
        
        // Uso de Streams y lambdas para mapear la lista de Domain Models a DTOs de Respuesta
        List<UserResponse> responseList = users.stream()
            .map(user -> modelMapper.map(user, UserResponse.class))
            .collect(Collectors.toList());
            
        return ResponseEntity.ok(responseList);
    }
    
    // GET: Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        User user = userManagementPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado")); // Control de excepción

        UserResponse response = modelMapper.map(user, UserResponse.class);
        return ResponseEntity.ok(response);
    }

    // PUT: Actualizar completamente (requiere todos los campos)
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @Valid @RequestBody UserCreateRequest request) {
        User domainUser = modelMapper.map(request, User.class);
        domainUser.setId(id); // Asegurar el ID

        User updatedUser = userManagementPort.save(domainUser); // Asume que save maneja el update si el ID existe
        UserResponse response = modelMapper.map(updatedUser, UserResponse.class);
        return ResponseEntity.ok(response);
    }
    
    // PATCH: Actualizar parcialmente (debería usar un DTO específico para el patch)
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> updatePartial(@PathVariable Long id, @RequestBody Object partialUpdateRequest) {
        // En una implementación real, aquí se usaría un DTO de Patch y lógica de reflection/mapeo
        // Para simplificar, asumimos que el puerto de dominio maneja la lógica de actualización parcial.
        
        User updatedUser = userManagementPort.updatePartial(id, modelMapper.map(partialUpdateRequest, User.class));
        UserResponse response = modelMapper.map(updatedUser, UserResponse.class);
        return ResponseEntity.ok(response);
    }

    // DELETE: Eliminar por ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userManagementPort.deleteById(id);
    }
}
