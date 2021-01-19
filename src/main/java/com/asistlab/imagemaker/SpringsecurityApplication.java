package com.asistlab.imagemaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.io.File;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SpringsecurityApplication {

    public static void main(String[] args) {
        new File(System.getProperty("user.dir")).mkdir();
        SpringApplication.run(SpringsecurityApplication.class, args);
    }

}
