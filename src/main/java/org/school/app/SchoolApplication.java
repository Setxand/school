package org.school.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication

//@EnableAutoConfiguration
//@EntityScan(basePackages = "org.school.app.tennisapp.repository")
public class SchoolApplication {

	public static void main(String[] args) {
        SpringApplication.run(SchoolApplication.class, args);
//		new SpringApplicationBuilder(SchoolApplication.class)
//				.headless(false)
//				.run(args);
	}

}

