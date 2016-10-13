package ch.uzh.ifi.feedback.orchestrator;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.ServletModule;

public class OrchestratorServletModule extends ServletModule {
	
    @Override
    protected void configureServlets() {
    	
      filter("/*").through(createTimeStampFilter());
      serve("/*").with(OrchestratorServlet.class);
      
    }
    
    @Provides @Named("timestamp") @RequestScoped Timestamp provideTimeStamp() {
        throw new IllegalStateException("timestamp must be manually seeded");
      }
    
    @Provides @Named("language") @RequestScoped String provideLanguage() 
    {
        throw new IllegalStateException("language must be manually seeded");
    }
    
    @Provides @Named("application") @RequestScoped String provideApplication() 
    {
        return null;
    }
    
   protected Filter createTimeStampFilter() {
	     return new Filter() {
	    	 
	       @Override public void doFilter(
	          ServletRequest request,  ServletResponse response, FilterChain chain) throws IOException, ServletException 
	       {
	         HttpServletRequest httpRequest = (HttpServletRequest) request;
	         String timestampValue = httpRequest.getParameter("timestamp");
	         Timestamp timestamp = Timestamp.from(Instant.now());
	         
	         if(timestampValue != null)
	        	 timestamp = Timestamp.valueOf(timestampValue);
	         
	         httpRequest.setAttribute(
	             Key.get(Timestamp.class, Names.named("timestamp")).toString(),
	             timestamp);
	         
	         chain.doFilter(request, response);
	       }

	      @Override public void init(FilterConfig filterConfig) throws ServletException { }

	      @Override public void destroy() { }
	     };
	  } 
}
