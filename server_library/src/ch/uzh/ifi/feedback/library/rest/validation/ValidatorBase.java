package ch.uzh.ifi.feedback.library.rest.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.annotations.NotNull;
import ch.uzh.ifi.feedback.library.rest.annotations.Unique;
import ch.uzh.ifi.feedback.library.rest.service.IDbItem;
import ch.uzh.ifi.feedback.library.rest.service.IDbService;
import ch.uzh.ifi.feedback.library.rest.service.ItemBase;
import ch.uzh.ifi.feedback.library.rest.service.ServiceBase;
import javassist.NotFoundException;

import static java.util.Arrays.asList;

/**
 * This class provides basic functionality for validating instances of IDbItem.
 * 
 * @param <T> the model class
 * @author Florian Schüpfer
 * @version 1.0
 * @since   2016-11-14
 */
public class ValidatorBase<T extends IDbItem> implements IValidator<T> {
	
	private Class<?> clazz;
	protected IDbService<T> dbService;
	protected ValidationSerializer serializer;
	
	public ValidatorBase(Class<?> clazz, IDbService<T> service, ValidationSerializer serializer)
	{
		this.clazz = clazz;
		this.dbService = service;
		this.serializer = serializer;
	}
	
	/**
	 * This method validates each field on an instance based on annotations found on that field.
	 * 
	 * @param object the object to validate
	 * @return the ValidationResult containing the validation errors
	 */
	public ValidationResult Validate(T object) throws Exception
	{
		ValidationResult result = new ValidationResult();
		
		if(object.getId() != null && object.hasChanges())
			CheckId(object, result);

		for(Field f : ItemBase.GetFields(clazz, new ArrayList<>()))
		{
			f.setAccessible(true);
			for(Annotation a : f.getAnnotations())
			{
				if (a instanceof NotNull)
				{
					try {
						Object o = f.get(object);
						CheckNotNull(f, o, result);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				if(a instanceof Unique)
				{
					try {
						Object o = f.get(object);
						CheckUnique(f, o, object, result);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		return result;
	}
	
	/**
	 * This method merges an object with its old version stored in the database.
	 * 
	 * @param object the object to merge
	 * @return the merged object
	 */
	public T Merge(T object) throws Exception
	{
		if(object.getId() == null)
			throw new UnsupportedOperationException("ID on object must be set for update");
		
		boolean res = dbService.CheckId(object.getId());
		if(!res)
			throw new NotFoundException("Object with ID '" + object.getId() + "' not found");
		
		T oldObject = dbService.GetById(object.getId());
		object = (T) object.Merge(oldObject);
		return object;
	}
	
	/**
	 * This method checks that a field is not null and stores the result in the ValidationResult object.
	 * 
	 * @param field the field to validate
	 * @param object the object to validate
	 * @param result the ValidationResult object
	 */
	protected void CheckNotNull(Field field, Object object, ValidationResult result)
	{
		if (object == null)
		{
			result.setHasErrors(true);
			ValidationError error = new ValidationError(field.getName(), object, "Not null: Field must not be null");
			result.GetValidationErrors().add(error);
		}
	}
	
	/**
	 * This method checks that an object with a specific id exists in the database and stores the result in the ValidationResult object.
	 * 
	 * @param object the object to validate
	 * @param result the ValidationResult object
	 */
	protected void CheckId(T object, ValidationResult result) throws Exception
	{
		if(object.getId() != null)
		{
			boolean res = dbService.CheckId(object.getId());
			
			if(!res)
			{
				result.setHasErrors(true);
				ValidationError error = new ValidationError("id", object.getId(), "not found: Id was not found");
				result.GetValidationErrors().add(error);
			}
		}
	}
	
	/**
	 * This method checks that the value of a field is unique in the database.
	 * 
	 * @param field the field to validate
	 * @param value the value for the field that has to be checked for uniqueness
	 * @param object the object to validate
	 * @param result the ValidationResult object
	 */
	protected void CheckUnique(Field field, Object value, T object, ValidationResult result) throws Exception
	{
		String fieldName = field.getName();
		if(field.isAnnotationPresent(DbAttribute.class))
			fieldName = field.getAnnotation(DbAttribute.class).value();
		
		List<T> dbResult = dbService.GetWhere(asList(value), fieldName + " = ?");
		if(dbResult.size() > 0)
		{
			if(dbResult.size() == 1 && object.getId() != null)
			{
				T stored = dbResult.get(0);
				if(stored.getId().equals(object.getId()))
					return;
			}
			
			result.setHasErrors(true);
			ValidationError error = new ValidationError(field.getName(), value, "unique: field must be unique");
			result.GetValidationErrors().add(error);
		}
	}
}
