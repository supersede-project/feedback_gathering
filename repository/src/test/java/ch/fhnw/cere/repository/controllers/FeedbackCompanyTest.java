package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.repository.models.ApiUserPermission;
import ch.fhnw.cere.repository.models.FeedbackCompany;
import ch.fhnw.cere.repository.repositories.ApiUserPermissionRepository;
import ch.fhnw.cere.repository.repositories.FeedbackCompanyRepository;
import ch.fhnw.cere.repository.services.FeedbackCompanyService;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultMatcher;

import javax.servlet.ServletException;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Aydinli on 01.02.2018.
 */
public class FeedbackCompanyTest extends BaseIntegrationTest{
    private String basePathEn = "/feedback_repository/en/";

    @Autowired
    private FeedbackCompanyRepository feedbackCompanyRepository;

    @Autowired
    private FeedbackCompanyService feedbackCompanyService;

    @Autowired
    private ApiUserPermissionRepository apiUserPermissionRepository;

    FeedbackCompany feedbackCompany1;
    FeedbackCompany feedbackCompany2;
    FeedbackCompany feedbackCompany3;

    @Before
    public void setup() throws Exception{
        super.setup();
        feedbackCompanyRepository.deleteAllInBatch();

        feedbackCompany1 = feedbackCompanyRepository.save(new FeedbackCompany("hey ho 1",
                "upcoming",false));

        feedbackCompany2 = feedbackCompanyRepository.save(new FeedbackCompany("hey ho 2",
                "upcoming",true));

        feedbackCompany3 = feedbackCompanyRepository.save(new FeedbackCompany("hey ho 3",
                "upcoming",false));
        apiUserPermissionRepository.save(new ApiUserPermission(appAdminUser, 1, true));
        apiUserPermissionRepository.save(new ApiUserPermission(appAdminUser, 20, true));
    }

    @After
    public void cleanup(){
        super.cleanUp();
        feedbackCompanyRepository.deleteAllInBatch();
        apiUserPermissionRepository.deleteAllInBatch();
    }

    @Test
    public void testGetFeedbackCompanies() throws Exception{
        String adminJWTToken = requestAppAdminJWTToken();

//        String result = this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
//                "/feedback_company")
//                .header("Authorization", adminJWTToken)).andReturn().getResponse()
//                .getContentAsString();

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/feedback_company")
                .header("Authorization", adminJWTToken)
                .header("promote",false))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void testGetFeedbackCompanyNotExistent()throws Exception{
        String adminJWTToken = requestAppAdminJWTToken();
        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/feedback_company/"+0)
                .header("Authorization", adminJWTToken))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteFeedbackCompany() throws Exception{
        String adminJWTToken = requestAppAdminJWTToken();

        this.mockMvc.perform(delete(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/feedback_company/"+feedbackCompany1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk());

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/feedback_company")
                .header("Authorization", adminJWTToken)
                .header("promote",false))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testGetFeedbackCompany() throws Exception{
        String adminJWTToken = requestAppAdminJWTToken();

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/feedback_company/"+feedbackCompany1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) feedbackCompany1.getId())));
    }

    @Test
    public void testCreateFeedbackCompany() throws Exception{
        String adminJWTToken = requestAppAdminJWTToken();

        String feedbackCompany = new JSONObject()
                .put("status","pending")
                .put("text","crazy feature")
                .put("promote",true)
                .toString();

        this.mockMvc.perform(post(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/feedback_company")
                .header("Authorization", adminJWTToken)
                .content(feedbackCompany))
                .andExpect(status().isCreated());
    }
}
