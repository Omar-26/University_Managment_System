package com.egabi.university;

import com.egabi.university.config.JwtConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtConfig.class)
public class UniversityApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(UniversityApplication.class, args);
    }
    
}
