package ch.uzh.ifi.feedback.library.rest.validation;

public class ValidationError {

	private String fieldName;
	private Object value;
	
	public ValidationError(String fieldName, Object reason)
	{
		this.fieldName = fieldName;
		this.value = reason;
	}
}
