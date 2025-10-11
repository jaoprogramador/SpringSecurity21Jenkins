//package com.jao.login_auth.infrastructure.adapter.out.cache;
  package com.jao.login_auth_ms.infrastructure.adapter.out.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.jao.login_auth_ms.domain.model.LoginAttempt;
import com.jao.login_auth_ms.domain.port.out.LoginAttemptCachePort;

import java.time.Duration;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RedisLoginAttemptAdapter implements LoginAttemptCachePort {

    // Se inyecta el RedisTemplate configurado
    private final RedisTemplate<String, Object> redisTemplate;
    
    // Tiempo de vida de la clave en Redis (Ejemplo: 1 hora)
    private static final Duration KEY_TTL = Duration.ofHours(1);
    
    // La clave es el email del usuario
    private static final String REDIS_KEY_PREFIX = "login_attempt:";

    // Uso de lambdas y Optional<T>
    @Override
    public Optional<LoginAttempt> getAttempt(String key) {
        String fullKey = REDIS_KEY_PREFIX + key;
        
        // Recuperar el objeto, casteándolo a LoginAttempt
        // Nota: Esto funciona porque configuramos el GenericJackson2JsonRedisSerializer
        Object result = redisTemplate.opsForValue().get(fullKey);

        // Uso de Optional.ofNullable y mapeo para asegurar un retorno limpio
        return Optional.ofNullable(result)
                .filter(LoginAttempt.class::isInstance) // Asegura que es del tipo correcto
                .map(LoginAttempt.class::cast);
    }

    @Override
    public void saveAttempt(LoginAttempt attempt) {
        String fullKey = REDIS_KEY_PREFIX + attempt.getKey();
        
        // Almacenar el objeto y establecer un TTL (Time To Live)
        // Esto permite que el contador se resetee automáticamente tras un tiempo.
        redisTemplate.opsForValue().set(fullKey, attempt, KEY_TTL);
    }

    @Override
    public void removeAttempt(String key) {
        String fullKey = REDIS_KEY_PREFIX + key;
        
        // Eliminar la clave de Redis
        // Uso de lambda: el método 'delete' puede aceptar un lambda/función.
        Optional.ofNullable(redisTemplate.delete(fullKey))
                .filter(Boolean::booleanValue)
                .ifPresent(deleted -> System.out.println("Clave Redis eliminada para: " + key));
    }
}
