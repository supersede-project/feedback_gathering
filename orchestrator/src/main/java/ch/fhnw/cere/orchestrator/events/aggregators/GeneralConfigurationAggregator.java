package ch.fhnw.cere.orchestrator.events.aggregators;

import ch.fhnw.cere.orchestrator.events.Event;
import ch.fhnw.cere.orchestrator.events.generalconfiguration.GeneralConfigurationCreatedEvent;
import ch.fhnw.cere.orchestrator.models.GeneralConfiguration;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GeneralConfigurationAggregator {

    public GeneralConfiguration construct(List<Event<GeneralConfiguration>> events) {
        assert events.get(0) instanceof GeneralConfigurationCreatedEvent;

        GeneralConfiguration generalConfiguration = null;
        for (Event<GeneralConfiguration> event : orderByDate(events)) {
            generalConfiguration = event.apply(generalConfiguration);
        }

        return generalConfiguration;
    }

    public GeneralConfiguration construct(List<Event<GeneralConfiguration>> events, long version) {
        assert events.get(0) instanceof GeneralConfigurationCreatedEvent;

        GeneralConfiguration generalConfiguration = null;
        int currentVersion = 1;
        for (Event<GeneralConfiguration> event : orderByDate(events)) {
            generalConfiguration = event.apply(generalConfiguration);
            if (currentVersion == version) {
                break;
            }
            currentVersion++;
        }

        return generalConfiguration;
    }

    private List<Event<GeneralConfiguration>> orderByDate(List<Event<GeneralConfiguration>> events) {
        return events.stream()
                .sorted(Comparator.comparing(Event::getCreatedAt))
                .collect(Collectors.toList());
    }
}
