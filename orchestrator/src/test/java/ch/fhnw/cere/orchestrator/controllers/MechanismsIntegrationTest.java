package ch.fhnw.cere.orchestrator.controllers;

import ch.fhnw.cere.orchestrator.controllers.helpers.ApplicationTreeBuilder;
import ch.fhnw.cere.orchestrator.models.*;
import ch.fhnw.cere.orchestrator.repositories.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.ServletException;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class MechanismsIntegrationTest extends BaseIntegrationTest {

    private Application application1;
    private Configuration configuration1;
    private Configuration configuration2;
    private Mechanism mechanism1;
    private Mechanism mechanism2;
    private Mechanism mechanism3;
    private ApiUserPermission apiUserPermission;

    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private ParameterRepository parameterRepository;
    @Autowired
    private MechanismRepository mechanismRepository;
    @Autowired
    private ConfigurationMechanismRepository configurationMechanismRepository;
    @Autowired
    private ApplicationTreeBuilder applicationTreeBuilder;
    @Autowired
    private ApiUserPermissionRepository apiUserPermissionRepository;

    private String basePathEn = "/orchestrator/feedback/en/applications";
    private String basePathDe = "/orchestrator/feedback/de/applications";


    @Before
    public void setup() throws Exception {
        super.setup();

        this.parameterRepository.deleteAllInBatch();
        this.configurationMechanismRepository.deleteAllInBatch();
        this.mechanismRepository.deleteAllInBatch();
        this.applicationRepository.deleteAllInBatch();

        this.application1 = applicationRepository.save(applicationTreeBuilder.buildApplicationTree("Test application for mechanisms"));

        this.configuration1 = application1.getConfigurations().stream().filter(configuration -> configuration.getType().equals(TriggerType.PUSH)).findFirst().get();
        this.configuration2 = application1.getConfigurations().stream().filter(configuration -> configuration.getType().equals(TriggerType.PULL)).findFirst().get();

        this.mechanism1 = configuration1.getMechanisms().get(0);
        this.mechanism2 = configuration2.getMechanisms().get(0);
        this.mechanism3 = mechanismRepository.save(new Mechanism(MechanismType.RATING_TYPE, null, new ArrayList<>()));

        this.apiUserPermission = apiUserPermissionRepository.save(new ApiUserPermission(appAdminUser, application1, true));
    }

    @After
    public void cleanUp() {
        super.cleanUp();

        this.parameterRepository.deleteAllInBatch();
        this.configurationMechanismRepository.deleteAllInBatch();
        this.mechanismRepository.deleteAllInBatch();
        this.applicationRepository.deleteAllInBatch();
    }

    @Test
    public void getMechanisms() throws Exception {
        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/mechanisms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].type", is("TEXT_TYPE")))
                .andExpect(jsonPath("$[1].type", is("TEXT_TYPE")));
    }

    @Test
    public void getMechanism() throws Exception {
        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/mechanisms/" + mechanism1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is("TEXT_TYPE")))
                .andExpect(jsonPath("$.parameters", is(hasSize(4))));

        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/mechanisms/" + mechanism2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is("TEXT_TYPE")))
                .andExpect(jsonPath("$.parameters", is(hasSize(0))));
    }

    @Test
    public void createMechanism() throws Exception {
        String mechanismJson = "{\n" +
                "      \"type\": \"RATING_TYPE\",\n" +
                "      \"active\": true,\n" +
                "      \"order\": 2,\n" +
                "      \"canBeActivated\": false,\n" +
                "      \"parameters\": [\n" +
                "        {\n" +
                "          \"key\": \"title\",\n" +
                "          \"value\": \"Please rate\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"key\": \"maxRating\",\n" +
                "          \"value\": 5\n" +
                "        }" +
                "      ]\n" +
                "    }";

        String adminJWTToken = requestAppAdminJWTToken();

        this.mockMvc.perform(post(basePathEn + "/" + application1.getId() + "/mechanisms")
                .contentType(contentType)
                .header("Authorization", adminJWTToken)
                .content(mechanismJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void createMechanismWithNestedParameters() throws Exception {
        String mechanismJson = "{\n" +
                "      \"type\": \"CATEGORY_TYPE\",\n" +
                "      \"active\": true,\n" +
                "      \"order\": 2,\n" +
                "      \"canBeActivated\": false,\n" +
                "      \"parameters\": [\n" +
                "        {\n" +
                "          \"key\": \"title\",\n" +
                "          \"value\": \"Please choose from the following categories\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"key\": \"ownAllowed\",\n" +
                "          \"value\": 1\n" +
                "        },\n" +
                "        {\n" +
                "          \"key\": \"multiple\",\n" +
                "          \"value\": 1\n" +
                "        },\n" +
                "        {\n" +
                "          \"key\": \"options\",\n" +
                "          \"parameters\": [\n" +
                "            {\n" +
                "              \"key\": \"BUG_CATEGORY\",\n" +
                "              \"value\": \"Bug\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"key\": \"FEATURE_REQUEST_CATEGORY\",\n" +
                "              \"value\": \"Feature Request\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"key\": \"GENERAL_CATEGORY\",\n" +
                "              \"value\": \"General Feedback\"\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      ]\n" +
                "    }";

        String adminJWTToken = requestAppAdminJWTToken();

        this.mockMvc.perform(post(basePathEn + "/" + application1.getId() + "/mechanisms")
                .contentType(contentType)
                .header("Authorization", adminJWTToken)
                .content(mechanismJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteMechanismThatIsStillAttachedToAConfiguration() throws Exception {
        String adminJWTToken = requestAppAdminJWTToken();

        this.mockMvc.perform(delete(basePathEn + "/" + application1.getId() + "/mechanisms/" + mechanism1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isConflict());
    }

    @Test
    public void deleteMechanism() throws Exception {
        String adminJWTToken = requestAppAdminJWTToken();

        this.mockMvc.perform(delete(basePathEn + "/" + application1.getId() + "/mechanisms/" + mechanism3.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk());
    }

    @Test
    public void updateMechanism() throws Exception {
        this.mechanism1.setType(MechanismType.RATING_TYPE);
        String mechanismJson = toJson(this.mechanism1);

        String adminJWTToken = requestAppAdminJWTToken();

        this.mockMvc.perform(put(basePathEn + "/" + application1.getId() + "/mechanisms/" + mechanism1.getId())
                .contentType(contentType)
                .content(mechanismJson)
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) this.mechanism1.getId())))
                .andExpect(jsonPath("$.type", is(MechanismType.RATING_TYPE.toString())));
    }

    @Test
    public void getMechanismsOfConfiguration() throws Exception {
        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/configurations/" + configuration1.getId() + "/mechanisms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].type", is("TEXT_TYPE")));

        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/configurations/" + configuration2.getId() + "/mechanisms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].type", is("TEXT_TYPE")));
    }

    @Test
    public void getMechanismOfConfiguration() throws Exception {
        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/configurations/" + configuration1.getId() + "/mechanisms/" + mechanism1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is("TEXT_TYPE")))
                .andExpect(jsonPath("$.parameters", is(hasSize(4))));

        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/configurations/" + configuration2.getId() + "/mechanisms/" + mechanism2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is("TEXT_TYPE")))
                .andExpect(jsonPath("$.parameters", is(hasSize(0))));

        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/configurations/" + configuration1.getId() + "/mechanisms/" + mechanism2.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createMechanismForConfiguration() throws Exception {
        String mechanismJson = "{\n" +
                "      \"type\": \"CATEGORY_TYPE\",\n" +
                "      \"active\": true,\n" +
                "      \"order\": 2,\n" +
                "      \"canBeActivated\": false,\n" +
                "      \"parameters\": [\n" +
                "        {\n" +
                "          \"key\": \"title\",\n" +
                "          \"value\": \"Please choose from the following categories\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"key\": \"ownAllowed\",\n" +
                "          \"value\": 1\n" +
                "        },\n" +
                "        {\n" +
                "          \"key\": \"multiple\",\n" +
                "          \"value\": 1\n" +
                "        },\n" +
                "        {\n" +
                "          \"key\": \"options\",\n" +
                "          \"parameters\": [\n" +
                "            {\n" +
                "              \"key\": \"BUG_CATEGORY\",\n" +
                "              \"value\": \"Bug\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"key\": \"FEATURE_REQUEST_CATEGORY\",\n" +
                "              \"value\": \"Feature Request\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"key\": \"GENERAL_CATEGORY\",\n" +
                "              \"value\": \"General Feedback\"\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      ]\n" +
                "    }";

        String adminJWTToken = requestAppAdminJWTToken();

        MvcResult result = this.mockMvc.perform(post(basePathEn + "/" + application1.getId() + "/configurations/" + configuration1.getId() + "/mechanisms")
                .contentType(contentType)
                .header("Authorization", adminJWTToken)
                .content(mechanismJson))
                .andExpect(status().isCreated()).andReturn();

        String createdMechanismString = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Mechanism createdMechanism = mapper.readValue(createdMechanismString, Mechanism.class);

        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/mechanisms/" + createdMechanism.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is("CATEGORY_TYPE")))
                .andExpect(jsonPath("$.parameters", is(hasSize(4))));

        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/configurations/" + configuration1.getId() + "/mechanisms/" + createdMechanism.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is("CATEGORY_TYPE")))
                .andExpect(jsonPath("$.parameters", is(hasSize(4))));


        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/configurations/" + configuration1.getId() + "/mechanisms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].type", is("TEXT_TYPE")))
                .andExpect(jsonPath("$[1].type", is("CATEGORY_TYPE")));
    }

    @Test(expected = ServletException.class)
    public void deleteMechanismForConfigurationWithoutPermission() throws Exception {
        apiUserPermission.setPermission(false);
        apiUserPermissionRepository.save(apiUserPermission);

        String adminJWTToken = requestAppAdminJWTToken();

        this.mockMvc.perform(delete(basePathEn + "/" + application1.getId() + "/configurations/" + configuration1.getId() + "/mechanisms/" + mechanism1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteMechanismForConfiguration() throws Exception {
        String adminJWTToken = requestAppAdminJWTToken();

        this.mockMvc.perform(delete(basePathEn + "/" + application1.getId() + "/configurations/" + configuration1.getId() + "/mechanisms/" + mechanism1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk());
    }

    @Test
    public void updateMechanismForConfiguration() throws Exception {
        this.mechanism1.setType(MechanismType.RATING_TYPE);
        String mechanismJson = toJson(this.mechanism1);

        String adminJWTToken = requestAppAdminJWTToken();

        this.mockMvc.perform(put(basePathEn + "/" + application1.getId() + "/configurations/" + configuration1.getId() + "/mechanisms/" + mechanism1.getId())
                .contentType(contentType)
                .content(mechanismJson)
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) this.mechanism1.getId())))
                .andExpect(jsonPath("$.type", is(MechanismType.RATING_TYPE.toString())));
    }
}