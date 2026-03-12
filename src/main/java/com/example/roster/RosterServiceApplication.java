package com.example.roster;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableFeignClients
public class RosterServiceApplication {

    public static void main(String[] args) {
        System.out.println("ROSTER SERVICE STARTING...");
        SpringApplication.run(RosterServiceApplication.class, args);
    }

}
