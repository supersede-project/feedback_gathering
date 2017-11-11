package ch.fhnw.cere.orchestrator.controllers;

import ch.fhnw.cere.orchestrator.models.Mechanism;
import ch.fhnw.cere.orchestrator.models.MechanismType;
import ch.fhnw.cere.orchestrator.models.Parameter;
import ch.fhnw.cere.orchestrator.repositories.ConfigurationMechanismRepository;
import ch.fhnw.cere.orchestrator.repositories.MechanismRepository;
import ch.fhnw.cere.orchestrator.repositories.ParameterRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
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
    private Parameter parameter3;
    private Mechanism mechanism1;

    private Parameter parentParameter1;
    private Parameter childParameter1;
    private Parameter childParameter2;
    private Parameter childParameter3;
    private Parameter childParameter4;

    @Autowired
    private ParameterRepository parameterRepository;
    @Autowired
    private MechanismRepository mechanismRepository;
    @Autowired
    private ConfigurationMechanismRepository configurationMechanismRepository;
    private String basePathEn = "/orchestrator/feedback/en/parameters";
    private String basePathDe = "/orchestrator/feedback/de/parameters";


    @Before
    public void setup() throws Exception {
        super.setup();

        this.parameterRepository.deleteAllInBatch();
        this.configurationMechanismRepository.deleteAllInBatch();
        this.mechanismRepository.deleteAllInBatch();

        this.mechanism1 = mechanismRepository.save(new Mechanism(MechanismType.TEXT_TYPE, null, null));

        this.parameter1 = parameterRepository.save(new Parameter("title", "Title EN", new Date(), new Date(), "en", null, null, mechanism1));
        this.parameter2 = parameterRepository.save(new Parameter("title", "Titel DE", new Date(), new Date(), "de", null, null, mechanism1));
        this.parameter3 = parameterRepository.save(new Parameter("font-size", "10", new Date(), new Date(), "en", null, null, mechanism1));
    }

    @After
    public void cleanUp() {
        super.cleanUp();
        this.parameterRepository.deleteAllInBatch();
        this.configurationMechanismRepository.deleteAllInBatch();
        this.mechanismRepository.deleteAllInBatch();
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
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].language", is("en")));

        mockMvc.perform(get(basePathDe + "/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].language", is("de")));
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

    @Test(expected = ServletException.class)
    public void postParameterUnauthorized() throws Exception {
        Parameter parameter = new Parameter("newKey", "newValue", new Date(), new Date(), "en", null, null, null, null);
        String parameterJson = toJson(parameter);

        this.mockMvc.perform(post(basePathEn)
                .contentType(contentType)
                .content(parameterJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void postParameter() throws Exception {
        Parameter parameter = new Parameter("newKey", "newValue", new Date(), new Date(), "en", null, null, null, null);
        String parameterJson = toJson(parameter);
        String adminJWTToken = requestAdminJWTToken();

        this.mockMvc.perform(post(basePathEn)
                .contentType(contentType)
                .header("Authorization", adminJWTToken)
                .content(parameterJson))
                .andExpect(status().isCreated());
    }

    @Test(expected = ServletException.class)
    public void deleteParameterUnauthorized() throws Exception {
        this.mockMvc.perform(delete(basePathEn + "/" + parameter1.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteParameter() throws Exception {
        String adminJWTToken = requestAdminJWTToken();
        this.mockMvc.perform(delete(basePathEn + "/" + parameter1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk());
    }

    @Test(expected = ServletException.class)
    public void updateParameterUnauthorized() throws Exception {
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

    @Test
    public void updateParameter() throws Exception {
        this.parameter2.setValue("Titel DE updated");
        String parameterJson = toJson(this.parameter2);
        String adminJWTToken = requestAdminJWTToken();

        this.mockMvc.perform(put(basePathEn + "/")
                .contentType(contentType)
                .content(parameterJson)
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key", is("title")))
                .andExpect(jsonPath("$.value", is("Titel DE updated")))
                .andExpect(jsonPath("$.id", is((int) this.parameter2.getId())));
    }
}