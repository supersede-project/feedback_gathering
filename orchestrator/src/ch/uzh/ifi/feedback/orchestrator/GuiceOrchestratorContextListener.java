package ch.uzh.ifi.feedback.orchestrator;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class GuiceOrchestratorContextListener extends GuiceServletContextListener {

	  @Override
	  protected Injector getInjector() {
	    return Guice.createInjector(new OrchestratorModule(), new OrchestratorServletModule());
	  }
	}
