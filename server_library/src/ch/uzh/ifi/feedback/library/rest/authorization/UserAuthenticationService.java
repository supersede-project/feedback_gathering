package ch.uzh.ifi.feedback.library.rest.authorization;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.authorization.AuthenticationCache;
import ch.uzh.ifi.feedback.library.rest.authorization.AuthorizationException;
import ch.uzh.ifi.feedback.library.rest.authorization.ITokenAuthenticationService;
import ch.uzh.ifi.feedback.library.rest.authorization.UserToken;

import static java.util.Arrays.asList;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

@Singleton
public class UserAuthenticationService implements ITokenAuthenticationService {

	private IApiUserService userService;
	private AuthenticationCache cache;
	
	@Inject
	public UserAuthenticationService(IApiUserService userService, AuthenticationCache cache)
	{
		this.userService = userService;
		this.cache = cache;
	}
	
	public UserToken Authenticate(ApiUser user) throws SQLException, AuthorizationException
	{
		List<ApiUser> validatedUsers = userService.GetWhere(asList(user.getName(), user.getPassword()), "`name` = ?", "`password` = ?");
		if(validatedUsers.size() != 1)
			throw new AuthorizationException("The user and the provided password dont match!");
		
		return cache.Register(validatedUsers.get(0));
	}
	
	public boolean Authenticate(HttpServletRequest request, UserRole role)
	{
		String authorizationHeader = request.getHeader("Authorization");
		
		if(authorizationHeader == null)
			return false;
		
		UserToken token = new UserToken(UUID.fromString(authorizationHeader));
		return cache.Authenticate(token, role);
	}
}
