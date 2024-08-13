package com.flux;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@OpenAPIDefinition
@EntityScan
@EnableScheduling
public class FluxApplication {
    public static void main(String[] args) {
        SpringApplication.run(FluxApplication.class, args);
    }
}
