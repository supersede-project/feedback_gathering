package ch.uzh.ifi.feedback.library.rest.authorization;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.authorization.UserToken;

@Singleton
public class AuthenticationCache {
	
	private Map<String, UserToken> userTokens;
	
	public AuthenticationCache()
	{
		this.userTokens = new HashMap<>();
	}
	
	public UserToken Register(String username)
	{
		UserToken token = new UserToken(UUID.randomUUID());
		userTokens.put(username, token);
		return token;
	}
	
	public boolean Authenticate(UserToken token)
	{
		return userTokens.entrySet().stream().anyMatch(t -> t.getValue().equals(token));
	}
}
