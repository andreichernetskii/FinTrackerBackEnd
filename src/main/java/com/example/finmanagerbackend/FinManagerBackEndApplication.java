package com.example.finmanagerbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class FinManagerBackEndApplication {

    public static void main( String[] args ) {
        SpringApplication.run( FinManagerBackEndApplication.class, args );
    }

}
