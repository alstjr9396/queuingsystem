package me.minseok.queueing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class QueueingApplication {

    public static void main(String[] args) {
        SpringApplication.run(QueueingApplication.class, args);
    }

}
