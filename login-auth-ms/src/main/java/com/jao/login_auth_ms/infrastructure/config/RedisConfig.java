package com.jao.login_auth_ms.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        // Serializador para las claves (usaremos el email/username como clave)
        template.setKeySerializer(new StringRedisSerializer());
        
        // Serializador para los valores (el objeto LoginAttempt)
        // GenericJackson2JsonRedisSerializer utiliza Jackson para convertir el objeto a JSON
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        
        // Para asegurar que los hashes también usen string
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        
        template.afterPropertiesSet();
        return template;
    }
}

