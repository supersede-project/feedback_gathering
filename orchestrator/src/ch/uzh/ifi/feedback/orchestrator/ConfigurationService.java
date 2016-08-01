package ch.uzh.ifi.feedback.orchestrator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.internal.bind.SqlDateTypeAdapter;

import ch.uzh.ifi.feedback.library.transaction.IDbResultParser;
import ch.uzh.ifi.feedback.library.transaction.TransactionManager;
import ch.uzh.ifi.feedback.orchestrator.model.Application;
import ch.uzh.ifi.feedback.orchestrator.model.Configuration;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackMechanism;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackParameter;
import ch.uzh.ifi.feedback.orchestrator.model.GeneralConfiguration;
import ch.uzh.ifi.feedback.orchestrator.model.PullConfiguration;
import jdk.nashorn.internal.runtime.regexp.joni.Config;

public class ConfigurationService {

	private TransactionManager transactionManager;
	private IDbResultParser<List<FeedbackMechanism>> dbResultParser;
	
	public ConfigurationService(TransactionManager transactionManager, IDbResultParser<List<FeedbackMechanism>> dbResultParser)
	{
		this.transactionManager = transactionManager;
		this.dbResultParser = dbResultParser;
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
		    app.setId(appId);
		    
		    AddMechanisms(con, app);
		    AddGeneralConfigurations(con, app);
		    AddPullConfigurations(con, app);
		    
		});

	    return app;
	}
	
	public void InsertConfiguration(Application config) throws Exception {
		
		Connection c = transactionManager.createDatabaseConnection();
		PreparedStatement s = c.prepareStatement("SELECT id FROM feedback_orchestrator.applications as app WHERE app.name = ? ;");
		s.setString(1, config.getName());
		ResultSet rs = s.executeQuery();
		if(rs.next())
			throw new Exception("Application already registered!");
		
		transactionManager.withTransaction((con) -> {
			int appId = CreateApplication(config, con);
			InsertPullConfigurations(config.getPullConfigurations(), appId, con);
			InsertFeedbackMechanisms(config.getFeedbackMechanisms(), appId, con);
			InsertGeneralConfigurations(config.getGeneralConfigurations(), appId, con);
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
			UpdatePullConfigurations(config.getPullConfigurations(), appId, con);
			UpdateFeedbackMechanisms(config.getFeedbackMechanisms(), appId, con);
			UpdateGeneralConfigurations(config.getGeneralConfigurations(), appId, con);
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
	
	private void InsertGeneralConfigurations(List<GeneralConfiguration> generalConfigs, int applicationId, Connection con) throws SQLException
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
		    	InsertParameter(param, null, config.getId(), null, null, con);
		    }
		}
	}
	
	private void UpdateGeneralConfigurations(List<GeneralConfiguration> generalConfigs, int applicationId, Connection con) throws SQLException
	{
		List<GeneralConfiguration> newConfigs= generalConfigs.stream().filter(c -> c.getId() == null).collect(Collectors.toList());
		generalConfigs.removeAll(newConfigs);
		InsertGeneralConfigurations(newConfigs, applicationId, con);
		
		for(GeneralConfiguration config : generalConfigs)
		{
		    PreparedStatement s = con.prepareStatement(
		    		"Update feedback_orchestrator.general_configurations as c "
		    		+ "SET c.updated_at = now() "
		    		+ "WHERE c.id = ? ;");
		    s.setInt(1, config.getId());
		    s.execute();
		   
		   for(FeedbackParameter param : config.getParameters())
		   {
			   if(param.getId() == null){
				   InsertParameter(param, null, config.getId(), null, null, con);
			   }else{
				   UpdateParameter(param, null, config.getId(), null, null, con);
			   }
		   }
		}
	}
	
	private void InsertFeedbackMechanisms(List<FeedbackMechanism> mechanisms, int applicationId, Connection con) throws SQLException
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
		    	InsertParameter(param, null, null, key, null, con);
		    }
		}
	}
	
	private void UpdateFeedbackMechanisms(List<FeedbackMechanism> mechanisms, int applicationId, Connection con) throws SQLException
	{
		List<FeedbackMechanism> newMechanisms= mechanisms.stream().filter(c -> c.getId() == null).collect(Collectors.toList());
		mechanisms.removeAll(newMechanisms);
		InsertFeedbackMechanisms(newMechanisms, applicationId, con);
		
		for(FeedbackMechanism mechanism : mechanisms)
		{
		    PreparedStatement s = con.prepareStatement(
		    		  "UPDATE feedback_orchestrator.mechanisms "
		    		+ "SET application_id = ?, `name` = IFNULL(?, `name`), active = IFNULL(?, active), `order` = IFNULL(?, `order`), "
		    		    + "can_be_activated = IFNULL(?, can_be_activated), updated_at = now() "
		    		+ "WHERE id = ? ;");
		    
		    s.setInt(1, applicationId);
		    s.setString(2, mechanism.getType());
		    s.setObject(3, mechanism.isActive());
		    s.setObject(4, mechanism.getOrder());
		    s.setObject(5, mechanism.isCanBeActivated());
		    s.setInt(6, mechanism.getId());
		    
		    System.out.println(s.toString());
		    s.execute();
		    
		    for(FeedbackParameter param : mechanism.getParameters())
		    {
		    	if(param.getId() == null){
		    		InsertParameter(param, null, null, mechanism.getId(), null, con);
		    	}else{
		    		UpdateParameter(param, null, null, mechanism.getId(), null, con);
		    	}
		    }
		}
	}
	
	private void InsertPullConfigurations(List<PullConfiguration> pullConfigurations, int applicationId, Connection con) throws SQLException
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
		    	InsertParameter(param, key, null, null, null, con);
		    }
		}
	}
	
	private void UpdatePullConfigurations(List<PullConfiguration> pullConfigurations, int applicationId, Connection con) throws SQLException
	{
		List<PullConfiguration> newConfigurations = pullConfigurations.stream().filter(c -> c.getId() == null).collect(Collectors.toList());
		pullConfigurations.removeAll(newConfigurations);
		InsertPullConfigurations(newConfigurations, applicationId, con);
		
		for(PullConfiguration config : pullConfigurations)
		{
		    PreparedStatement s = con.prepareStatement(
		    		    "UPDATE feedback_orchestrator.pull_configurations "
		    		  + "SET applications_id = ?, active = IFNULL(?, active), updated_at = now() "
		    		  + "WHERE id = ? ;");
		    
		    s.setInt(1, applicationId);
		    s.setObject(2, config.getActive());
		    s.setInt(3, config.getId());
		    s.execute();
		    
		    for(FeedbackParameter param : config.getParameters())
		    {
		    	if(param.getId() == null){
		    		InsertParameter(param, config.getId(), null, null, null, con);
		    	}else{
		    		UpdateParameter(param, config.getId(), null, null, null, con);
		    	}
		    }
		}
	}
	
	private void InsertParameter(
			FeedbackParameter param, 
			Integer pullConfigurationId, 
			Integer generalConfigurationId, 
			Integer mechanismId, 
			Integer parameterId, 
			Connection con) throws SQLException{
		
		PreparedStatement s = con.prepareStatement(
				  "INSERT INTO feedback_orchestrator.parameters "
				+ "(mechanism_id, `key`, value, default_value, editable_by_user, parameters_id, language, general_configurations_id, pull_configurations_id, created_at) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ;", PreparedStatement.RETURN_GENERATED_KEYS);
		
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
				InsertParameter(childParam, pullConfigurationId, generalConfigurationId, mechanismId, key, con);	
			}
			
		}else{
			s.setObject(3, param.getValue());
			s.execute();
		}
	}
	
	private void UpdateParameter(
			FeedbackParameter param, 
			Integer pullConfigurationId, 
			Integer generalConfigurationId, 
			Integer mechanismId, 
			Integer parameterId, 
			Connection con) throws SQLException{
		
		PreparedStatement s = con.prepareStatement(
				  	"UPDATE feedback_orchestrator.parameters "
				  + "SET mechanism_id = ?, `key` = ?, value = ?, default_value = IFNULL(?, default_value), editable_by_user = IFNULL(?, editable_by_user), parameters_id = ?, language = IFNULL(?, language), general_configurations_id = ?, pull_configurations_id = ? "
				  + "WHERE id = ? ;");
		
		s.setObject(1, mechanismId);
		s.setString(2, param.getKey());
		s.setObject(4, param.getDefaultValue());
		s.setObject(5, param.getEditableByUser());
		s.setObject(6, parameterId);
		s.setObject(7, param.getLanguage());
		s.setObject(8, generalConfigurationId);
		s.setObject(9, pullConfigurationId);
		s.setInt(10, param.getId());
		
		if(List.class.isAssignableFrom(param.getValue().getClass())){
			
			s.setObject(3, null);
			s.execute();

		    List<FeedbackParameter> children = (List<FeedbackParameter>)param.getValue();
			for (FeedbackParameter childParam : children){
				if(childParam.getId() == null)
				{
					InsertParameter(childParam, pullConfigurationId, generalConfigurationId, mechanismId, param.getId(), con);
				}else{
					UpdateParameter(childParam, pullConfigurationId, generalConfigurationId, mechanismId, param.getId(), con);		
				}
			}
			
		}else{
			s.setObject(3, param.getValue());
			s.execute();
		}
	}
	
	private void AddMechanisms(Connection con, Application app) throws SQLException
	{
	    PreparedStatement s = con.prepareStatement(

	    		"Select m.id, m.name as mechanism_name, m.order, m.active, m.can_be_activated FROM "
	    		+ "feedback_orchestrator.mechanisms as m "
	    		+ "JOIN feedback_orchestrator.applications as a on (m.application_id = a.id) "
	    		+ "WHERE a.id = ? ;"		    		
	    		);
	    
	    s.setInt(1, app.getId());
	    ResultSet result = s.executeQuery();
	    
	    List<FeedbackMechanism> mechanisms = new ArrayList<>();
	    while(result.next())
	    {
	    	FeedbackMechanism mechanism = new FeedbackMechanism();
	    	mechanism.setType(result.getString("mechanism_name"));
	    	mechanism.setActive(result.getBoolean("active"));
	    	mechanism.setCanBeActivated(result.getBoolean("can_be_activated"));
	    	mechanism.setOrder(result.getInt("order"));
	    	mechanism.setId(result.getInt("id"));
	    	mechanism.setParameters(GetParameters(con, "mechanisms", "mechanism_id", result.getInt("id")));
	    	mechanisms.add(mechanism);
	    }
	    
	    app.setFeedbackMechanisms(mechanisms);
	}
	
	private void AddGeneralConfigurations(Connection con, Application app) throws SQLException
	{
	    PreparedStatement s = con.prepareStatement(

	    		"Select c.id, c.created_at, c.updated_at FROM "
	    		+ "feedback_orchestrator.general_configurations as c "
	    		+ "JOIN feedback_orchestrator.applications as a on (c.application_id = a.id) "
	    		+ "WHERE a.id = ? ;"		    		
	    		);
	    
	    s.setInt(1, app.getId());
	    ResultSet result = s.executeQuery();
	    
	    List<GeneralConfiguration> configs = new ArrayList<>();
	    while(result.next())
	    {
	    	GeneralConfiguration config = new GeneralConfiguration();
	    	config.setCreatedAt(result.getTimestamp("created_at"));
	    	config.setUpdatedAt(result.getTimestamp("updated_at"));
	    	config.setId(result.getInt("id"));
	    	config.setParameters(GetParameters(con, "general_configurations", "general_configurations_id", config.getId()));
	    	configs.add(config);
	    }
	    
	    app.setGeneralConfigurations(configs);
	}
	
	private void AddPullConfigurations(Connection con, Application app) throws SQLException
	{
	    PreparedStatement s = con.prepareStatement(

	    		"Select c.id, c.created_at, c.updated_at, c.active FROM "
	    		+ "feedback_orchestrator.pull_configurations as c "
	    		+ "JOIN feedback_orchestrator.applications as a on (c.applications_id = a.id) "
	    		+ "WHERE a.id = ? ;"		    		
	    		);
	    
	    s.setInt(1, app.getId());
	    ResultSet result = s.executeQuery();
	    
	    List<PullConfiguration> configs = new ArrayList<>();
	    while(result.next())
	    {
	    	PullConfiguration config = new PullConfiguration();
	    	config.setCreatedAt(result.getTimestamp("created_at"));
	    	config.setUpdatedAt(result.getTimestamp("updated_at"));
	    	config.setActive(result.getBoolean("active"));
	    	config.setId(result.getInt("id"));
	    	config.setParameters(GetParameters(con, "pull_configurations", "pull_configurations_id", config.getId()));
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
	    	param.setId(result.getInt("id"));
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
