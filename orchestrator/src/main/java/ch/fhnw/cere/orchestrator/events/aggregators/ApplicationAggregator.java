package ch.fhnw.cere.orchestrator.events.aggregators;

import ch.fhnw.cere.orchestrator.events.Event;
import ch.fhnw.cere.orchestrator.events.application.ApplicationCreatedEvent;
import ch.fhnw.cere.orchestrator.models.Application;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ApplicationAggregator {

    public Application construct(List<Event<Application>> events) {
        assert events.get(0) instanceof ApplicationCreatedEvent;

        Application application = null;
        for (Event<Application> event : orderByDate(events)) {
            application = event.apply(application);
        }

        return application;
    }

    public Application construct(List<Event<Application>> events, long version) {
        assert events.get(0) instanceof ApplicationCreatedEvent;

        Application application = null;
        int currentVersion = 1;
        for (Event<Application> event : orderByDate(events)) {
            application = event.apply(application);
            if (currentVersion == version) {
                break;
            }
            currentVersion++;
        }

        return application;
    }

    public Application construct(List<Event<Application>> events, Date timestamp) {
        assert events.get(0) instanceof ApplicationCreatedEvent;

        Application application = null;
        for (Event<Application> event : orderByDate(events)) {
            if (event.getCreatedAt().after(timestamp)) {
                break;
            }
            application = event.apply(application);
        }

        return application;
    }

    public Application construct(List<Event<Application>> events, String tag) {
        assert events.get(0) instanceof ApplicationCreatedEvent;

        // TODO implement --> create a version to tag relation?
        // TODO caching
        return null;
    }

    private List<Event<Application>> orderByDate(List<Event<Application>> events) {
        return events.stream()
                .sorted(Comparator.comparing(Event::getCreatedAt))
                .collect(Collectors.toList());
    }
}
