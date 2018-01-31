package ch.fhnw.cere.repository.mail;

/**
 * Created by Aydinli on 20.01.2018.
 */
import ch.fhnw.cere.repository.models.EmailUnsubscribed;
import ch.fhnw.cere.repository.models.EndUser;
import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.models.orchestrator.Application;
import ch.fhnw.cere.repository.repositories.EndUserRepository;
import ch.fhnw.cere.repository.repositories.FeedbackRepository;
import ch.fhnw.cere.repository.repositories.TextFeedbackRepository;
import ch.fhnw.cere.repository.services.EmailUnsubscribedService;
import ch.fhnw.cere.repository.services.EndUserService;
import ch.fhnw.cere.repository.services.FeedbackService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class EmailService {
    private static Logger log = LoggerFactory.getLogger(Application.class);

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
    private EmailUnsubscribedService emailUnsubscribedService;

    @Autowired
    private JavaMailSender emailSender;


    @Qualifier("freeMarkerConfiguration")
    @Autowired
    private Configuration freemarkerConfig;

    @Value("${spring.mail.username}")
    protected String mailUsername;

    @Value("${supersede.email.newsletter.day.interval}")
    protected long newsletterDayInterval;

    long DAY_IN_MS = 1000 * 60 * 60 * 24;

    public void sendSimpleMessage(Mail mail) throws MessagingException, IOException, TemplateException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

//        helper.addAttachment("logo.png", new ClassPathResource("icon_notification_red.png"));

        Template t = freemarkerConfig.getTemplate("feedback_mail_f2f.ftl");
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, mail.getModel());

        helper.setTo(mail.getTo());
        helper.setText(html, true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(mailUsername);

        emailSender.send(message);
    }
//    public void sendSimpleMessage(final Mail mail) throws MessagingException, IOException, TemplateException {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setSubject(mail.getSubject());
//        message.setText(mail.getContent());
//        message.setTo(mail.getTo());
//        message.setFrom(mail.getFrom());
//
//        emailSender.send(message);
//    }

    public void sendNewsletter() throws MessagingException, IOException, TemplateException {
        Date currentDate = new Date();

        Date twoWeeksAgo = new Date(currentDate.getTime() - (newsletterDayInterval * DAY_IN_MS));

        List<EndUser> endUsers = endUserRepository.findAll();
//        EmailUnsubscribed emailUnsubscribed = new EmailUnsubscribed(endUser1,endUser1.getEmail());
//        emailUnsubscribedService.save(emailUnsubscribed);
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

            log.info("==== SENDING MAIL ====");
            Mail mail = new Mail();
            mail.setTo(user.getEmail());
            mail.setSubject("Notifications - Feedback Activities from the F2F Central");

            Map<String, Object> model = new HashMap<>();
            model.put("enduser",user);
            model.put("user_feedbacks",userFeedbacks);
            model.put("forum_feedbacks",forumFeedbacks);

            mail.setModel(model);

            sendSimpleMessage(mail);
            log.info("==== MAIL SENT ====");
        }
    }
}