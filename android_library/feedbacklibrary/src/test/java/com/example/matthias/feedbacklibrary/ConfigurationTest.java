package com.example.matthias.feedbacklibrary;

import com.example.matthias.feedbacklibrary.models.FeedbackConfiguration;
import com.example.matthias.feedbacklibrary.models.FeedbackConfigurationItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import static org.junit.Assert.*;

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
        List<FeedbackConfigurationItem> configurationItemList;
        FeedbackConfiguration configuration;
        String jsonString;
        Gson gson = new Gson();
        Type listType = new TypeToken<List<FeedbackConfigurationItem>>(){}.getType();

        // JSON with CORRECT order
        jsonString = readJSONFile("configurationInput/text_audio_sc_rating.json");
        configurationItemList = gson.fromJson(jsonString, listType);
        configuration = new FeedbackConfiguration(configurationItemList);

        // Number of feedback mechanisms
        assertEquals(4, configurationItemList.size());
        assertEquals(4, configuration.getAllMechanisms().size());
        // Check correct types in configuration file
        assertEquals("TEXT_TYPE", configurationItemList.get(0).getType());
        assertEquals("AUDIO_TYPE", configurationItemList.get(1).getType());
        assertEquals("SCREENSHOT_TYPE", configurationItemList.get(2).getType());
        assertEquals("RATING_TYPE", configurationItemList.get(3).getType());
        // Check correct types in configuration object
        assertEquals("TEXT_TYPE", configuration.getAllMechanisms().get(0).getType());
        assertEquals("AUDIO_TYPE", configuration.getAllMechanisms().get(1).getType());
        assertEquals("SCREENSHOT_TYPE", configuration.getAllMechanisms().get(2).getType());
        assertEquals("RATING_TYPE", configuration.getAllMechanisms().get(3).getType());
        // Check correct order in configuration object
        for(int i = 0; i < configuration.getAllMechanisms().size(); ++i) {
            assertEquals(i + 1, configuration.getAllMechanisms().get(i).getOrder());
        }

        // JSON with WRONG order
        jsonString = readJSONFile("configurationInput/wrong_order_text_audio_sc_rating.json");
        configurationItemList = gson.fromJson(jsonString, listType);
        configuration = new FeedbackConfiguration(configurationItemList);

        // Number of feedback mechanisms
        assertEquals(4, configurationItemList.size());
        assertEquals(4, configuration.getAllMechanisms().size());
        // Check correct types in configuration file
        assertEquals("TEXT_TYPE", configurationItemList.get(0).getType());
        assertEquals("AUDIO_TYPE", configurationItemList.get(1).getType());
        assertEquals("SCREENSHOT_TYPE", configurationItemList.get(2).getType());
        assertEquals("RATING_TYPE", configurationItemList.get(3).getType());
        // Check correct types in configuration object
        assertEquals("TEXT_TYPE", configuration.getAllMechanisms().get(3).getType());
        assertEquals("AUDIO_TYPE", configuration.getAllMechanisms().get(1).getType());
        assertEquals("SCREENSHOT_TYPE", configuration.getAllMechanisms().get(2).getType());
        assertEquals("RATING_TYPE", configuration.getAllMechanisms().get(0).getType());
        // Check correct order in configuration object
        for(int i = 0; i < configuration.getAllMechanisms().size(); ++i) {
            assertEquals(i + 1, configuration.getAllMechanisms().get(i).getOrder());
        }
    }
}