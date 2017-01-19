package ch.uzh.ifi.feedback.library.rest.validation;

@SuppressWarnings("serial")
public class ValidationException extends Exception {
	
	private String error;
	
	public ValidationException(String error)
	{
		this.error = error;
	}
	
	@Override
	public String getMessage() {
		return error;
	}

}
