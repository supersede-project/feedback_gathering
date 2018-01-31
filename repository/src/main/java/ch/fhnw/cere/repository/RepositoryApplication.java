package ch.fhnw.cere.repository;

import ch.fhnw.cere.repository.mail.EmailService;
import ch.fhnw.cere.repository.mail.Mail;
import ch.fhnw.cere.repository.models.EndUser;
import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.models.orchestrator.Application;
import ch.fhnw.cere.repository.services.EndUserService;
import ch.fhnw.cere.repository.services.FeedbackService;
import freemarker.template.TemplateException;
import org.hibernate.validator.constraints.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
//@EnableScheduling
public class RepositoryApplication {
	private static Logger log = LoggerFactory.getLogger(Application.class);

	@Autowired
	private FeedbackService feedbackService;

	@Autowired
	private EndUserService endUserService;

	@Autowired
	private EmailService emailService;

	public static void main(String[] args) {
		SpringApplication.run(RepositoryApplication.class, args);
	}


	/*
	 * Uncomment the method below in order to activate email newsletter service
	 * Define custom cronjob for execution
	 * In the application.properties file set supersede.email.newsletter.day.interval
	 * to define the day interval of the newsletter
	 */
//	@Transactional
//	@PostConstruct
//	//	@Scheduled(fixedRate = 5000) // Define cronjob here to send out the newsletter every two weeks
//	public void sendNewsetter () throws MessagingException, IOException, TemplateException {
//		emailService.sendNewsletter();
//	}
}
