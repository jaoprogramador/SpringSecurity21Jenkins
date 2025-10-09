package com.jao.login_auth_ms.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data // Proporciona getters, setters, toString, equals y hashCode (Lombok)
@NoArgsConstructor
@AllArgsConstructor
// NOTA: Esta clase es la entidad de Dominio, NO la @Entity de JPA (esa va en el adaptador)
public class User {

    private Long id;
    private String username; 
    private String email;
    private String passwordHash; // Contraseña ya hasheada y con salt (Security by Default)
    
    // CAMPOS DE SEGURIDAD (Security by Design/Default)
    private int failedLoginAttempts = 0;
    private boolean isAccountNonLocked = true; // El usuario está activo por defecto
    private LocalDateTime lockTime; // Timestamp del bloqueo (para posible desbloqueo temporal)
    
    // Rol simple para el ejemplo (para Spring Security)
    private String role = "USER"; 

    // Métodos de dominio para la lógica pura (usando lambdas en servicios/mappers)
    public boolean canLogin() {
        return this.isAccountNonLocked;
    }

    public void blockAccount() {
        this.isAccountNonLocked = false;
        this.lockTime = LocalDateTime.now();
    }
    
    public void resetLoginAttempts() {
        this.failedLoginAttempts = 0;
        this.isAccountNonLocked = true;
        this.lockTime = null;
    }
}
