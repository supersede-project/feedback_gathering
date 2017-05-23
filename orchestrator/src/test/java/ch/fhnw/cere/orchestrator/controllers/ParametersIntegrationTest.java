package ch.fhnw.cere.orchestrator.controllers;

import ch.fhnw.cere.orchestrator.models.Parameter;
import ch.fhnw.cere.orchestrator.repositories.ParameterRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ParametersIntegrationTest extends BaseIntegrationTest {

    private Parameter parameter1;
    private Parameter parameter2;

    @Autowired
    private ParameterRepository parameterRepository;
    private String basePathEn = "/en/parameters";
    private String basePathDe = "/de/parameters";


    @Before
    public void setup() throws Exception {
        super.setup();

        this.parameterRepository.deleteAllInBatch();

        this.parameter1 = parameterRepository.save(new Parameter("title", "Title EN", new Date(), new Date(), "en", null, null, null));
        this.parameter2 = parameterRepository.save(new Parameter("title", "Titel DE", new Date(), new Date(), "de", null, null, null));
    }

    @Test
    public void parameterNotFound() throws Exception {
        this.mockMvc.perform(get(basePathEn + "/9999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getParameters() throws Exception {
        mockMvc.perform(get(basePathEn + "/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getParameter() throws Exception {
        mockMvc.perform(get(basePathEn + "/" + parameter1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.parameter1.getId())))
                .andExpect(jsonPath("$.key", is("title")))
                .andExpect(jsonPath("$.value", is("Title EN")));
    }

    @Test
    public void postParameter() throws Exception {
        Parameter parameter = new Parameter("newKey", "newValue", new Date(), new Date(), "en", null, null, null, null);
        String parameterJson = toJson(parameter);

        this.mockMvc.perform(post(basePathEn)
                .contentType(contentType)
                .content(parameterJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteParameter() throws Exception {
        this.mockMvc.perform(delete(basePathEn + "/" + parameter1.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void updateParameter() throws Exception {
        this.parameter2.setValue("Titel DE updated");
        String parameterJson = toJson(this.parameter2);

        this.mockMvc.perform(put(basePathEn + "/")
                .contentType(contentType)
                .content(parameterJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) this.parameter2.getId())))
                .andExpect(jsonPath("$.key", is("title")))
                .andExpect(jsonPath("$.value", is("Titel DE updated")));
    }
}