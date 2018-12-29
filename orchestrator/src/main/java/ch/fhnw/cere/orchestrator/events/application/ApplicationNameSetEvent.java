package ch.fhnw.cere.orchestrator.events.application;

import ch.fhnw.cere.orchestrator.events.Event;
import ch.fhnw.cere.orchestrator.models.Application;

import java.util.Date;

public class ApplicationNameSetEvent implements Event<Application> {
    private long applicationId;
    private String name;
    private Date createdAt;

    private ApplicationNameSetEvent(Builder builder) {
        this.applicationId = builder.applicationId;
        this.name = builder.name;
        this.createdAt = builder.createdAt;
    }

    @Override
    public String toString() {
        return "ApplicationNameSetEvent{" +
                "applicationId=" + applicationId +
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
        return applicationId;
    }

    @Override
    public Application apply(Application entity) {
        entity.setName(name);
        return entity;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long applicationId;
        private String name;
        private Date createdAt;

        public Builder applicationId(long applicationId) {
            this.applicationId = applicationId;
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

        public ApplicationNameSetEvent build() {
            if (createdAt == null) {
                createdAt = new Date();
            }
            return new ApplicationNameSetEvent(this);
        }
    }
}
