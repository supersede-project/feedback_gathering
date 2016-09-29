package ch.uzh.ifi.feedback.orchestrator.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import com.google.inject.Inject;
import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.orchestrator.model.Application;
import ch.uzh.ifi.feedback.orchestrator.model.Configuration;
import ch.uzh.ifi.feedback.orchestrator.model.GeneralConfiguration;
import javassist.NotFoundException;
import static java.util.Arrays.asList;

public class ApplicationService extends ServiceBase<Application>{

	private ConfigurationService configurationService;
	private GeneralConfigurationService generalConfigurationService;
	
	@Inject
	public ApplicationService(
			ApplicationResultParser resultParser, 
			ConfigurationService configurationService,
			GeneralConfigurationService generalConfigurationService)
	{
		super(  resultParser, 
				Application.class, 
				"applications", 
				"feedback_orchestrator", 
				configurationService, 
				generalConfigurationService);

		this.configurationService = configurationService;
		this.generalConfigurationService = generalConfigurationService;
	}
	
	@Override
	public Application GetById(int id) throws SQLException, NotFoundException {

		Application app = super.GetById(id);
		app.getConfigurations().addAll(configurationService.GetWhereEquals(asList("application_id"), asList(id)));
		if (app.getGeneralConfigurationId() != null)
			app.setGeneralConfiguration(generalConfigurationService.GetById(app.getGeneralConfigurationId()));
		
		return app;
	}

	@Override
	public List<Application> GetAll() throws SQLException, NotFoundException {
		
		List<Application> apps = super.GetAll();
		for(Application app : apps)
		{
			app.getConfigurations().addAll(configurationService.GetWhereEquals(asList("application_id"), asList(app.getId())));
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
			if(generalConfig.getId() != null)
			{
				generalConfigId = generalConfigurationService.Insert(con, generalConfig);
				app.setGeneralConfigurationId(generalConfigId);
			}else{
				generalConfigurationService.Update(con, generalConfig);
			}
		}
		
		PreparedStatement s = con.prepareStatement(
				  "UPDATE feedback_orchestrator.applications as a "
				+ "SET `name` = IFNULL(?, `name`), `state` = IFNULL(?, `state`), general_configuration_id = IFNULL(?, general_configuration_id) "
				+ "WHERE a.id = ? ;");
		
		s.setString(1, app.getName());
		s.setObject(2, app.getState());
		s.setObject(3, generalConfigId);
		s.setInt(4, app.getId());
		s.execute();
		
		for(Configuration config : app.getConfigurations())
		{
			config.setApplicationId(app.getId());
			configurationService.Update(con, config);
		}
	}
}
