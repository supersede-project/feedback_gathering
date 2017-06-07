package ch.uzh.ifi.feedback.orchestrator.serialization;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.serialization.DefaultSerializer;
import ch.uzh.ifi.feedback.orchestrator.model.User;

@Singleton
public class UserSerializationService extends DefaultSerializer<User>{
}
