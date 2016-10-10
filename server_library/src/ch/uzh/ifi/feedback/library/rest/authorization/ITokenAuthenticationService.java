package ch.uzh.ifi.feedback.library.rest.authorization;

import javax.servlet.http.HttpServletRequest;

public interface ITokenAuthenticationService {
	boolean Authenticate(HttpServletRequest request, UserRole role);
}
