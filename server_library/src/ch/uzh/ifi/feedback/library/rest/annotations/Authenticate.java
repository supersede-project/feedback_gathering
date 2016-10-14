package ch.uzh.ifi.feedback.library.rest.annotations;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ch.uzh.ifi.feedback.library.rest.authorization.ITokenAuthenticationService;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Authenticate {
	public Class<? extends ITokenAuthenticationService> value();
	public String role() default "USER";

}
