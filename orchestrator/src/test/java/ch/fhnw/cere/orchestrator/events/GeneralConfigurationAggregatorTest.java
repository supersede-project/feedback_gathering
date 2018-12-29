package ch.fhnw.cere.orchestrator.events;

import ch.fhnw.cere.orchestrator.events.aggregators.GeneralConfigurationAggregator;
import ch.fhnw.cere.orchestrator.events.generalconfiguration.GeneralConfigurationCreatedEvent;
import ch.fhnw.cere.orchestrator.events.generalconfiguration.GeneralConfigurationNameSetEvent;
import ch.fhnw.cere.orchestrator.models.GeneralConfiguration;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GeneralConfigurationAggregatorTest {

    private final GeneralConfigurationAggregator generalConfigurationAggregator = new GeneralConfigurationAggregator();
    private final long generalConfigurationId = 1;

    @Test
    public void constructGeneralConfiguration() throws Exception {
        final List<Event<GeneralConfiguration>> applicationEvents = new ArrayList<Event<GeneralConfiguration>>() {{
            add(GeneralConfigurationCreatedEvent.builder().data(GeneralConfiguration.builder().id(generalConfigurationId).name("General Configuration 1").build()).build());
            add(GeneralConfigurationNameSetEvent.builder().name("General Conf 1").build());
        }};

        final GeneralConfiguration expectedApplication = GeneralConfiguration.builder().id(generalConfigurationId).name("General Conf 1").build();
        final GeneralConfiguration result = generalConfigurationAggregator.construct(applicationEvents);

        assertEquality(expectedApplication, result);
    }

    private void assertEquality(GeneralConfiguration expected, GeneralConfiguration result) {
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getName(), result.getName());
    }
}
