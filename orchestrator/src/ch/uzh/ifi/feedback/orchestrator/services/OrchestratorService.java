package ch.uzh.ifi.feedback.orchestrator.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Provider;

import ch.uzh.ifi.feedback.library.rest.service.IDbService;
import ch.uzh.ifi.feedback.library.rest.service.ServiceBase;
import ch.uzh.ifi.feedback.library.transaction.DbResultParser;
import ch.uzh.ifi.feedback.library.transaction.TransactionManager;
import ch.uzh.ifi.feedback.orchestrator.model.IOrchestratorItem;
import javassist.NotFoundException;
import sun.nio.cs.HistoricallyNamedCharset;

import static java.util.Arrays.asList;

public class OrchestratorService<T extends IOrchestratorItem<T>> extends ServiceBase<T> {

	private String mainTableName;
	private String mainTableKey;
	protected Provider<Timestamp> timestampProvider;
	
	public OrchestratorService(
			DbResultParser<T> resultParser, 
			Class<T> serviceClass,
			String mainTableName,
			String dbName,
			Provider<Timestamp> timestampProvider) 
	{
		super(resultParser, serviceClass, mainTableName + "_history", dbName);
		
		this.mainTableName = mainTableName;
		this.timestampProvider = timestampProvider;
		this.mainTableKey = mainTableName + "_id";
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
		List<Object> list = asList(id, timestampProvider.get(), timestampProvider.get(), timestampProvider.get());
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
	public List<T> GetWhere(List<Object> values, String... conditions) throws SQLException {
		List<Object> newValues = new ArrayList<>();
		newValues.addAll(asList(timestampProvider.get(), timestampProvider.get(), timestampProvider.get()));
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
	public List<T> GetAll() throws SQLException 
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
		String condition = 	"abs(timestampdiff(MICROSECOND, t.created_at, ?)) = "
							+ "("
							+ "SELECT min(abs(timestampdiff(MICROSECOND, t2.created_at, ?))) "
							+ "FROM %s.%s as t2 "
							+ "WHERE t.%s = t2.%s AND t.created_at <= ?"
							+ ") ";
		condition = String.format(condition, dbName, tableName, mainTableKey, mainTableKey);
		
		return condition;
	}
}
