package com.example.matthias.feedbacklibrary;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Class for testing the configuration
 */
public class ConfigurationTest {
    private String readJSONFile(String fileName) throws IOException {
        InputStream input = this.getClass().getClassLoader()
                .getResourceAsStream(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }
        reader.close();
        return out.toString();
    }

    @Test
    public void testConfigurationFileOrder() throws IOException {
/*        List<MechanismConfigurationItem> configurationItemList;
        FeedbackConfiguration configuration;
        String jsonString;
        Gson gson = new Gson();
        Type listType = new TypeToken<List<MechanismConfigurationItem>>(){}.getType();

        // JSON with CORRECT order
        jsonString = readJSONFile("configurationInput/text_audio_sc_rating.json");
        configurationItemList = gson.fromJson(jsonString, listType);
        configuration = new FeedbackConfiguration(configurationItemList);

        // Number of feedback mechanisms
        assertEquals(4, configurationItemList.size());
        assertEquals(4, configuration.getAllPushMechanisms().size());
        // Check correct types in configuration file
        assertEquals("TEXT_TYPE", configurationItemList.get(0).getType());
        assertEquals("AUDIO_TYPE", configurationItemList.get(1).getType());
        assertEquals("SCREENSHOT_TYPE", configurationItemList.get(2).getType());
        assertEquals("RATING_TYPE", configurationItemList.get(3).getType());
        // Check correct types in configuration object
        assertEquals("TEXT_TYPE", configuration.getAllPushMechanisms().get(0).getType());
        assertEquals("AUDIO_TYPE", configuration.getAllPushMechanisms().get(1).getType());
        assertEquals("SCREENSHOT_TYPE", configuration.getAllPushMechanisms().get(2).getType());
        assertEquals("RATING_TYPE", configuration.getAllPushMechanisms().get(3).getType());
        // Check correct order in configuration object
        for(int i = 0; i < configuration.getAllPushMechanisms().size(); ++i) {
            assertEquals(i + 1, configuration.getAllPushMechanisms().get(i).getOrder());
        }

        // JSON with WRONG order
        jsonString = readJSONFile("configurationInput/wrong_order_text_audio_sc_rating.json");
        configurationItemList = gson.fromJson(jsonString, listType);
        configuration = new FeedbackConfiguration(configurationItemList);

        // Number of feedback mechanisms
        assertEquals(4, configurationItemList.size());
        assertEquals(4, configuration.getAllPushMechanisms().size());
        // Check correct types in configuration file
        assertEquals("TEXT_TYPE", configurationItemList.get(0).getType());
        assertEquals("AUDIO_TYPE", configurationItemList.get(1).getType());
        assertEquals("SCREENSHOT_TYPE", configurationItemList.get(2).getType());
        assertEquals("RATING_TYPE", configurationItemList.get(3).getType());
        // Check correct types in configuration object
        assertEquals("TEXT_TYPE", configuration.getAllPushMechanisms().get(3).getType());
        assertEquals("AUDIO_TYPE", configuration.getAllPushMechanisms().get(1).getType());
        assertEquals("SCREENSHOT_TYPE", configuration.getAllPushMechanisms().get(2).getType());
        assertEquals("RATING_TYPE", configuration.getAllPushMechanisms().get(0).getType());
        // Check correct order in configuration object
        for(int i = 0; i < configuration.getAllPushMechanisms().size(); ++i) {
            assertEquals(i + 1, configuration.getAllPushMechanisms().get(i).getOrder());
        }
        */
    }
}