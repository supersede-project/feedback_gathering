package ch.uzh.ifi.feedback.library.transaction;

import java.util.List;

public interface IDbService<T> {
	
	void Create(T object);
	
	void Delete(T object);
	
	void Update(T Object);
	
	T Read(int id);
}
