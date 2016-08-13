package ch.uzh.ifi.feedback.library.rest.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import ch.uzh.ifi.feedback.library.rest.Service.IDbItem;
import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.library.transaction.TransactionManager;
import javassist.NotFoundException;

public class ValidatorBase<T extends IDbItem> {
	
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
		CheckId(object, result);
		for(Field f : clazz.getDeclaredFields())
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
			}
		}
		
		return result;
	}
	
	private void CheckNotNull(Field f, Object o, ValidationResult result)
	{
		if (o == null)
		{
			result.setHasErrors(true);
			ValidationError error = new ValidationError(f.getName(), "Not null: Field must not be null");
			result.GetValidationErrors().add(error);
		}
	}
	
	private void CheckId(T object, ValidationResult result) throws Exception
	{
		if(object.getId() != null)
		{
			try{
				dbService.GetById(TransactionManager.createDatabaseConnection(), object.getId());
			}
			catch(NotFoundException ex)
			{
				result.setHasErrors(true);
				ValidationError error = new ValidationError("id", "not found: Id was not found");
				result.GetValidationErrors().add(error);
			}
		}
	}
}
