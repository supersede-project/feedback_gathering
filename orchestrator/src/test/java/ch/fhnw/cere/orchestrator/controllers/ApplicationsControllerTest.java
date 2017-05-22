package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.OrchestratorApplication;
import ch.fhnw.cere.orchestrator.models.Application;
import ch.fhnw.cere.orchestrator.repositories.ApplicationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrchestratorApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class ApplicationsControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    private Application application1;
    private Application application2;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.applicationRepository.deleteAllInBatch();

        this.application1 = applicationRepository.save(new Application("Test App 1", 1, new Date(), new Date(), null));
        this.application2 = applicationRepository.save(new Application("Test App 2", 1, new Date(), new Date(), null));
    }

    @Test
    public void applicationNotFound() throws Exception {
        mockMvc.perform(get("/applications/9999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getApplications() throws Exception {
        mockMvc.perform(get("/applications/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getApplication() throws Exception {
        mockMvc.perform(get("/applications/" + application1.getId()))
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

        this.mockMvc.perform(post("/applications")
                .contentType(contentType)
                .content(applicationJson))
                .andExpect(status().isCreated());
    }

    protected String toJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString= null;
        try {
            jsonString = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}