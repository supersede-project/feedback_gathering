package ch.uzh.ifi.feedback.orchestrator.validation;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.orchestrator.model.MonitorType;
import ch.uzh.ifi.feedback.orchestrator.services.MonitorTypeService;

@Singleton
public class MonitorTypeValidator extends ValidatorBase<MonitorType> {

	@Inject
	public MonitorTypeValidator(MonitorTypeService service, ValidationSerializer serializer) {
		super(MonitorType.class, service, serializer);
	}
	
}
