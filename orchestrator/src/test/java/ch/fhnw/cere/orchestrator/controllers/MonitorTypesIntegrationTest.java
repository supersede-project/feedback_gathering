package ch.fhnw.cere.orchestrator.controllers;

import ch.fhnw.cere.orchestrator.models.MonitorType;
import ch.fhnw.cere.orchestrator.repositories.MonitorTypeRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class MonitorTypesIntegrationTest extends BaseIntegrationTest {

    private MonitorType monitorType1;
    private MonitorType monitorType2;

    @Autowired
    private MonitorTypeRepository monitorTypeRepository;
    private String basePath = "/orchestrator/monitoring/MonitorTypes";

    @Before
    public void setUp() throws Exception {
        super.setup();
        this.monitorTypeRepository.deleteAllInBatch();

        this.monitorType1 = monitorTypeRepository.save(new MonitorType("SocialNetworks"));
        this.monitorType2 = monitorTypeRepository.save(new MonitorType("MarketPlaces"));
    }

    @After
    public void cleanUp() {
        super.cleanUp();
        this.monitorTypeRepository.deleteAllInBatch();
    }

    @Test
    public void getMonitorTypes() throws Exception {
        mockMvc.perform(get(basePath + "/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getMonitorTypeByName() throws Exception {
        mockMvc.perform(get(basePath + "/" + "MarketPlaces"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.monitorType2.getId())))
                .andExpect(jsonPath("$.name", is("MarketPlaces")));
    }

    @Test(expected = Exception.class)
    public void createMonitorTypeUnauthorized() throws Exception {
        MonitorType monitorType = new MonitorType("UserEvents");
        String monitorTypeJson = toJson(monitorType);

        this.mockMvc.perform(post(basePath)
                .contentType(contentType)
                .content(monitorTypeJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void createMonitorType() throws Exception {
        String adminJWTToken = requestAppAdminJWTToken();
        MonitorType monitorType = new MonitorType("UserEvents");
        String monitorTypeJson = toJson(monitorType);

        this.mockMvc.perform(post(basePath)
                .contentType(contentType)
                .content(monitorTypeJson)
                .header("Authorization", adminJWTToken))
                .andExpect(status().isCreated());
    }

    @Test(expected = Exception.class)
    public void deleteMonitorTypeByNameUnauthorized() throws Exception {
        this.mockMvc.perform(delete(basePath + "/" + monitorType1.getName()))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteMonitorTypeByName() throws Exception {
        String adminJWTToken = requestAppAdminJWTToken();

        this.mockMvc.perform(delete(basePath + "/" + monitorType1.getName())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk());
    }
}