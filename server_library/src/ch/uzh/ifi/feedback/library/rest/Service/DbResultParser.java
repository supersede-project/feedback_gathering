package ch.uzh.ifi.feedback.library.rest.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Timestamp;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public abstract class DbResultParser<T> {
	
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
			fields.put(f.getName().toLowerCase(), f);
		}
	}
	
	public void SetFields(T obj, ResultSet rs) throws SQLException
	{
		ResultSetMetaData metadata = rs.getMetaData();
		for(int i=0; i<metadata.getColumnCount(); i++)
		{
			String columnName = metadata.getColumnLabel(i + 1);
			columnName = columnName.toLowerCase().replace("_", "");
			try {
				if(fields.containsKey(columnName))
				{
					Field f = fields.get(columnName);
					if(f.getType().isEnum()){
						Method parser = f.getType().getMethod("valueOf", String.class);
						Object value = parser.invoke(null, rs.getObject(i + 1));
						fields.get(columnName).set(obj, value);
					}
					else
					{
						fields.get(columnName).set(obj, rs.getObject(i + 1));
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
	}

}
