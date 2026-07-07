package com.saludlink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SaludLinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaludLinkApplication.class, args);
    }
}
