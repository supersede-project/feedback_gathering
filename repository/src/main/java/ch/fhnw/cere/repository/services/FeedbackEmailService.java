package ch.fhnw.cere.repository.services;


import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.models.Setting;
import ch.fhnw.cere.repository.services.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
public class FeedbackEmailService {

    @Autowired
    private SettingService settingService;

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendFeedbackNotification(final Feedback feedback) {
        Setting setting = settingService.findByApplicationId(feedback.getApplicationId());
        String recipients = setting.getFeedbackEmailReceivers();

        if(recipients == null || recipients.equals("")) {
            return;
        }

        sendMail(feedback, recipients);
    }

    public void sendMail(final Feedback feedback, String recipients) {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(recipients);
            if(feedback.getContextInformation() != null && feedback.getContextInformation().getUrl() != null) {
                helper.setSubject("New Feedback for " + feedback.getContextInformation().getUrl() + " from " + feedback.getUserIdentification());
            } else {
                helper.setSubject("New Feedback from " + feedback.getUserIdentification());
            }

            helper.setText("<html><body>" +
                            "Hello<br />" +
                            feedback.getId() +
                            "</body></html>",
                    true);

            //FileSystemResource res = new FileSystemResource(new File());
            //helper.addInline("identifier1234", res);
            //helper.addAttachment("Filename", res);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
