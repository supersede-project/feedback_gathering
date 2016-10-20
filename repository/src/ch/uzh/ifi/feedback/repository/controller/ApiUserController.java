package ch.uzh.ifi.feedback.repository.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.RestController;
import ch.uzh.ifi.feedback.library.rest.annotations.Authenticate;
import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.DELETE;
import ch.uzh.ifi.feedback.library.rest.annotations.GET;
import ch.uzh.ifi.feedback.library.rest.annotations.POST;
import ch.uzh.ifi.feedback.library.rest.annotations.PUT;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import ch.uzh.ifi.feedback.library.rest.authorization.ApiUser;
import ch.uzh.ifi.feedback.library.rest.authorization.ApiUserValidator;
import ch.uzh.ifi.feedback.library.rest.authorization.IApiUserService;
import ch.uzh.ifi.feedback.library.rest.authorization.UserAuthenticationService;

@Controller(ApiUser.class)
public class ApiUserController extends RestController<ApiUser> {

	@Inject
	public ApiUserController(
			IApiUserService dbService, 
			ApiUserValidator validator, 
			HttpServletRequest request,
			HttpServletResponse response) 
	{
		super(dbService, validator, request, response);
	}
	
	@GET
	@Path("/{lang}/api_users")
	public List<ApiUser> GetAll() throws Exception
	{
		return super.GetAll();
	}
	
	@GET
	@Path("/{lang}/api_users/{id}")
	public ApiUser GetById(@PathParam("id") Integer id) throws Exception
	{
		return super.GetById(id);
	}

	@POST
	@Path("/{lang}/api_users")
	public ApiUser Insert(ApiUser user) throws Exception
	{
		return super.Insert(user);
	}
	
	@PUT
	@Path("/{lang}/api_users")
	@Authenticate(service = UserAuthenticationService.class, role = "ADMIN")
	public ApiUser Update(ApiUser user) throws Exception
	{
		return super.Insert(user);
	}
	
	@DELETE
	@Path("/{lang}/api_users/{id}")
	@Authenticate(service = UserAuthenticationService.class, role = "ADMIN")
	public void Delete(@PathParam("id") Integer id) throws Exception
	{
		super.Delete(id);
	}
}
