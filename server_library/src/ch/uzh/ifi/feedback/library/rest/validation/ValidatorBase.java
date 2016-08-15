package ch.uzh.ifi.feedback.library.rest.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.feedback.library.rest.Service.IDbItem;
import ch.uzh.ifi.feedback.library.rest.Service.ItemBase;
import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.transaction.TransactionManager;
import javassist.NotFoundException;

import static java.util.Arrays.asList;

public class ValidatorBase<T extends IDbItem<T>> {
	
	private Class<?> clazz;
	protected ServiceBase<T> dbService;
	
	public ValidatorBase(Class<?> clazz, ServiceBase<T> service)
	{
		this.clazz = clazz;
		this.dbService = service;
	}
	
	public ValidationResult Validate(T object) throws Exception
	{
		ValidationResult result = new ValidationResult();
		
		if(object.getId() != null)
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
						CheckUnique(f, o, result);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		return result;
	}
	
	public T Merge(T object) throws Exception
	{
		T oldObject = dbService.GetById(object.getId());
		object = object.Merge(oldObject);
		return object;
	}
	
	protected void CheckNotNull(Field f, Object o, ValidationResult result)
	{
		if (o == null)
		{
			result.setHasErrors(true);
			ValidationError error = new ValidationError(f.getName(), "Not null: Field must not be null");
			result.GetValidationErrors().add(error);
		}
	}
	
	protected void CheckId(T object, ValidationResult result) throws Exception
	{
		if(object.getId() != null)
		{
			try{
				dbService.GetById(object.getId());
			}
			catch(NotFoundException ex)
			{
				result.setHasErrors(true);
				ValidationError error = new ValidationError("id", "not found: Id was not found");
				result.GetValidationErrors().add(error);
			}
		}
	}
	
	protected void CheckUnique(Field f, Object o, ValidationResult result) throws Exception
	{
		String fieldName = f.getName();
		if(f.isAnnotationPresent(DbAttribute.class))
			fieldName = f.getAnnotation(DbAttribute.class).value();
		
		List<T> dbResult = dbService.GetWhereEquals(asList(fieldName), asList(o));
		if(dbResult.size() > 1)
		{
			result.setHasErrors(true);
			ValidationError error = new ValidationError(f.getName(), "unique: field must be unique");
			result.GetValidationErrors().add(error);
		}
	}
}
