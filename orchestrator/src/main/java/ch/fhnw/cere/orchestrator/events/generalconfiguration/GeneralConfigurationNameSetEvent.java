package ch.fhnw.cere.orchestrator.events.generalconfiguration;

import ch.fhnw.cere.orchestrator.events.Event;
import ch.fhnw.cere.orchestrator.models.GeneralConfiguration;

import java.util.Date;

public class GeneralConfigurationNameSetEvent implements Event<GeneralConfiguration> {
    private long generalConfigurationId;
    private String name;
    private Date createdAt;

    private GeneralConfigurationNameSetEvent(Builder builder) {
        this.generalConfigurationId = builder.generalConfigurationId;
        this.name = builder.name;
        this.createdAt = builder.createdAt;
    }

    @Override
    public String toString() {
        return "GeneralConfigurationNameSetEvent{" +
                "generalConfigurationId=" + generalConfigurationId +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public long getEntityId() {
        return generalConfigurationId;
    }

    @Override
    public GeneralConfiguration apply(GeneralConfiguration entity) {
        entity.setName(name);
        return entity;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long generalConfigurationId;
        private String name;
        private Date createdAt;

        public Builder generalConfigurationId(long generalConfigurationId) {
            this.generalConfigurationId = generalConfigurationId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder createdAt(Date createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public GeneralConfigurationNameSetEvent build() {
            if (createdAt == null) {
                createdAt = new Date();
            }
            return new GeneralConfigurationNameSetEvent(this);
        }
    }
}
