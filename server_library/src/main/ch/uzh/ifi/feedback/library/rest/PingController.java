package ch.uzh.ifi.feedback.library.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;

import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;

/**
 * This controller is responsible for answering the ping signal.
 * 
 * @author Florian Schüpfer
 * @version 1.0
 * @since   2016-11-14
 */
@RequestScoped
@Controller(void.class)
public class PingController {

	private HttpServletResponse response;

	@Inject
	public PingController(HttpServletResponse response)
	{
		this.response = response;
	}
	
	@Path("/ping")
	public void AnswerPing() throws IOException
	{
		response.getWriter().append("pong");
	}
}
