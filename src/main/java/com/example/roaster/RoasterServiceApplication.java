package com.example.roaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RoasterServiceApplication {

    public static void main(String[] args) {
        System.out.println("ROASTER SERVICE STARTING...");
        SpringApplication.run(RoasterServiceApplication.class, args);
    }

}
