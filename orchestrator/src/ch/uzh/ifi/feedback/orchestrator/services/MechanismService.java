package ch.uzh.ifi.feedback.orchestrator.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.mysql.jdbc.Statement;

import ch.uzh.ifi.feedback.library.rest.Service.DatabaseConfiguration;
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
	public MechanismService(
			ParameterService parameterService, 
			MechanismResultParser resultParser,
			DatabaseConfiguration config)
	{
		super(
				resultParser, 
				FeedbackMechanism.class, 
				"mechanisms",
				config.getOrchestratorDb(), 
				parameterService);
		
		this.parameterService = parameterService;
	}
	
	private int InsertNewMechanism(Connection con) throws SQLException
	{
		String stmt = String.format("INSERT INTO %s.mechanisms (id) VALUES (NULL) ;", this.dbName);
	    PreparedStatement s1 = con.prepareStatement(stmt, PreparedStatement.RETURN_GENERATED_KEYS);
	    
	    s1.execute();
	    ResultSet keys = s1.getGeneratedKeys();
	    keys.next();
	    int mechanismId = keys.getInt(1);
	    
	    return mechanismId;
	}
	
	private void InsertMechanismHistory(Connection con, FeedbackMechanism mechanism, int mechanismId)
			throws SQLException {
		
		String stmt = String.format("INSERT INTO %s.mechanisms_history (`name`, `mechanisms_id`) VALUES (?, ?) ;", this.dbName);
		PreparedStatement s2 = con.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
	    
	    s2.setString(1, mechanism.getType());
	    s2.setInt(2, mechanismId);
	    s2.execute();
	    ResultSet keys = s2.getGeneratedKeys();
	    keys.next();
	    int mechanismHistoryId = keys.getInt(1);
	    
	    stmt = String.format(
	    		"INSERT INTO %s.configurations_mechanisms_history "
	    		+ "(configurations_id, mechanisms_history_id, active, `order`, can_be_activated) "
	    		+ "VALUES (?, ?, ?, ?, ?) ;", this.dbName);
	    
	    PreparedStatement s = con.prepareStatement(stmt);
	    
	    s.setInt(1, mechanism.getConfigurationsid());
	    s.setInt(2, mechanismHistoryId);
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
	    		parameterService.Insert(con, param);
	    	}else{
	    		parameterService.Update(con, param);
	    	}
	    }
	}

	@Override
	public List<FeedbackMechanism> GetAll() throws SQLException, NotFoundException
	{
	    return GetWhere(asList());
	}
	
	@Override
	public List<FeedbackMechanism> GetWhere(List<Object> values, String... conditions)
			throws SQLException, NotFoundException {
		
		Connection con = TransactionManager.createDatabaseConnection();
		
		String statement = String.format(
				"SELECT DISTINCT t.mechanisms_id, t.name, cm.order, cm.active, cm.can_be_activated, t.created_at "
	    		 + "FROM %s.mechanisms_history as t "
	    		 + "JOIN %s.configurations_mechanisms_history as cm ON cm.mechanisms_history_id = t.id ", this.dbName, this.dbName);
		
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
	
	@Override
	public FeedbackMechanism GetById(int mechanismId) throws SQLException, NotFoundException
	{
		FeedbackMechanism mechanism = super.GetById(mechanismId);
    	mechanism.setParameters(parameterService.GetWhere(asList(mechanism.getId()), "mechanisms_id = ?"));
	    return mechanism;
	}
}
