package ch.uzh.supersede.feedbacklibrary;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.uzh.supersede.feedbacklibrary.configurations.Configuration;
import ch.uzh.supersede.feedbacklibrary.configurations.OrchestratorConfiguration;
import ch.uzh.supersede.feedbacklibrary.configurations.OrchestratorConfigurationItem;
import ch.uzh.supersede.feedbacklibrary.models.CategoryMechanism;
import ch.uzh.supersede.feedbacklibrary.models.AbstractMechanism;
import ch.uzh.supersede.feedbacklibrary.models.TextMechanism;
import ch.uzh.supersede.feedbacklibrary.utils.Utils;

import static ch.uzh.supersede.feedbacklibrary.models.AbstractMechanism.CATEGORY_TYPE;
import static ch.uzh.supersede.feedbacklibrary.models.AbstractMechanism.TEXT_TYPE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Class for testing the configuration
 */
public class ConfigurationTest {
    @Test
    public void configurationFileMechanismOrderTest() throws IOException {
        OrchestratorConfiguration orderedOrchestratorConfiguration = createConfiguration("configurationInput/application_5.json", -1);
        OrchestratorConfiguration shuffledOrchestratorConfiguration = createConfiguration("configurationInput/application_5_shuffled.json", -1);
        Configuration orderedActiveConfiguration = orderedOrchestratorConfiguration.getActiveConfiguration();
        Configuration shuffledActiveConfiguration = shuffledOrchestratorConfiguration.getActiveConfiguration();

        // Check if both configurations are of type PUSH and have the same id
        assertEquals("PUSH", orderedActiveConfiguration.getType());
        assertEquals("PUSH", shuffledActiveConfiguration.getType());
        assertEquals(orderedActiveConfiguration.getId(), shuffledActiveConfiguration.getId());

        // Check if both active configurations contain 6 mechanisms and that their order corresponds
        List<AbstractMechanism> orderedMechanismList = orderedActiveConfiguration.getMechanisms();
        List<AbstractMechanism> shuffledMechanismList = shuffledActiveConfiguration.getMechanisms();
        assertEquals(6, orderedMechanismList.size());
        assertEquals(6, shuffledMechanismList.size());
        for (int i = 0; i < 6; ++i) {
            assertEquals(orderedMechanismList.get(i).getId(), shuffledMechanismList.get(i).getId());
        }
    }

    private OrchestratorConfiguration createConfiguration(String configurationFile, long selectedPullConfigurationId) throws IOException {
        String jsonString;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        Gson gson = gsonBuilder.create();
        jsonString = readJSONFile(configurationFile);
        if (selectedPullConfigurationId == -1) {
            return new OrchestratorConfiguration(gson.fromJson(jsonString, OrchestratorConfigurationItem.class), true, -1);
        }
        return new OrchestratorConfiguration(gson.fromJson(jsonString, OrchestratorConfigurationItem.class), false, selectedPullConfigurationId);
    }

    @Test
    public void mechanismValidationTest() throws IOException {
        OrchestratorConfiguration orchestratorConfiguration = createConfiguration("configurationInput/application_6.json", -1);
        List<AbstractMechanism> mechanismList = orchestratorConfiguration.getActiveConfiguration().getMechanisms();
        List<String> stubList = new ArrayList<>();

        // Check if the first to mechanisms are of type TEXT_TYPE
        assertEquals(mechanismList.get(0).getType(), TEXT_TYPE);
        assertEquals(mechanismList.get(1).getType(), TEXT_TYPE);

        ((TextMechanism) mechanismList.get(0)).setInputText("");
        ((TextMechanism) mechanismList.get(1)).setInputText("");
        assertFalse((mechanismList.get(0)).isValid(stubList));
        assertTrue((mechanismList.get(1)).isValid(stubList));

        // Check if the mechanisms at index 8 and 9 are of type CATEGORY_TYPE
        assertEquals(mechanismList.get(8).getType(), CATEGORY_TYPE);
        assertEquals(mechanismList.get(9).getType(), CATEGORY_TYPE);

        assertFalse((mechanismList.get(8)).isValid(stubList));
        List<String> selectedOptions = new ArrayList<>();
        selectedOptions.add("selected");
        ((CategoryMechanism) mechanismList.get(8)).setSelectedOptions(selectedOptions);
        assertTrue((mechanismList.get(8)).isValid(stubList));
        assertTrue((mechanismList.get(9)).isValid(stubList));
    }

    @Test
    public void pullConfigurationTest() throws IOException {
        long triggerOne = 11L;
        long triggerTwo = 12L;
        OrchestratorConfiguration oneOrchestratorConfiguration = createConfiguration("configurationInput/application_5.json", triggerOne);
        OrchestratorConfiguration twoOrchestratorConfiguration = createConfiguration("configurationInput/application_5_shuffled.json", triggerTwo);
        Configuration oneActiveConfiguration = oneOrchestratorConfiguration.getActiveConfiguration();
        Configuration twoActiveConfiguration = twoOrchestratorConfiguration.getActiveConfiguration();

        // Check if both configurations are of type PULL
        assertEquals("PULL", oneActiveConfiguration.getType());
        assertEquals("PULL", oneActiveConfiguration.getType());

        // Check if the triggered configuration id corresponds to the one of the active configuration
        assertEquals(triggerOne, oneActiveConfiguration.getId());
        assertEquals(triggerTwo, twoActiveConfiguration.getId());

        // Check for the 'showIntermediateDialog' parameter
        for (Map<String, Object> map : oneActiveConfiguration.getGeneralConfiguration().getParameters()) {
            if (map.get("id").equals("showIntermediateDialog")) {
                assertTrue(Utils.intToBool(((Double) map.get("value")).intValue()));
                break;
            }
        }
        for (Map<String, Object> map : twoActiveConfiguration.getGeneralConfiguration().getParameters()) {
            if (map.get("id").equals("showIntermediateDialog")) {
                assertFalse(Utils.intToBool(((Double) map.get("value")).intValue()));
                break;
            }
        }
    }

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
}