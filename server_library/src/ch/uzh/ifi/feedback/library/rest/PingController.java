package ch.uzh.ifi.feedback.library.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;

import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;

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
