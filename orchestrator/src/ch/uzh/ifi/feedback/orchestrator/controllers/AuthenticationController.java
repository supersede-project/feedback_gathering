package ch.uzh.ifi.feedback.orchestrator.controllers;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;

import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.POST;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.authorization.UserToken;
import ch.uzh.ifi.feedback.orchestrator.authorization.UserAuthenticationService;
import ch.uzh.ifi.feedback.orchestrator.model.ApiUser;

@RequestScoped
@Controller(ApiUser.class)
public class AuthenticationController {
	
	private UserAuthenticationService authenticationService;
	
	@Inject
	public AuthenticationController(UserAuthenticationService authenticationService)
	{
		this.authenticationService = authenticationService;
	}
	
	@Path("/authenticate")
	@POST
	public UserToken Authenticate(ApiUser user) throws Exception
	{
		return authenticationService.Authenticate(user);
	}
}
