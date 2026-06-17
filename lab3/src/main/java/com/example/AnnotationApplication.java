package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import com.example.model.Book;
import org.learnquest.Laptop;

@SpringBootApplication
public class AnnotationApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(AnnotationApplication.class, args);

        // 1. Resolve, process and evaluate Book Component
        Book book = context.getBean(Book.class);
        book.setTitle("Tell Me Your Dreams");
        System.out.println("The title of your book is " + book.getTitle());

        // 2. Resolve, process and evaluate foreign Laptop Scan Component
        Laptop comp = context.getBean(Laptop.class);
        comp.setBrand("Dell");
        System.out.println("The brand of laptop is: " + comp.getBrand());
    }
}
