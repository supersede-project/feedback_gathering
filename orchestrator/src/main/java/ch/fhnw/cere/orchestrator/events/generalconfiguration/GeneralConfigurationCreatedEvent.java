package ch.fhnw.cere.orchestrator.events.generalconfiguration;

import ch.fhnw.cere.orchestrator.events.Event;
import ch.fhnw.cere.orchestrator.models.GeneralConfiguration;

import java.util.Date;

public class GeneralConfigurationCreatedEvent implements Event<GeneralConfiguration> {
    private GeneralConfiguration data;
    private Date createdAt;

    private GeneralConfigurationCreatedEvent(Builder builder) {
        this.data = builder.data;
        this.createdAt = builder.createdAt;
    }

    @Override
    public String toString() {
        return "GeneralConfigurationCreatedEvent{" +
                "data=" + data +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public long getEntityId() {
        return data.getId();
    }

    @Override
    public GeneralConfiguration apply(GeneralConfiguration entity) {
        assert entity == null;
        return data;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private GeneralConfiguration data;
        private Date createdAt;

        public Builder data(GeneralConfiguration data) {
            this.data = data;
            return this;
        }

        public Builder createdAt(Date createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public GeneralConfigurationCreatedEvent build() {
            if (createdAt == null) {
                createdAt = new Date();
            }
            return new GeneralConfigurationCreatedEvent(this);
        }
    }
}
