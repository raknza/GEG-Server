package com.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.bind.annotation.*;


@SpringBootApplication
@ComponentScan(basePackages = {"com.controller"})
@RestController
@CrossOrigin
@EnableMongoRepositories(basePackages = {"com.dao"})
public class application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(application.class);
    }




}
