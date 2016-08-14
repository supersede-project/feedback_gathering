package ch.uzh.ifi.feedback.orchestrator.serialization;

import ch.uzh.ifi.feedback.library.rest.serialization.DefaultSerializer;

public abstract class OrchestratorSerializationService<T> extends DefaultSerializer<T> {

	public abstract void SetNestedParameters(T object);
}
