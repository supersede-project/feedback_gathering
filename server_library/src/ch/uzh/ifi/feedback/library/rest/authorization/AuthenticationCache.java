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
	
	public UserToken Register(ApiUser user)
	{
		if(userTokens.containsKey(user.getId()))
			return userTokens.get(user.getId());
		
		UserToken token = new UserToken(UUID.randomUUID());
		userTokens.put(user.getId(), token);
		userMap.put(user.getId(), user);
		
		return token;
	}
	
	public boolean Authenticate(UserToken token, UserRole role)
	{
		Optional<Entry<Integer, UserToken>> optionalEntry = userTokens.entrySet().stream().filter(t -> t.getValue().equals(token)).findFirst();
		if(!optionalEntry.isPresent())
			return false;
		
		Integer userId = optionalEntry.get().getKey();
		ApiUser user = userMap.get(userId);
		if(role.equals(UserRole.ADMIN) && !user.getRole().equals(UserRole.ADMIN))
			return false;
		
		Integer application = applicationProvider.get();
		if(application != null)
		{
			return user.getPermissions().stream().anyMatch(u -> u.getApplicationId().equals(application));
		}
		
		return true;
	}
}
