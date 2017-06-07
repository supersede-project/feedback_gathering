package ch.uzh.ifi.feedback.orchestrator.serialization;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.serialization.DefaultSerializer;

@Singleton
public abstract class OrchestratorSerializationService<T> extends DefaultSerializer<T> {

	public abstract void SetNestedParameters(T object);
}
