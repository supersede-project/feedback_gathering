package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.RepositoryApplication;
import ch.fhnw.cere.repository.mail.EmailService;
import ch.fhnw.cere.repository.mail.Mail;
import ch.fhnw.cere.repository.models.orchestrator.Application;
import freemarker.template.TemplateException;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Aydinli on 20.01.2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RepositoryApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class F2FEmailServiceTest {

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

        log.info("==== seinding mail ====");
        Mail mail = new Mail();
        mail.setFrom("kuersat.aydinli@gmail.com");
        mail.setTo("f2f_central@hotmail.com");
        mail.setSubject("Sending Email with Freemarker HTML Template Example");

        Map model = new HashMap();
        model.put("name", "Memorynotfound.com");
        model.put("location", "Belgium");
        model.put("signature", "https://memorynotfound.com");
        mail.setModel(model);

        emailService.sendSimpleMessage(mail);
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
