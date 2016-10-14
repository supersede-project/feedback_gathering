package ch.uzh.ifi.feedback.library.rest.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Validate {
	public Class<? extends ValidatorBase<?>> value();
}
