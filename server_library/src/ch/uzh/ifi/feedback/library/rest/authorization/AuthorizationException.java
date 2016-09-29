package ch.uzh.ifi.feedback.library.rest.authorization;

@SuppressWarnings("serial")
public class AuthorizationException extends Exception{
	
	private String error;
	
	public AuthorizationException(String error) {
		this.error = error;
	}
	@Override
	public String getMessage() {
		return error;
	}

}
