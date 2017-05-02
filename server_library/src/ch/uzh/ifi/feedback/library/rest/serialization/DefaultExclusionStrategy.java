package ch.uzh.ifi.feedback.library.rest.serialization;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import ch.uzh.ifi.feedback.library.rest.authorization.ApiUser;

/**
 * Represents the default exclusion strategy for the Gson serializer. It configures fields or classes
 * that should not be serialized. Each Gson instance should be initialized with an instance of this class.
 *
 * @author Florian Schüpfer
 * @version 1.0
 * @since   2016-11-14
 */
public class DefaultExclusionStrategy implements ExclusionStrategy {

    public boolean shouldSkipClass(Class<?> arg0) {
        return false;
    }

    public boolean shouldSkipField(FieldAttributes f) {

        return (f.getDeclaringClass() == ApiUser.class && f.getName().equals("password"));
    }
}
