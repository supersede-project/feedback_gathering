package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.RepositoryApplication;
import ch.fhnw.cere.repository.mail.EmailService;
import ch.fhnw.cere.repository.mail.Mail;
import ch.fhnw.cere.repository.models.EndUser;
import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.models.orchestrator.Application;
import freemarker.template.TemplateException;
import org.hibernate.Hibernate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.*;

/**
 * Created by Aydinli on 20.01.2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RepositoryApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class F2FEmailServiceTest {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private EndUserService endUserService;

    @Autowired
    private EmailService emailService;
    private static Logger log = LoggerFactory.getLogger(Application.class);

    @Test
    public void testMail() throws MessagingException, IOException, TemplateException {

//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println("Hello !!!");
//            }
//        };
//
//        Timer timer = new Timer();
//        long delay = 0;
//        long intevalPeriod = 1000;
//        timer.scheduleAtFixedRate(timerTask, delay,
//                intevalPeriod);
        EndUser testUser = endUserService.find(1);
        Hibernate.initialize(feedbackService.findByUserIdentification(1));
        List<Feedback> userFeedbacks = feedbackService.findByUserIdentification(1);

        userFeedbacks.forEach(feedback -> feedback.setTextFeedbacks(feedback.getTextFeedbacks()));

        List<Feedback> forumFeedbacks = feedbackService.findByPublished(true);

        log.info("==== seinding mail ====");
        Mail mail = new Mail();
        mail.setFrom("kuersat.aydinli@gmail.com");
        mail.setTo("f2f_central@hotmail.com");
        mail.setSubject("Notifications - Feedback Activities from the last 2 weeks");

        Map<String, Object> model = new HashMap<>();
        model.put("enduser",testUser);
        model.put("user_feedbacks",userFeedbacks);
        model.put("forum_feedbacks",forumFeedbacks);

        mail.setModel(model);

        emailService.sendSimpleMessage(mail);
        log.info("==== mail sent ====");
//
//        Mail mail = new Mail();
//        mail.setFrom("kuersat.aydinli@gmail.com");
//        mail.setTo("f2f_central@hotmail.com");
//        mail.setSubject("Sending Simple Email with JavaMailSender Example");
//        mail.setContent("This tutorial demonstrates how to send a simple email using Spring Framework.");
//
//        emailService.sendSimpleMessage(mail);

    }
}
