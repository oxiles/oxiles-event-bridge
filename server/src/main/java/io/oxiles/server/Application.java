package io.oxiles.server;

import io.oxiles.annotation.EnableEventeum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude= RabbitAutoConfiguration.class)
@EnableEventeum
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
