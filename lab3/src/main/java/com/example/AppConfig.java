package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import com.example.model.Book;
import org.learnquest.Laptop;

@Configuration
@ComponentScan("com.example")
@ComponentScan("org.learnquest")
public class AppConfig {

    @Bean
    public Book getBook() {
        return new Book();
    }

    @Bean
    public Laptop getComputer() {
        return new Laptop();
    }
}
