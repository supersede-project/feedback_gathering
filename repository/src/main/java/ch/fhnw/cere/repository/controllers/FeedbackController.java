package ch.fhnw.cere.repository.controllers;


import ch.fhnw.cere.repository.controllers.exceptions.BadRequestException;
import ch.fhnw.cere.repository.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.repository.integration.DataProviderIntegrator;
import ch.fhnw.cere.repository.integration.FeedbackCentralIntegrator;
import ch.fhnw.cere.repository.integration.MdmFileIntegrator;
import ch.fhnw.cere.repository.models.*;
import ch.fhnw.cere.repository.models.orchestrator.Application;
import ch.fhnw.cere.repository.models.view.FeedbackView;
import ch.fhnw.cere.repository.services.*;
import ch.fhnw.cere.repository.util.FeedbackUtil;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;


@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}/feedbacks")
public class FeedbackController extends BaseController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FeedbackController.class);

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private DataProviderIntegrator dataProviderIntegrator;

    @Autowired
    private MdmFileIntegrator mdmFileIntegrator;

    @Autowired
    private FeedbackCentralIntegrator feedbackCentralIntegrator;

    @Autowired
    private FeedbackEmailService feedbackEmailService;

    @Autowired
    private OrchestratorApplicationService orchestratorApplicationService;

    @Autowired
    private FeedbackStatusService feedbackStatusService;

    @Autowired
    private AndroidUserService androidUserService;

    @Value("${integration.feedback_central}")
    private boolean feedbackCentralIntegrationEnabled;

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<Feedback> getApplicationFeedbacks(@RequestParam(value = "view", required = false) String view,
                                                  @RequestParam(value = "ids", required = false) List<Long> idList,
                                                  @RequestParam(value = "relevantForUser", required = false) String username,
                                                  @RequestParam(value = "isReported", required = false) boolean isReported,
                                                  @PathVariable long applicationId) {

        if(isReported) {
            return FeedbackUtil.setMinMaxVotes(feedbackService.findByIsReported(applicationId), applicationId, feedbackService);
        }
        if (view != null && idList == null) {
            if (view.equals("public")) {
                return FeedbackUtil.setMinMaxVotes(feedbackService.findByApplicationIdAndIsPublic(applicationId, true), applicationId, feedbackService);
            } else if (view.equals("private")) {
                return FeedbackUtil.setMinMaxVotes(feedbackService.findByApplicationIdAndIsPublic(applicationId, false), applicationId, feedbackService);
            }
            throw new NotFoundException();
        } else if (idList != null && !idList.isEmpty()) {
            return FeedbackUtil.setMinMaxVotes(feedbackService.findAllByFeedbackIdIn(applicationId, idList), applicationId, feedbackService);
        } else if (username != null) {
            AndroidUser androidUser = androidUserService.findByNameAndApplicationId(username, applicationId);
            if (androidUser != null) {
                return FeedbackUtil.setMinMaxVotes(feedbackService.findByUserIdentificationOrIsPublicAndApplicationId(applicationId,username,true),applicationId, feedbackService);
            }
        }
        return FeedbackUtil.setMinMaxVotes(feedbackService.findByApplicationId(applicationId), applicationId, feedbackService);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/compact")
    @JsonView(FeedbackView.Compact.class)
    public List<Feedback> getApplicationFeedbacksCompact(@RequestParam(value = "view", required = false) String view,
                                                         @RequestParam(value = "ids", required = false) List<Long> idList,
                                                         @RequestParam(value = "isRelevantForUser", required = false) String username,
                                                         @RequestParam(value = "isReported", required = false) boolean isReported,
                                                         @PathVariable long applicationId) {
        return getApplicationFeedbacks(view, idList, username, isReported, applicationId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/full")
    public List<Feedback> getDetailedApplicationFeedbacks(@PathVariable long applicationId) {
        List<Feedback> feedbacks = feedbackService.findByApplicationId(applicationId());

        Application orchestratorApplication = null;
        try {
            orchestratorApplication = this.orchestratorApplicationService.loadApplication(feedbacks.get(0).getLanguage(), feedbacks.get(0).getApplicationId());
            for (Feedback feedback : feedbacks) {
                Feedback.appendMechanismsToFeedback(orchestratorApplication, feedback);
                feedback.setApplication(orchestratorApplication);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return feedbacks;
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Feedback getFeedback(@PathVariable long applicationId, @PathVariable long id) {
        Feedback feedback = feedbackService.find(id);
        if (feedback == null) {
            throw new NotFoundException();
        }
        return FeedbackUtil.setMinMaxVotes(feedback, applicationId, feedbackService);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/user_identification/{userIdentification}")
    public List<Feedback> getFeedbacksByUserIdentification(@PathVariable long applicationId, @PathVariable String userIdentification) {
        return FeedbackUtil.setMinMaxVotes(feedbackService.findByUserIdentification(userIdentification), applicationId, feedbackService);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "", consumes = "multipart/form-data")
    public Feedback createFeedback(HttpServletRequest request) throws IOException, ServletException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultiValueMap<String, MultipartFile> parts = multipartRequest.getMultiFileMap();

        MultipartFile jsonPart = parts.getFirst("json");
        ByteArrayInputStream stream = new ByteArrayInputStream(jsonPart.getBytes());
        String jsonString = IOUtils.toString(stream, "UTF-8");

        LOGGER.info(jsonString);

        ObjectMapper mapper = new ObjectMapper();
        Feedback feedback = mapper.readValue(jsonString, Feedback.class);
        feedback.setApplicationId(applicationId());
        if (feedback.getLanguage() == null) {
            feedback.setLanguage("en");
        }

        // TODO rewrite
        if (feedback.getContextInformation() != null) {
            feedback.getContextInformation().setFeedback(feedback);
        }
        if (feedback.getTextFeedbacks() != null) {
            for (TextFeedback textFeedback : feedback.getTextFeedbacks()) {
                textFeedback.setFeedback(feedback);
            }
        }
        if (feedback.getRatingFeedbacks() != null) {
            for (RatingFeedback ratingFeedback : feedback.getRatingFeedbacks()) {
                ratingFeedback.setFeedback(feedback);
            }
        }
        if (feedback.getCategoryFeedbacks() != null) {
            for (CategoryFeedback categoryFeedback : feedback.getCategoryFeedbacks()) {
                categoryFeedback.setFeedback(feedback);
            }
        }
        if (feedback.getScreenshotFeedbacks() != null) {
            for (ScreenshotFeedback screenshotFeedback : feedback.getScreenshotFeedbacks()) {
                screenshotFeedback.setFeedback(feedback);
            }
        }
        if (feedback.getAttachmentFeedbacks() != null) {
            for (AttachmentFeedback attachmentFeedback : feedback.getAttachmentFeedbacks()) {
                attachmentFeedback.setFeedback(feedback);
            }
        }
        if (feedback.getAudioFeedbacks() != null) {
            for (AudioFeedback audioFeedback : feedback.getAudioFeedbacks()) {
                audioFeedback.setFeedback(feedback);
            }
        }
        if (feedback.getStatuses() != null) {
            for (Status status : feedback.getStatuses()) {
                status.setFeedback(feedback);
            }
        }

        if (feedback.getTags() != null) {
            for (FeedbackTag feedbackTag : feedback.getTags()) {
                feedbackTag.setFeedback(feedback);
            }
        }

        parts.remove("json");
        feedback = fileStorageService.storeFiles(feedback, parts);

        Feedback createdFeedback = feedbackService.save(feedback);
        dataProviderIntegrator.ingestJsonData(feedback);
        feedbackEmailService.sendFeedbackNotification(createdFeedback);
        if (feedbackCentralIntegrationEnabled) {
            feedbackCentralIntegrator.ingestJsonData(feedback);
        }

        try {
            List<File> allFiles = fileStorageService.getAllStoredFilesOfFeedback(feedback);
            for (File file : allFiles) {
                if (file.exists()) {
                    LOGGER.info("MdmFileIntegrator: File exists: " + file.getAbsolutePath());
                } else {
                    LOGGER.error("MdmFileIntegrator: File does NOT exist: " + file.getAbsolutePath());
                }
                mdmFileIntegrator.sendFile(file);
                LOGGER.info("MdmFileIntegrator: File sent to WP2");
            }
        } catch (Exception e) {
            LOGGER.error("MdmFileIntegrator: Files could not be forwarded to WP2: " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        return createdFeedback;
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(value = "/attachments/{path:.+}", method = RequestMethod.GET)
    public void downloadFeedbackAttachmentFile(@PathVariable long applicationId, @PathVariable("path") String fileName, HttpServletResponse response) {
        try {
            InputStream inputStream = new FileInputStream(fileStorageService.getFeedbackAttachmentFileByPath(fileName, applicationId));
            org.apache.commons.io.IOUtils.copy(inputStream, response.getOutputStream());
            response.addHeader("Content-disposition", "attachment;filename=" + fileName);
            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(value = "/audios/{path:.+}", method = RequestMethod.GET)
    public void downloadFeedbackAudioFile(@PathVariable long applicationId, @PathVariable("path") String fileName, HttpServletResponse response) {
        try {
            InputStream inputStream = new FileInputStream(fileStorageService.getFeedbackAudioFileByPath(fileName, applicationId));
            org.apache.commons.io.IOUtils.copy(inputStream, response.getOutputStream());
            response.addHeader("Content-disposition", "attachment;filename=" + fileName);
            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(value = "/screenshots/{path:.+}", method = RequestMethod.GET)
    public void downloadFeedbackScreenshotFile(@PathVariable long applicationId, @PathVariable("path") String fileName, HttpServletResponse response) {
        try {
            InputStream inputStream = new FileInputStream(fileStorageService.getFeedbackScreenshotFileByPath(fileName, applicationId));
            org.apache.commons.io.IOUtils.copy(inputStream, response.getOutputStream());
            response.addHeader("Content-disposition", "attachment;filename=" + fileName);
            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteFeedback(@PathVariable long applicationId, @PathVariable long id) {
        feedbackService.delete(id);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.PUT, value = "")
    public Feedback updateFeedback(@PathVariable long applicationId, @RequestBody Feedback feedback) {
        return feedbackService.save(feedback);
    }

    @PreAuthorize("@securityService.hasSuperAdminPermission()")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, value = "/merge")
    public List<Feedback> mergeFeedbackListWithOrchestratorConfiguration(@RequestBody OrchestratorRepositoryDataMergeRequest orchestratorRepositoryDataMergeRequest) throws IOException, ServletException {
        System.err.println("MERGE");
        List<Feedback> feedbacks = orchestratorRepositoryDataMergeRequest.getFeedback();
        Application orchestratorApplication = orchestratorRepositoryDataMergeRequest.getApplication();

        try {
            for (Feedback feedback : feedbacks) {
                Feedback.appendMechanismsToFeedback(orchestratorApplication, feedback);
                feedback.setApplication(orchestratorApplication);
            }
        } catch (Exception e) {
            System.err.println("MERGE FAILED");
            e.printStackTrace();
        }

        return feedbacks;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public Feedback modifyFeedback(@PathVariable("id") long id, HttpEntity<String> requestBody) {
        if (requestBody.getBody() != null) {
            JSONObject obj = new JSONObject(requestBody.getBody());
            Feedback modifiedFeedback = feedbackService.find(id);
            if (modifiedFeedback != null) {
                if (obj.has("public")) {
                    boolean publicValue = obj.getBoolean("public");
                    modifiedFeedback.setPublic(publicValue);
                }
                if (obj.has("feedbackStatus")) {
                    String statusValue = obj.getString("feedbackStatus");
                    FeedbackStatus feedbackStatus = feedbackStatusService.findByStatus(statusValue);
                    if (feedbackStatus != null) {
                        modifiedFeedback.setFeedbackStatus(feedbackStatus);
                    }
                }
                return feedbackService.save(modifiedFeedback);
            } else {
                throw new NotFoundException();
            }
        }
        throw new BadRequestException();
    }

}
