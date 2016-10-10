package ch.uzh.ifi.feedback.repository.validation;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.service.IDbService;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import ch.uzh.ifi.feedback.repository.model.StatusOption;
import ch.uzh.ifi.feedback.repository.service.StatusOptionService;
import ch.uzh.ifi.feedback.repository.service.StatusService;

@Singleton
public class StatusOptionValidator extends ValidatorBase<StatusOption> {

	@Inject
	public StatusOptionValidator(
			StatusOptionService service, 
			ValidationSerializer serializer) {
		super(StatusOption.class, service, serializer);
	}

}
