package com.company.map;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
@OpenAPIDefinition(
        info = @Info(
                title = "Rest API",
                description = "MapBank",
                version = "v1"
        )
)
public class MapBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(MapBankApplication.class, args);
    }

}
