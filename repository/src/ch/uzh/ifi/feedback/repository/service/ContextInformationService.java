package ch.uzh.ifi.feedback.repository.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.service.ServiceBase;
import ch.uzh.ifi.feedback.library.transaction.DatabaseConfiguration;
import ch.uzh.ifi.feedback.library.transaction.IDatabaseConfiguration;
import ch.uzh.ifi.feedback.repository.model.ContextInformation;

@Singleton
public class ContextInformationService extends ServiceBase<ContextInformation>{

	@Inject
	public ContextInformationService(ContextInformationResultParser resultParser, IDatabaseConfiguration config) 
	{
		super(resultParser, ContextInformation.class, "context_informations", config.getDatabase());
	}
}
