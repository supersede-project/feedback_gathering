package ch.uzh.ifi.feedback.repository.serialization;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.serialization.DefaultSerializer;
import ch.uzh.ifi.feedback.repository.model.StatusOption;

@Singleton
public class StatusOptionSerializationService extends DefaultSerializer<StatusOption>{
}
