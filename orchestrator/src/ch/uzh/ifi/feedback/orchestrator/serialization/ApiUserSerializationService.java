package ch.uzh.ifi.feedback.orchestrator.serialization;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.authorization.ApiUser;
import ch.uzh.ifi.feedback.library.rest.serialization.DefaultSerializer;

@Singleton
public class ApiUserSerializationService extends DefaultSerializer<ApiUser>{
}
