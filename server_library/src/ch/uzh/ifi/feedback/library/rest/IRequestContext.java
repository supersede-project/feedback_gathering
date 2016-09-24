package ch.uzh.ifi.feedback.library.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IRequestContext {
	
	HttpServletRequest getRequest();
	HttpServletResponse getResponse();
	String getRequestLanguage();
}
