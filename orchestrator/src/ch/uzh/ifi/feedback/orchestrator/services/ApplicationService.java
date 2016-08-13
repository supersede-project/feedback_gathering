package ch.uzh.ifi.feedback.orchestrator.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.google.inject.Inject;
import com.mysql.jdbc.Statement;
import ch.uzh.ifi.feedback.library.rest.Service.IDbService;
import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.orchestrator.model.Application;
import ch.uzh.ifi.feedback.orchestrator.model.Configuration;
import ch.uzh.ifi.feedback.orchestrator.model.GeneralConfiguration;
import javassist.NotFoundException;

public class ApplicationService extends ServiceBase<Application>{

	private ConfigurationService configurationService;
	private GeneralConfigurationService generalConfigurationService;
	
	@Inject
	public ApplicationService(
			ApplicationResultParser resultParser, 
			ConfigurationService configurationService,
			GeneralConfigurationService generalConfigurationService)
	{
		super(resultParser, Application.class, "applications", configurationService, generalConfigurationService);

		this.configurationService = configurationService;
		this.generalConfigurationService = generalConfigurationService;
	}
	
	@Override
	public Application GetById(Connection con, int id) throws SQLException, NotFoundException {

		Application app = super.GetById(con, id);
		app.getConfigurations().addAll(configurationService.GetAllFor(con, "application_id", id));
		app.setGeneralConfiguration(generalConfigurationService.GetById(con, app.getGeneralConfigurationId()));
		
		return app;
	}

	@Override
	public List<Application> GetAll(Connection con) throws SQLException, NotFoundException {
		
		List<Application> apps = super.GetAll(con);
		for(Application app : apps)
		{
			app.getConfigurations().addAll(configurationService.GetAllFor(con, "application_id", app.getId()));
			app.setGeneralConfiguration(generalConfigurationService.GetById(con, app.getGeneralConfigurationId()));
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
		
		PreparedStatement s = con.prepareStatement(
				  "INSERT INTO feedback_orchestrator.applications "
				+ "(`name`, `state`, general_configuration_id) VALUES (?, ?, ?) ;", Statement.RETURN_GENERATED_KEYS);
		
		s.setString(1, app.getName());
		s.setObject(2, app.getState());
		s.setObject(3, generalConfigId);
		s.execute();
		
		ResultSet result = s.getGeneratedKeys();
		result.next();
		int key = result.getInt(1);
		
		for(Configuration config : app.getConfigurations())
		{
			configurationService.InsertFor(con, config, "application_id", key);
		}
	
		return key;
	}
	
	@Override
	public void Update(Connection con, Application app) throws SQLException, NotFoundException, UnsupportedOperationException 
	{
		super.CheckId(con, app.getId());
		
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
				  "UPDATE TABLE feedback_orchestrator.applications as a"
				+ "SET `name` = IFNULL(?, `name`), `state` = IFNULL(?, `state`), general_configuration_id = IFNULL(?, general_configuration_id) "
				+ "WHERE a.id = ? ;");
		
		s.setString(1, app.getName());
		s.setObject(2, app.getState());
		s.setObject(3, generalConfigId);
		s.setInt(4, app.getId());
		s.execute();
		
		for(Configuration config : app.getConfigurations())
		{
			configurationService.UpdateFor(con, config, "application_id", app.getId());
		}
	}
}
