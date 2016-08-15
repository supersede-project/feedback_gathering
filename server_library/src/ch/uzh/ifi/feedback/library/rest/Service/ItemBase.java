package ch.uzh.ifi.feedback.library.rest.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.feedback.library.rest.validation.Id;

public abstract class ItemBase<T> implements IDbItem<T> {

	@Id
	private Integer id;
	
	@Override
	public Integer getId() {
		
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public T Merge(T original) {
		Class<?> clazz = this.getClass();
		for(Field f : GetFields(clazz, new ArrayList<>()))
		{
			f.setAccessible(true);
			try {
				Object newValue = f.get(this);
				Object oldValue = f.get(original);
				if (newValue == null)
				{
					f.set(this, oldValue);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return (T)this;
	}
	
	  public static List<Field> GetFields(Class<?> clazz, List<Field> fields) 
	  {
	      for(Field f : clazz.getDeclaredFields())
	      {
	    	  f.setAccessible(true);
	    	  fields.add(f);
	      }
	      Class<?> superClass = clazz.getSuperclass();
	      if (superClass == null) {
	        return fields;
	      } else {
	        return GetFields(superClass, fields);
	      }
	  }
}
