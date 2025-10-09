package com.jao.login_auth_ms.application.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        // Usamos una lambda para configurar la estrategia de mapeo (Ejemplo de uso de lambda)
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        
        // Aquí se podrían añadir mapeos personalizados con lambdas si las clases difieren
        // mapper.createTypeMap(Source.class, Destination.class)
        //       .addMappings(mapping -> mapping.map(Source::getField, Destination::setOtherField));

        return mapper;
    }
}
