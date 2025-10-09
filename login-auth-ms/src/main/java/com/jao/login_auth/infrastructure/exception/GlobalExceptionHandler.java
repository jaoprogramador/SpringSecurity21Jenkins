package com.jao.login_auth.infrastructure.exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// Objeto para estandarizar el formato de error
record ErrorResponse(String error, String message, Map<String, String> details) {}

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 1. Control de Excepciones de Validación (Restricción: Validaciones al Login)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        
        // Uso de Streams y lambdas para recolectar errores de campo
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                    FieldError::getField,
                    (fieldError) -> Optional.ofNullable(fieldError.getDefaultMessage()).orElse("Error de validación")
                ));
        
        log.warn("Error de validación: {}", errors);
        
        ErrorResponse errorResponse = new ErrorResponse(
            "Validation Error", 
            "La solicitud falló la validación de entrada.", 
            errors
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 2. Control de Errores de Dominio (Ejemplo: Credenciales Inválidas o Cuenta Bloqueada)
    // Se usa RuntimeException para simplicidad, pero se recomienda una excepción de dominio específica
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleDomainExceptions(RuntimeException ex) {
        
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // Por defecto
        String errorType = "Internal Error";
        
        if (ex.getMessage().contains("Credenciales inválidas") || ex.getMessage().contains("Usuario no encontrado")) {
            status = HttpStatus.UNAUTHORIZED;
            errorType = "Authentication Failed";
        } else if (ex.getMessage().contains("Cuenta Bloqueada")) {
            status = HttpStatus.FORBIDDEN;
            errorType = "Account Locked";
        } else if (ex.getMessage().contains("not found")) { // Para errores de recursos no encontrados
            status = HttpStatus.NOT_FOUND;
            errorType = "Resource Not Found";
        }
        
        log.error("Excepción de Dominio/Lógica: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(errorType, ex.getMessage(), new HashMap<>());
        return new ResponseEntity<>(errorResponse, status);
    }
}

