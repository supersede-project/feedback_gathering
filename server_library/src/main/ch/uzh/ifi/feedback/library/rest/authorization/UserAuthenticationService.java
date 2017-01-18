package ch.uzh.ifi.feedback.library.rest.authorization;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.authorization.AuthenticationCache;
import ch.uzh.ifi.feedback.library.rest.authorization.AuthorizationException;
import ch.uzh.ifi.feedback.library.rest.authorization.ITokenAuthenticationService;
import ch.uzh.ifi.feedback.library.rest.authorization.PasswordStorage.CannotPerformOperationException;
import ch.uzh.ifi.feedback.library.rest.authorization.PasswordStorage.InvalidHashException;
import ch.uzh.ifi.feedback.library.rest.authorization.UserToken;

import static java.util.Arrays.asList;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

/**
 * This class is responsible for the authentication of API users and requests coming from them.
 * 
 * @author Florian Schüpfer
 * @version 1.0
 * @since   2016-11-14
 */
@Singleton
public class UserAuthenticationService implements ITokenAuthenticationService {

	private ApiUserService userService;
	private AuthenticationCache cache;
	
	@Inject
	public UserAuthenticationService(ApiUserService userService, AuthenticationCache cache)
	{
		this.userService = userService;
		this.cache = cache;
	}
	
	/**
	 * Authenticates an APIUser against the database by its username and password.
	 * 
	 * @param user the user to authenticate
	 * @return A user token that identifies the user in future requests
	 * @throws SQLException
	 * @throws AuthorizationException
	 */
	public UserToken Authenticate(ApiUser user) throws SQLException, AuthorizationException
	{
		List<ApiUser> users = userService.GetWhere(asList(user.getName()), "`name` = ?");
		if(users.size() != 1)
			throw new AuthorizationException("The and the provided password dont match!");
		
		ApiUser apiUser = users.get(0);
		try {
			if(!PasswordStorage.verifyPassword(user.getPassword(), apiUser.getPassword()))
				throw new AuthorizationException("The user and the provided password dont match!");
			
		} catch (CannotPerformOperationException | InvalidHashException e) {
			throw new AuthorizationException(e.getMessage());
		}
		
		return cache.Register(apiUser);
	}
	
	/**
	 * Authenticates an HttpServletRequest with its UserToken sent in the 'Authorization' header.
	 * 
	 * @param request the request to authenticate
	 * @param auth the authentication annotation attribute describing the authentication logic to use.
	 * @return true if the token is valid of false when its not valid
	 */
	public boolean Authenticate(HttpServletRequest request, ch.uzh.ifi.feedback.library.rest.annotations.Authenticate auth)
	{
		String authorizationHeader = request.getHeader("Authorization");
		
		if(authorizationHeader == null)
			return false;
		
		UserToken token = new UserToken(UUID.fromString(authorizationHeader));
		return cache.Authenticate(token, auth);
	}
}
