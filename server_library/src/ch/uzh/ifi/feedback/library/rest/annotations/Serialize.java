package ch.uzh.ifi.feedback.library.rest.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ch.uzh.ifi.feedback.library.rest.serialization.ISerializationService;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Serialize {
	public Class<? extends ISerializationService<?>> value();
}
