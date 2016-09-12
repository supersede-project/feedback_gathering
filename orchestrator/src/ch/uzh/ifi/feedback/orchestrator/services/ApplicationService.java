package ch.uzh.ifi.feedback.orchestrator.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.Service.DatabaseConfiguration;
import ch.uzh.ifi.feedback.orchestrator.model.Application;
import ch.uzh.ifi.feedback.orchestrator.model.Configuration;
import ch.uzh.ifi.feedback.orchestrator.model.GeneralConfiguration;
import javassist.NotFoundException;
import static java.util.Arrays.asList;

public class ApplicationService extends OrchestratorService<Application>{

	private ConfigurationService configurationService;
	private GeneralConfigurationService generalConfigurationService;
	
	@Inject
	public ApplicationService(
			ApplicationResultParser resultParser, 
			ConfigurationService configurationService,
			GeneralConfigurationService generalConfigurationService,
			DatabaseConfiguration config)
	{
		super(  resultParser, 
				Application.class, 
				"applications1",
				config.getOrchestratorDb(), 
				configurationService, 
				generalConfigurationService);

		this.configurationService = configurationService;
		this.generalConfigurationService = generalConfigurationService;
	}
	
	@Override
	public Application GetById(int id) throws SQLException, NotFoundException {

		Application app = super.GetById(id);
		app.getConfigurations().addAll(configurationService.GetWhere(asList(id), "applications_id = ?"));
		
		if(app.getGeneralConfigurationId() != null)
			app.setGeneralConfiguration(generalConfigurationService.GetById(app.getGeneralConfigurationId()));
		
		return app;
	}

	@Override
	public List<Application> GetAll() throws SQLException, NotFoundException {
		
		List<Application> apps = super.GetAll();
		for(Application app : apps)
		{
			app.getConfigurations().addAll(configurationService.GetWhere(asList(app.getId()), "applications_id = ?"));
			if(app.getGeneralConfigurationId() != null)
				app.setGeneralConfiguration(generalConfigurationService.GetById(app.getGeneralConfigurationId()));
		}
		
		return apps;
	}
	
	@Override
	public int Insert(Connection con, Application app) throws SQLException, NotFoundException, UnsupportedOperationException 
	{
		GeneralConfiguration generalConfig = app.getGeneralConfiguration();
		Integer generalConfigId = null;
		if(generalConfig != null)
		{
			generalConfigId = generalConfigurationService.Insert(con, generalConfig);
			app.setGeneralConfigurationId(generalConfigId);
		}
		
		int appId = super.Insert(con, app);
		for(Configuration config : app.getConfigurations())
		{
			config.setApplicationId(appId);
			configurationService.Insert(con, config);
		}
	
		return appId;
	}
	
	@Override
	public void Update(Connection con, Application app) throws SQLException, NotFoundException, UnsupportedOperationException 
	{
		GeneralConfiguration generalConfig = app.getGeneralConfiguration();
		Integer generalConfigId = null;
		if(generalConfig != null)
		{
			if(generalConfig.getId() == null)
			{
				generalConfigId = generalConfigurationService.Insert(con, generalConfig);
				app.setGeneralConfigurationId(generalConfigId);
			}else{
				generalConfigurationService.Update(con, generalConfig);
			}
		}
		
		super.Update(con, app);
		
		for(Configuration config : app.getConfigurations())
		{
			config.setApplicationId(app.getId());
			configurationService.Update(con, config);
		}
	}
}
