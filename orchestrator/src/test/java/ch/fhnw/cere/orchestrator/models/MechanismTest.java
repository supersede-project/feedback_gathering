package ch.fhnw.cere.orchestrator.models;


import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


public class MechanismTest {

    private Mechanism mechanism1;

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
        mechanism1 = new Mechanism(MechanismType.TEXT_TYPE, null, new ArrayList<>());

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
    }

    @Test
    public void testParametersByLanguage() {
        List<Parameter> languageParameters2 = mechanism1.parametersByLanguage("de", "en");
        assertTrue(languageParameters2.size() == 3);
        assertTrue(languageParameters2.contains(parameter2));
        assertTrue(languageParameters2.contains(parameter3));
        assertTrue(languageParameters2.contains(parentParameter1));

        // check whether recusion works as well and nested parameters are also delivered in the correct language
        Parameter optionsParameter = languageParameters2.stream().filter(parameter -> parameter.getKey().equals("options")).findFirst().get();
        assertTrue(optionsParameter.getParameters().size() == 2);
        assertTrue(optionsParameter.getParameters().contains(childParameter2));
        assertTrue(optionsParameter.getParameters().contains(childParameter3));

        List<Parameter> languageParameters3 = mechanism1.parametersByLanguage("en", "en");
        assertTrue(languageParameters3.contains(parameter1));
        assertTrue(languageParameters3.contains(parameter3));
        assertTrue(languageParameters3.contains(parentParameter1));

        List<Parameter> languageParameters4 = mechanism1.parametersByLanguage("fr", "en");
        assertTrue(languageParameters4.size() == 3);
        assertTrue(languageParameters4.contains(parameter1));
        assertTrue(languageParameters4.contains(parameter3));
        assertTrue(languageParameters4.contains(parentParameter1));

        List<Parameter> languageParameters5 = mechanism1.parametersByLanguage("it", "en");
        assertTrue(languageParameters5.size() == 3);
        assertTrue(languageParameters5.contains(parameter1));
        assertTrue(languageParameters5.contains(parameter3));
        assertTrue(languageParameters5.contains(parentParameter1));
    }

    @Test
    public void testFilterByLanguage() {
        mechanism1.filterByLanguage("de", "en");
        System.out.println(mechanism1.getParameters());

        assertEquals(3, mechanism1.getParameters().size());
        assertTrue(mechanism1.getParameters().contains(parameter2));
        assertTrue(mechanism1.getParameters().contains(parameter3));
        assertTrue(mechanism1.getParameters().contains(parentParameter1));
        Parameter optionsParameter = mechanism1.getParameters().stream().filter(parameter -> parameter.getKey().equals("options")).findFirst().get();
        assertTrue(optionsParameter.getParameters().contains(childParameter2));
        assertTrue(optionsParameter.getParameters().contains(childParameter3));
    }
}
