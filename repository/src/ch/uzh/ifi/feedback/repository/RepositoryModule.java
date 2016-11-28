package ch.uzh.ifi.feedback.repository;

import com.google.inject.AbstractModule;

import ch.uzh.ifi.feedback.library.mail.IMailConfiguration;
import ch.uzh.ifi.feedback.library.rest.IRestManager;
import ch.uzh.ifi.feedback.library.rest.RestManager;
import ch.uzh.ifi.feedback.library.transaction.IDatabaseConfiguration;
import ch.uzh.ifi.feedback.repository.mail.RepositoryMailConfiguration;
import ch.uzh.ifi.feedback.repository.transaction.RepositoryDatabaseConfiguration;

class RepositoryModule extends AbstractModule {

	@Override
	protected void configure() {
		
		bind(IDatabaseConfiguration.class).to(RepositoryDatabaseConfiguration.class);
		bind(IMailConfiguration.class).to(RepositoryMailConfiguration.class);
		bind(IRestManager.class).to(RestManager.class);
	}

}
