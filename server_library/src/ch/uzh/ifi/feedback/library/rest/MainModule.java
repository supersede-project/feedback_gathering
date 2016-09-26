package ch.uzh.ifi.feedback.library.rest;

import com.google.inject.AbstractModule;

import ch.uzh.ifi.feedback.library.rest.Service.DatabaseConfiguration;

public class MainModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(IRequestContext.class).to(ServletRequestContext.class);
	}
}
