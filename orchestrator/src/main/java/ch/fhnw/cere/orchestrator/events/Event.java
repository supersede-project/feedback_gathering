package ch.fhnw.cere.orchestrator.events;

import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.Date;

@Immutable
public interface Event<T> {
    Date getCreatedAt();
    long getEntityId();
    T apply(T entity);
}