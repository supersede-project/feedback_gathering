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

		//Check that only one mechanism of a specific type can be active at a time
		if(object.isActive())
		{
			List<FeedbackMechanism> mechanisms = mechanismService.GetWhere(
					asList(object.getConfigurationsid(), object.getType(), object.isActive()), 
					"configurations_id = ?", "`name` = ?", "active = ?");
			
			if(mechanisms.size() == 1)
			{
				FeedbackMechanism other = mechanisms.get(0);
				if(object.getId() == null || !other.getId().equals(object.getId()))
				{
					result.setHasErrors(true);
					ValidationError error = new ValidationError("isActive", object.getType(), "unique: there can only be one mechanism active for a specific mechanism type in a configuration");
					result.GetValidationErrors().add(error);
				}
			}
		}
		
		for(FeedbackParameter param : object.getParameters())
		{
			ValidationResult childResult = parameterValidator.Validate(param);
			if(childResult.hasErrors())
			{
				result.setHasErrors(true);
				result.GetValidationErrors().addAll(childResult.GetValidationErrors());
			}
		}
		
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
