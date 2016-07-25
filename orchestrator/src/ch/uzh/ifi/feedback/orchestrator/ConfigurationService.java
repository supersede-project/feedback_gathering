package ch.uzh.ifi.feedback.orchestrator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import ch.uzh.ifi.feedback.library.transaction.IDbResultParser;
import ch.uzh.ifi.feedback.library.transaction.TransactionManager;
import ch.uzh.ifi.feedback.orchestrator.Model.Application;
import ch.uzh.ifi.feedback.orchestrator.Model.Configuration;
import ch.uzh.ifi.feedback.orchestrator.Model.FeedbackMechanism;
import ch.uzh.ifi.feedback.orchestrator.Model.FeedbackParameter;
import ch.uzh.ifi.feedback.orchestrator.Model.GeneralConfiguration;
import ch.uzh.ifi.feedback.orchestrator.Model.PullConfiguration;

public class ConfigurationService {

	private TransactionManager transactionManager;
	private IDbResultParser<List<FeedbackMechanism>> dbResultParser;
	
	public ConfigurationService(TransactionManager transactionManager, IDbResultParser<List<FeedbackMechanism>> dbResultParser)
	{
		this.transactionManager = transactionManager;
		this.dbResultParser = dbResultParser;
	}

	public List<FeedbackMechanism> Read(String application) throws Exception {
		transactionManager.withTransaction((con) -> {
		    dbResultParser.Parse(ExecuteTransaction(con, application));
		});
		
		return dbResultParser.GetResult();
	}
	
	public void CreateConfiguration(Application config) throws Exception {
		
		Connection c = transactionManager.createDatabaseConnection();
		PreparedStatement s = c.prepareStatement("SELECT * FROM feedback_orchestrator.applications as app WHERE app.name = ? ;");
		s.setString(1, config.getName());
		ResultSet rs = s.executeQuery();
		if(rs.next())
			throw new Exception("Application already registered!");
		
		transactionManager.withTransaction((con) -> {
			int appId = CreateApplication(config, con);
			StorePullConfigurations(config.getPullConfigurations(), appId, con);
			StoreFeedbackMechanisms(config.getFeedbackMechanisms(), appId, con);
			StoreGeneralConfigurations(config.getGeneralConfigurations(), appId, con);
		});
		
	}
	
	private int CreateApplication(Application application, Connection con) throws SQLException
	{
		PreparedStatement s = con.prepareStatement(
				"INSERT INTO feedback_orchestrator.applications (name) VALUES (?) ;", PreparedStatement.RETURN_GENERATED_KEYS);
		s.setString(1, application.getName());
		s.execute();
	    ResultSet keys = s.getGeneratedKeys();
	    keys.next();
	    return keys.getInt(1);
	}
	
	private void StoreGeneralConfigurations(List<GeneralConfiguration> generalConfigs, int applicationId, Connection con) throws SQLException
	{
		for(GeneralConfiguration config : generalConfigs)
		{
		    PreparedStatement s = con.prepareStatement(
		    		"INSERT INTO feedback_orchestrator.general_configurations "
		    		+ "(application_id) "
		    		+ "VALUES (?) ;", PreparedStatement.RETURN_GENERATED_KEYS);
		    
		    s.setInt(1, applicationId);
		    s.execute();
		    ResultSet keys = s.getGeneratedKeys();
		    keys.next();
		    int key = keys.getInt(1);
		    
		    for(FeedbackParameter param : config.getParameters())
		    {
		    	StoreParameter(param, null, null, key, null, con);
		    }
		}
	}
	
	private void StoreFeedbackMechanisms(List<FeedbackMechanism> mechanisms, int applicationId, Connection con) throws SQLException
	{
		for(FeedbackMechanism mechanism : mechanisms)
		{
		    PreparedStatement s = con.prepareStatement(
		    		"INSERT INTO feedback_orchestrator.mechanisms "
		    		+ "(application_id, name, active, order, can_be_activated, created_at, updated_at) "
		    		+ "VALUES (?, ?, ?, ?, ?, ?, ?) ;", PreparedStatement.RETURN_GENERATED_KEYS);
		    
		    s.setInt(1, applicationId);
		    s.setString(2, mechanism.getType());
		    s.setBoolean(3, mechanism.isActive());
		    s.setInt(4, mechanism.getOrder());
		    s.setBoolean(5, mechanism.isCanBeActivated());
		    s.execute();
		    ResultSet keys = s.getGeneratedKeys();
		    keys.next();
		    int key = keys.getInt(1);
		    
		    for(FeedbackParameter param : mechanism.getParameters())
		    {
		    	StoreParameter(param, null, null, key, null, con);
		    }
		}
	}
	
	private void StorePullConfigurations(List<PullConfiguration> pullConfigurations, int applicationId, Connection con) throws SQLException
	{
		for(PullConfiguration config : pullConfigurations)
		{
		    PreparedStatement s = con.prepareStatement(
		    		"INSERT INTO feedback_orchestrator.pull_configurations (applications_id, active) VALUES (?, ?) ;", PreparedStatement.RETURN_GENERATED_KEYS);
		    
		    s.setInt(1, applicationId);
		    s.setBoolean(2, config.getActive());
		    s.execute();
		    ResultSet keys = s.getGeneratedKeys();
		    keys.next();
		    int key = keys.getInt(1);
		    
		    for(FeedbackParameter param : config.getParameters())
		    {
		    	StoreParameter(param, key, null, null, null, con);
		    }
		}
	}
	
	private void StoreParameter(
			FeedbackParameter param, 
			Integer pullConfigurationId, 
			Integer generalConfigurationId, 
			Integer mechanismId, 
			Integer parameterId, 
			Connection con) throws SQLException{
		
		PreparedStatement s = con.prepareStatement(
				"INSERT INTO feedback_orchestrator.parameters "
				+ "(mechanism_id, key, value, default_value, editable_by_user, parameters_id, language, general_configurations_id, pull_configurations_id) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ;", PreparedStatement.RETURN_GENERATED_KEYS);
		s.setObject(1, mechanismId);
		s.setString(2, param.getKey());
		s.setObject(4, param.getDefaultValue());
		s.setBoolean(5, param.getEditableByUser());
		s.setObject(6, parameterId);
		s.setString(7, param.getLanguage());
		s.setObject(8, generalConfigurationId);
		s.setObject(9, pullConfigurationId);
		
		if(param.getValue().getClass().equals(List.class)){
			
			s.setObject(3, null);
			s.execute();
		    ResultSet keys = s.getGeneratedKeys();
		    keys.next();
		    int key = keys.getInt(1);

			List<Object> children = (List<Object>)param.getValue();
			for (Object o : children){
				FeedbackParameter childParam = (FeedbackParameter)o;
				StoreParameter(childParam, pullConfigurationId, generalConfigurationId, mechanismId, key, con);	
			}
			
		}else{
			s.setObject(3, param.getValue());
			s.execute();
		}
	}
	
	public ResultSet ExecuteTransaction(Connection con, String application) throws SQLException
	{
	    PreparedStatement s = con.prepareStatement(

	    		"Select m.name as mechanism_name, m.order, m.active, m.can_be_activated, p.key, p.value, p.default_value, p.editable_by_user FROM "
	    		+ "feedback_orchestrator.mechanisms as m "
	    		+ "LEFT JOIN feedback_orchestrator.applications as a on (m.application_id = a.id) "
	    		+ "LEFT JOIN feedback_orchestrator.parameters as p on (p.mechanism_id = m.id) "
	    		+ "WHERE a.name = ? ;"		    		
	    		);
	    
	    s.setString(1, application);
	    ResultSet result = s.executeQuery();
	    
	    return result;
	}
}
