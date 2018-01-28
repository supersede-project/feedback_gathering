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

//	@Transactional
//	@PostConstruct
////	@Scheduled(fixedRate = 5000)
//	public void sendEmail() throws MessagingException, IOException, TemplateException {
////		EndUser testUser = endUserService.find(99999999);
////		List<Feedback> userFeedbacks = feedbackService.findByUserIdentification(99999999);
////		userFeedbacks.removeIf(feedback -> feedback.getId() < 149);
//
//		EndUser testUser = endUserService.find(1);
//		List<Feedback> userFeedbacks = feedbackService.findByUserIdentification(1);
//		List<Feedback> forumFeedbacks = feedbackService.findByPublished(true);
//
//		Mail mail = new Mail();
//		mail.setFrom("kuersat.aydinli@gmail.com");
//		mail.setTo("f2f_central@hotmail.com");
//		mail.setSubject("Notifications - Feedback Activities from the last 2 weeks");
//
//		Map<String, Object> model = new HashMap<>();
//		model.put("enduser",testUser);
//		model.put("user_feedbacks",userFeedbacks);
//		model.put("forum_feedbacks",forumFeedbacks);
//
//		mail.setModel(model);
//		emailService.sendSimpleMessage(mail);
//		log.info("==== E-MAIL SENT ====");
//	}
}
