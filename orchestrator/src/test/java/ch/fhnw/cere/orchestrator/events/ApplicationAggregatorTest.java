package ch.fhnw.cere.orchestrator.events;

import ch.fhnw.cere.orchestrator.events.aggregators.ApplicationAggregator;
import ch.fhnw.cere.orchestrator.events.application.ApplicationCreatedEvent;
import ch.fhnw.cere.orchestrator.events.application.ApplicationNameSetEvent;
import ch.fhnw.cere.orchestrator.events.application.ApplicationStateSetEvent;
import ch.fhnw.cere.orchestrator.models.Application;
import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ApplicationAggregatorTest {

    private final ApplicationAggregator applicationAggregator = new ApplicationAggregator();
    private final long applicationId = 1;

    @Test
    public void constructApplication_1() throws Exception {
        final List<Event<Application>> applicationEvents = new ArrayList<Event<Application>>() {{
            add(ApplicationCreatedEvent.builder().data(Application.builder().id(applicationId).name("Application 1").build()).build());
        }};

        final Application expectedApplication = Application.builder().id(applicationId).name("Application 1").build();
        final Application result = applicationAggregator.construct(applicationEvents);

        assertEquality(expectedApplication, result);
    }

    @Test
    public void constructApplication_2() throws Exception {
        final List<Event<Application>> applicationEvents = new ArrayList<Event<Application>>() {{
            add(ApplicationCreatedEvent.builder().data(Application.builder().id(applicationId).name("Application 1").build()).build());
            add(ApplicationNameSetEvent.builder().name("App 1").build());
            add(ApplicationStateSetEvent.builder().active().build());
        }};

        final Application expectedApplication = Application.builder().id(applicationId).name("App 1").state(1).build();
        final Application result = applicationAggregator.construct(applicationEvents);

        assertEquality(expectedApplication, result);
    }

    @Test
    public void constructApplicationForVersion() throws Exception {
        final List<Event<Application>> applicationEvents = new ArrayList<Event<Application>>() {{
            add(ApplicationCreatedEvent.builder().data(Application.builder().id(applicationId).name("Application 1").build()).build());
            add(ApplicationNameSetEvent.builder().name("App 1").build());
            add(ApplicationStateSetEvent.builder().active().build());
        }};

        final Application expectedApplication = Application.builder().id(applicationId).name("App 1").state(0).build();
        final Application result = applicationAggregator.construct(applicationEvents, 2);

        assertEquality(expectedApplication, result);
    }

    @Test
    public void constructApplicationForDate() throws Exception {
        final List<Event<Application>> applicationEvents = new ArrayList<Event<Application>>() {{
            add(ApplicationCreatedEvent.builder().createdAt(Date.from(Instant.ofEpochMilli(1300000000))).data(Application.builder().id(applicationId).name("Application 1").build()).build());
            add(ApplicationNameSetEvent.builder().createdAt(Date.from(Instant.ofEpochMilli(1400000000))).name("App 1").build());
            add(ApplicationStateSetEvent.builder().createdAt(Date.from(Instant.ofEpochMilli(1500000000))).active().build());
        }};

        final Application expectedApplication = Application.builder().id(applicationId).name("App 1").state(0).build();
        final Application result = applicationAggregator.construct(applicationEvents, Date.from(Instant.ofEpochMilli(1450000000)));

        assertEquality(expectedApplication, result);
    }

    private void assertEquality(Application expected, Application result) {
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getName(), result.getName());
        assertEquals(expected.getState(), result.getState());
    }
}
