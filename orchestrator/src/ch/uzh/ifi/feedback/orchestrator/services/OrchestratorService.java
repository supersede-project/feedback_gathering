package ch.uzh.ifi.feedback.orchestrator.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.orchestrator.model.IOrchestratorItem;
import javassist.NotFoundException;
import sun.nio.cs.HistoricallyNamedCharset;

import static java.util.Arrays.asList;

public class OrchestratorService<T extends IOrchestratorItem<T>> extends ServiceBase<T> implements IOrchestratorService<T>{

	private Timestamp selectedTimestamp;
	private List<IOrchestratorService<?>> childServices;
	private String mainTableName;
	private String mainTableKey;
	
	public OrchestratorService(
			DbResultParser<T> resultParser, 
			Class<T> serviceClass,
			String mainTableName,
			String dbName,
			IOrchestratorService<?>... services) 
	{
		super(resultParser, serviceClass, mainTableName + "_history", dbName, services);
		
		this.mainTableName = mainTableName;
		this.mainTableKey = mainTableName + "_id";
		this.selectedTimestamp = Timestamp.from(Instant.now());
		
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
		
		String statement = String.format("INSERT INTO %s.%s (id) values (null) ;", this.dbName, this.mainTableName);
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
		String idCondition = String.format("%s = ?", mainTableKey);
		List<Object> list = asList(id, selectedTimestamp, selectedTimestamp);
		List<T> result = super.GetWhere(list, idCondition, getTimeCondition());
		
		return result.size() > 0 ? result.get(0) : null;
	}
	
	@Override
	public void Update(Connection con, T object) throws SQLException, NotFoundException, UnsupportedOperationException {
		super.Insert(con, object);
	}
	
	@Override
	public List<T> GetWhere(List<Object> values, String... conditions) throws SQLException {
		List<Object> newValues = new ArrayList<>();
		newValues.addAll(asList(selectedTimestamp, selectedTimestamp));
		newValues.addAll(values);
		
		List<String> conds = new ArrayList<>();
		conds.add(getTimeCondition());
		for(String cond : conditions)
		{
			conds.add(cond);
		}
		
		return super.GetWhere(newValues, conds.toArray(new String[0]));
	}
	
	@Override
	public List<T> GetAll() throws SQLException, NotFoundException 
	{
		/*
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
		*/
		List<T> result = GetWhere(asList(selectedTimestamp, selectedTimestamp), getTimeCondition());
		
		return result;
	}
	
	private String getTimeCondition()
	{
		String condition = 	"timestampdiff(SECOND, created_at, ?) = "
							+ "("
							+ "SELECT min(timestampdiff(SECOND, created_at, ?)) "
							+ "FROM %s.%s"
							+ ")";
		condition = String.format(condition, dbName, tableName);
		
		return condition;
	}
}
