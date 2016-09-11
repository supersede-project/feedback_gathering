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
import ch.uzh.ifi.feedback.orchestrator.model.GeneralConfiguration;
import ch.uzh.ifi.feedback.orchestrator.serialization.GeneralConfigurationSerializationService;
import ch.uzh.ifi.feedback.orchestrator.services.GeneralConfigurationService;

@Controller(GeneralConfiguration.class)
public class GeneralConfigurationController extends RestController<GeneralConfiguration> {

	@Inject
	public GeneralConfigurationController(GeneralConfigurationService dbService) {
		super(dbService);
	}
	
	@GET
	@Path("/general_configurations/{config_id}")
	public GeneralConfiguration GetById( @PathParam("config_id") Integer id) throws Exception 
	{
		return super.GetById(id);
	}
	
	@GET
	@Path("/general_configurations")
	public List<GeneralConfiguration> GetAll() throws Exception 
	{
		return super.GetAll();
	}
	
	@GET
	@Path("/applications/{app_id}/general_configurations")
	public List<GeneralConfiguration> GetAllByApplication( @PathParam("app_id")Integer appId) throws Exception 
	{
		return super.GetAllFor("applications_id", appId);
	}
	
	@GET
	@Path("/configurations/{config_id}/general_configurations")
	public List<GeneralConfiguration> GetAllByConfiguration( @PathParam("config_id")Integer configId) throws Exception 
	{
		return super.GetAllFor("configurations_id", configId);
	}
	
	@PUT
	@Path("/general_configurations")
	public GeneralConfiguration UpdateGeneralConfiguration(GeneralConfiguration config) throws Exception 
	{
		super.Update(config);
		return config;
	}
	
	/*
	@POST
	@Path("/applications/{app_id}/general_configurations")
	public GeneralConfiguration InsertGeneralConfigurationForApplication(@PathParam("app_id")Integer appId, GeneralConfiguration config) throws Exception 
	{
		super.InsertFor(config, "applications_id", appId);
		return config;
	}
	
	@POST
	@Path("/configurations/{config_id}/general_configurations")
	public GeneralConfiguration InsertGeneralConfigurationForConfiguration(@PathParam("app_id")Integer configId, GeneralConfiguration config) throws Exception 
	{
		super.InsertFor(config, "configurations_id", configId);
		return config;
	}*/
}
