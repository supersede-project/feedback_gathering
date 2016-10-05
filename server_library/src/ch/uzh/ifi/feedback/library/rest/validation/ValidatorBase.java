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
	
	protected void CheckNotNull(Field f, Object o, ValidationResult result)
	{
		if (o == null)
		{
			result.setHasErrors(true);
			ValidationError error = new ValidationError(f.getName(), o, "Not null: Field must not be null");
			result.GetValidationErrors().add(error);
		}
	}
	
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
	
	protected void CheckUnique(Field f, Object value, T object, ValidationResult result) throws Exception
	{
		String fieldName = f.getName();
		if(f.isAnnotationPresent(DbAttribute.class))
			fieldName = f.getAnnotation(DbAttribute.class).value();
		
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
			ValidationError error = new ValidationError(f.getName(), value, "unique: field must be unique");
			result.GetValidationErrors().add(error);
		}
	}
}
