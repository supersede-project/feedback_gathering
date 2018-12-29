package ch.fhnw.cere.orchestrator.events;

import ch.fhnw.cere.orchestrator.events.aggregators.RootAggregator;
import ch.fhnw.cere.orchestrator.events.application.ApplicationCreatedEvent;
import ch.fhnw.cere.orchestrator.events.application.ApplicationGeneralConfigurationSetEvent;
import ch.fhnw.cere.orchestrator.events.application.ApplicationNameSetEvent;
import ch.fhnw.cere.orchestrator.events.application.ApplicationStateSetEvent;
import ch.fhnw.cere.orchestrator.events.generalconfiguration.GeneralConfigurationCreatedEvent;
import ch.fhnw.cere.orchestrator.events.generalconfiguration.GeneralConfigurationNameSetEvent;
import ch.fhnw.cere.orchestrator.models.Application;
import ch.fhnw.cere.orchestrator.models.GeneralConfiguration;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RootAggregatorTest {

    private final RootAggregator rootAggregator = new RootAggregator();
    private final long applicationId = 1;
    private final long generalConfigurationId = 2;
    private final GeneralConfiguration generalConfiguration = GeneralConfiguration.builder().id(generalConfigurationId).name("General Configuration").build();
    private final List<Event> events = new ArrayList<Event>() {{
        add(ApplicationCreatedEvent.builder().data(Application.builder().id(applicationId).name("Application 1").build()).build());
        add(ApplicationNameSetEvent.builder().name("App 1").build());
        add(GeneralConfigurationCreatedEvent.builder().data(generalConfiguration).build());
        add(ApplicationGeneralConfigurationSetEvent.builder().applicationId(applicationId).generalConfiguration(generalConfiguration).build());
        add(ApplicationStateSetEvent.builder().inactive().build());
        add(GeneralConfigurationNameSetEvent.builder().generalConfigurationId(generalConfigurationId).name("General Conf for App 1").build());
        add(ApplicationStateSetEvent.builder().active().build());
    }};

    @Test
    public void constructApplicationTree() throws Exception {
        final Application expectedApplication = Application.builder()
                .id(applicationId)
                .name("App 1")
                .state(1)
                .generalConfiguration(GeneralConfiguration.builder()
                        .id(2)
                        .name("General Conf for App 1")
                        .build())
                .build();
        final Application result = rootAggregator.construct(events);

        assertEquality(expectedApplication, result);
    }

    @Test
    public void constructApplicationTreeForVersion() throws Exception {
        final Application expectedApplicationVersion2 = Application.builder()
                .id(applicationId)
                .name("App 1")
                .state(0)
                .build();
        final Application resultVersion2 = rootAggregator.construct(events, 2);

        assertEquality(expectedApplicationVersion2, resultVersion2);


        final Application expectedApplicationVersion4 = Application.builder()
                .id(applicationId)
                .name("App 1")
                .state(0)
                .generalConfiguration(GeneralConfiguration.builder()
                        .id(2)
                        .name("General Configuration")
                        .build())
                .build();
        final Application resultVersion4 = rootAggregator.construct(events, 4);

        assertEquality(expectedApplicationVersion4, resultVersion4);


        final Application expectedApplicationVersion6 = Application.builder()
                .id(applicationId)
                .name("App 1")
                .state(0)
                .generalConfiguration(GeneralConfiguration.builder()
                        .id(2)
                        .name("General Conf for App 1")
                        .build())
                .build();
        final Application resultVersion6 = rootAggregator.construct(events, 6);

        assertEquality(expectedApplicationVersion6, resultVersion6);
    }

    private void assertEquality(Application expected, Application result) {
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getName(), result.getName());
        assertEquals(expected.getState(), result.getState());
        assertTrue(
                (expected.getGeneralConfiguration() == null && result.getGeneralConfiguration() == null) ||
                (expected.getGeneralConfiguration() != null && result.getGeneralConfiguration() != null));

        if (expected.getConfigurations() != null && result.getConfigurations() != null) {
            assertEquals(expected.getGeneralConfiguration().getId(), result.getGeneralConfiguration().getId());
            assertEquals(expected.getGeneralConfiguration().getName(), result.getGeneralConfiguration().getName());
        }
    }
}
