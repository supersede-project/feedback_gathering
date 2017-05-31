package ch.fhnw.cere.repository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;

import javax.servlet.MultipartConfigElement;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class RepositoryApplication {
	public static void main(String[] args) {
		SpringApplication.run(RepositoryApplication.class, args);
	}
}
