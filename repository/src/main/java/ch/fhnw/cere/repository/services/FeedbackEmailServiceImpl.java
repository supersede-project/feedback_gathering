package ch.fhnw.cere.repository.services;


import ch.fhnw.cere.repository.models.*;
import ch.fhnw.cere.repository.models.orchestrator.Application;
import ch.fhnw.cere.repository.models.orchestrator.Mechanism;
import ch.fhnw.cere.repository.models.orchestrator.MechanismTemplateModel;
import org.json.JSONObject;
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
            helper.setTo(recipients);
            if(feedback.getContextInformation() != null && feedback.getContextInformation().getUrl() != null) {
                helper.setSubject("New Feedback for " + feedback.getContextInformation().getUrl() + " from " + feedback.getUserIdentification());
            } else {
                helper.setSubject("New Feedback from " + feedback.getUserIdentification());
            }

            Application orchestratorApplication = this.orchestratorApplicationService.loadApplication(feedback.getLanguage(), feedback.getApplicationId());
            this.appendMechanismsToFeedback(orchestratorApplication, feedback);

            Map<String, Object> model = new HashMap<>();
            model.put("feedback", feedback);

            Template t = freemarkerConfiguration.getTemplate("feedback_mail.ftl");
            String text = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

            helper.setText(text,true);
            this.addAttachments(feedback, helper);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    public void setOrchestratorService(OrchestratorApplicationService orchestratorApplicationService) {
        this.orchestratorApplicationService = orchestratorApplicationService;
    }

    private void addAttachments(Feedback feedback, MimeMessageHelper helper) throws MessagingException {
        File resourcesDirectory = new File(repositoryFilesDirectory);

        for(AttachmentFeedback attachmentFeedback : feedback.getAttachmentFeedbacks()) {
            File attachment = new File(resourcesDirectory.getAbsolutePath() + File.separator + this.attachmentsDirectory + File.separator + attachmentFeedback.getPath());
            FileSystemResource res = new FileSystemResource(attachment);
            helper.addAttachment(res.getFilename(), res);
        }

        for(ScreenshotFeedback screenshotFeedback : feedback.getScreenshotFeedbacks()) {
            File screenshot = new File(resourcesDirectory.getAbsolutePath() + File.separator + this.screenshotsDirectory + File.separator + screenshotFeedback.getPath());
            FileSystemResource res = new FileSystemResource(screenshot);
            helper.addAttachment(res.getFilename(), res);
        }

        for(AudioFeedback audioFeedback : feedback.getAudioFeedbacks()) {
            File screenshot = new File(resourcesDirectory.getAbsolutePath() + File.separator + this.audiosDirectory + File.separator + audioFeedback.getPath());
            FileSystemResource res = new FileSystemResource(screenshot);
            helper.addAttachment(res.getFilename(), res);
        }
    }

    private Feedback appendMechanismsToFeedback(Application application, Feedback feedback) {
        feedback.setAttachmentFeedbacks((List<AttachmentFeedback>)appendMechanismToMechanismFeedbacks(application, feedback.getAttachmentFeedbacks(), feedback.getConfigurationId()));
        feedback.setScreenshotFeedbacks((List<ScreenshotFeedback>)appendMechanismToMechanismFeedbacks(application, feedback.getScreenshotFeedbacks(), feedback.getConfigurationId()));
        feedback.setAudioFeedbacks((List<AudioFeedback>)appendMechanismToMechanismFeedbacks(application, feedback.getAudioFeedbacks(), feedback.getConfigurationId()));
        feedback.setRatingFeedbacks((List<RatingFeedback>)appendMechanismToMechanismFeedbacks(application, feedback.getRatingFeedbacks(), feedback.getConfigurationId()));
        feedback.setCategoryFeedbacks((List<CategoryFeedback>)appendMechanismToMechanismFeedbacks(application, feedback.getCategoryFeedbacks(), feedback.getConfigurationId()));
        feedback.setTextFeedbacks((List<TextFeedback>)appendMechanismToMechanismFeedbacks(application, feedback.getTextFeedbacks(), feedback.getConfigurationId()));
        return feedback;
    }

    private List<? extends MechanismFeedback> appendMechanismToMechanismFeedbacks(Application application, List<? extends MechanismFeedback> mechanismFeedbacks, long configurationId) {
        List<MechanismFeedback> mechanismFeedbacksWithMechanism = new ArrayList<>();
        if(mechanismFeedbacks == null) {
            return null;
        }

        for(MechanismFeedback mechanismFeedback : mechanismFeedbacks) {
            Mechanism mechanism = application.mechanismByConfigurationIdAndMechanismId(configurationId, mechanismFeedback.getMechanismId());
            MechanismTemplateModel mechanismTemplateModel = new MechanismTemplateModel(mechanism);
            mechanismFeedback.setMechanism(mechanismTemplateModel);
            mechanismFeedbacksWithMechanism.add(mechanismFeedback);
        }
        return mechanismFeedbacksWithMechanism;
    }
}
