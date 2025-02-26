package com.sau.bitirme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.sau.bitirme.repository")
public class BitirmeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BitirmeApplication.class, args);
    }

}
