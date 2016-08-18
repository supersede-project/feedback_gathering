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
import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.orchestrator.model.Configuration;
import ch.uzh.ifi.feedback.orchestrator.model.ConfigurationType;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackMechanism;
import ch.uzh.ifi.feedback.orchestrator.model.GeneralConfiguration;
import javassist.NotFoundException;
import jdk.nashorn.internal.runtime.regexp.joni.Config;

public class ConfigurationService extends ServiceBase<Configuration>{

	private MechanismService mechanismService;
	private GeneralConfigurationService generalConfigurationService;
	
	@Inject
	public ConfigurationService(
			ConfigurationResultParser resultParser, 
			MechanismService mechanismService,
			GeneralConfigurationService generalConfigurationService) 
	{
		super(
			resultParser, 
			Configuration.class, 
			"configurations", 
			"feedback_orchestrator", 
			mechanismService, 
			generalConfigurationService);
		
		this.mechanismService = mechanismService;
		this.generalConfigurationService = generalConfigurationService;
	}
	
	@Override
	public Configuration GetById(int id) throws SQLException, NotFoundException {

		Configuration config = super.GetById(id);
		config.getFeedbackMechanisms().addAll(mechanismService.GetAllFor("configuration_id", id));
		if(config.getGeneralConfigurationId() != null)
			config.setGeneralConfiguration(generalConfigurationService.GetById(config.getGeneralConfigurationId()));
		
		return config;
	}

	@Override
	public List<Configuration> GetAll() throws SQLException, NotFoundException {

		List<Configuration> configurations = super.GetAll();
		for(Configuration config : configurations)
		{
			config.getFeedbackMechanisms().addAll(mechanismService.GetAllFor("configuration_id", config.getId()));
			if(config.getGeneralConfigurationId() != null)
				config.setGeneralConfiguration(generalConfigurationService.GetById(config.getGeneralConfigurationId()));
		}
		
		return configurations;
	}
	
	@Override
	public List<Configuration> GetWhereEquals(List<String> attributeNames, List<Object> values) throws SQLException, NotFoundException {
		List<Configuration> configurations = super.GetWhereEquals(attributeNames, values);
		
		for(Configuration config : configurations)
		{
			config.getFeedbackMechanisms().addAll(mechanismService.GetAllFor("configuration_id", config.getId()));
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
			if(generalConfig.getId() != null)
			{
				generalConfigId = generalConfigurationService.Insert(con, generalConfig);
				config.setGeneralConfigurationId(generalConfigId);
			}else{
				generalConfigurationService.Update(con, generalConfig);
			}
		}
		
		PreparedStatement s = con.prepareStatement(
				  "UPDATE feedback_orchestrator.configurations as c "
				+ "SET `name` = IFNULL(?, `name`), `type` = IFNULL(?, `type`), general_configuration_id = IFNULL(?, general_configuration_id) "
				+ "WHERE c.id = ? ;");
		
		s.setObject(1, config.getName());
		s.setObject(2, config.getType().toString());
		s.setObject(3, generalConfigId);
		s.setInt(4, config.getId());
		
		for(FeedbackMechanism mechanism : config.getFeedbackMechanisms())
		{
			if(mechanism.getId() != null)
			{
				mechanismService.InsertFor(con, mechanism, "configuration_id", config.getId());
			}else{
				mechanismService.UpdateFor(con, mechanism, "configuration_id", config.getId());
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
			mechanismService.InsertFor(con, mechanism, "configuration_id", configId);
		}
		
		return configId;
	}

}
