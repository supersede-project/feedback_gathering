package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.RepositoryApplication;
import ch.fhnw.cere.repository.mail.EmailService;
import ch.fhnw.cere.repository.mail.Mail;
import ch.fhnw.cere.repository.models.EndUser;
import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.models.TextFeedback;
import ch.fhnw.cere.repository.models.orchestrator.Application;
import ch.fhnw.cere.repository.repositories.EndUserRepository;
import ch.fhnw.cere.repository.repositories.FeedbackRepository;
import freemarker.template.TemplateException;
import org.hibernate.Hibernate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

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
    private FeedbackRepository feedbackRepository;

    @Autowired
    private EndUserRepository endUserRepository;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private EndUserService endUserService;

    @Autowired
    private EmailService emailService;

    private static Logger log = LoggerFactory.getLogger(Application.class);

    private Feedback feedback1;
    private Feedback feedback2;
    private Feedback feedback3;
    private Feedback feedback4;
    private Feedback feedback5;
    private Feedback feedback6;
    private Feedback feedback7;
    private Feedback feedback8;

    private EndUser endUser1;
    private EndUser endUser2;

    @Before
    public void setup(){
        feedbackRepository.deleteAllInBatch();
        endUserRepository.deleteAllInBatch();

        endUser1 = endUserRepository.save(new EndUser(1,"kaydin1",123));
        endUser2 = endUserRepository.save(new EndUser(1,"kaydin2",123));

        feedback1 = feedbackRepository.save(new Feedback("Feedback 1 User 1", endUser1.getId(), 1, 11, "en"));
        TextFeedback textFeedback1 = new TextFeedback(feedback1,
                "This is test text of Feedback 1 and User 1",88);
        List<TextFeedback> textFeedbacks1 = new ArrayList<>();
        textFeedbacks1.add(textFeedback1);
        feedback1.setTextFeedbacks(textFeedbacks1);feedbackRepository.save(feedback1);

        feedback2 = feedbackRepository.save(new Feedback("Feedback 2 User 1", endUser1.getId(), 1, 11, "en"));
        feedback2.setPublished(true);feedbackRepository.save(feedback2);
        feedback3 = feedbackRepository.save(new Feedback("Feedback 3 User 1", endUser1.getId(), 1, 22, "en"));
        feedback4 = feedbackRepository.save(new Feedback("Feedback 4 User 1", endUser1.getId(), 1, 22, "en"));
        feedback4.setPublished(true);feedbackRepository.save(feedback4);
        feedback5 = feedbackRepository.save(new Feedback("Feedback 5 User 2", endUser2.getId(), 1, 11, "en"));
        feedback6 = feedbackRepository.save(new Feedback("Feedback 6 User 2", endUser2.getId(), 1, 11, "en"));
        feedback6.setPublished(true);feedbackRepository.save(feedback6);
        feedback7 = feedbackRepository.save(new Feedback("Feedback 7 User 2", endUser2.getId(), 1, 22, "en"));
        feedback8 = feedbackRepository.save(new Feedback("Feedback 8 User 2", endUser2.getId(), 1, 22, "en"));
    }

    @After
    public void cleanup(){
        feedbackRepository.deleteAllInBatch();
        endUserRepository.deleteAllInBatch();
    }


    @Test
    @Transactional
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
        EndUser testUser = endUserService.find(endUser1.getId());
        List<Feedback> userFeedbacks = feedbackService.findByUserIdentification(endUser1.getId());
        List<Feedback> forumFeedbacks = feedbackService.findByPublished(true);

//        List<TextFeedback> textFeedbacks = feedbackService.getTextFeedbacks(
//                feedback1.getId()
//        );
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
