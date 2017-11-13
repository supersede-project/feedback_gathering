package ch.fhnw.cere.repository.services;


import ch.fhnw.cere.repository.controllers.FeedbackController;
import ch.fhnw.cere.repository.models.*;
import ch.fhnw.cere.repository.models.orchestrator.Application;
import ch.fhnw.cere.repository.models.orchestrator.Mechanism;
import ch.fhnw.cere.repository.models.orchestrator.MechanismTemplateModel;
import com.sun.media.jfxmedia.logging.Logger;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class FeedbackEmailServiceImpl implements FeedbackEmailService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FeedbackController.class);

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

    private OrchestratorApplicationService orchestratorApplicationService;

    @Async
    public void sendFeedbackNotification(final Feedback feedback) {
        Setting setting = settingService.findByApplicationId(feedback.getApplicationId());
        if(setting == null) {
            return;
        }

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
            String[] recipientArray = recipients.split(",");
            helper.setTo(recipientArray);
            if(feedback.getContextInformation() != null && feedback.getContextInformation().getUrl() != null) {
                helper.setSubject("New Feedback for " + feedback.getContextInformation().getUrl() + " from " + feedback.getUserIdentification());
            } else {
                helper.setSubject("New Feedback from " + feedback.getUserIdentification());
            }

            Template emailTemplate = freemarkerConfiguration.getTemplate("feedback_mail.ftl");

            try {
                Application orchestratorApplication = this.orchestratorApplicationService.loadApplication(feedback.getLanguage(), feedback.getApplicationId());
                Feedback.appendMechanismsToFeedback(orchestratorApplication, feedback);
            } catch(Exception e) {
                Logger.logMsg(Logger.ERROR, "Orchestrator not available for repository email sender. Alternative email template chosen.");
                emailTemplate = freemarkerConfiguration.getTemplate("feedback_mail_without_configuration.ftl");
            }

            Map<String, Object> model = new HashMap<>();
            model.put("feedback", feedback);
            String text = FreeMarkerTemplateUtils.processTemplateIntoString(emailTemplate, model);

            helper.setText(text,true);
            this.addAttachments(feedback, helper);

            mailSender.send(message);
            LOGGER.debug("Feedback email sent to " + recipients);
        } catch (MessagingException e) {
            LOGGER.debug("Problem occurred when sending email");
            e.printStackTrace();
        }
    }

    @Autowired
    public void setOrchestratorService(OrchestratorApplicationService orchestratorApplicationService) {
        this.orchestratorApplicationService = orchestratorApplicationService;
    }

    private void addAttachments(Feedback feedback, MimeMessageHelper helper) throws MessagingException {
        File resourcesDirectory = new File(repositoryFilesDirectory);

        if(feedback.getAttachmentFeedbacks() != null) {
            for(AttachmentFeedback attachmentFeedback : feedback.getAttachmentFeedbacks()) {
                File attachment = new File(resourcesDirectory.getAbsolutePath() + File.separator + this.attachmentsDirectory + File.separator + attachmentFeedback.getPath());
                FileSystemResource res = new FileSystemResource(attachment);
                String filename = res.getFilename();
                if(!filename.endsWith(".png")) {
                    filename = filename + ".png";
                }
                helper.addAttachment(filename, res);
            }
        }

        if(feedback.getScreenshotFeedbacks() != null) {
            for(ScreenshotFeedback screenshotFeedback : feedback.getScreenshotFeedbacks()) {
                File screenshot = new File(resourcesDirectory.getAbsolutePath() + File.separator + this.screenshotsDirectory + File.separator + screenshotFeedback.getPath());
                FileSystemResource res = new FileSystemResource(screenshot);
                String filename = res.getFilename();
                if(!filename.endsWith(".png")) {
                    filename = filename + ".png";
                }
                helper.addAttachment(filename, res);
            }
        }

        if(feedback.getAudioFeedbacks() != null) {
            for(AudioFeedback audioFeedback : feedback.getAudioFeedbacks()) {
                File screenshot = new File(resourcesDirectory.getAbsolutePath() + File.separator + this.audiosDirectory + File.separator + audioFeedback.getPath());
                FileSystemResource res = new FileSystemResource(screenshot);
                String filename = res.getFilename();
                if(!filename.endsWith(".png")) {
                    filename = filename + ".png";
                }
                helper.addAttachment(filename, res);
            }
        }
    }
}
