package ch.uzh.ifi.feedback.orchestrator.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.inject.Inject;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import ch.uzh.ifi.feedback.library.rest.Service.IDbService;
import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackMechanism;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackParameter;
import javassist.NotFoundException;
import static java.util.Arrays.asList;

public class ParameterService extends OrchestratorService<FeedbackParameter>{
	
	@Inject
	public ParameterService(ParameterResultParser resultParser) 
	{
		super(resultParser, FeedbackParameter.class, "parameters", "feedback_orchestrator");
	}
	
	/*
	@Override
	public List<FeedbackParameter> GetWhereEquals(List<String> attributeNames, List<Object> values)
			throws SQLException, NotFoundException {
		List<FeedbackParameter> params = super.GetWhereEquals(attributeNames, values);
		
		Map<Integer, List<FeedbackParameter>> childMap = new HashMap<>();
		Map<FeedbackParameter, Integer> parameterMap = new HashMap<>();
		
		if(GetLanguage() != null)
			params = params.stream().filter(p -> p.getLanguage().equals(this.GetLanguage())).collect(Collectors.toList());
		
		List<FeedbackParameter> rootParams = GetRootParams(params, parameterMap, childMap);
	    
	    return setParametersRecursive(rootParams, parameterMap, childMap);
	}
	*/
	
	@Override
	public List<FeedbackParameter> GetWhere(List<Object> values, String... conditions)
			throws SQLException, NotFoundException {
		
		List<FeedbackParameter> params = super.GetWhere(values, conditions);
		
		Map<Integer, List<FeedbackParameter>> childMap = new HashMap<>();
		Map<FeedbackParameter, Integer> parameterMap = new HashMap<>();
		
		if(GetLanguage() != null)
			params = params.stream().filter(p -> p.getLanguage().equals(this.GetLanguage())).collect(Collectors.toList());
		
		List<FeedbackParameter> rootParams = GetRootParams(params, parameterMap, childMap);
	    
	    return setParametersRecursive(rootParams, parameterMap, childMap);
	}
	
	@Override
	public List<FeedbackParameter> GetAll() throws SQLException, NotFoundException
	{
		return GetWhere(asList());
		/*
		Map<Integer, List<FeedbackParameter>> childMap = new HashMap<>();
		Map<FeedbackParameter, Integer> parameterMap = new HashMap<>();
		List<FeedbackParameter> params = super.GetAll();
		if(GetLanguage() != null)
			params = params.stream().filter(p -> p.getLanguage().equals(this.GetLanguage())).collect(Collectors.toList());
		
		List<FeedbackParameter> rootParams = GetRootParams(params, parameterMap, childMap);
	    
	    return setParametersRecursive(rootParams, parameterMap, childMap);
	    */
	}
	
	private List<FeedbackParameter> GetRootParams(List<FeedbackParameter> params, Map<FeedbackParameter, Integer> parameterMap, Map<Integer, List<FeedbackParameter>> childMap)
	{
		List<FeedbackParameter> rootParams = new ArrayList<>();
	    for(FeedbackParameter param : params)
	    {
	    	parameterMap.put(param, param.getId());
	    	
	    	Integer parameterKey = (Integer)param.getParentParameterId();
	    	if(parameterKey != null){
	    		if(!childMap.containsKey(parameterKey))
	    			childMap.put(parameterKey, new ArrayList<>());
	    			
	    		childMap.get(parameterKey).add(param);
	    	}else{
	    		rootParams.add(param);
	    	}
	    }
	    
	    return rootParams;
	}
	
	@Override
	public FeedbackParameter GetById(int id) throws SQLException, NotFoundException
	{
		/*
		FeedbackParameter param = super.GetById(id);
    	return param;
    	*/
		return GetWhere(asList(id), "parameters_id = ?").get(0);
	}
	
	/*
	@Override
	public void InsertFor(Connection con, FeedbackParameter param, String foreignKeyName, int foreignKey) throws SQLException, NotFoundException
	{
		switch(foreignKeyName){
			case "mechanism_id":
				InsertParameter(param, null, foreignKey, null, con);
				break;
			case "configuration_id":
				InsertParameter(param, foreignKey, null, null, con);
				break;
			default:
				throw new NotFoundException("");
		}
	}
	

	public void InsertParameter(
			FeedbackParameter param, 
			Integer generalConfigurationId, 
			Integer mechanismId, 
			Integer parameterId, 
			Connection con) throws SQLException, NotFoundException{
		
		PreparedStatement s = con.prepareStatement(
				  "INSERT INTO feedback_orchestrator.parameters "
				+ "(mechanism_id, `key`, value, default_value, editable_by_user, parameters_id, "
				+ "language, general_configurations_id,  created_at) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ;", PreparedStatement.RETURN_GENERATED_KEYS);
		
		s.setObject(1, mechanismId);
		s.setString(2, param.getKey());
		s.setObject(4, param.getDefaultValue());
		s.setObject(5, param.getEditableByUser());
		s.setObject(6, parameterId);
		if(param.getLanguage() == null)
		{
			s.setString(7, "en");
		}else{
			s.setObject(7, param.getLanguage());
		}

		s.setObject(8, generalConfigurationId);
		s.setTimestamp(9, param.getCreatedAt());
		
		int key;
		if(List.class.isAssignableFrom(param.getValue().getClass())){
			
			s.setObject(3, null);
			s.execute();
		    ResultSet keys = s.getGeneratedKeys();
		    keys.next();
		    key = keys.getInt(1);
		    

		    List<FeedbackParameter> children = (List<FeedbackParameter>)param.getValue();
			for (FeedbackParameter childParam : children){
				InsertParameter(childParam, generalConfigurationId, mechanismId, key, con);	
			}
			
		}else{
			s.setObject(3, param.getValue());
			s.execute();
			
		    ResultSet keys = s.getGeneratedKeys();
		    keys.next();
		    key = keys.getInt(1);
		}
	}
	*/
	
	@Override
	public int Insert(Connection con, FeedbackParameter param)
			throws SQLException, NotFoundException, UnsupportedOperationException {

		int parameterId;
		
		if (param.getValue() == null || List.class.isAssignableFrom(param.getValue().getClass()))
		{
			List<FeedbackParameter> children = (List<FeedbackParameter>)param.getValue();
			param.setValue(null);
			parameterId = super.Insert(con, param);
			for(FeedbackParameter child : children)
			{
				child.setParentParameterId(parameterId);
				child.setMechanismId(param.getMechanismId());
				child.setGenaralConfigurationId(param.getGenaralConfigurationId());
				Insert(con, child);
			}
		}else{
			parameterId = super.Insert(con, param);
		}
		
		return parameterId;
	}
	
	@Override
	public void Update(Connection con, FeedbackParameter param)
			throws SQLException, NotFoundException, UnsupportedOperationException {

		if (List.class.isAssignableFrom(param.getValue().getClass()))
		{
			List<FeedbackParameter> children = (List<FeedbackParameter>)param.getValue();
			param.setValue(null);
			super.Update(con, param);
			
			for(FeedbackParameter child : children)
			{
				if(child.getId() == null)
				{
					child.setParentParameterId(param.getId());
					child.setMechanismId(param.getMechanismId());
					child.setGenaralConfigurationId(param.getGenaralConfigurationId());
					Insert(con, child);
				}else{
					Update(con, child);
				}
			}
		}
	}
	
	/*
	@Override
	public void UpdateFor(Connection con, FeedbackParameter param, String foreignKeyName, int foreignKey) throws SQLException, NotFoundException
	{
		switch(foreignKeyName){
			case "mechanism_id":
				UpdateParameter(param, null, foreignKey, null, con);
				break;
			case "configuration_id":
				UpdateParameter(param, null, null, foreignKey, con);
				break;
			default:
				throw new NotFoundException("Foreign Key '" + foreignKeyName + "' does not exist in table 'Parameters'");
		}
	}
	
	@Override
	public void Update(Connection con, FeedbackParameter param) throws SQLException, NotFoundException
	{
		UpdateParameter(param, null, null, null, con);
	}
	
	public void UpdateParameter(
			FeedbackParameter param, 
			Integer parameterId,
			Integer mechanismId,
			Integer generalConfigurationId,
			Connection con) throws SQLException, NotFoundException{
		
		PreparedStatement s = con.prepareStatement(
				  	"UPDATE feedback_orchestrator.parameters "
				  + "SET mechanism_id = ?, `key` = ?, value = ?, default_value = IFNULL(?, default_value), editable_by_user = IFNULL(?, editable_by_user), "
				       + "parameters_id = ?, language = IFNULL(?, language), general_configurations_id = ? "
				  + "WHERE id = ? ;");
		
		s.setObject(1, mechanismId);
		s.setString(2, param.getKey());
		s.setObject(4, param.getDefaultValue());
		s.setObject(5, param.getEditableByUser());
		s.setObject(6, parameterId);
		s.setObject(7, param.getLanguage());
		s.setObject(8, generalConfigurationId);
		s.setInt(9, param.getId());
		
		if(List.class.isAssignableFrom(param.getValue().getClass())){
			
			s.setObject(3, null);
			s.execute();

		    List<FeedbackParameter> children = (List<FeedbackParameter>)param.getValue();
			for (FeedbackParameter childParam : children){
				if(childParam.getId() == null)
				{
					InsertParameter(childParam, generalConfigurationId, mechanismId, param.getId(), con);
				}else{
					UpdateParameter(childParam, generalConfigurationId, mechanismId, param.getId(), con);		
				}
			}
			
		}else{
			s.setObject(3, param.getValue());
			s.execute();
		}
	}
	*/
	
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
}
