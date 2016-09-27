package ch.uzh.ifi.feedback.library.rest.authorization;

import javax.servlet.http.HttpServletRequest;

import ch.uzh.ifi.feedback.library.rest.IRequestContext;

public interface ITokenAuthenticationService {
	boolean Authenticate(HttpServletRequest request);
}
