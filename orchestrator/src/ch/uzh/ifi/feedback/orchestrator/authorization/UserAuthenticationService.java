package ch.uzh.ifi.feedback.orchestrator.authorization;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.authorization.AuthenticationCache;
import ch.uzh.ifi.feedback.library.rest.authorization.AuthorizationException;
import ch.uzh.ifi.feedback.library.rest.authorization.ITokenAuthenticationService;
import ch.uzh.ifi.feedback.library.rest.authorization.UserToken;
import ch.uzh.ifi.feedback.orchestrator.model.ApiUser;
import ch.uzh.ifi.feedback.orchestrator.services.ApiUserService;

import static java.util.Arrays.asList;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

public class UserAuthenticationService implements ITokenAuthenticationService {

	private ApiUserService userService;
	private AuthenticationCache cache;
	
	@Inject
	public UserAuthenticationService(ApiUserService userService, AuthenticationCache cache)
	{
		this.userService = userService;
		this.cache = cache;
	}
	
	public UserToken Authenticate(ApiUser user) throws SQLException, AuthorizationException
	{
		List<ApiUser> validatedUsers = userService.GetWhere(asList(user.getName(), user.getPassword()), "name = ?", "password = ?");
		if(validatedUsers.size() != 1)
			throw new AuthorizationException("The user and the provided password dont match!");
		
		return cache.Register(validatedUsers.get(0).getName());
	}
	
	public boolean Authenticate(HttpServletRequest request)
	{
		String authorizationHeader = request.getHeader("Authorization");
		UserToken token = new UserToken(UUID.fromString(authorizationHeader));
		return cache.Authenticate(token);
	}
}
