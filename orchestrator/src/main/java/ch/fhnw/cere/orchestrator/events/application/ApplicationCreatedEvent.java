package ch.fhnw.cere.orchestrator.events.application;

import ch.fhnw.cere.orchestrator.events.Event;
import ch.fhnw.cere.orchestrator.models.Application;

import java.util.Date;

public class ApplicationCreatedEvent implements Event<Application> {
    private Application data;
    private Date createdAt;

    private ApplicationCreatedEvent(Builder builder) {
        this.data = builder.data;
        this.createdAt = builder.createdAt;
    }

    @Override
    public String toString() {
        return "ApplicationCreatedEvent{" +
                "data=" + data +
                ", createdAt=" + createdAt +
                '}';
    }

    public Application getData() {
        return data;
    }

    public void setData(Application data) {
        this.data = data;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public long getEntityId() {
        return data.getId();
    }

    @Override
    public Application apply(Application entity) {
        assert entity == null;
        return data;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Application data;
        private Date createdAt;

        public Builder data(Application data) {
            this.data = data;
            return this;
        }

        public Builder createdAt(Date createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ApplicationCreatedEvent build() {
            if (createdAt == null) {
                createdAt = new Date();
            }
            return new ApplicationCreatedEvent(this);
        }
    }
}