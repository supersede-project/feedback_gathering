package ch.fhnw.cere.orchestrator.events.application;

import ch.fhnw.cere.orchestrator.events.Event;
import ch.fhnw.cere.orchestrator.models.Application;
import ch.fhnw.cere.orchestrator.models.GeneralConfiguration;

import java.util.Date;

public class ApplicationGeneralConfigurationSetEvent implements Event<Application> {
    private long applicationId;
    private GeneralConfiguration generalConfiguration;
    private Date createdAt;

    private ApplicationGeneralConfigurationSetEvent(Builder builder) {
        this.applicationId = builder.applicationId;
        this.generalConfiguration = builder.generalConfiguration;
        this.createdAt = builder.createdAt;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public long getEntityId() {
        return applicationId;
    }

    @Override
    public Application apply(Application entity) {
        entity.setGeneralConfiguration(generalConfiguration);
        return entity;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long applicationId;
        private GeneralConfiguration generalConfiguration;
        private Date createdAt;

        public Builder applicationId(long applicationId) {
            this.applicationId = applicationId;
            return this;
        }

        public Builder generalConfiguration(GeneralConfiguration generalConfiguration) {
            this.generalConfiguration = generalConfiguration;
            return this;
        }

        public Builder createdAt(Date createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ApplicationGeneralConfigurationSetEvent build() {
            if (createdAt == null) {
                createdAt = new Date();
            }
            return new ApplicationGeneralConfigurationSetEvent(this);
        }

    }
}
