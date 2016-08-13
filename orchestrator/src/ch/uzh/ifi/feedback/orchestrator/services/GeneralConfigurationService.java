package ch.uzh.ifi.feedback.orchestrator.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.Service.IDbService;
import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.orchestrator.model.Configuration;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackParameter;
import ch.uzh.ifi.feedback.orchestrator.model.GeneralConfiguration;
import javassist.NotFoundException;

public class GeneralConfigurationService extends ServiceBase<GeneralConfiguration> {

	private ParameterService parameterService;
	
	@Inject
	public GeneralConfigurationService(ParameterService parameterService, GeneralConfigurationResultParser resultParser) 
	{
		super(resultParser, GeneralConfiguration.class, "general_configurations", parameterService);
		this.parameterService = parameterService;
	}
	
	@Override
	public GeneralConfiguration GetById(Connection con, int id) throws SQLException, NotFoundException {

    	GeneralConfiguration config = super.GetById(con, id);
    	config.setParameters(parameterService.GetAllFor(con, "configuration_id", config.getId()));
		
		return config;
	}

	@Override
	public List<GeneralConfiguration> GetAll(Connection con) throws SQLException, NotFoundException {

		List<GeneralConfiguration> configs = super.GetAll(con);
		for(GeneralConfiguration config : configs)
		{
	    	config.setParameters(parameterService.GetAllFor(con, "configuration_id", config.getId()));
		}
		
	    return configs;
	}

	/*
	@Override
	public List<GeneralConfiguration> GetAllFor(Connection con, String foreignKeyName, int foreignKey)
			throws SQLException, NotFoundException {
		
		switch(foreignKeyName)
		{
			case "application_id":
				return GetMechanismsFor(con, "applications", "application_id", foreignKey);
			case "configuration_id":
				return GetMechanismsFor(con, "configurations", "configuration_id", foreignKey);
			default:
				throw new NotFoundException("foreign key " + foreignKeyName + " does not exist on table general_configurations");
		}
	}
	
	private List<GeneralConfiguration> GetMechanismsFor(Connection con, String foreignTableName, String foreignKeyName, int foreignKey)
			throws SQLException, NotFoundException {

		List<GeneralConfiguration> configs = super.GetAllFor(con, foreignTableName, foreignKeyName, foreignKey);
		for(GeneralConfiguration config : configs)
		{
	    	config.setParameters(parameterService.GetAllFor(con, "configuration_id", config.getId()));
		}
	    
	    return configs;
	}*/
	
	@Override
	public void Update(Connection con, GeneralConfiguration config)
			throws SQLException, NotFoundException {
		
		super.CheckId(con, config.getId());
		
	    PreparedStatement s = con.prepareStatement(
	    		  "UPDATE feedback_orchestrator.general_configurations as c "
	    		+ "SET c.updated_at = now() "
	    		+ "WHERE c.id = ? ;");
	    
	    s.setInt(1, config.getId());
	    s.execute();
	   
	   for(FeedbackParameter param : config.getParameters())
	   {
		   if(param.getId() == null){
			   parameterService.InsertFor(con, param, "configuration_id", config.getId());
		   }else{
			   parameterService.UpdateFor(con, param, "configuration_id", config.getId());
		   }
	   }
	}

	@Override
	public int Insert(Connection con, GeneralConfiguration config)
			throws SQLException, NotFoundException, UnsupportedOperationException {
		
	    PreparedStatement s = con.prepareStatement(
	    		"INSERT INTO feedback_orchestrator.general_configurations "
	    		+ "(name) "
	    		+ "VALUES (?) ;", PreparedStatement.RETURN_GENERATED_KEYS);
	    
	    s.setString(1, config.getName());
	    s.execute();
	    ResultSet keys = s.getGeneratedKeys();
	    keys.next();
	    int key = keys.getInt(1);
	    
	    for(FeedbackParameter param : config.getParameters())
	    {
	    	parameterService.InsertFor(con, param, "configuration_id", key);
	    }
	    
	    return key;
	}
	
	/*
	@Override
	public void InsertFor(Connection con, GeneralConfiguration config, String foreignKeyName, int foreignKey)
			throws SQLException, NotFoundException {
		
		switch(foreignKeyName)
		{
			case "application_id":
				Insert(con, config, foreignKey, null);
				break;
			case "configuration_id":
				Insert(con, config, null, foreignKey);
				break;
			default:
				throw new NotFoundException("foreign key " + foreignKeyName + " does not exist on table general_configurations");
		}
	}
	
	private void Insert(Connection con, GeneralConfiguration config, Integer applicationId, Integer configurationId)
			throws SQLException, NotFoundException{
		
	    PreparedStatement s = con.prepareStatement(
	    		"INSERT INTO feedback_orchestrator.general_configurations "
	    		+ "(application_id, configuration_id) "
	    		+ "VALUES (?, ?) ;", PreparedStatement.RETURN_GENERATED_KEYS);
	    
	    s.setInt(1, applicationId);
	    s.setInt(2, configurationId);
	    s.execute();
	    ResultSet keys = s.getGeneratedKeys();
	    keys.next();
	    int key = keys.getInt(1);
	    
	    for(FeedbackParameter param : config.getParameters())
	    {
	    	parameterService.InsertFor(con, param, "general_configuration", key);
	    }
	}
	*/
}
