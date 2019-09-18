package com.maaksoft.file_operations;

import org.springframework.context.annotation.Bean;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.context.properties.EnableConfigurationProperties;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.maaksoft.file_operations.property.FopsProperties;

@SpringBootApplication
@EnableConfigurationProperties({FopsProperties.class})
public class FopsApplication {

    @Autowired
    private FopsProperties fopsProperties;

    public static void main(String[] args) {
	SpringApplication.run(FopsApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {

        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins(fopsProperties.getAllowedUrls().split(","));
            }

        };

    }
	
}
