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
import ch.uzh.ifi.feedback.library.transaction.TransactionManager;
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
		
		if(result.size() == 0)
			throw new NotFoundException("Object with ID '" + id + "' not found!");
		
		return result.get(0);
	}
	
	@Override
	public void Update(Connection con, T object) throws SQLException, NotFoundException, UnsupportedOperationException {
		if(object.hasChanges())
			super.Insert(con, object);
	}
	
	@Override
	public List<T> GetWhere(List<Object> values, String... conditions) throws SQLException, NotFoundException {
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
		return GetWhere(asList());
	}
	
	@Override
	public boolean CheckId(int id) throws SQLException {
		
		Connection con = TransactionManager.createDatabaseConnection();
		
		String statement = String.format("SELECT * FROM %s.%s as t WHERE t.id = ? ;", dbName, mainTableName);
		PreparedStatement s = con.prepareStatement(statement);
		s.setInt(1, id);
		
		ResultSet result = s.executeQuery();
		boolean res = result.next();
		
		con.close();
		
		if(!res)
			return false;
		
		return true;
	}
	
	protected String getTimeCondition()
	{
		String condition = 	"abs(timestampdiff(SECOND, created_at, ?)) = "
							+ "("
							+ "SELECT min(abs(timestampdiff(SECOND, created_at, ?))) "
							+ "FROM %s.%s as t2 "
							+ "WHERE t.%s = t2.%s"
							+ ") ";
		condition = String.format(condition, dbName, tableName, mainTableKey, mainTableKey);
		
		return condition;
	}
}
