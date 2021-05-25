package io.devlog.devlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DevlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevlogApplication.class, args);
    }

}
