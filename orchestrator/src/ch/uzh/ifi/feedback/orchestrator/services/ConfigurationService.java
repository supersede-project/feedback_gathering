package ch.uzh.ifi.feedback.orchestrator.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.Service.DatabaseConfiguration;
import ch.uzh.ifi.feedback.orchestrator.model.Configuration;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackMechanism;
import ch.uzh.ifi.feedback.orchestrator.model.GeneralConfiguration;
import javassist.NotFoundException;
import static java.util.Arrays.asList;

public class ConfigurationService extends OrchestratorService<Configuration>{

	private MechanismService mechanismService;
	private GeneralConfigurationService generalConfigurationService;
	
	@Inject
	public ConfigurationService(
			ConfigurationResultParser resultParser, 
			MechanismService mechanismService,
			GeneralConfigurationService generalConfigurationService,
			DatabaseConfiguration config) 
	{
		super(
			resultParser, 
			Configuration.class, 
			"configurations", 
			config.getOrchestratorDb(), 
			mechanismService, 
			generalConfigurationService);
		
		this.mechanismService = mechanismService;
		this.generalConfigurationService = generalConfigurationService;
	}
	
	@Override
	public Configuration GetById(int id) throws SQLException, NotFoundException {

		Configuration config = super.GetById(id);
		//config.getFeedbackMechanisms().addAll(mechanismService.GetAllFor("configuration_id", id));
		config.getFeedbackMechanisms().addAll(mechanismService.GetWhere(asList(id), "configurations_id = ?"));
		if(config.getGeneralConfigurationId() != null)
			config.setGeneralConfiguration(generalConfigurationService.GetById(config.getGeneralConfigurationId()));
		
		return config;
	}

	@Override
	public List<Configuration> GetAll() throws SQLException, NotFoundException {

		List<Configuration> configurations = super.GetAll();
		for(Configuration config : configurations)
		{
			//config.getFeedbackMechanisms().addAll(mechanismService.GetAllFor("configuration_id", config.getId()));
			config.getFeedbackMechanisms().addAll(mechanismService.GetWhere(asList(config.getId()), "configurations_id = ?"));
			if(config.getGeneralConfigurationId() != null)
				config.setGeneralConfiguration(generalConfigurationService.GetById(config.getGeneralConfigurationId()));
		}
		
		return configurations;
	}
	
	@Override
	public List<Configuration> GetWhere(List<Object> values, String... conditions) throws SQLException, NotFoundException 
	{
		List<Configuration> configurations =  super.GetWhere(values, conditions);
		
		for(Configuration config : configurations)
		{
			config.getFeedbackMechanisms().addAll(mechanismService.GetWhere(asList(config.getId()), "configurations_id = ?"));
			if(config.getGeneralConfigurationId() != null)
				config.setGeneralConfiguration(generalConfigurationService.GetById(config.getGeneralConfigurationId()));
		}
		
		return configurations;
	}

	@Override
	public void Update(Connection con, Configuration config) throws SQLException, NotFoundException, UnsupportedOperationException 
	{
		GeneralConfiguration generalConfig = config.getGeneralConfiguration();
		Integer generalConfigId = null;
		if(generalConfig != null)
		{
			if(generalConfig.getId() == null)
			{
				generalConfigId = generalConfigurationService.Insert(con, generalConfig);
				config.setGeneralConfigurationId(generalConfigId);
			}else{
				generalConfigurationService.Update(con, generalConfig);
			}
		}
		
		super.Update(con, config);
		for(FeedbackMechanism mechanism : config.getFeedbackMechanisms())
		{
			mechanism.setConfigurationsid(config.getId());
			if(mechanism.getId() == null)
			{
				mechanismService.Insert(con, mechanism);
				//mechanismService.InsertFor(con, mechanism, "configuration_id", config.getId());
			}else{
				mechanismService.Update(con, mechanism);
				//mechanismService.UpdateFor(con, mechanism, "configuration_id", config.getId());
			}
		}
	}
	
	@Override
	public int Insert(Connection con, Configuration config)
			throws SQLException, NotFoundException, UnsupportedOperationException {
		
		GeneralConfiguration generalConfig = config.getGeneralConfiguration();
		if(generalConfig != null)
		{
			int generalConfigId = generalConfigurationService.Insert(con, generalConfig);
			config.setGeneralConfigurationId(generalConfigId);
		}
		
		int configId = super.Insert(con, config);
		
		for(FeedbackMechanism mechanism : config.getFeedbackMechanisms())
		{
			mechanism.setConfigurationsid(config.getId());
			mechanismService.Insert(con, mechanism);
			//mechanismService.InsertFor(con, mechanism, "configuration_id", configId);
		}
		
		return configId;
	}

}
