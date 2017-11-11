package ch.fhnw.cere.orchestrator.models;


import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ApplicationTest {
    private Application application1;

    private Configuration pushConfiguration1;
    private Configuration pullConfiguration1;

    private Mechanism mechanism1;

    private ConfigurationMechanism configurationMechanism1;
    private ConfigurationMechanism configurationMechanism2;

    private Parameter parameter1;
    private Parameter parameter2;
    private Parameter parameter3;

    private Parameter parentParameter1;
    private Parameter childParameter1;
    private Parameter childParameter2;
    private Parameter childParameter3;
    private Parameter childParameter4;


    @Before
    public void setup() {
        application1 = new Application("Test application 1", 1, new Date(), new Date(), new ArrayList<>());

        pushConfiguration1 = new Configuration("Push configuration 1", TriggerType.PUSH, new Date(), new Date(), null, application1);
        pullConfiguration1 = new Configuration("Pull configuration 1", TriggerType.PULL, new Date(), new Date(), null, application1);

        mechanism1 = new Mechanism(MechanismType.TEXT_TYPE, null, new ArrayList<>());

        configurationMechanism1 = new ConfigurationMechanism(pushConfiguration1, mechanism1, true, 1, new Date(), new Date());
        configurationMechanism2 = new ConfigurationMechanism(pullConfiguration1, mechanism1, true, 1, new Date(), new Date());

        mechanism1.setConfigurationMechanisms(new ArrayList<ConfigurationMechanism>() {
            {
                add(configurationMechanism1);
                add(configurationMechanism2);
            }
        });

        pushConfiguration1.setConfigurationMechanisms(new ArrayList<ConfigurationMechanism>() {
            {
                add(configurationMechanism1);
            }
        });

        pullConfiguration1.setConfigurationMechanisms(new ArrayList<ConfigurationMechanism>() {
            {
                add(configurationMechanism2);
            }
        });

        parameter1 = new Parameter("title", "Title EN", new Date(), new Date(), "en", null, null, mechanism1);
        parameter2 = new Parameter("title", "Titel DE", new Date(), new Date(), "de", null, null, mechanism1);
        parameter3 = new Parameter("font-size", "10", new Date(), new Date(), "en", null, null, mechanism1);

        parentParameter1 = new Parameter("options", null, new Date(), new Date(), "en", null, null, mechanism1);
        childParameter1 = new Parameter("CAT_1", "Cat 1 EN", new Date(), new Date(), "en", parentParameter1, null, null);
        childParameter2 = new Parameter("CAT_2", "Cat 2 EN", new Date(), new Date(), "en", parentParameter1, null, null);
        childParameter3 = new Parameter("CAT_1", "Cat 1 DE", new Date(), new Date(), "de", parentParameter1, null, null);
        childParameter4 = new Parameter("CAT_3", "Cat 3 FR", new Date(), new Date(), "fr", parentParameter1, null, null);
        List<Parameter> childParameters = new ArrayList<Parameter>() {
            {
                add(childParameter1);
                add(childParameter2);
                add(childParameter3);
                add(childParameter4);
            }
        };
        parentParameter1.setParameters(childParameters);

        mechanism1.setParameters(new ArrayList<Parameter>() {
            {
                add(parameter1);
                add(parameter2);
                add(parameter3);
                add(parentParameter1);
            }
        });

        ArrayList<Configuration> configurations = new ArrayList<Configuration>() {
            {
                add(pushConfiguration1);
                add(pullConfiguration1);
            }
        };
        application1.setConfigurations(configurations);
    }

    @Test
    public void testFilterByLanguageDe() {
        application1.filterByLanguage("de", "en");

        Mechanism testMechanism1 = application1.getConfigurations().get(0).getMechanisms().get(0);
        assertEquals(testMechanism1.getParameters().size(), 3);
        assertTrue(testMechanism1.getParameters().contains(parameter2));
        assertTrue(testMechanism1.getParameters().contains(parameter3));
        assertTrue(testMechanism1.getParameters().contains(parentParameter1));
    }

    @Test
    public void testFilterByLanguageEn() {
        application1.filterByLanguage("en", "en");

        Mechanism testMechanism2 = application1.getConfigurations().get(0).getMechanisms().get(0);
        assertEquals(testMechanism2.getParameters().size(), 3);
        assertTrue(testMechanism2.getParameters().contains(parameter1));
        assertTrue(testMechanism2.getParameters().contains(parameter3));
        assertTrue(testMechanism2.getParameters().contains(parentParameter1));
    }

    @Test
    public void testFilterByLanguageFr() {
        application1.filterByLanguage("fr", "en");

        Mechanism testMechanism2 = application1.getConfigurations().get(0).getMechanisms().get(0);
        assertEquals(testMechanism2.getParameters().size(), 3);
        assertTrue(testMechanism2.getParameters().contains(parameter1));
        assertTrue(testMechanism2.getParameters().contains(parameter3));
        assertTrue(testMechanism2.getParameters().contains(parentParameter1));
    }
}
