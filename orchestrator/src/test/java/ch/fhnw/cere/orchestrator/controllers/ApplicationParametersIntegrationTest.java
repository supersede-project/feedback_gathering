package ch.fhnw.cere.orchestrator.controllers;

import ch.fhnw.cere.orchestrator.controllers.helpers.ApplicationTreeBuilder;
import ch.fhnw.cere.orchestrator.models.*;
import ch.fhnw.cere.orchestrator.repositories.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class ApplicationParametersIntegrationTest extends BaseIntegrationTest {

    private Application application;
    private Application application2;

    private Mechanism mechanism1;
    private GeneralConfiguration generalConfiguration;
    private Configuration configuration1;
    private ConfigurationMechanism configurationMechanism1;

    private Parameter parameter1;
    private Parameter parameter2;
    private Parameter parameter3;

    private Parameter parentParameter1;
    private Parameter childParameter1;
    private Parameter childParameter2;
    private Parameter childParameter3;
    private Parameter childParameter4;

    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private ApplicationTreeBuilder applicationTreeBuilder;
    @Autowired
    private ParameterRepository parameterRepository;
    @Autowired
    private MechanismRepository mechanismRepository;
    @Autowired
    private GeneralConfigurationRepository generalConfigurationRepository;
    @Autowired
    private ConfigurationMechanismRepository configurationMechanismRepository;


    private String basePathEn = "/orchestrator/feedback/en/applications/";


    @Before
    public void setup() throws Exception {
        super.setup();

        this.parameterRepository.deleteAllInBatch();
        this.configurationMechanismRepository.deleteAllInBatch();
        this.generalConfigurationRepository.deleteAllInBatch();
        this.mechanismRepository.deleteAllInBatch();
        this.applicationRepository.deleteAllInBatch();

        this.application = applicationRepository.save(new Application("Application 1", 1, new Date(), new Date(), null));

        this.mechanism1 = mechanismRepository.save(new Mechanism(MechanismType.TEXT_TYPE, null, null));

        this.parameter1 = parameterRepository.save(new Parameter("title", "Title EN", new Date(), new Date(), "en", 1, null, null, null, mechanism1));
        this.parameter2 = parameterRepository.save(new Parameter("title", "Titel DE", new Date(), new Date(), "de", 2, null, null, null, mechanism1));
        this.parameter3 = parameterRepository.save(new Parameter("font-size", "10", new Date(), new Date(), "en", 3, null, null, null, mechanism1));

        this.parentParameter1 = parameterRepository.save(new Parameter("options", null, new Date(), new Date(), "en", 4, null, null, null, mechanism1));
        this.childParameter1 = parameterRepository.save(new Parameter("CAT_1", "category 1", new Date(), new Date(), "en", 1, this.parentParameter1, null, null, null));
        this.childParameter2 = parameterRepository.save(new Parameter("CAT_2", "category 2", new Date(), new Date(), "en", 2, this.parentParameter1, null, null, null));
        this.childParameter3 = parameterRepository.save(new Parameter("CAT_3", "category 3", new Date(), new Date(), "en", 3, this.parentParameter1, null, null, null));
        this.childParameter4 = parameterRepository.save(new Parameter("CAT_4", "category 4", new Date(), new Date(), "en", 4, this.parentParameter1, null, null, null));

        this.application2 = applicationRepository.save(applicationTreeBuilder.buildApplicationTree("Test application 3"));
    }

    @After
    public void cleanUp() {
        super.cleanUp();
        this.parameterRepository.deleteAllInBatch();
        this.configurationMechanismRepository.deleteAllInBatch();
        this.generalConfigurationRepository.deleteAllInBatch();
        this.mechanismRepository.deleteAllInBatch();
        this.applicationRepository.deleteAllInBatch();
    }

    @Test
    public void switchParameterOrderForMechanism() throws Exception {
        String adminJWTToken = requestAdminJWTToken();

        this.mockMvc.perform(put(basePathEn + application.getId() + "/mechanisms/" + mechanism1.getId() + "/parameters/" +
                parameter1.getId() + "/switchOrder/" + parameter2.getId())
                .contentType(contentType)
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].id", is((int) parameter2.getId())))
                .andExpect(jsonPath("$[0].order", is(1)))
                .andExpect(jsonPath("$[1].id", is((int) parameter1.getId())))
                .andExpect(jsonPath("$[1].order", is(2)))
                .andExpect(jsonPath("$[2].id", is((int) parameter3.getId())))
                .andExpect(jsonPath("$[2].order", is(3)))
                .andExpect(jsonPath("$[3].id", is((int) parentParameter1.getId())))
                .andExpect(jsonPath("$[3].order", is(4)));
    }

    @Test
    public void reorderParameterOrderForMechanism() throws Exception {
        String adminJWTToken = requestAdminJWTToken();

        this.mockMvc.perform(put(basePathEn + application.getId() + "/mechanisms/" + mechanism1.getId() + "/parameters/" +
                parameter1.getId() + "/switchOrder/" + parameter2.getId())
                .contentType(contentType)
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].id", is((int) parameter2.getId())))
                .andExpect(jsonPath("$[0].order", is(1)))
                .andExpect(jsonPath("$[1].id", is((int) parameter1.getId())))
                .andExpect(jsonPath("$[1].order", is(2)))
                .andExpect(jsonPath("$[2].id", is((int) parameter3.getId())))
                .andExpect(jsonPath("$[2].order", is(3)))
                .andExpect(jsonPath("$[3].id", is((int) parentParameter1.getId())))
                .andExpect(jsonPath("$[3].order", is(4)));

        this.mockMvc.perform(get("/orchestrator/feedback/en/mechanisms/" + mechanism1.getId() + "/parameters")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is((int) parameter1.getId())))
                .andExpect(jsonPath("$[0].order", is(2)))
                .andExpect(jsonPath("$[1].id", is((int) parameter3.getId())))
                .andExpect(jsonPath("$[1].order", is(3)))
                .andExpect(jsonPath("$[2].id", is((int) parentParameter1.getId())))
                .andExpect(jsonPath("$[2].order", is(4)));
    }

    @Test
    public void switchParameterOrderForParentParameter() throws Exception {
        String adminJWTToken = requestAdminJWTToken();

        this.mockMvc.perform(put(basePathEn + application.getId() + "/parameter/" + parentParameter1.getId() + "/parameters/" +
                childParameter2.getId() + "/switchOrder/" + childParameter4.getId())
                .contentType(contentType)
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].id", is((int) childParameter1.getId())))
                .andExpect(jsonPath("$[0].order", is(1)))
                .andExpect(jsonPath("$[1].id", is((int) childParameter4.getId())))
                .andExpect(jsonPath("$[1].order", is(2)))
                .andExpect(jsonPath("$[2].id", is((int) childParameter3.getId())))
                .andExpect(jsonPath("$[2].order", is(3)))
                .andExpect(jsonPath("$[3].id", is((int) childParameter2.getId())))
                .andExpect(jsonPath("$[3].order", is(4)));
    }

    @Test
    public void reorderParameterForParentParameter() throws Exception {
        String adminJWTToken = requestAdminJWTToken();

        this.mockMvc.perform(put(basePathEn + application.getId() + "/parameter/" + parentParameter1.getId() + "/parameters/" +
                childParameter1.getId() + "/reorder/4")
                .contentType(contentType)
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].id", is((int) childParameter2.getId())))
                .andExpect(jsonPath("$[0].order", is(1)))
                .andExpect(jsonPath("$[1].id", is((int) childParameter3.getId())))
                .andExpect(jsonPath("$[1].order", is(2)))
                .andExpect(jsonPath("$[2].id", is((int) childParameter4.getId())))
                .andExpect(jsonPath("$[2].order", is(3)))
                .andExpect(jsonPath("$[3].id", is((int) childParameter1.getId())))
                .andExpect(jsonPath("$[3].order", is(4)));
    }

    @Test
    public void reorderParameterForParentParameter2() throws Exception {
        String adminJWTToken = requestAdminJWTToken();

        this.mockMvc.perform(put(basePathEn + application.getId() + "/parameter/" + parentParameter1.getId() + "/parameters/" +
                childParameter2.getId() + "/reorder/3")
                .contentType(contentType)
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].id", is((int) childParameter1.getId())))
                .andExpect(jsonPath("$[0].order", is(1)))
                .andExpect(jsonPath("$[1].id", is((int) childParameter3.getId())))
                .andExpect(jsonPath("$[1].order", is(2)))
                .andExpect(jsonPath("$[2].id", is((int) childParameter2.getId())))
                .andExpect(jsonPath("$[2].order", is(3)))
                .andExpect(jsonPath("$[3].id", is((int) childParameter4.getId())))
                .andExpect(jsonPath("$[3].order", is(4)));
    }

    @Test
    public void reorderParameterForParentParameter3() throws Exception {
        String adminJWTToken = requestAdminJWTToken();

        this.mockMvc.perform(put(basePathEn + application.getId() + "/parameter/" + parentParameter1.getId() + "/parameters/" +
                childParameter4.getId() + "/reorder/2")
                .contentType(contentType)
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].id", is((int) childParameter1.getId())))
                .andExpect(jsonPath("$[0].order", is(1)))
                .andExpect(jsonPath("$[1].id", is((int) childParameter4.getId())))
                .andExpect(jsonPath("$[1].order", is(2)))
                .andExpect(jsonPath("$[2].id", is((int) childParameter2.getId())))
                .andExpect(jsonPath("$[2].order", is(3)))
                .andExpect(jsonPath("$[3].id", is((int) childParameter3.getId())))
                .andExpect(jsonPath("$[3].order", is(4)));
    }

    @Test
    public void reorderParameterForParentParameter4() throws Exception {
        String adminJWTToken = requestAdminJWTToken();

        this.mockMvc.perform(put(basePathEn + application.getId() + "/parameter/" + parentParameter1.getId() + "/parameters/" +
                childParameter4.getId() + "/reorder/1")
                .contentType(contentType)
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].id", is((int) childParameter4.getId())))
                .andExpect(jsonPath("$[0].order", is(1)))
                .andExpect(jsonPath("$[1].id", is((int) childParameter1.getId())))
                .andExpect(jsonPath("$[1].order", is(2)))
                .andExpect(jsonPath("$[2].id", is((int) childParameter2.getId())))
                .andExpect(jsonPath("$[2].order", is(3)))
                .andExpect(jsonPath("$[3].id", is((int) childParameter3.getId())))
                .andExpect(jsonPath("$[3].order", is(4)));
    }

    @Test
    public void reorderParameterForParentParameter5() throws Exception {
        String adminJWTToken = requestAdminJWTToken();

        this.mockMvc.perform(put(basePathEn + application.getId() + "/parameter/" + parentParameter1.getId() + "/parameters/" +
                childParameter2.getId() + "/reorder/3")
                .contentType(contentType)
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].id", is((int) childParameter1.getId())))
                .andExpect(jsonPath("$[0].order", is(1)))
                .andExpect(jsonPath("$[1].id", is((int) childParameter3.getId())))
                .andExpect(jsonPath("$[1].order", is(2)))
                .andExpect(jsonPath("$[2].id", is((int) childParameter2.getId())))
                .andExpect(jsonPath("$[2].order", is(3)))
                .andExpect(jsonPath("$[3].id", is((int) childParameter4.getId())))
                .andExpect(jsonPath("$[3].order", is(4)));
    }

    @Test
    public void reorderParameterForParentParameter6() throws Exception {
        String adminJWTToken = requestAdminJWTToken();

        this.mockMvc.perform(put(basePathEn + application.getId() + "/parameter/" + parentParameter1.getId() + "/parameters/" +
                childParameter3.getId() + "/reorder/3")
                .contentType(contentType)
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].id", is((int) childParameter1.getId())))
                .andExpect(jsonPath("$[0].order", is(1)))
                .andExpect(jsonPath("$[1].id", is((int) childParameter2.getId())))
                .andExpect(jsonPath("$[1].order", is(2)))
                .andExpect(jsonPath("$[2].id", is((int) childParameter3.getId())))
                .andExpect(jsonPath("$[2].order", is(3)))
                .andExpect(jsonPath("$[3].id", is((int) childParameter4.getId())))
                .andExpect(jsonPath("$[3].order", is(4)));
    }
}