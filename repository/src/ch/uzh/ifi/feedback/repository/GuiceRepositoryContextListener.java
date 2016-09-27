package ch.uzh.ifi.feedback.repository;

import javax.servlet.annotation.WebListener;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

public class GuiceRepositoryContextListener extends GuiceServletContextListener {

	  @Override
	  protected Injector getInjector() {
	    return Guice.createInjector(new RepositoryModule(), new ServletModule() {

	        @Override
	        protected void configureServlets() {
	          serve("/*").with(FeedbackServlet.class);
	        }
	        
	      });
	  }
	}
