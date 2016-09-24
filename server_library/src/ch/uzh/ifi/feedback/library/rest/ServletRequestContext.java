package ch.uzh.ifi.feedback.library.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Singleton;

@Singleton
public class ServletRequestContext implements IRequestContext {

	private HttpServletRequest request;
	private HttpServletResponse response;
	private String requestLanguage;
	
	@Override
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	@Override
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	@Override
	public String getRequestLanguage() {
		return requestLanguage;
	}
	public void setRequestLanguage(String requestLanguage) {
		this.requestLanguage = requestLanguage;
	}
	
}
