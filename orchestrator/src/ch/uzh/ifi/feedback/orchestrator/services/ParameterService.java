package ch.uzh.ifi.feedback.orchestrator.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import ch.uzh.ifi.feedback.library.rest.service.IDbService;
import ch.uzh.ifi.feedback.library.rest.service.ServiceBase;
import ch.uzh.ifi.feedback.library.transaction.DatabaseConfiguration;
import ch.uzh.ifi.feedback.library.transaction.IDatabaseConfiguration;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackMechanism;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackParameter;
import ch.uzh.ifi.feedback.orchestrator.transaction.OrchestratorDatabaseConfiguration;
import javassist.NotFoundException;
import static java.util.Arrays.asList;

@Singleton
public class ParameterService extends OrchestratorService<FeedbackParameter>{
	
	private Provider<String> languageProvider;

	@Inject
	public ParameterService(
			ParameterResultParser resultParser, 
			OrchestratorDatabaseConfiguration config,
			@Named("timestamp") Provider<Timestamp> timeStampProvider,
			@Named("language") Provider<String> languageProvider) 
	{
		super(resultParser, FeedbackParameter.class, "parameters", config.getDatabase(), timeStampProvider);
		this.languageProvider = languageProvider;
	}
	
	@Override
	public List<FeedbackParameter> GetWhere(List<Object> values, String... conditions)
			throws SQLException {
		
		List<FeedbackParameter> params = super.GetWhere(values, conditions);
		
		Map<Integer, List<FeedbackParameter>> childMap = new HashMap<>();
		Map<FeedbackParameter, Integer> parameterMap = new HashMap<>();

		params = params.stream().filter(p -> p.getLanguage().equals(languageProvider.get())).collect(Collectors.toList());
		
		List<FeedbackParameter> rootParams = GetRootParams(params, parameterMap, childMap);
	    
	    return setParametersRecursive(rootParams, parameterMap, childMap);
	}
	
	@Override
	public List<FeedbackParameter> GetAll() throws SQLException
	{
		return GetWhere(asList());
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
		List<FeedbackParameter> params = GetWhere(asList(id), "parameters_id = ?");
		return params.size() > 0 ? params.get(0) : null;
	}
	
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
		}else{
			super.Update(con, param);
		}
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
}
