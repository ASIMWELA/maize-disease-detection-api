package com.detection.maize.disease.config;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public Hibernate5Module hibernateModule(){
        return new Hibernate5Module();
    }
}
