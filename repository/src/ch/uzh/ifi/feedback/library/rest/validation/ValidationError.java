package ch.uzh.ifi.feedback.library.rest.validation;

public class ValidationError {

	private String fieldName;
	private Object value;
	private String reason;
	
	public ValidationError(String fieldName, Object value, String reason)
	{
		this.fieldName = fieldName;
		this.value = value;
		this.reason = reason;
	}
}
