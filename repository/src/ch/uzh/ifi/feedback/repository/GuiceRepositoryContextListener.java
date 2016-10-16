package ch.uzh.ifi.feedback.repository;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.ServletModule;

public class GuiceRepositoryContextListener extends GuiceServletContextListener {

	  @Override
	  protected Injector getInjector() {
	    return Guice.createInjector(new RepositoryModule(), new ServletModule() {

	        @Override
	        protected void configureServlets() {
	          serve("/*").with(FeedbackServlet.class);
	        }
	        
	        @Provides @Named("language") @RequestScoped String provideLanguage() 
	        {
	            throw new IllegalStateException("language must be manually seeded");
	        }
	        
	        @Provides @Named("application") @RequestScoped Integer provideApplication() 
	        {
	        	return null;
	        }
	        
	      });
	  }
	}
