package ch.uzh.ifi.feedback.orchestrator.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.library.rest.Service.IDbService;
import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.library.transaction.TransactionManager;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackMechanism;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackParameter;
import javassist.NotFoundException;
import static java.util.Arrays.asList;

public class MechanismService extends OrchestratorService<FeedbackMechanism> {
	
	private ParameterService parameterService;
	
	@Inject
	public MechanismService(ParameterService parameterService, MechanismResultParser resultParser){
		super(
				resultParser, 
				FeedbackMechanism.class, 
				"mechanisms",
				"feedback_orchestrator", 
				parameterService);
		
		this.parameterService = parameterService;
	}
	
	private int InsertNewMechanism(Connection con) throws SQLException
	{
	    PreparedStatement s1 = con.prepareStatement(
	    		"INSERT INTO feedback_orchestrator.mechanisms (id) VALUES (NULL) ;", PreparedStatement.RETURN_GENERATED_KEYS);
	    
	    s1.execute();
	    ResultSet keys = s1.getGeneratedKeys();
	    keys.next();
	    int mechanismId = keys.getInt(1);
	    
	    return mechanismId;
	}
	
	private void InsertMechanismHistory(Connection con, FeedbackMechanism mechanism, int mechanismId)
			throws SQLException {
		PreparedStatement s2 = con.prepareStatement(
	    		"INSERT INTO feedback_orchestrator.mechanisms_history (`name`, `mechanisms_id`) VALUES (?, ?) ;");
	    
	    s2.setString(1, mechanism.getType());
	    s2.setInt(2, mechanismId);
	    s2.execute();
	    
	    PreparedStatement s = con.prepareStatement(
	    		"INSERT INTO feedback_orchestrator.configurations_mechanisms_history "
	    		+ "(configurations_id, mechanisms_id, active, `order`, can_be_activated) "
	    		+ "VALUES (?, ?, ?, ?, ?) ;");
	    
	    s.setInt(1, mechanism.getConfigurationsid());
	    s.setInt(2, mechanismId);
	    s.setBoolean(3, mechanism.isActive());
	    s.setInt(4, mechanism.getOrder());
	    s.setBoolean(5, mechanism.isCanBeActivated());
	    s.execute();
	}
	
	@Override
	public int Insert(Connection con, FeedbackMechanism mechanism)
			throws SQLException, NotFoundException, UnsupportedOperationException {
		
	    int mechanismId = InsertNewMechanism(con);
	    
	    InsertMechanismHistory(con, mechanism, mechanismId);
		
	    for(FeedbackParameter param : mechanism.getParameters())
	    {
	    	param.setMechanismId(mechanismId);
	    	parameterService.Insert(con, param);
	    	//parameterService.InsertFor(con, param, "mechanism_id", mechanismId);
	    }
	    
	    return mechanismId;
	}
	
	@Override
	public void Update(Connection con, FeedbackMechanism mechanism)
			throws SQLException, NotFoundException, UnsupportedOperationException {

		InsertMechanismHistory(con, mechanism, mechanism.getId());
		
	    for(FeedbackParameter param : mechanism.getParameters())
	    {
    		param.setMechanismId(mechanism.getId());
	    	if(param.getId() == null){
		    	//parameterService.InsertFor(con, param, "mechanism_id", mechanism.getId());
	    		parameterService.Insert(con, param);
	    	}else{
	    		//parameterService.UpdateFor(con, param, "mechanism_id", mechanism.getId());
	    		parameterService.Update(con, param);
	    	}
	    }
	}

	@Override
	public List<FeedbackMechanism> GetAll() throws SQLException, NotFoundException
	{
		/*
		Connection con = TransactionManager.createDatabaseConnection();
		
	    PreparedStatement s = con.prepareStatement(

	    		   "SELECT m.id, m.name, cm.order, cm.active, cm.can_be_activated "
	    		 + "FROM feedback_orchestrator.mechanisms as m "
	    		 + "JOIN feedback_orchestrator.configurations_mechanisms as cm ON cm.mechanism_id = m.id ;"    		
	    		);

	    ResultSet result = s.executeQuery();
	    
	    List<FeedbackMechanism> mechanisms = new ArrayList<>();
	    while(result.next())
	    {
	    	FeedbackMechanism mechanism = new FeedbackMechanism();
	    	
	    	resultParser.SetFields(mechanism, result);
	    	mechanism.setParameters(parameterService.GetWhereEquals(asList("mechanism_id"), asList(mechanism.getId())));
	    	mechanisms.add(mechanism);
	    }
	    
	    con.close();
	    */
		
	    return GetWhere(asList());
	}
	
	@Override
	public List<FeedbackMechanism> GetWhere(List<Object> values, String... conditions)
			throws SQLException, NotFoundException {
		
		Connection con = TransactionManager.createDatabaseConnection();
		String statement = 
				   "SELECT t.mechanisms_id, t.name, cm.order, cm.active, cm.can_be_activated, t.created_at "
	    		 + "FROM feedback_orchestrator.mechanisms_history as t "
	    		 + "JOIN feedback_orchestrator.configurations_mechanisms_history as cm ON cm.mechanisms_id = t.mechanisms_id ";
		
		statement += "WHERE " + getTimeCondition();
		
		for(int i=0; i<conditions.length; i++)
		{
			statement += "AND %s ";
		}
		statement += ";";
		statement = String.format(statement, (Object[])conditions);
		
		PreparedStatement s = con.prepareStatement(statement);
		
		s.setObject(1, getTimestamp());
		s.setObject(2, getTimestamp());
		
		for(int i=0; i<values.size();i++)
		{
			s.setObject(i+3, values.get(i));
		}
		ResultSet result = s.executeQuery();
	
		List<FeedbackMechanism> resultList = getList(result);
		for(FeedbackMechanism m : resultList)
		{
			m.setParameters(parameterService.GetWhere(asList(m.getId()), "mechanisms_id = ?"));
		}

		con.close();
		
		return resultList;
	}
	
	/*
	@Override
	public List<FeedbackMechanism> GetAllFor(String foreignKeyName, int configurationId) throws SQLException, NotFoundException
	{
		Connection con = TransactionManager.createDatabaseConnection();
		
	    PreparedStatement s = con.prepareStatement(

	    		  "SELECT m.id, m.name, cm.order, cm.active, cm.can_be_activated "
	    		+ "FROM feedback_orchestrator.mechanisms as m "
	    		+ "JOIN feedback_orchestrator.configurations_mechanisms as cm "
	    		+ "WHERE cm.mechanism_id = m.id AND cm.configuration_id = ? ;"		    		
	    		);

	    s.setInt(1, configurationId);
	    ResultSet result = s.executeQuery();
	    
	    List<FeedbackMechanism> mechanisms = new ArrayList<>();
	    while(result.next())
	    {
	    	FeedbackMechanism mechanism = new FeedbackMechanism();
	    	resultParser.SetFields(mechanism, result);
	    	mechanism.setParameters(parameterService.GetWhereEquals(asList("mechanism_id"), asList(mechanism.getId())));
	    	mechanisms.add(mechanism);
	    }
	    
	    con.close();
	    return mechanisms;
	}
	*/
	@Override
	public FeedbackMechanism GetById(int mechanismId) throws SQLException, NotFoundException
	{
	/*	
	    PreparedStatement s = con.prepareStatement(

	    		  "SELECT m.id, m.name "
	    		+ "FROM feedback_orchestrator.mechanisms as m "
	    		+ "WHERE m.id = ? ;"		    		
	    		);

	    s.setInt(1, mechanismId);
	    ResultSet result = s.executeQuery();
	    
	    if(!result.next())
		
    	FeedbackMechanism mechanism = new FeedbackMechanism();
    	resultParser.SetFields(mechanism, result);
    		    	throw new NotFoundException("mechanism with id: " + mechanismId + "does not exist");
	    */
		
		FeedbackMechanism mechanism = super.GetById(mechanismId);
    	mechanism.setParameters(parameterService.GetWhereEquals(asList("mechanisms_id"), asList(mechanism.getId())));
	    return mechanism;
	}
}
