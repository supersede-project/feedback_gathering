package ch.fhnw.cere.repository.integration;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.mail.internet.MimeMessage;

import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.models.Setting;
import ch.fhnw.cere.repository.services.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import java.io.File;


public class FeedbackEmailService {

    private JavaMailSender mailSender;

    @Autowired
    private SettingService settingService;

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendFeedbackNotification(final Feedback feedback) {
        Setting setting = settingService.findByApplicationId(feedback.getApplicationId());
        String recipients = setting.getFeedbackEmailReceivers();

        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost("mail.host.com");
        MimeMessage message = sender.createMimeMessage();

        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(recipients);
            helper.setSubject("New Feedback for " + feedback.getContextInformation().getUrl() + " from " + feedback.getUserIdentification());

            helper.setText("<html><body>" +
                            "Hello<br />" +
                            feedback.getId() +
                            "</body></html>",
                    true);

            //FileSystemResource res = new FileSystemResource(new File());
            //helper.addInline("identifier1234", res);
            //helper.addAttachment("Filename", res);

            sender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
