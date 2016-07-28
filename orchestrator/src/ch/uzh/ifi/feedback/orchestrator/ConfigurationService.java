package ch.uzh.ifi.feedback.orchestrator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.internal.bind.SqlDateTypeAdapter;

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
	
	public void CreateConfiguration(Application config) throws Exception {
		
		Connection c = transactionManager.createDatabaseConnection();
		PreparedStatement s = c.prepareStatement("SELECT id FROM feedback_orchestrator.applications as app WHERE app.name = ? ;");
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
	
	public void UpdateConfiguration(Application config) throws Exception {
		Connection c = transactionManager.createDatabaseConnection();
		PreparedStatement s = c.prepareStatement("SELECT id FROM feedback_orchestrator.applications as app WHERE app.name = ? ;");
		s.setString(1, config.getName());
		ResultSet rs = s.executeQuery();
		if(!rs.next())
			throw new Exception("Configuration does not exist!");
		
		int appId = rs.getInt("id");
		
		transactionManager.withTransaction((con) -> {
			StorePullConfigurations(config.getPullConfigurations(), appId, con);
			//StoreFeedbackMechanisms(config.getFeedbackMechanisms(), appId, con);
			//StoreGeneralConfigurations(config.getGeneralConfigurations(), appId, con);
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
			int key;
			
			//Do update
			if(config.getCreatedAt() != null)
			{
			    PreparedStatement s = con.prepareStatement(
			    		"Update feedback_orchestrator.general_configurations as c"
			    		+ "SET c.updated_at = now() "
			    		+ "WHERE c.created_at = ? AND c.application_id = ?;");
			    s.setTimestamp(1, config.getCreatedAt());
			    s.setInt(2, applicationId);
			    s.execute();
			    
			    s = con.prepareStatement(
			    		  "SELECT id from feedback_orchestrator.general_configurations as c"
			    		+ "WHERE c.created_at = ? AND c.application_id = ?;");
			    s.setTimestamp(1, config.getCreatedAt());
			    s.setInt(2, applicationId);
			    ResultSet rs = s.executeQuery();
			    
			    key = rs.getInt("id");
			}
			else{
				//Do create
			    PreparedStatement s = con.prepareStatement(
			    		"INSERT INTO feedback_orchestrator.general_configurations "
			    		+ "(application_id) "
			    		+ "VALUES (?) ;", PreparedStatement.RETURN_GENERATED_KEYS);
			    
			    s.setInt(1, applicationId);
			    s.execute();
			    ResultSet keys = s.getGeneratedKeys();
			    keys.next();
			    key = keys.getInt(1);
			}
			
		    for(FeedbackParameter param : config.getParameters())
		    {
		    	StoreParameter(param, null, key, null, null, con);
		    }
		}
	}
	
	private void StoreFeedbackMechanisms(List<FeedbackMechanism> mechanisms, int applicationId, Connection con) throws SQLException
	{
		for(FeedbackMechanism mechanism : mechanisms)
		{
		    PreparedStatement s = con.prepareStatement(
		    		"INSERT INTO feedback_orchestrator.mechanisms "
		    		+ "(application_id, `name`, active, `order`, can_be_activated) "
		    		+ "VALUES (?, ?, ?, ?, ?) ;", PreparedStatement.RETURN_GENERATED_KEYS);
		    
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
		    		  "INSERT INTO feedback_orchestrator.pull_configurations (applications_id, active, created_at) VALUES (?, ?, ?) "
		    		+ "ON DUPLICATE KEY UPDATE updated_at = now(), active = VALUES(active) ;", PreparedStatement.RETURN_GENERATED_KEYS);
		    
		    s.setInt(1, applicationId);
		    s.setBoolean(2, config.getActive());
		    s.setTimestamp(3, config.getCreatedAt());
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
				+ "(mechanism_id, `key`, value, default_value, editable_by_user, parameters_id, language, general_configurations_id, pull_configurations_id, created_at) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
				+ "ON DUPLICATE KEY UPDATE updated_at = now(), mechanism_id = VALUES(mechanism_id), `key` = VALUES(`key`), value = VALUES(value), "
				+ "editable_by_user = VALUES(editable_by_user), parameters_id = VALUES(parameters_id), language = VALUES(language), "
				+ "general_configurations_id = VALUES(general_configurations_id), pull_configurations_id = VALUES(pull_configurations_id);", 
				PreparedStatement.RETURN_GENERATED_KEYS);
		
		System.out.println(s.toString());
		
		s.setObject(1, mechanismId);
		s.setString(2, param.getKey());
		s.setObject(4, param.getDefaultValue());
		s.setObject(5, param.getEditableByUser());
		s.setObject(6, parameterId);
		s.setObject(7, param.getLanguage());
		s.setObject(8, generalConfigurationId);
		s.setObject(9, pullConfigurationId);
		s.setTimestamp(10, param.getCreatedAt());
		
		if(List.class.isAssignableFrom(param.getValue().getClass())){
			
			s.setObject(3, null);
			s.execute();
		    ResultSet keys = s.getGeneratedKeys();
		    keys.next();
		    int key = keys.getInt(1);

		    List<FeedbackParameter> children = (List<FeedbackParameter>)param.getValue();
			for (FeedbackParameter childParam : children){
				StoreParameter(childParam, pullConfigurationId, generalConfigurationId, mechanismId, key, con);	
			}
			
		}else{
			s.setObject(3, param.getValue());
			s.execute();
		}
	}
	
	public Application GetConfiguration(String application) throws Exception
	{
		Application app = new Application();
		app.setName(application);
		
		transactionManager.withTransaction((con) -> {
			
		    PreparedStatement s = con.prepareStatement(
		    		"Select a.id FROM feedback_orchestrator.applications as a WHERE a.name = ? ;");
		    s.setString(1, application);
		    ResultSet result = s.executeQuery();
		    result.next();
		    int appId = result.getInt("id");
		    
		    AddMechanisms(con, app, appId);
		    AddGeneralConfigurations(con, app, appId);
		    AddPullConfigurations(con, app, appId);
		    
		});

	    return app;
	}
	
	private void AddMechanisms(Connection con, Application app, int appId) throws SQLException
	{
	    PreparedStatement s = con.prepareStatement(

	    		"Select m.id, m.name as mechanism_name, m.order, m.active, m.can_be_activated FROM "
	    		+ "feedback_orchestrator.mechanisms as m "
	    		+ "JOIN feedback_orchestrator.applications as a on (m.application_id = a.id) "
	    		+ "WHERE a.id = ? ;"		    		
	    		);
	    
	    s.setInt(1, appId);
	    ResultSet result = s.executeQuery();
	    
	    List<FeedbackMechanism> mechanisms = new ArrayList<>();
	    while(result.next())
	    {
	    	FeedbackMechanism mechanism = new FeedbackMechanism();
	    	mechanism.setType(result.getString("mechanism_name"));
	    	mechanism.setActive(result.getBoolean("active"));
	    	mechanism.setCanBeActivated(result.getBoolean("can_be_activated"));
	    	mechanism.setOrder(result.getInt("order"));
	    	mechanism.setParameters(GetParameters(con, "mechanisms", "mechanism_id", result.getInt("id")));
	    	mechanisms.add(mechanism);
	    }
	    
	    app.setFeedbackMechanisms(mechanisms);
	}
	
	private void AddGeneralConfigurations(Connection con, Application app, int appId) throws SQLException
	{
	    PreparedStatement s = con.prepareStatement(

	    		"Select c.id, c.created_at, c.updated_at FROM "
	    		+ "feedback_orchestrator.general_configurations as c "
	    		+ "JOIN feedback_orchestrator.applications as a on (c.application_id = a.id) "
	    		+ "WHERE a.id = ? ;"		    		
	    		);
	    
	    s.setInt(1, appId);
	    ResultSet result = s.executeQuery();
	    
	    List<GeneralConfiguration> configs = new ArrayList<>();
	    while(result.next())
	    {
	    	GeneralConfiguration config = new GeneralConfiguration();
	    	config.setCreatedAt(result.getTimestamp("created_at"));
	    	config.setUpdatedAt(result.getTimestamp("updated_at"));
	    	int configKey = result.getInt("id");
	    	config.setParameters(GetParameters(con, "general_configurations", "general_configurations_id", configKey));
	    	configs.add(config);
	    }
	    
	    app.setGeneralConfigurations(configs);
	}
	
	private void AddPullConfigurations(Connection con, Application app, int appId) throws SQLException
	{
	    PreparedStatement s = con.prepareStatement(

	    		"Select c.id, c.created_at, c.updated_at, c.active FROM "
	    		+ "feedback_orchestrator.pull_configurations as c "
	    		+ "JOIN feedback_orchestrator.applications as a on (c.applications_id = a.id) "
	    		+ "WHERE a.id = ? ;"		    		
	    		);
	    
	    s.setInt(1, appId);
	    ResultSet result = s.executeQuery();
	    
	    List<PullConfiguration> configs = new ArrayList<>();
	    while(result.next())
	    {
	    	PullConfiguration config = new PullConfiguration();
	    	config.setCreatedAt(result.getTimestamp("created_at"));
	    	config.setUpdatedAt(result.getTimestamp("updated_at"));
	    	config.setActive(result.getBoolean("active"));
	    	
	    	int configKey = result.getInt("id");
	    	config.setParameters(GetParameters(con, "pull_configurations", "pull_configurations_id", configKey));
	    	configs.add(config);
	    }
	    
	    app.setPullConfigurations(configs);
	}
	
	private List<FeedbackParameter> GetParameters(Connection con, String foreignTableName, String foreignKeyName, int foreignKey) throws SQLException
	{
		String sql = String.format(
				"Select p.id, p.parameters_id, p.key, p.value, p.default_value, p.editable_by_user, p.language, p.created_at, p.updated_at FROM "
	    		+ "feedback_orchestrator.parameters as p "
	    		+ "JOIN feedback_orchestrator.%s as f on (p.%s = f.id) "
	    		+ "WHERE f.id = ? ;", foreignTableName, foreignKeyName);
				
	    PreparedStatement s = con.prepareStatement(sql);
	    s.setInt(1, foreignKey);
	    ResultSet result = s.executeQuery();
	    
		Map<Integer, List<FeedbackParameter>> childMap = new HashMap<>();
		Map<FeedbackParameter, Integer> parameterMap = new HashMap<>();
		List<FeedbackParameter> rootParams = new ArrayList<>();
		
	    while(result.next())
	    {
	    	FeedbackParameter param = new FeedbackParameter();
	    	param.setKey(result.getString("key"));
	    	param.setValue(result.getObject("value"));
	    	param.setDefaultValue(result.getObject("default_value"));
	    	param.setEditableByUser(result.getBoolean("editable_by_user"));
	    	param.setLanguage(result.getString("language"));
	    	param.setCreatedAt(result.getTimestamp("created_at"));
	    	param.setUpdatedAt(result.getTimestamp("updated_at"));
	    	parameterMap.put(param, result.getInt("id"));
	    	
	    	Integer parameterKey = (Integer)result.getObject("parameters_id");
	    	if(parameterKey != null){
	    		if(!childMap.containsKey(parameterKey))
	    			childMap.put(parameterKey, new ArrayList<>());
	    			
	    		childMap.get(parameterKey).add(param);
	    	}else{
	    		rootParams.add(param);
	    	}
	    }
	    
	    return setParametersRecursive(rootParams, parameterMap, childMap);
	}
	
	private List<FeedbackParameter> setParametersRecursive(
			List<FeedbackParameter> params, 
			Map<FeedbackParameter, Integer> parameterMap, 
			Map<Integer, List<FeedbackParameter>> childMap)
	{
		for(FeedbackParameter param : params)
		{
			if(childMap.containsKey(parameterMap.get(param)))
			{
				List<FeedbackParameter> children = childMap.get(parameterMap.get(param));
				param.setValue(setParametersRecursive(children, parameterMap, childMap));
			}
		}
		
		return params;
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
