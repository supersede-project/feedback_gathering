package ch.uzh.ifi.feedback.library.rest.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.feedback.library.rest.annotations.DbIgnore;
import ch.uzh.ifi.feedback.library.rest.annotations.Id;

/**
 * This class is the base class for all model classes in the orchestrator and repository.
 * It provides the merge functionality.
 *
 * @param <T> the model class
 * @author Florian Schüpfer
 * @version 1.0
 * @since   2016-11-14
 */
public abstract class ItemBase<T> implements IDbItem<T> {

	@DbIgnore
	protected transient boolean hasChanges;
	
	@Override
	public abstract Integer getId();

	@Override
	public abstract void setId(Integer id);
	
	/**
	 * This method merges an instance of this class with an older instance of this class.
	 * fields that are null are set to the old value. Fields that don't have the same value are not changed.
	 * If new values are present, the value of hasChanges is set to true.
	 *
	 * @param original the object to merge with
	 * @return the result of the merge operation
	 */
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

	/**
	 * This method collects all field metadata information for an instance of IDITem<T>.
	 * The method is called recursively for the whole inheritance hierarchy of an instance, such that
	 * inherited fields are alos included.
	 *
	 * @param clazz the class for which the field information has to be extracted
	 * @param fields list of fields collected so far
	 * @return all fields of this class and its superclasses
	 */
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
