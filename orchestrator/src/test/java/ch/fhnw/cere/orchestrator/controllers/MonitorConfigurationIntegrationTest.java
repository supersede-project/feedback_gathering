package ch.fhnw.cere.orchestrator.controllers;

import ch.fhnw.cere.orchestrator.models.MonitorConfiguration;
import ch.fhnw.cere.orchestrator.models.MonitorTool;
import ch.fhnw.cere.orchestrator.models.MonitorType;
import ch.fhnw.cere.orchestrator.repositories.MonitorConfigurationRepository;
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


public class MonitorConfigurationIntegrationTest extends BaseIntegrationTest {

    private MonitorType monitorType1;
    private MonitorType monitorType2;

    private MonitorTool monitorTool1;
    private MonitorTool monitorTool2;

    private MonitorConfiguration monitorConfiguration1;
    private MonitorConfiguration monitorConfiguration2;

    @Autowired
    private MonitorTypeRepository monitorTypeRepository;
    @Autowired
    private MonitorToolRepository monitorToolRepository;
    @Autowired
    private MonitorConfigurationRepository monitorConfigurationRepository;
    private String basePath = "/orchestrator/monitoring/MonitorTypes";

    @Before
    public void setUp() throws Exception {
        super.setup();
        this.monitorConfigurationRepository.deleteAllInBatch();
        this.monitorToolRepository.deleteAllInBatch();
        this.monitorTypeRepository.deleteAllInBatch();

        this.monitorType1 = monitorTypeRepository.save(new MonitorType("SocialNetworks"));
        this.monitorType2 = monitorTypeRepository.save(new MonitorType("MarketPlaces"));

        this.monitorTool1 = monitorToolRepository.save(new MonitorTool("SocialNetworksTool1", null, monitorType1, "Social Networks Tool 1 Name"));
        this.monitorTool2 = monitorToolRepository.save(new MonitorTool("MarketPlacesTool1", null, monitorType2, "Market Places Tool 1 Name"));

        this.monitorConfiguration1 = monitorConfigurationRepository.save(new MonitorConfiguration(monitorTool1, "WP4", "Sat June 08 02:16:57 2016", "500", "http://localhost:9092", "olympicGamesTwitterMonitoring", "active", "keyword1 AND keyword2", null, null, null));
        this.monitorConfiguration2 = monitorConfigurationRepository.save(new MonitorConfiguration(monitorTool1, "manual", "Sat June 08 02:16:57 2016", "304", "http://localhost:9092", "ignoreMe", "active", "keyword4 AND keyword AND keyword5", null, null, null));
    }

    @After
    public void cleanUp() {
        super.cleanUp();

        this.monitorConfigurationRepository.deleteAllInBatch();
        this.monitorToolRepository.deleteAllInBatch();
        this.monitorTypeRepository.deleteAllInBatch();
    }

    @Test
    public void getMonitorConfiguration() throws Exception {
        mockMvc.perform(get(basePath + "/SocialNetworks/Tools/SocialNetworksTool1/ToolConfigurations/" + monitorConfiguration1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.monitorConfiguration1.getId())))
                .andExpect(jsonPath("$.configSender", is("WP4")));
    }

    @Test(expected = Exception.class)
    public void createMonitorConfigurationUnauthorized() throws Exception {
        MonitorConfiguration monitorConfiguration = new MonitorConfiguration(monitorTool1, "sender", "Sat June 08 02:16:57 2017", "100", "http://localhost:9092", "ignoreMeAgain", "active", "keyword44 AND keyword AND keyword55", null, null, null);
        String monitorConfigurationJson = toJson(monitorConfiguration);

        this.mockMvc.perform(post(basePath + "/SocialNetworks/Tools/SocialNetworksTool1/ToolConfigurations")
                .contentType(contentType)
                .content(monitorConfigurationJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void createMonitorConfiguration() throws Exception {
        String adminJWTToken = requestAppAdminJWTToken();
        MonitorConfiguration monitorConfiguration = new MonitorConfiguration(monitorTool1, "sender", "Sat June 08 02:16:57 2017", "100", "http://localhost:9092", "ignoreMeAgain", "active", "keyword44 AND keyword AND keyword55", null, null, null);
        String monitorConfigurationJson = toJson(monitorConfiguration);

        this.mockMvc.perform(post(basePath + "/SocialNetworks/Tools/SocialNetworksTool1/ToolConfigurations")
                .contentType(contentType)
                .content(monitorConfigurationJson)
                .header("Authorization", adminJWTToken))
                .andExpect(status().isCreated());
    }

    @Test(expected = Exception.class)
    public void deleteMonitorConfigurationUnauthorized() throws Exception {
        this.mockMvc.perform(delete(basePath  + "/SocialNetworks/Tools/SocialNetworksTool1/ToolConfigurations/" + monitorConfiguration1.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteMonitorConfiguration() throws Exception {
        String adminJWTToken = requestAppAdminJWTToken();

        this.mockMvc.perform(delete(basePath + "/SocialNetworks/Tools/SocialNetworksTool1/ToolConfigurations/" + monitorConfiguration1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk());
    }

    @Test(expected = Exception.class)
    public void updateMonitorConfigurationUnauthorized() throws Exception {
        this.monitorConfiguration2.setConfigSender("updated sender");
        String monitoringConfigurationJson = toJson(this.monitorConfiguration2);

        this.mockMvc.perform(put(basePath + "/SocialNetworks/Tools/SocialNetworksTool1/ToolConfigurations/" + monitorConfiguration1.getId())
                .contentType(contentType)
                .content(monitoringConfigurationJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) this.monitorConfiguration2.getId())))
                .andExpect(jsonPath("$.configSender", is("updated sender")))
                .andExpect(jsonPath("$.timeSlot", is("304")));
    }

    @Test
    public void updateMonitorConfiguration() throws Exception {
        String adminJWTToken = requestAppAdminJWTToken();

        this.monitorConfiguration2.setConfigSender("updated sender");
        String monitoringConfigurationJson = toJson(this.monitorConfiguration2);

        this.mockMvc.perform(put(basePath + "/SocialNetworks/Tools/SocialNetworksTool1/ToolConfigurations/" + monitorConfiguration1.getId())
                .header("Authorization", adminJWTToken)
                .contentType(contentType)
                .content(monitoringConfigurationJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) this.monitorConfiguration2.getId())))
                .andExpect(jsonPath("$.configSender", is("updated sender")))
                .andExpect(jsonPath("$.timeSlot", is("304")));
    }
}