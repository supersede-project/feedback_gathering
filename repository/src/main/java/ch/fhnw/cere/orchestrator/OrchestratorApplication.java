package ch.fhnw.cere.orchestrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class OrchestratorApplication {
	public static void main(String[] args) {
		SpringApplication.run(OrchestratorApplication.class, args);
	}
}
