package ch.uzh.ifi.feedback.library.rest.authorization;

import java.util.UUID;

public class UserToken {

	private UUID token;

	public UserToken(UUID id)
	{
		this.token = id;
	}
	
	public UUID getToken() {
		return token;
	}

	public void setToken(UUID token) {
		this.token = token;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(!(obj instanceof UserToken))
			return false;
		
		UserToken other = (UserToken)obj;
		if(!other.getToken().equals(this.token))
			return false;
		
		return true;
	}
}
