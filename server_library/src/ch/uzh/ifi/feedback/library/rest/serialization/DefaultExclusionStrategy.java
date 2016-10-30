package ch.uzh.ifi.feedback.library.rest.serialization;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import ch.uzh.ifi.feedback.library.rest.authorization.ApiUser;

public class DefaultExclusionStrategy implements ExclusionStrategy {

    public boolean shouldSkipClass(Class<?> arg0) {
        return false;
    }

    public boolean shouldSkipField(FieldAttributes f) {

        return (f.getDeclaringClass() == ApiUser.class && f.getName().equals("password"));
    }
}
