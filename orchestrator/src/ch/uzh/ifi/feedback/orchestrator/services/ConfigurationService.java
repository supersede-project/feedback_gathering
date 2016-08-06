package ch.uzh.ifi.feedback.orchestrator.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import com.google.inject.Inject;
import com.mysql.jdbc.Statement;

import ch.uzh.ifi.feedback.library.rest.Service.IDbService;
import ch.uzh.ifi.feedback.orchestrator.model.Configuration;
import ch.uzh.ifi.feedback.orchestrator.model.ConfigurationType;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackMechanism;
import ch.uzh.ifi.feedback.orchestrator.model.GeneralConfiguration;
import javassist.NotFoundException;

public class ConfigurationService implements IDbService<Configuration>{

	private ConfigurationResultParser resultParser;
	private MechanismService mechanismService;
	private GeneralConfigurationService generalConfigurationService;
	
	@Inject
	public ConfigurationService(
			ConfigurationResultParser resultParser, 
			MechanismService mechanismService,
			GeneralConfigurationService generalConfigurationService) 
	{
		this.resultParser = resultParser;
		this.mechanismService = mechanismService;
		this.generalConfigurationService = generalConfigurationService;
	}
	
	@Override
	public Configuration GetById(Connection con, int id) throws SQLException, NotFoundException {
		
		PreparedStatement s = con.prepareStatement(
				  "SELECT c.id, c.name, c.created_at as createdAt, type "
				+ "FROM feedback_orchestrator.configurations as c "
				+ "WHERE c.id = ? ;");
		
		s.setInt(1, id);
		ResultSet result = s.executeQuery();
		
		Configuration config = new Configuration();
		resultParser.SetFields(config, result);
		config.setType(ConfigurationType.valueOf(result.getString("type")));
		config.getFeedbackMechanisms().addAll(mechanismService.GetAllFor(con, "configuration_id", id));
		config.getGeneralConfigurations().addAll(generalConfigurationService.GetAllFor(con, "configuration_id", id));
		
		return config;
	}

	@Override
	public List<Configuration> GetAll(Connection con) throws SQLException, NotFoundException {
		
		PreparedStatement s = con.prepareStatement(
				  "SELECT c.id, c.name, c.created_at as createdAt, type "
				+ "FROM feedback_orchestrator.configurations as c ;");
		
		ResultSet result = s.executeQuery();
		
		List<Configuration> configurations = new ArrayList<>();
		while(result.next())
		{
			Configuration config = new Configuration();
			resultParser.SetFields(config, result);
			config.setType(ConfigurationType.valueOf(result.getString("type")));
			config.getFeedbackMechanisms().addAll(mechanismService.GetAllFor(con, "configuration_id", config.getId()));
			config.getGeneralConfigurations().addAll(generalConfigurationService.GetAllFor(con, "configuration_id", config.getId()));
			configurations.add(config);
		}
		
		return configurations;
	}

	@Override
	public List<Configuration> GetAllFor(Connection con, String foreignKeyName, int foreignKey)
			throws SQLException, NotFoundException {
		
		switch(foreignKeyName)
		{
			case "application_id":
				return GetConfigurationsFor(con, "applications", foreignKeyName, foreignKey);
			default:
				throw new NotFoundException("The foreign key name " + foreignKeyName + " does not exist in table configurations");
		}
	}
	
	private List<Configuration> GetConfigurationsFor(Connection con, String foreignTableName, String foreignKeyName, int foreignKey)
			throws SQLException, NotFoundException {
		
		String statement = String.format(
				  "SELECT c.id, c.name, c.created_at as createdAt, type "
				+ "FROM feedback_orchestrator.configurations as c "
				+ "JOIN feedback_orchestrator.%s as f ON f.id = c.%s "
				+ "WHERE f.id = ?;", foreignTableName, foreignKeyName);
		
		PreparedStatement s = con.prepareStatement(statement);
		s.setInt(1, foreignKey);
		
		ResultSet result = s.executeQuery();
		
		List<Configuration> configurations = new ArrayList<>();
		while(result.next())
		{
			Configuration config = new Configuration();
			resultParser.SetFields(config, result);
			config.setType(ConfigurationType.valueOf(result.getString("type")));
			config.getFeedbackMechanisms().addAll(mechanismService.GetAllFor(con, "configuration_id", config.getId()));
			config.getGeneralConfigurations().addAll(generalConfigurationService.GetAllFor(con, "configuration_id", config.getId()));
			configurations.add(config);
		}
		
		return configurations;
		
	}

	@Override
	public void Update(Connection con, Configuration config) throws SQLException, NotFoundException, UnsupportedOperationException {
		
		PreparedStatement s1 = con.prepareStatement(
				  "SELECT application_id FROM feedback_orchestrator.configurations as c "
				+ "WHERE c.id = ? ;");
		
		s1.setInt(1, config.getId());
		ResultSet res = s1.executeQuery();
		int appId = res.getInt("application_id");
		
		CheckIfTypeIsUnique(con, config, appId);
				
		PreparedStatement s = con.prepareStatement(
				  "UPDATE TABLE feedback_orchestrator.configurations as c "
				+ "SET `name` = IFNULL(?, `name`), `type` = IFNULL(?, `type`) "
				+ "WHERE c.id = ? ;");
		
		s.setObject(1, config.getName());
		s.setObject(2, config.getType());
		s.setInt(3, config.getId());
		
		for(FeedbackMechanism mechanism : config.getFeedbackMechanisms())
		{
			if(mechanism.getId() != null)
			{
				mechanismService.InsertFor(con, mechanism, "configuration_id", config.getId());
			}else{
				mechanismService.UpdateFor(con, mechanism, "configuration_id", config.getId());
			}
		}
		
		for(GeneralConfiguration generalConfig : config.getGeneralConfigurations())
		{
			if(generalConfig.getId() != null)
			{
				generalConfigurationService.InsertFor(con, generalConfig, "configuration_id", config.getId());
			}else{
				generalConfigurationService.UpdateFor(con, generalConfig, "configuration_id", config.getId());
			}
		}
	}
	
	@Override
	public void InsertFor(Connection con, Configuration config, String foreignKeyName, int foreignKey)
			throws SQLException, NotFoundException, UnsupportedOperationException {
		
		CheckIfTypeIsUnique(con, config, foreignKey);
		
		PreparedStatement s = con.prepareStatement(
				  "INSERT INTO TABLE feedback_orchestrator.configurations "
				+ "(`name`, `type`) VALUES (?, ?) ;", Statement.RETURN_GENERATED_KEYS);
		
		s.setObject(1, config.getName());
		s.setObject(2, config.getType());
		s.execute();
		
		ResultSet result = s.getGeneratedKeys();
		result.next();
		int configurationId = result.getInt(1);
		
		
		for(FeedbackMechanism mechanism : config.getFeedbackMechanisms())
		{
			mechanismService.InsertFor(con, mechanism, "configuration_id", configurationId);
		}
		
		for(GeneralConfiguration generalConfig : config.getGeneralConfigurations())
		{
			generalConfigurationService.InsertFor(con, generalConfig, "configuration_id", configurationId);
		}
	}
	
	private void CheckIfTypeIsUnique(Connection con, Configuration config, int appId) throws UnsupportedOperationException, SQLException
	{
		PreparedStatement s2 = con.prepareStatement(
				  "SELECT * FROM feedback_orchestrator.configurations as c "
				+ "WHERE c.application_id = ? AND c.type = ? ;");
		
		s2.setInt(1, appId);
		s2.setObject(2, config.getType());
		ResultSet res2 = s2.executeQuery();
		
		if(res2.next())
			throw new UnsupportedOperationException();
	}

}
