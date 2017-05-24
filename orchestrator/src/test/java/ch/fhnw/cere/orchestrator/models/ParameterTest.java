package ch.fhnw.cere.orchestrator.models;


import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


public class ParameterTest {

    Parameter parameter1;
    Parameter parameter2;
    Parameter parameter3;

    Parameter parentParameter1;
    Parameter childParameter1;
    Parameter childParameter2;
    Parameter childParameter3;
    Parameter childParameter4;

    @Before
    public void setup() {
        parameter1 = new Parameter("title", "Title EN", new Date(), new Date(), "en", null, null, null);
        parameter2 = new Parameter("title", "Titel DE", new Date(), new Date(), "de", null, null, null);
        parameter3 = new Parameter("font-size", "10", new Date(), new Date(), "en", null, null, null);

        parentParameter1 = new Parameter("options", null, new Date(), new Date(), "en", null, null, null);
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
    }

    @Test
    public void testParametersByLanguage() {
        List<Parameter> languageParameters1 = parameter1.parametersByLanguage("de", "en");
        assertNull(languageParameters1);

        List<Parameter> languageParameters2 = parentParameter1.parametersByLanguage("de", "en");
        assertTrue(languageParameters2.size() == 2);
        assertTrue(languageParameters2.contains(childParameter3));
        assertTrue(languageParameters2.contains(childParameter2));

        List<Parameter> languageParameters3 = parentParameter1.parametersByLanguage("en", "en");
        assertTrue(languageParameters3.size() == 2);
        assertTrue(languageParameters3.contains(childParameter1));
        assertTrue(languageParameters3.contains(childParameter2));

        List<Parameter> languageParameters4 = parentParameter1.parametersByLanguage("fr", "en");
        assertTrue(languageParameters4.size() == 3);
        assertTrue(languageParameters4.contains(childParameter1));
        assertTrue(languageParameters4.contains(childParameter2));
        assertTrue(languageParameters4.contains(childParameter4));

        List<Parameter> languageParameters5 = parentParameter1.parametersByLanguage("it", "en");
        assertTrue(languageParameters5.size() == 2);
        assertTrue(languageParameters5.contains(childParameter1));
        assertTrue(languageParameters5.contains(childParameter2));
    }
}
