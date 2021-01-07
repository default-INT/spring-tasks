package by.gstu.springsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class SpringsecurityApplication {

    public static void main(String[] args) {
        new File(System.getProperty("user.dir")).mkdir();
        SpringApplication.run(SpringsecurityApplication.class, args);
    }

}
