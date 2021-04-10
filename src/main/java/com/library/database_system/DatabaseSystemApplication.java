package com.library.database_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@SpringBootApplication
@RestController
public class DatabaseSystemApplication {

	public static void main(String[] args) {

		SpringApplication.run(DatabaseSystemApplication.class, args);
	}

}
