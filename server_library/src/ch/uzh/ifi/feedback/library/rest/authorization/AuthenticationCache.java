package ch.uzh.ifi.feedback.library.rest.authorization;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import ch.uzh.ifi.feedback.library.rest.authorization.UserToken;

/**
 * This class stores the user tokens of authenticated users and authenticates requests to resources.
 * 
 * @author Florian Schüpfer
 * @version 1.0
 * @since   2016-11-14
 */
@Singleton
public class AuthenticationCache {
	
	private Map<Integer, UserToken> userTokens;
	private Map<Integer, ApiUser> userMap;
	private Provider<Integer> applicationProvider;
	
	@Inject
	public AuthenticationCache(@Named("application")Provider<Integer> applicationProvider)
	{
		this.applicationProvider = applicationProvider;
		this.userTokens = new ConcurrentHashMap<>();
		this.userMap = new ConcurrentHashMap<>();
	}
	
	/**
	 * Generates and returns a new UserToken and stores the user in the map. If the user is already registered,
	 * the stored token is returned.
	 * 
	 * @param user the user to register
	 * @return a UserToken to identify the user in future requests
	 */
	public UserToken Register(ApiUser user)
	{
		if(userTokens.containsKey(user.getId()))
			return userTokens.get(user.getId());
		
		UserToken token = new UserToken(UUID.randomUUID());
		userTokens.put(user.getId(), token);
		userMap.put(user.getId(), user);
		
		return token;
	}
	
	/**
	 * Authenticates and authorizes a UserToken for a specific resource. It checks that the user is authenticated and that he has
	 * the necessary privileges (admin or application specific) for the operation requested.
	 * 
	 * @param token the UserToken sent in the request
	 * @param auth the authentication header that describes the accessed resource
	 * @return 'true' if the user is authorized for the request, 'false' else
	 */
	public boolean Authenticate(UserToken token, ch.uzh.ifi.feedback.library.rest.annotations.Authenticate auth)
	{
		//check if user is authenticated
		Optional<Entry<Integer, UserToken>> optionalEntry = userTokens.entrySet().stream().filter(t -> t.getValue().equals(token)).findFirst();
		if(!optionalEntry.isPresent())
			return false;
		
		//check if admin privilege needed
		Integer userId = optionalEntry.get().getKey();
		ApiUser user = userMap.get(userId);
		if(auth.role().equals(UserRole.ADMIN.toString()) && !user.getRole().equals(UserRole.ADMIN))
			return false;
		
		//check if authorization is scoped. Note that admin has permission for all use cases
		if(auth.scope().equals(AuthorizationScope.APPLICATION.toString()))
		{
			Integer application = applicationProvider.get();
			if(application != null && !user.getRole().equals(UserRole.ADMIN))
			{
				return user.getPermissions().stream().anyMatch(u -> u.getApplicationId().equals(application));
			}
		}

		return true;
	}
}
