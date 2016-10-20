package ch.uzh.ifi.feedback.orchestrator.services;

import static java.util.Arrays.asList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import ch.uzh.ifi.feedback.library.rest.service.IDbService;
import ch.uzh.ifi.feedback.library.rest.service.ServiceBase;
import ch.uzh.ifi.feedback.library.transaction.DatabaseConfiguration;
import ch.uzh.ifi.feedback.library.transaction.IDatabaseConfiguration;
import ch.uzh.ifi.feedback.orchestrator.model.Configuration;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackParameter;
import ch.uzh.ifi.feedback.orchestrator.model.GeneralConfiguration;
import ch.uzh.ifi.feedback.orchestrator.transaction.OrchestratorDatabaseConfiguration;
import javassist.NotFoundException;

@Singleton
public class GeneralConfigurationService extends OrchestratorService<GeneralConfiguration> {

	private ParameterService parameterService;
	
	@Inject
	public GeneralConfigurationService(
			ParameterService parameterService, 
			GeneralConfigurationResultParser resultParser,
			OrchestratorDatabaseConfiguration config,
			@Named("timestamp")Provider<Timestamp> timestampProvider) 
	{
		super(
				resultParser, 
				GeneralConfiguration.class, 
				"general_configurations",
				config.getDatabase(),
				timestampProvider);
		
		this.parameterService = parameterService;
	}
	
	@Override
	public GeneralConfiguration GetById(int id) throws SQLException, NotFoundException {

    	GeneralConfiguration config = super.GetById(id);
    	config.setParameters(parameterService.GetWhere(asList(id), "general_configurations_id = ?"));
		
		return config;
	}

	@Override
	public List<GeneralConfiguration> GetAll() throws SQLException {

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
	   super.Update(con, config);
	   
	   for(FeedbackParameter param : config.getParameters())
	   {
		   param.setGenaralConfigurationId(config.getId());
		   if(param.getId() == null){
			   parameterService.Insert(con, param);
		   }else{	  
			   parameterService.Update(con, param);
		   }
	   }
	}

	@Override
	public int Insert(Connection con, GeneralConfiguration config)
			throws SQLException, NotFoundException, UnsupportedOperationException {
		
		int id = super.Insert(con, config);
	    for(FeedbackParameter param : config.getParameters())
	    {
	    	param.setGenaralConfigurationId(id);
	    	parameterService.Insert(con, param);
	    }
	    
	    return id;
	}
}
