package com.jao.login_auth_ms.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    
    // Aplicamos validación de entrada
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El formato del email es incorrecto")
    private String email;
    
    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;
}

