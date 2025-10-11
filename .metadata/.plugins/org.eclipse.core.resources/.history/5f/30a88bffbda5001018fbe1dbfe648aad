package com.jao.login_auth_ms.domain.service;

import com.jao.login_auth.infrastructure.config.JwtService;
import com.jao.login_auth_ms.domain.model.LoginAttempt;
import com.jao.login_auth_ms.domain.model.User;
import com.jao.login_auth_ms.domain.port.in.LoginUseCase;
import com.jao.login_auth_ms.domain.port.out.EmailNotificationPort;
import com.jao.login_auth_ms.domain.port.out.LoginAttemptCachePort;
import com.jao.login_auth_ms.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

// Usamos @Service para que Spring lo reconozca como un bean, 
// aunque en el Hexagonal es la implementación del puerto de entrada.
@Service 
@RequiredArgsConstructor // Inyección por constructor gracias a Lombok
public class LoginAuthService implements LoginUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final LoginAttemptCachePort attemptCachePort;
    private final EmailNotificationPort emailNotificationPort;
    private final PasswordEncoder passwordEncoder; // Usado para hashear/comparar claves
    private final JwtService jwtService; 
    private static final int MAX_ATTEMPTS = 3;
    @Override
    public String authenticate(String email, String rawPassword) {
        
        // 1. Buscar Usuario
        User user = userRepositoryPort.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Credenciales inválidas")); 

        // 2. Controlar Estado de Bloqueo 
        if (!user.isAccountNonLocked()) {
            throw new RuntimeException("Cuenta Bloqueada. Revisa tu email.");
        }

        // 3. Verificar Contraseña
        if (passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            
            // ÉXITO: Reiniciar contadores
            user.resetLoginAttempts();
            userRepositoryPort.save(user); 
            attemptCachePort.removeAttempt(email); 
            
            // 🔥 IMPLEMENTACIÓN FINAL DEL JWT
            // Genera el token usando el email (subject) y el rol (claim)
            return jwtService.generateToken(user.getEmail(), user.getRole()); 

        } else {
            
            // FALLO
            handleFailedLogin(user);
            throw new RuntimeException("Credenciales inválidas"); 
        }
    }
	/* Version1
	 * =========
	 * @Override public String authenticate(String email, String rawPassword) {
	 * 
	 * // 1. Buscar Usuario User user = userRepositoryPort.findByEmail(email)
	 * .orElseThrow(() -> new RuntimeException("Credenciales inválidas")); //
	 * Control de excepciones: No encontrado
	 * 
	 * // 2. Controlar Estado de Bloqueo (Security by Design) if
	 * (!user.isAccountNonLocked()) { // El usuario ya está bloqueado, lanza una
	 * excepción de negocio throw new
	 * RuntimeException("Cuenta Bloqueada. Revisa tu email."); }
	 * 
	 * // 3. Verificar Contraseña (Uso del PasswordEncoder con Hash y Salt) if
	 * (passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
	 * 
	 * // ÉXITO: Reiniciar contadores user.resetLoginAttempts();
	 * userRepositoryPort.save(user); // Persistir el estado limpio
	 * attemptCachePort.removeAttempt(email); // Limpiar la caché de intentos
	 * 
	 * // TODO: Generar y devolver un JWT (Aquí iría la lógica de generación del
	 * token) return "SUCCESS_TOKEN_FOR_" + user.getEmail();
	 * 
	 * } else {
	 * 
	 * // FALLO: Incrementar contador de intentos handleFailedLogin(user); throw new
	 * RuntimeException("Credenciales inválidas"); // Devolver error genérico
	 * 
	 * } }
	 */

    /**
     * Lógica que gestiona el intento fallido de login. 
     * Usa la caché (Redis) para el conteo rápido y la DB/Email para el bloqueo final.
     */
    private void handleFailedLogin(User user) {
        final String key = user.getEmail();
        
        // Usamos la lambda `orElseGet` para crear un nuevo intento si no existe.
        LoginAttempt attempt = attemptCachePort.getAttempt(key)
                .orElseGet(() -> new LoginAttempt(key, 0));

        attempt.incrementAttempt();
        
        if (attempt.isExceeded(MAX_ATTEMPTS)) {
            // Se ha superado el límite (3 intentos).
            
            // 1. Bloquear cuenta en el Dominio y Persistencia
            user.blockAccount();
            userRepositoryPort.save(user);
            
            // 2. Notificar al usuario (Puerto de Salida)
            emailNotificationPort.notifyAccountLocked(user.getEmail(), user.getUsername());
            
            // 3. Eliminar la clave de la caché (ya que está bloqueado en DB)
            attemptCachePort.removeAttempt(key); 
            
        } else {
            // Aún no se supera el límite. Guardar el nuevo contador en la caché (TTL corto)
            attemptCachePort.saveAttempt(attempt);
        }
    }
}

