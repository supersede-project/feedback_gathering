package ch.uzh.ifi.feedback.orchestrator.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.Service.IDbService;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackMechanism;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackParameter;
import javassist.NotFoundException;

public class MechanismService implements IMechanismService {
	
	private ParameterService parameterService;
	
	@Inject
	public MechanismService(ParameterService parameterService){
		this.parameterService = parameterService;
	}
	
	public void InsertFor(Connection con, FeedbackMechanism mechanism, String foreignKeyName, int configurationId) throws SQLException, NotFoundException
	{
	    PreparedStatement s1 = con.prepareStatement(
	    		"INSERT INTO feedback_orchestrator.mechanisms (`name`) VALUES (?) ;", PreparedStatement.RETURN_GENERATED_KEYS);
	    
	    s1.setString(1, mechanism.getType());
	    s1.execute();
	    ResultSet keys = s1.getGeneratedKeys();
	    keys.next();
	    int mechanismId = keys.getInt(1);
	    
	    
	    PreparedStatement s = con.prepareStatement(
	    		"INSERT INTO feedback_orchestrator.configurations_mechanisms "
	    		+ "(configuration_id, mechanism_id, active, `order`, can_be_activated) "
	    		+ "VALUES (?, ?, ?, ?, ?) ;");
	    
	    s.setInt(1, configurationId);
	    s.setInt(2, mechanismId);
	    s.setBoolean(3, mechanism.isActive());
	    s.setInt(4, mechanism.getOrder());
	    s.setBoolean(5, mechanism.isCanBeActivated());
	    s.execute();
	    
	    for(FeedbackParameter param : mechanism.getParameters())
	    {
	    	parameterService.InsertFor(con, param, "mechanism_id", mechanismId);
	    }	
	}
	
	public void UpdateFor(Connection con, FeedbackMechanism mechanism, String foreignKeyName, int configurationId) throws SQLException, NotFoundException
	{
	    PreparedStatement s1 = con.prepareStatement(
	    		  "UPDATE feedback_orchestrator.mechanisms "
	    		+ "SET `name` = IFNULL(?, `name`), updated_at = now() "
	    		+ "WHERE id = ? ;");
	    
	    s1.setString(1, mechanism.getType());
	    s1.setInt(6, mechanism.getId());
	    
	    PreparedStatement s2 = con.prepareStatement(
	    		  "UPDATE feedback_orchestrator.configurations_mechanisms "
	    		+ "SET active = IFNULL(?, active), `order` = IFNULL(?, `order`), can_be_activated = IFNULL(?, can_be_activated) "
	    		+ "WHERE mechanism_id = ? AND configuration_id = ?;");
	    
	    s2.setObject(1, mechanism.isActive());
	    s2.setObject(2, mechanism.getOrder());
	    s2.setObject(3, mechanism.isCanBeActivated());
	    s2.setInt(4, mechanism.getId());
	    s2.setInt(5, configurationId);
	    
	    s2.execute();
	    
	    for(FeedbackParameter param : mechanism.getParameters())
	    {
	    	if(param.getId() == null){
		    	parameterService.InsertFor(con, param, "mechanism_id", mechanism.getId());
	    	}else{
	    		parameterService.UpdateFor(con, param, "mechanism_id", mechanism.getId());
	    	}
	    }
	}
	
	public List<FeedbackMechanism> GetAll(Connection con) throws SQLException, NotFoundException
	{
	    PreparedStatement s = con.prepareStatement(

	    		  "SELECT m.id, m.name as mechanism_name, cm.order, cm.active, cm.can_be_activated "
	    		 + "FROM feedback_orchestrator.mechanisms as m "
	    		+ "JOIN feedback_orchestrator.configurations_mechanisms as cm ON cm.mechanism_id = m.id ;"    		
	    		);

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
	    	mechanism.setParameters(parameterService.GetAllFor(con, "mechanism_id", result.getInt("id")));
	    	mechanisms.add(mechanism);
	    }
	    
	    return mechanisms;
	}
	
	public List<FeedbackMechanism> GetAllFor(Connection con, String foreignKeyName, int configurationId) throws SQLException, NotFoundException
	{
	    PreparedStatement s = con.prepareStatement(

	    		  "SELECT m.id, m.name as mechanism_name, cm.order, cm.active, cm.can_be_activated "
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
	    	mechanism.setType(result.getString("mechanism_name"));
	    	mechanism.setActive(result.getBoolean("active"));
	    	mechanism.setCanBeActivated(result.getBoolean("can_be_activated"));
	    	mechanism.setOrder(result.getInt("order"));
	    	mechanism.setId(result.getInt("id"));
	    	mechanism.setParameters(parameterService.GetAllFor(con, "mechanism_id", result.getInt("id")));
	    	mechanisms.add(mechanism);
	    }
	    
	    return mechanisms;
	}
	
	public FeedbackMechanism GetById(Connection con, int mechanismId) throws SQLException, NotFoundException
	{
	    PreparedStatement s = con.prepareStatement(

	    		  "SELECT m.id, m.name as mechanism_name "
	    		+ "FROM feedback_orchestrator.mechanisms as m "
	    		+ "WHERE m.id = ? ;"		    		
	    		);

	    s.setInt(1, mechanismId);
	    ResultSet result = s.executeQuery();
	    
	    if(!result.next())
	    	throw new NotFoundException("mechanism with id: " + mechanismId + "does not exist");
	    
    	FeedbackMechanism mechanism = new FeedbackMechanism();
    	mechanism.setType(result.getString("mechanism_name"));
    	mechanism.setId(result.getInt("id"));
    	mechanism.setParameters(parameterService.GetAllFor(con, "mechanism_id", mechanismId));
   
	    return mechanism;
	}
}
