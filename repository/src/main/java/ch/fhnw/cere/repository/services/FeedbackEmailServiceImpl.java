package ch.fhnw.cere.repository.services;


import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.models.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import freemarker.template.TemplateException;
import freemarker.template.Template;
import freemarker.template.Configuration;

import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Service
public class FeedbackEmailServiceImpl implements FeedbackEmailService {

    @Autowired
    private SettingService settingService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Configuration freemarkerConfiguration;

    @Value("${supersede.upload_directory}")
    protected String repositoryFilesDirectory;

    @Value("${supersede.upload_directory.screenshots_folder_name}")
    protected String screenshotsDirectory;

    @Value("${supersede.upload_directory.attachments_folder_name}")
    protected String attachmentsDirectory;

    @Value("${supersede.upload_directory.audios_folder_name}")
    protected String audiosDirectory;

    @Async
    public void sendFeedbackNotification(final Feedback feedback) {
        Setting setting = settingService.findByApplicationId(feedback.getApplicationId());
        String recipients = setting.getFeedbackEmailReceivers();

        if(recipients == null || recipients.equals("")) {
            return;
        }

        try {
            sendMail(feedback, recipients);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMail(final Feedback feedback, String recipients) throws IOException, TemplateException {
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

            Map<String, Object> model = new HashMap<>();
            model.put("feedback", feedback);

            Template t = freemarkerConfiguration.getTemplate("feedback_mail.ftl");
            String text = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

            helper.setText(text,true);

            this.addAttachments(helper);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void addAttachments(MimeMessageHelper helper) throws MessagingException {
        File resourcesDirectory = new File(repositoryFilesDirectory);

        File attachment = new File(resourcesDirectory.getAbsolutePath() + File.separator + this.attachmentsDirectory + File.separator + "/test_file.pdf");
        FileSystemResource res = new FileSystemResource(attachment);
        helper.addAttachment("Filename.pdf", res);

        File attachment2 = new File(resourcesDirectory.getAbsolutePath() + File.separator + this.screenshotsDirectory + File.separator + "/screenshot_1_example.png");
        FileSystemResource res2 = new FileSystemResource(attachment2);
        helper.addAttachment("screenshot.png", res2);
    }
}
