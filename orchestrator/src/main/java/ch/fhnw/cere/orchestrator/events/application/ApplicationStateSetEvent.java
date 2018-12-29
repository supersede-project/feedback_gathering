package ch.fhnw.cere.orchestrator.events.application;

import ch.fhnw.cere.orchestrator.events.Event;
import ch.fhnw.cere.orchestrator.models.Application;

import java.util.Date;

public class ApplicationStateSetEvent implements Event<Application> {
    private long applicationId;
    private int state;
    private Date createdAt;

    private ApplicationStateSetEvent(Builder builder) {
        this.applicationId = builder.applicationId;
        this.state = builder.state;
        this.createdAt = builder.createdAt;
    }

    @Override
    public String toString() {
        return "ApplicationStateSetEvent{" +
                "applicationId=" + applicationId +
                ", state='" + state + '\'' +
                ", createdAt=" + createdAt +
                '}';
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
        entity.setState(state);
        return entity;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long applicationId;
        private int state;
        private Date createdAt;

        public Builder applicationId(long applicationId) {
            this.applicationId = applicationId;
            return this;
        }

        // TODO enum for state!
        public Builder active() {
            this.state = 1;
            return this;
        }

        public Builder inactive() {
            this.state = 0;
            return this;
        }

        public Builder createdAt(Date createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ApplicationStateSetEvent build() {
            if (createdAt == null) {
                createdAt = new Date();
            }
            return new ApplicationStateSetEvent(this);
        }
    }
}
