package ch.fhnw.cere.orchestrator.controllers;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class PingIntegrationTest extends BaseIntegrationTest {

    @Before
    public void setup() throws Exception {
        super.setup();
    }

    @After
    public void cleanUp() {
        super.cleanUp();
    }

    @Test
    public void testPing() throws Exception {
        mockMvc.perform(get("/ping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("pong")));
    }
}