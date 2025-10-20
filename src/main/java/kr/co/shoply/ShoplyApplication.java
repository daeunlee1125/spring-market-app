package kr.co.shoply;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ShoplyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoplyApplication.class, args);
    }

}
