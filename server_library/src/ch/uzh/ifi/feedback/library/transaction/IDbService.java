package ch.uzh.ifi.feedback.library.transaction;

public interface IDbService<T> {
	
	void Save(T object);
	
	void Delete(T object);
	
	void Update(T Object);
	
	T Read();
	
}
