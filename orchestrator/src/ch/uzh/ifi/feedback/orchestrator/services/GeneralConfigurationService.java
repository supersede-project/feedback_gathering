package ch.uzh.ifi.feedback.orchestrator.services;

import static java.util.Arrays.asList;

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

public class GeneralConfigurationService extends OrchestratorService<GeneralConfiguration> {

	private ParameterService parameterService;
	
	@Inject
	public GeneralConfigurationService(ParameterService parameterService, GeneralConfigurationResultParser resultParser) 
	{
		super(
				resultParser, 
				GeneralConfiguration.class, 
				"general_configurations",
				"feedback_orchestrator", 
				parameterService);
		
		this.parameterService = parameterService;
	}
	
	@Override
	public GeneralConfiguration GetById(int id) throws SQLException, NotFoundException {

    	GeneralConfiguration config = super.GetById(id);
    	config.setParameters(parameterService.GetWhere(asList(id), "general_configurations_id = ?"));
		
		return config;
	}

	@Override
	public List<GeneralConfiguration> GetAll() throws SQLException, NotFoundException {

		List<GeneralConfiguration> configs = super.GetAll();
		for(GeneralConfiguration config : configs)
		{
	    	config.setParameters(parameterService.GetWhere(asList(config.getId()), "general_configurations_id = ?"));
		}
		
	    return configs;
	}

	@Override
	public void Update(Connection con, GeneralConfiguration config)
			throws SQLException, NotFoundException {
		/*
	    PreparedStatement s = con.prepareStatement(
	    		  "UPDATE feedback_orchestrator.general_configurations as c "
	    		+ "SET c.updated_at = now() "
	    		+ "WHERE c.id = ? ;");
	    
	    s.setInt(1, config.getId());
	    s.execute();
	   */
	   super.Update(con, config);
	   for(FeedbackParameter param : config.getParameters())
	   {
		   param.setGenaralConfigurationId(config.getId());
		   if(param.getId() == null){
			   parameterService.Insert(con, param);
			   //parameterService.InsertFor(con, param, "configuration_id", config.getId());
		   }else{
			   
			  // parameterService.UpdateFor(con, param, "configuration_id", config.getId());
			   parameterService.Update(con, param);
		   }
	   }
	}

	@Override
	public int Insert(Connection con, GeneralConfiguration config)
			throws SQLException, NotFoundException, UnsupportedOperationException {
		
		/*
	    PreparedStatement s = con.prepareStatement(
	    		"INSERT INTO feedback_orchestrator.general_configurations "
	    		+ "(name) "
	    		+ "VALUES (?) ;", PreparedStatement.RETURN_GENERATED_KEYS);
	    
	    s.setString(1, config.getName());
	    s.execute();
	    ResultSet keys = s.getGeneratedKeys();
	    keys.next();
	    int key = keys.getInt(1);
	    */
		int id = super.Insert(con, config);
	    for(FeedbackParameter param : config.getParameters())
	    {
	    	//parameterService.InsertFor(con, param, "configuration_id", key);
	    	param.setGenaralConfigurationId(id);
	    	parameterService.Insert(con, param);
	    }
	    
	    return id;
	}
}
