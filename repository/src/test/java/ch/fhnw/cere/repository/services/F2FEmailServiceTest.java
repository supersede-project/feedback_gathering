package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.RepositoryApplication;
import ch.fhnw.cere.repository.mail.EmailService;
import ch.fhnw.cere.repository.mail.Mail;
import ch.fhnw.cere.repository.models.EmailUnsubscribed;
import ch.fhnw.cere.repository.models.EndUser;
import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.models.TextFeedback;
import ch.fhnw.cere.repository.models.orchestrator.Application;
import ch.fhnw.cere.repository.repositories.EmailUnsubscribedRepository;
import ch.fhnw.cere.repository.repositories.EndUserRepository;
import ch.fhnw.cere.repository.repositories.FeedbackRepository;
import ch.fhnw.cere.repository.repositories.TextFeedbackRepository;
import freemarker.template.TemplateException;
import org.hibernate.Hibernate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${supersede.email.newsletter.day.interval}")
    protected long emailDayInterval;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private EndUserRepository endUserRepository;

    @Autowired
    private TextFeedbackRepository textFeedbackRepository;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private EndUserService endUserService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailUnsubscribedService emailUnsubscribedService;

    @Autowired
    private EmailUnsubscribedRepository emailUnsubscribedRepository;

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
    private EndUser endUser3;

    long DAY_IN_MS = 1000 * 60 * 60 * 24;

    @Before
    public void setup(){
        feedbackRepository.deleteAllInBatch();
        endUserRepository.deleteAllInBatch();
        textFeedbackRepository.deleteAllInBatch();
        emailUnsubscribedRepository.deleteAllInBatch();

        Date now = new Date();

//        endUser1 = endUserRepository.save(new EndUser(1,"Ronnie Schaniel",123,"ronnieschaniel@gmail.com"));
//        endUser2 = endUserRepository.save(new EndUser(1,"Melanie Stade",123,"mela.stade@gmail.com"));
//        endUser3 = endUserRepository.save(new EndUser(1,"Marina Melwin",123,"marina.melwin@uzh.ch"));
        endUser1 = endUserRepository.save(new EndUser(1,"Ronnie Schaniel",123,"f2f_central@hotmail.com"));
        endUser2 = endUserRepository.save(new EndUser(1,"Melanie Stade",123,"f2f_central@hotmail.com"));
        endUser3 = endUserRepository.save(new EndUser(1,"Marina Melwin",123,"f2f_central@hotmail.com"));

        feedback1 = feedbackRepository.save(new Feedback("Feedback 1 Ronnie", endUser1.getId(), 1, 11, "en"));
        TextFeedback textFeedback1 = new TextFeedback(feedback1,
                "This is a sample placeholder text of Feedback 1",88);
        textFeedbackRepository.save(textFeedback1);
        List<TextFeedback> textFeedbacks1 = new ArrayList<>();
        textFeedbacks1.add(textFeedback1);
        feedback1.setTextFeedbacks(textFeedbacks1);
        feedback1.setBlocked(true);feedbackRepository.save(feedback1);

        feedback2 = feedbackRepository.save(new Feedback("Feedback 2 Ronnie", endUser1.getId(), 1, 11, "en"));
        TextFeedback textFeedback2 = new TextFeedback(feedback2,
                "This is a sample placeholder text of Feedback 2 - PUBLISHED",88);
        textFeedbackRepository.save(textFeedback2);
        List<TextFeedback> textFeedbacks2 = new ArrayList<>();
        textFeedbacks2.add(textFeedback2);
        feedback2.setTextFeedbacks(textFeedbacks2);;
        feedback2.setPublished(true);
        feedback2.setBlocked(true);feedbackRepository.save(feedback2);

        feedback3 = feedbackRepository.save(new Feedback("Feedback 3 Ronnie", endUser1.getId(), 1, 22, "en"));
        TextFeedback textFeedback3 = new TextFeedback(feedback3,
                "This is a sample placeholder text of Feedback 3",88);
        textFeedbackRepository.save(textFeedback3);
        List<TextFeedback> textFeedbacks3 = new ArrayList<>();
        textFeedbacks3.add(textFeedback3);
        feedback3.setTextFeedbacks(textFeedbacks3);
        feedback3.setBlocked(true);feedbackRepository.save(feedback3);

        feedback4 = feedbackRepository.save(new Feedback("Feedback 4 Melanie", endUser2.getId(), 1, 22, "en"));
        TextFeedback textFeedback4 = new TextFeedback(feedback4,
                "This is a sample placeholder text of Feedback 4 - PUBLISHED",88);
        textFeedbackRepository.save(textFeedback4);
        List<TextFeedback> textFeedbacks4 = new ArrayList<>();
        textFeedbacks4.add(textFeedback4);
        feedback4.setTextFeedbacks(textFeedbacks4);
        feedback4.setPublished(true);feedbackRepository.save(feedback4);
        feedback4.setCreatedAt(new Date(now.getTime() - (15 * DAY_IN_MS)));
        feedbackRepository.save(feedback4);

        feedback5 = feedbackRepository.save(new Feedback("Feedback 5 Melanie", endUser2.getId(), 1, 11, "en"));
        TextFeedback textFeedback5 = new TextFeedback(feedback5,
                "This is a sample placeholder text of Feedback 5",88);
        textFeedbackRepository.save(textFeedback5);
        List<TextFeedback> textFeedbacks5 = new ArrayList<>();
        textFeedbacks5.add(textFeedback5);
        feedback5.setTextFeedbacks(textFeedbacks5);feedbackRepository.save(feedback5);

        feedback6 = feedbackRepository.save(new Feedback("Feedback 6 Melanie", endUser2.getId(), 1, 11, "en"));
        TextFeedback textFeedback6 = new TextFeedback(feedback6,
                "This is a sample placeholder text of Feedback 6 - PUBLISHED",88);
        textFeedbackRepository.save(textFeedback6);
        List<TextFeedback> textFeedbacks6 = new ArrayList<>();
        textFeedbacks6.add(textFeedback6);
        feedback6.setTextFeedbacks(textFeedbacks6);
        feedback6.setPublished(true);feedbackRepository.save(feedback6);

        feedback7 = feedbackRepository.save(new Feedback("Feedback 7 Marina", endUser3.getId(), 1, 22, "en"));
        TextFeedback textFeedback7 = new TextFeedback(feedback7,
                "This is a sample placeholder text of Feedback 7",88);
        textFeedbackRepository.save(textFeedback7);
        List<TextFeedback> textFeedbacks7 = new ArrayList<>();
        textFeedbacks7.add(textFeedback7);
        feedback7.setTextFeedbacks(textFeedbacks7);feedbackRepository.save(feedback7);
        feedback7.setCreatedAt(new Date(now.getTime() - (15 * DAY_IN_MS)));
        feedbackRepository.save(feedback7);

        feedback8 = feedbackRepository.save(new Feedback("Feedback 8 Marina", endUser3.getId(), 1, 22, "en"));
        TextFeedback textFeedback8 = new TextFeedback(feedback8,
                "This is a sample placeholder text of Feedback 8 - PUBLISHED",88);
        textFeedbackRepository.save(textFeedback8);
        List<TextFeedback> textFeedbacks8 = new ArrayList<>();
        textFeedbacks8.add(textFeedback8);
        feedback8.setTextFeedbacks(textFeedbacks8);
        feedback8.setPublished(true);feedbackRepository.save(feedback8);

        EmailUnsubscribed emailUnsubscribed = new EmailUnsubscribed(endUser1,endUser1.getEmail());
        emailUnsubscribedService.save(emailUnsubscribed);
    }

    @After
    public void cleanup(){
        feedbackRepository.deleteAllInBatch();
        endUserRepository.deleteAllInBatch();
        textFeedbackRepository.deleteAllInBatch();
        emailUnsubscribedRepository.deleteAllInBatch();
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

        Date currentDate = new Date();

        Date twoWeeksAgo = new Date(currentDate.getTime() - (emailDayInterval * DAY_IN_MS));

        List<EndUser> endUsers = endUserRepository.findAll();

        for(EndUser user : endUsers){
            if(emailUnsubscribedService.findByEnduserId(user.getId()) != null){
                continue;
            }

            List<Feedback> userFeedbacks = feedbackService.findByUserIdentification(user.getId());
            List<Feedback> userFeedbacksRemoved = new ArrayList<>();
            for(Feedback userFeedback : userFeedbacks){
                if(userFeedback.getCreatedAt().before(twoWeeksAgo)){
                    userFeedbacksRemoved.add(userFeedback);
                }
            }
            userFeedbacks.removeAll(userFeedbacksRemoved);

            List<Feedback> forumFeedbacks = feedbackService.findByPublished(true);
            List<Feedback> forumFeedbacksRemoved = new ArrayList<>();
            for(Feedback forumFeedback : forumFeedbacks){
                if(forumFeedback.getCreatedAt().before(twoWeeksAgo)){
                    forumFeedbacksRemoved.add(forumFeedback);
                }
            }
            forumFeedbacks.removeAll(forumFeedbacksRemoved);

            List<Feedback> blockedFeedbacks = feedbackService.findByBlocked(true);

            log.info("==== SENDING MAIL ====");
            Mail mail = new Mail();
            mail.setTo(user.getEmail());
            mail.setSubject("Notifications - Feedback Activities from the F2F Central");

            Map<String, Object> model = new HashMap<>();
            model.put("enduser",user);
            model.put("user_feedbacks",userFeedbacks);
            model.put("forum_feedbacks",forumFeedbacks);

            mail.setModel(model);

            emailService.sendSimpleMessage(mail);
            log.info("==== MAIL SENT ====");
        }

//        EndUser testUser = endUserService.find(endUser1.getId());
//        List<Feedback> userFeedbacks = feedbackService.findByUserIdentification(endUser1.getId());
//        List<Feedback> forumFeedbacks = feedbackService.findByPublished(true);
//
//        log.info("==== seinding mail ====");
//        Mail mail = new Mail();
//        mail.setFrom("kuersat.aydinli@gmail.com");
//        mail.setTo("f2f_central@hotmail.com");
//        mail.setSubject("Notifications - Feedback Activities from the last 2 weeks");
//
//        Map<String, Object> model = new HashMap<>();
//        model.put("enduser",testUser);
//        model.put("user_feedbacks",userFeedbacks);
//        model.put("forum_feedbacks",forumFeedbacks);
//
//        mail.setModel(model);
//
//        emailService.sendSimpleMessage(mail);
//        log.info("==== mail sent ====");
    }
}
