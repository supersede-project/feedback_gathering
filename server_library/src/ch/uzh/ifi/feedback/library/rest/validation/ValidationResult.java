package ch.uzh.ifi.feedback.library.rest.validation;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
	
	private boolean hasErrors;
	private List<ValidationError> validationErrors;
	
	public ValidationResult(){
		this.validationErrors = new ArrayList<>();
	}
	
	public List<ValidationError> GetValidationErrors()
	{
		return validationErrors;
	}
	
	public boolean hasErrors() {
		return hasErrors;
	}
	
	public void setHasErrors(boolean hasErrors) {
		this.hasErrors = hasErrors;
	}
}
