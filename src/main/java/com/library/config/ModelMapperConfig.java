package com.library.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.STRICT)
            .setSkipNullEnabled(true)
            .setAmbiguityIgnored(true);
        
        // Custom type mapping for String to String
        modelMapper.createTypeMap(String.class, String.class)
            .setConverter(context -> context.getSource() != null ? context.getSource().trim() : null);
            
        return modelMapper;
    }
}
