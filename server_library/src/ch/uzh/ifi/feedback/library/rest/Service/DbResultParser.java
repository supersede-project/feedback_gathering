package ch.uzh.ifi.feedback.library.rest.Service;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public abstract class DbResultParser {
	
	private Map<String, Field> fields;
	
	public DbResultParser(Class<?> clazz)
	{
		InitFields(clazz);
	}
	
	private void InitFields(Class<?> clazz)
	{
		fields = new HashMap<>();
		for (Field f : clazz.getDeclaredFields())
		{
			f.setAccessible(true);
			fields.put(f.getName(), f);
		}
	}
	
	public void SetFields(Object obj, ResultSet rs) throws SQLException
	{
		ResultSetMetaData metadata = rs.getMetaData();
		for(int i=0; i<metadata.getColumnCount(); i++)
		{
			String columnName = metadata.getColumnLabel(i + 1);
			try {
				if(fields.containsKey(columnName))
					fields.get(columnName).set(obj, rs.getObject(i + 1));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

}
