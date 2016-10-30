package ch.uzh.ifi.feedback.orchestrator.validation;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.orchestrator.model.MonitorTool;
import ch.uzh.ifi.feedback.orchestrator.services.MonitorToolService;

@Singleton
public class MonitorToolValidator extends ValidatorBase<MonitorTool>{
	
	@Inject
	public MonitorToolValidator(MonitorToolService service, ValidationSerializer serializer) {
		super(MonitorTool.class, service, serializer);
	}

}
