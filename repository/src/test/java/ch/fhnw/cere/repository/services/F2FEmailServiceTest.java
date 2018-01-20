package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.RepositoryApplication;
import ch.fhnw.cere.repository.mail.EmailService;
import ch.fhnw.cere.repository.mail.Mail;
import ch.fhnw.cere.repository.models.orchestrator.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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
    public void testMail(){
        log.info("==== seinding mail ====");

        Mail mail = new Mail();
        mail.setFrom("kuersat.aydinli@gmail.com");
        mail.setTo("f2f_central@hotmail.com");
        mail.setSubject("Sending Simple Email with JavaMailSender Example");
        mail.setContent("This tutorial demonstrates how to send a simple email using Spring Framework.");

        emailService.sendSimpleMessage(mail);
    }
}
