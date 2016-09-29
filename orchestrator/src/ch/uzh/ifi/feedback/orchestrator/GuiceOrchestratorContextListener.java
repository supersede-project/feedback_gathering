package ch.uzh.ifi.feedback.orchestrator;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

public class GuiceOrchestratorContextListener extends GuiceServletContextListener {

	  @Override
	  protected Injector getInjector() {
	    return Guice.createInjector(new OrchestratorModule(), new ServletModule() {

	        @Override
	        protected void configureServlets() {
	          serve("/*").with(OrchestratorServlet.class);
	        }
	        
	      });
	  }
	}
