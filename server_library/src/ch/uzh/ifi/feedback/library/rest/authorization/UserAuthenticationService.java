package ch.uzh.ifi.feedback.library.rest.authorization;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.authorization.AuthenticationCache;
import ch.uzh.ifi.feedback.library.rest.authorization.AuthorizationException;
import ch.uzh.ifi.feedback.library.rest.authorization.ITokenAuthenticationService;
import ch.uzh.ifi.feedback.library.rest.authorization.PasswordStorage.CannotPerformOperationException;
import ch.uzh.ifi.feedback.library.rest.authorization.PasswordStorage.InvalidHashException;
import ch.uzh.ifi.feedback.library.rest.authorization.UserToken;
import javassist.NotFoundException;

import static java.util.Arrays.asList;

import java.io.UnsupportedEncodingException;
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
		List<ApiUser> users = userService.GetWhere(asList(user.getName()), "`name` = ?");
		if(users.size() != 1)
			throw new AuthorizationException("The user or the provided password dont match!");
		
		ApiUser apiUser = users.get(0);
		try {
			if(!PasswordStorage.verifyPassword(user.getPassword(), apiUser.getPassword()))
				throw new AuthorizationException("The user and the provided password dont match!");
			
		} catch (CannotPerformOperationException | InvalidHashException e) {
			throw new AuthorizationException(e.getMessage());
		}
		
		return cache.Register(apiUser);
	}
	
	public boolean Authenticate(HttpServletRequest request, ch.uzh.ifi.feedback.library.rest.annotations.Authenticate auth)
	{
		String authorizationHeader = request.getHeader("Authorization");
		
		if(authorizationHeader == null)
			return false;
		
		UserToken token = new UserToken(UUID.fromString(authorizationHeader));
		return cache.Authenticate(token, auth);
	}
}
