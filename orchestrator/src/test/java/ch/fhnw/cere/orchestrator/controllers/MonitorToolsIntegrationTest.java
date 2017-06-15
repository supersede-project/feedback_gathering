package ch.fhnw.cere.orchestrator.controllers;

import ch.fhnw.cere.orchestrator.models.MonitorTool;
import ch.fhnw.cere.orchestrator.models.MonitorType;
import ch.fhnw.cere.orchestrator.repositories.MonitorToolRepository;
import ch.fhnw.cere.orchestrator.repositories.MonitorTypeRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class MonitorToolsIntegrationTest extends BaseIntegrationTest {

    private MonitorType monitorType1;
    private MonitorType monitorType2;

    private MonitorTool monitorTool1;
    private MonitorTool monitorTool2;

    @Autowired
    private MonitorTypeRepository monitorTypeRepository;
    @Autowired
    private MonitorToolRepository monitorToolRepository;
    private String basePath = "/orchestrator/monitoring/MonitorTypes";

    @Before
    public void setUp() throws Exception {
        super.setup();
        this.monitorToolRepository.deleteAllInBatch();
        this.monitorTypeRepository.deleteAllInBatch();

        this.monitorType1 = monitorTypeRepository.save(new MonitorType("SocialNetworks"));
        this.monitorType2 = monitorTypeRepository.save(new MonitorType("MarketPlaces"));

        this.monitorTool1 = monitorToolRepository.save(new MonitorTool("SocialNetworksTool1", null, monitorType1, "Social Networks Tool 1 Name"));
        this.monitorTool2 = monitorToolRepository.save(new MonitorTool("MarketPlacesTool1", null, monitorType2, "Market Places Tool 1 Name"));
    }

    @After
    public void cleanUp() {
        super.cleanUp();
        this.monitorToolRepository.deleteAllInBatch();
        this.monitorTypeRepository.deleteAllInBatch();
    }

    @Test
    public void getMonitorToolByName() throws Exception {
        mockMvc.perform(get(basePath + "/SocialNetworks/Tools/SocialNetworksTool1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.monitorTool1.getId())))
                .andExpect(jsonPath("$.name", is("SocialNetworksTool1")))
                .andExpect(jsonPath("$.monitorName", is("Social Networks Tool 1 Name")));

        mockMvc.perform(get(basePath + "/MarketPlaces/Tools/MarketPlacesTool1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.monitorTool2.getId())))
                .andExpect(jsonPath("$.name", is("MarketPlacesTool1")))
                .andExpect(jsonPath("$.monitorName", is("Market Places Tool 1 Name")));
    }

    @Test(expected = Exception.class)
    public void createMonitorToolUnauthorized() throws Exception {
        MonitorTool monitorTool = new MonitorTool("SocialNetworksTool2", null, monitorType1, "Social Networks Tool 2 Name");
        String monitorToolJson = toJson(monitorTool);

        this.mockMvc.perform(post(basePath)
                .contentType(contentType)
                .content(monitorToolJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void createMonitorTool() throws Exception {
        String adminJWTToken = requestAppAdminJWTToken();
        MonitorTool monitorTool = new MonitorTool("SocialNetworksTool2", null, monitorType1, "Social Networks Tool 2 Name");
        String monitorToolJson = toJson(monitorTool);

        this.mockMvc.perform(post(basePath)
                .contentType(contentType)
                .content(monitorToolJson)
                .header("Authorization", adminJWTToken))
                .andExpect(status().isCreated());
    }

    @Test(expected = Exception.class)
    public void deleteMonitorToolByNameUnauthorized() throws Exception {
        this.mockMvc.perform(delete(basePath + "/" + monitorType1.getName() + "/Tools/" + "SocialNetworksTool1"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteMonitorToolByName() throws Exception {
        String adminJWTToken = requestAppAdminJWTToken();

        this.mockMvc.perform(delete(basePath + "/" + monitorType1.getName() + "/Tools/" + "SocialNetworksTool1")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk());
    }
}