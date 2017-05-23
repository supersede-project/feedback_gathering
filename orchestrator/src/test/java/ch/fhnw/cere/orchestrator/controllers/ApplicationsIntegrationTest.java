package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.models.Application;
import ch.fhnw.cere.orchestrator.repositories.ApplicationRepository;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.nullValue;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class ApplicationsIntegrationTest extends BaseIntegrationTest {

    private Application application1;
    private Application application2;

    @Autowired
    private ApplicationRepository applicationRepository;
    private String basePath = "/en/applications";


    @Before
    public void setup() throws Exception {
        super.setup();

        this.applicationRepository.deleteAllInBatch();

        this.application1 = applicationRepository.save(new Application("Test App 1", 1, new Date(), new Date(), null));
        this.application2 = applicationRepository.save(new Application("Test App 2", 1, new Date(), new Date(), null));
    }

    @Test
    public void applicationNotFound() throws Exception {
        this.mockMvc.perform(get(basePath + "/9999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getApplications() throws Exception {
        mockMvc.perform(get(basePath + "/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getApplication() throws Exception {
        mockMvc.perform(get(basePath + "/" + application1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.application1.getId())))
                .andExpect(jsonPath("$.name", is("Test App 1")))
                .andExpect(jsonPath("$.state", is(1)))
                .andExpect(jsonPath("$.configurations", is(Matchers.empty())));
    }

    @Test
    public void postApplication() throws Exception {
        Application application = new Application("Test App 3", 1, new Date(), new Date(), null);
        String applicationJson = toJson(application);

        this.mockMvc.perform(post(basePath)
                .contentType(contentType)
                .content(applicationJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteApplication() throws Exception {
        this.mockMvc.perform(delete(basePath + "/" + application1.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void updateApplication() throws Exception {
        this.application2.setName("Updated name for App 2");
        this.application2.setState(0);
        String applicationJson = toJson(this.application2);

        this.mockMvc.perform(put(basePath + "/")
                .contentType(contentType)
                .content(applicationJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) this.application2.getId())))
                .andExpect(jsonPath("$.name", is("Updated name for App 2")))
                .andExpect(jsonPath("$.state", is(0)))
                .andExpect(jsonPath("$.configurations", is(nullValue())));
    }
}