package org.school.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SchoolApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(SchoolApplication.class)
				.headless(false)
				.run(args);
	}

}

