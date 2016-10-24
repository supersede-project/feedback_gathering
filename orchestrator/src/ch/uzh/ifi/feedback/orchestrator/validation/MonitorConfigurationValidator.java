package ch.uzh.ifi.feedback.orchestrator.validation;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.orchestrator.model.MonitorConfiguration;
import ch.uzh.ifi.feedback.orchestrator.services.MonitorConfigurationService;

@Singleton
public class MonitorConfigurationValidator extends ValidatorBase<MonitorConfiguration>{
		
	@Inject
	public MonitorConfigurationValidator(MonitorConfigurationService service, ValidationSerializer serializer) {
		super(MonitorConfiguration.class, service, serializer);
	}
	
}
