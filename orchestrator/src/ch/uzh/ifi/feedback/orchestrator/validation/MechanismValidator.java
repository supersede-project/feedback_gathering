package ch.uzh.ifi.feedback.orchestrator.validation;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.validation.ValidationError;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationException;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationResult;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackMechanism;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackParameter;
import ch.uzh.ifi.feedback.orchestrator.services.MechanismService;
import javassist.NotFoundException;
import static java.util.Arrays.asList;

public class MechanismValidator extends ValidatorBase<FeedbackMechanism> {

	private ParameterValidator parameterValidator;
	private MechanismService mechanismService;
	
	@Inject
	public MechanismValidator(ParameterValidator parameterValidator, MechanismService service, ValidationSerializer serializer) {
		super(FeedbackMechanism.class, service, serializer);
		this.parameterValidator = parameterValidator;
		this.mechanismService = service;
	}
	
	@Override
	public ValidationResult Validate(FeedbackMechanism object) throws Exception {
		ValidationResult result = super.Validate(object);
		
		List<Object> childrenErrors = new ArrayList<>();
		for(FeedbackParameter param : object.getParameters())
		{
			ValidationResult childResult = parameterValidator.Validate(param);
			if(childResult.hasErrors())
			{
				result.setHasErrors(true);
				List<ValidationError> errors = childResult.GetValidationErrors();
				childrenErrors.add(errors);
			}
		}
		result.GetValidationErrors().add(new ValidationError("Parameters", childrenErrors));
		
		if(result.hasErrors())
			throw new ValidationException(serializer.Serialize(result));
			
		return result;
	}
	
	@Override
	public FeedbackMechanism Merge(FeedbackMechanism object) throws Exception
	{
		if(object.getId() == null)
			throw new UnsupportedOperationException("ID on object must be set for update");
		
		boolean res = dbService.CheckId(object.getId());
		if(!res)
			throw new NotFoundException("Object with ID '" + object.getId() + "' not found");
		
		FeedbackMechanism oldObject = dbService.GetWhere(
				asList(object.getId(), object.getConfigurationsid()), 
				"t.mechanisms_id = ?", "cm.configurations_id = ?").get(0);
		
		object = object.Merge(oldObject);
		return object;
	}

}
