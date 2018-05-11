package ch.fhnw.cere.orchestrator.controllers;

import ch.fhnw.cere.orchestrator.models.Mechanism;
import ch.fhnw.cere.orchestrator.models.MechanismType;
import ch.fhnw.cere.orchestrator.models.Parameter;
import ch.fhnw.cere.orchestrator.repositories.ConfigurationMechanismRepository;
import ch.fhnw.cere.orchestrator.repositories.MechanismRepository;
import ch.fhnw.cere.orchestrator.repositories.ParameterRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static com.jayway.restassured.RestAssured.expect;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class MechanismsParametersIntegrationTest extends BaseIntegrationTest {

    private Parameter parameter1;
    private Parameter parameter2;
    private Parameter parameter3;
    private Mechanism mechanism1;

    @Autowired
    private ParameterRepository parameterRepository;
    @Autowired
    private MechanismRepository mechanismRepository;
    @Autowired
    private ConfigurationMechanismRepository configurationMechanismRepository;
    private String basePathEn = "/orchestrator/feedback/en/mechanisms/";
    private String basePathDe = "/orchestrator/feedback/de/mechanisms/";


    @Before
    public void setup() throws Exception {
        super.setup();

        this.parameterRepository.deleteAllInBatch();
        this.configurationMechanismRepository.deleteAllInBatch();
        this.mechanismRepository.deleteAllInBatch();

        this.mechanism1 = mechanismRepository.save(new Mechanism(MechanismType.TEXT_TYPE, null, null));

        this.parameter1 = parameterRepository.save(new Parameter("title", "Title EN", new Date(), new Date(), "en", null, null, mechanism1));
        this.parameter2 = parameterRepository.save(new Parameter("title", "Titel DE", new Date(), new Date(), "de", null, null, mechanism1));
        this.parameter3 = parameterRepository.save(new Parameter("font-size", "10px", new Date(), new Date(), "en", null, null, mechanism1));
    }

    @After
    public void cleanUp() {
        super.cleanUp();
        this.parameterRepository.deleteAllInBatch();
        this.configurationMechanismRepository.deleteAllInBatch();
        this.mechanismRepository.deleteAllInBatch();
    }

    @Test
    public void getParameters() throws Exception {
        mockMvc.perform(get(basePathEn + this.mechanism1.getId() + "/parameters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].language", is("en")));

        mockMvc.perform(get(basePathDe + this.mechanism1.getId() + "/parameters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].language", is("de")))
                .andExpect(jsonPath("$[0].key", is("title")))
                .andExpect(jsonPath("$[0].value", is("Titel DE")))
                .andExpect(jsonPath("$[1].language", is("en")))
                .andExpect(jsonPath("$[1].key", is("font-size")))
                .andExpect(jsonPath("$[1].value", is("10px")));
    }

    @Test
    public void createParameterForMechanism() throws Exception {
        Parameter parameter = new Parameter("newKey", "newValue", new Date(), new Date(), "en", null, null, null, null);
        String parameterJson = toJson(parameter);
        String adminJWTToken = requestAdminJWTToken();

        mockMvc.perform(post(basePathEn + this.mechanism1.getId() + "/parameters")
                .contentType(contentType)
                .header("Authorization", adminJWTToken)
                .content(parameterJson))
                .andExpect(status().isCreated());

        mockMvc.perform(get(basePathEn + this.mechanism1.getId() + "/parameters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].language", is("en")))
                .andExpect(jsonPath("$[0].key", is("title")))
                .andExpect(jsonPath("$[0].value", is("Title EN")))
                .andExpect(jsonPath("$[1].language", is("en")))
                .andExpect(jsonPath("$[1].key", is("font-size")))
                .andExpect(jsonPath("$[1].value", is("10px")))
                .andExpect(jsonPath("$[2].language", is("en")))
                .andExpect(jsonPath("$[2].key", is("newKey")))
                .andExpect(jsonPath("$[2].value", is("newValue")));
    }

    @Test
    public void updateParameterForMechanism() throws Exception {
        this.parameter1.setValue("Title EN updated");
        String parameterJson = toJson(this.parameter1);
        String adminJWTToken = requestAdminJWTToken();

        mockMvc.perform(put(basePathEn + this.mechanism1.getId() + "/parameters/" + this.parameter1.getId())
                .contentType(contentType)
                .header("Authorization", adminJWTToken)
                .content(parameterJson))
                .andExpect(status().isOk());

        mockMvc.perform(get(basePathEn + this.mechanism1.getId() + "/parameters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].language", is("en")))
                .andExpect(jsonPath("$[0].key", is("title")))
                .andExpect(jsonPath("$[0].value", is("Title EN updated")))
                .andExpect(jsonPath("$[1].language", is("en")))
                .andExpect(jsonPath("$[1].key", is("font-size")))
                .andExpect(jsonPath("$[1].value", is("10px")));
    }

    @Test
    public void deleteParameterForMechanism() throws Exception {
        String adminJWTToken = requestAdminJWTToken();

        mockMvc.perform(delete(basePathEn + this.mechanism1.getId() + "/parameters/" + this.parameter3.getId())
                .contentType(contentType)
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk());

        mockMvc.perform(get(basePathDe + this.mechanism1.getId() + "/parameters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].language", is("de")))
                .andExpect(jsonPath("$[0].key", is("title")))
                .andExpect(jsonPath("$[0].value", is("Titel DE")));
    }
}