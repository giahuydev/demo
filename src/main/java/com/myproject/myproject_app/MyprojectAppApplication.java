package com.myproject.myproject_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MyprojectAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyprojectAppApplication.class, args);
	}

}
