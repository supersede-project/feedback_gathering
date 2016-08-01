package ch.uzh.ifi.feedback.orchestrator.controllers;

import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import ch.uzh.ifi.feedback.library.transaction.IDbService;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackParameter;

@Controller
public class ParameterController {
	
	public ParameterController() {
	}
	
	@Path("/parameters/{parameter_id}")
	public FeedbackParameter Get( @PathParam("parameter_id") Integer id) throws Exception 
	{
		FeedbackParameter param = new FeedbackParameter();
		param.setKey("id");
		param.setValue(id);
		
		return param;
	}

	/*
	@Handler(Routes = {
			"/mechanisms/{mechanism_id}/parameters", 
			"/pull_configurations/{pull_config_id}/parameters",
			"/general_configurations/{general_config_id}/parameters"
			})	
	@Override
	public void Post(HttpServletRequest request, HttpServletResponse response, FeedbackParameter param) throws Exception
	{
		param.setMechanismId((int)request.getAttribute("mechanism_id"));
		param.setPullConfigurationId((int)request.getAttribute("pull_config_id"));
		param.setGenaralConfigurationId((int)request.getAttribute("general_config_id"));
		getDbService().Create(param);
		//response.getWriter().append(serializationService.Serialize(dbService.Read()));
	}
	

	@Handler(Routes = {"/parameters/{parameter_id}"})
	@Override
	public void Put(HttpServletRequest request, HttpServletResponse response, FeedbackParameter param) throws Exception
	{
		param.setId((int)request.getAttribute("parameter_id"));
		getDbService().Update(param);
		//response.getWriter().append(serializationService.Serialize(dbService.Read()));
	}*/
	
}
