package com.evanw.datebyrate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.evanw.datebyrate")
@EnableJpaRepositories(basePackages = "com.evanw.datebyrate")
@EnableCaching
public class DatebyrateApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatebyrateApplication.class, args);
    }

}
