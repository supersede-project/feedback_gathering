package ch.uzh.ifi.feedback.orchestrator.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.library.rest.Service.IDbItem;
import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.library.transaction.TransactionManager;
import javassist.NotFoundException;

public class OrchestratorService<T extends IDbItem<?>> extends ServiceBase<T> implements IOrchestratorService<T>{

	private Timestamp selectedTimestamp;
	private List<IOrchestratorService<?>> childServices;
	private String mainTableName;
	private String mainTableKey;
	
	public OrchestratorService(
			DbResultParser<T> resultParser, 
			Class<T> serviceClass,
			String mainTableName,
			String historyTableName, 
			String dbName,
			IOrchestratorService<?>[] services) 
	{
		super(resultParser, serviceClass, historyTableName, dbName, services);
		
		this.mainTableName = mainTableName;
		this.mainTableKey = mainTableName + "_id";
		childServices = new ArrayList<IOrchestratorService<?>>();
		for(IOrchestratorService<?> service : services)
		{
			childServices.add(service);
		}
	}

	@Override
	public void setTimestamp(Timestamp timestamp) {
		this.selectedTimestamp = timestamp;
		childServices.stream().forEach(s -> s.setTimestamp(timestamp));
	}

	@Override
	public Timestamp getTimestamp() {
		return this.selectedTimestamp;
	}
	
	@Override
	public int Insert(Connection con, T object) throws SQLException, NotFoundException, UnsupportedOperationException {
		
		String statement = String.format("INSERT INTO %s.%s ;", this.dbName, this.mainTableName);
		PreparedStatement s = con.prepareStatement(statement, PreparedStatement.RETURN_GENERATED_KEYS);
		s.execute();
		ResultSet rs = s.getGeneratedKeys();
		rs.next();
		int key = rs.getInt(1);
		object.setId(key);
		
		super.Insert(con, object);
		
		return key;
	}
	
	@Override
	public T GetById(int id) throws SQLException, NotFoundException 
	{
		Connection con = TransactionManager.createDatabaseConnection();
		Timestamp ts = this.selectedTimestamp;
		String statement = String.format(
				  "SELECT * FROM %s.%s "
				+ "WHERE %s = ? "
				+ "AND abs(TIMEDIFF(created_at, ?)) = "
				+ "("
					+ "SELECT min(abs(TIMEDIFF(created_at, ?))) "
					+ "FROM %s.%s"
				+ ") ;", dbName, mainTableName, mainTableKey, dbName, mainTableName);
		
		PreparedStatement s = con.prepareStatement(statement);
		s.setInt(1, id);
		s.setTimestamp(2, ts);
		s.setTimestamp(3, ts);
		ResultSet result = s.executeQuery();
		
		T instance = null;
		try {
			instance = serviceClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		resultParser.SetFields(instance, result);
		con.close();
		
		return instance;
	}
	
	@Override
	public List<T> GetAll() throws SQLException, NotFoundException 
	{
		Connection con = TransactionManager.createDatabaseConnection();
		Timestamp ts = this.selectedTimestamp;
		String statement = String.format(
				  "SELECT * FROM %s.%s "
				+ "WHERE abs(TIMEDIFF(created_at, ?)) = "
				+ "("
					+ "SELECT min(abs(TIMEDIFF(created_at, ?))) "
					+ "FROM %s.%s"
				+ ") ;", dbName, mainTableName, mainTableKey, dbName, mainTableName);
		
		PreparedStatement s = con.prepareStatement(statement);
		s.setTimestamp(1, ts);
		s.setTimestamp(2, ts);
		ResultSet result = s.executeQuery();
		
		List<T> resultList = getList(result);
		con.close();
		
		return resultList;
	}

}
