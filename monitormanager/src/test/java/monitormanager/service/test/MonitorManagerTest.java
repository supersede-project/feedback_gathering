package monitormanager.service.test;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import monitormanager.service.Application;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class MonitorManagerTest {
	
	private final static Logger log = LogManager.getLogger(MonitorManagerTest.class);


    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
            .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
            .findAny()
            .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }
    
    /**
     * This test creates a new base model using an UML file and escaping
     * the \"\ character properly by changing it to \'\
     * @throws Exception
     */
    @Test
    public void updateMonitorConfiguration() throws Exception {
    	File f = new File("");		
		List<String> lines = Files.readAllLines(Paths.get(f.getAbsolutePath() + "/src/test/java/monitormanager/service/test/create_monitor_conf.json"), StandardCharsets.UTF_8);
		String createConf = "";
		for (String s : lines) createConf += s + "\n";
		lines = Files.readAllLines(Paths.get(f.getAbsolutePath() + "/src/test/java/monitormanager/service/test/update_monitor.json"), StandardCharsets.UTF_8);
		String updateMonitorConf = "";
		for (String s : lines) updateMonitorConf += s + "\n";
		
		MvcResult result = mockMvc.perform(post("/Twitter/configuration")
				.content(createConf)
				.contentType(contentType))
				.andExpect(status().isCreated())
				.andReturn();
		
		JsonObject jsonObject = (new JsonParser()).parse(result.getResponse().getContentAsString()).getAsJsonObject();
		
		mockMvc.perform(put("/Twitter/configuration/" + jsonObject.get("idConf").getAsInt())
				.content(updateMonitorConf)
				.contentType(contentType))
				.andExpect(status().isOk())
				.andReturn();
		
		mockMvc.perform(delete("/Twitter/configuration/" + jsonObject.get("idConf").getAsInt()))
				.andExpect(status().isOk());
		
		System.out.println("Reach here");
		
    }
    
    /*@Test
    public void listModels() throws Exception {
    	mockMvc.perform(get("/models/AdaptabilityModel"))
    			.andExpect(status().isOk());
    }*/
    
    /*@Test
    public void getModel() throws Exception {
    	mockMvc.perform(get("/models/AdaptabilityModel/1"))
    			.andExpect(status().isOk());
    }*/
    
    /*@Test
    public void createModel() throws Exception {
    	mockMvc.perform(post("/models/AdaptabilityModel")
    			.content(new JsonObject().toString())
    			.contentType(contentType))
    			.andExpect(status().isCreated());
    }*/
    
    /*@Test
    public void updateModel() throws Exception {
    	mockMvc.perform(put("/models/AdaptabilityModel/1")
    			.content(new JsonObject().toString())
    			.contentType(contentType))
    			.andExpect(status().isOk());
    }*/
    
    /*@Test
    public void deleteModel() throws Exception {
    	mockMvc.perform(delete("/models/AdaptabilityModel/1"))
    			.andExpect(status().isOk())
    			.andExpect(jsonPath("$.error", is("DELETE")));
    }*/

}