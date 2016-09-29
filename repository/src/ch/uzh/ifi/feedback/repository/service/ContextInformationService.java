package ch.uzh.ifi.feedback.repository.service;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.Service.DatabaseConfiguration;
import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.repository.model.ContextInformation;

public class ContextInformationService extends ServiceBase<ContextInformation>{

	@Inject
	public ContextInformationService(ContextInformationResultParser resultParser, DatabaseConfiguration config) 
	{
		super(resultParser, ContextInformation.class, "context_informations", config.getRepositoryDb());
	}
}
