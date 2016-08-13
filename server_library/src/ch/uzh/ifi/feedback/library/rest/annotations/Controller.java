package ch.uzh.ifi.feedback.library.rest.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

import ch.uzh.ifi.feedback.library.rest.Service.IDbItem;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Controller {
	public Class<?> value();
}
