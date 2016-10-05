package ch.uzh.ifi.feedback.library.rest.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.feedback.library.rest.annotations.DbIgnore;
import ch.uzh.ifi.feedback.library.rest.annotations.Id;

public abstract class ItemBase<T> implements IDbItem<T> {

	@DbIgnore
	protected transient boolean hasChanges;
	
	@Override
	public abstract Integer getId();

	@Override
	public abstract void setId(Integer id);
	
	@Override
	public T Merge(T original) {
		Class<?> clazz = this.getClass();
		
		hasChanges = false;
		for(Field f : GetFields(clazz, new ArrayList<>()))
		{
			f.setAccessible(true);
			try {
				Object newValue = f.get(this);
				Object oldValue = f.get(original);
				if (newValue == null && f.getName() != "createdAt")
				{
					f.set(this, oldValue);
					
				}else if(f.getName() != "createdAt" && !newValue.equals(oldValue))
				{
					hasChanges = true;
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return (T)this;
	}
	
	public boolean hasChanges(){
		return this.hasChanges;
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
