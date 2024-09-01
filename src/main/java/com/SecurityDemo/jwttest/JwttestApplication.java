package com.SecurityDemo.jwttest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableJpaAuditing(auditorAwareRef = "auditorAware") // to auto identify the user id when needed in entities (Ex: createdby,...);
public class JwttestApplication {

	@Value("${server.port}")
	String port;

	public static void main(String[] args) {
		SpringApplication.run(JwttestApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner runner() {
//		return args -> {
//			// Print the port the application is running on
//			System.out.println("App has been start working on port " + port);
//		};
//	}

}
