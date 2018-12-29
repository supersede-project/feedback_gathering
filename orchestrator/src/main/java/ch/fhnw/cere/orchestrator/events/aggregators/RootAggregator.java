package ch.fhnw.cere.orchestrator.events.aggregators;

import ch.fhnw.cere.orchestrator.events.Event;
import ch.fhnw.cere.orchestrator.events.application.ApplicationCreatedEvent;
import ch.fhnw.cere.orchestrator.models.Application;
import ch.fhnw.cere.orchestrator.models.GeneralConfiguration;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RootAggregator {

    public Application construct(List<Event> events) {
        assert events.get(0) instanceof ApplicationCreatedEvent;

        Application application = null;
        GeneralConfiguration generalConfiguration = null;

        // TODO find a better way
        for (Event event : orderByDate(events)) {
            try {
                application = (Application) event.apply(application);
            } catch (ClassCastException e) {
                generalConfiguration = (GeneralConfiguration) event.apply(generalConfiguration);
            }
        }

        return application;
    }

    public Application construct(List<Event> events, long version) {
        assert events.get(0) instanceof ApplicationCreatedEvent;

        Application application = null;
        GeneralConfiguration generalConfiguration = null;
        int currentVersion = 1;

        for (Event event : orderByDate(events)) {
            try {
                application = (Application) event.apply(application);
            } catch (ClassCastException e) {
                generalConfiguration = (GeneralConfiguration) event.apply(generalConfiguration);
            }
            if (currentVersion == version) {
                break;
            }
            currentVersion++;
        }

        return application;
    }

    private List<Event> orderByDate(List<Event> events) {
        return events.stream()
                .sorted(Comparator.comparing(Event::getCreatedAt))
                .collect(Collectors.toList());
    }
}
