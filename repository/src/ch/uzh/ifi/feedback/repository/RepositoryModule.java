package ch.uzh.ifi.feedback.repository;

import com.google.inject.AbstractModule;

import ch.uzh.ifi.feedback.library.rest.IRestManager;
import ch.uzh.ifi.feedback.library.rest.RestManager;
import ch.uzh.ifi.feedback.library.rest.authorization.IApiUserService;
import ch.uzh.ifi.feedback.repository.service.ApiUserService;

class RepositoryModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IRestManager.class).to(RestManager.class);
		bind(IApiUserService.class).to(ApiUserService.class);
	}

}
