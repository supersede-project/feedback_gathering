package ch.fhnw.cere.repository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class RepositoryApplication {
	public static void main(String[] args) {
		SpringApplication.run(RepositoryApplication.class, args);
	}
}
