package ch.uzh.ifi.feedback.orchestrator.validation;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.validation.ValidationResult;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackMechanism;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackParameter;
import ch.uzh.ifi.feedback.orchestrator.services.MechanismService;
import javassist.NotFoundException;
import static java.util.Arrays.asList;

@Singleton
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
