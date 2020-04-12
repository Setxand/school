package org.school.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

}

