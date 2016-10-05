package ch.uzh.ifi.feedback.library.rest.validation.test;

import ch.uzh.ifi.feedback.library.rest.service.ServiceBase;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;

public class TestItemValidator extends ValidatorBase<TestItem> {

	public TestItemValidator(ServiceBase<TestItem> service, ValidationSerializer serializer) {
		super(TestItem.class, service, serializer);
	}

}
