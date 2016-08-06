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
import ch.uzh.ifi.feedback.orchestrator.model.Application;
import ch.uzh.ifi.feedback.orchestrator.model.Configuration;
import ch.uzh.ifi.feedback.orchestrator.model.GeneralConfiguration;
import javassist.NotFoundException;

public class ApplicationService implements IDbService<Application> {

	private ApplicationResultParser resultParser;
	private ConfigurationService configurationService;
	private GeneralConfigurationService generalConfigurationService;
	
	@Inject
	public ApplicationService(
			ApplicationResultParser resultParser, 
			ConfigurationService configurationService,
			GeneralConfigurationService generalConfigurationService) {
		this.resultParser = resultParser;
		this.configurationService = configurationService;
		this.generalConfigurationService = generalConfigurationService;
	}
	
	@Override
	public Application GetById(Connection con, int id) throws SQLException, NotFoundException {
		
		PreparedStatement s = con.prepareStatement(
				  "SELECT a.id, a.name, a.state, a.created_at as createdAt "
				+ "FROM feedback_orchestrator.applications as a "
				+ "WHERE a.id = ? ;");
		
		s.setInt(1, id);
		ResultSet result = s.executeQuery();
		
		if(!result.next())
			throw new NotFoundException("Application with id " + id + "does not exist");
		
		Application app = new Application();
		resultParser.SetFields(app, result);
		app.getConfigurations().addAll(configurationService.GetAllFor(con, "application_id", id));
		app.getGeneralConfigurations().addAll(generalConfigurationService.GetAllFor(con, "application_id", id));
		
		return app;
	}

	@Override
	public List<Application> GetAll(Connection con) throws SQLException, NotFoundException {
		
		PreparedStatement s = con.prepareStatement(
				  "SELECT a.id, a.name, a.state, a.created_at as createdAt "
				+ "FROM feedback_orchestrator.applications as a ;");
		
		ResultSet result = s.executeQuery();
		List<Application> apps = new ArrayList<>();
		
		while(result.next())
		{
			Application app = new Application();
			resultParser.SetFields(app, result);
			app.getConfigurations().addAll(configurationService.GetAllFor(con, "application_id", result.getInt("id")));
			app.getGeneralConfigurations().addAll(generalConfigurationService.GetAllFor(con, "application_id", result.getInt("id")));
			apps.add(app);
		}
		
		return apps;
	}
	
	@Override
	public void Insert(Connection con, Application app) throws SQLException, NotFoundException, UnsupportedOperationException 
	{
		PreparedStatement s = con.prepareStatement(
				  "INSERT INTO TABLE feedback_orchestrator.applications "
				+ "(`name`, `state`) VALUES (?, ?) ;", Statement.RETURN_GENERATED_KEYS);
		
		s.setString(1, app.getName());
		s.setObject(2, app.getState());
		s.execute();
		
		ResultSet result = s.getGeneratedKeys();
		result.next();
		int key = result.getInt(1);
		
		for(Configuration config : app.getConfigurations())
		{
			configurationService.InsertFor(con, config, "application_id", key);
		}
		
		for(GeneralConfiguration config : app.getGeneralConfigurations())
		{
			generalConfigurationService.InsertFor(con, config, "application_id", key);
		}
	}
	
	@Override
	public void Update(Connection con, Application app) throws SQLException, NotFoundException, UnsupportedOperationException 
	{
		PreparedStatement s = con.prepareStatement(
				  "UPDATE TABLE feedback_orchestrator.applications as a"
				+ "SET `name` = IFNULL(?, `name`), `state` = IFNULL(?, `state`) "
				+ "WHERE a.id = ? ;");
		
		s.setString(1, app.getName());
		s.setObject(2, app.getState());
		s.setInt(3, app.getId());
		s.execute();
		
		for(Configuration config : app.getConfigurations())
		{
			configurationService.UpdateFor(con, config, "application_id", app.getId());
		}
		
		for(GeneralConfiguration config : app.getGeneralConfigurations())
		{
			generalConfigurationService.UpdateFor(con, config, "application_id", app.getId());
		}
	}
}
