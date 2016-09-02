package ch.uzh.ifi.feedback.orchestrator.controllers;

import java.util.List;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.RestController;
import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.GET;
import ch.uzh.ifi.feedback.library.rest.annotations.POST;
import ch.uzh.ifi.feedback.library.rest.annotations.PUT;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import ch.uzh.ifi.feedback.orchestrator.model.Configuration;
import ch.uzh.ifi.feedback.orchestrator.serialization.ConfigurationSerializationService;
import ch.uzh.ifi.feedback.orchestrator.services.ConfigurationService;

@Controller(Configuration.class)
public class ConfigurationController extends RestController<Configuration>{

	@Inject
	public ConfigurationController(ConfigurationSerializationService serializationService,
			ConfigurationService dbService) {
		super(serializationService, dbService);
	}
	
	@GET
	@Path("/configurations/{config_id}")
	public Configuration GetById( @PathParam("config_id") Integer id) throws Exception 
	{
		return super.GetById(id);
	}
	
	@GET
	@Path("/configurations")
	public List<Configuration> GetAll() throws Exception 
	{
		return super.GetAll();
	}
	
	@GET
	@Path("/applications/{app_id}/configurations")
	public List<Configuration> GetAllByApplication( @PathParam("app_id")Integer appId) throws Exception 
	{
		return super.GetAllFor("applications_id", appId);
	}
	
	@PUT
	@Path("/configurations")
	public Configuration UpdateConfiguration(Configuration config) throws Exception 
	{
		super.Update(config);
		return config;
	}
	
	@POST
	@Path("/applications/{app_id}/configurations")
	public Configuration InsertConfigurationForApplication(@PathParam("app_id")Integer appId, Configuration config) throws Exception 
	{
		config.setApplicationId(appId);
		super.Insert(config);
		return config;
	}
}
