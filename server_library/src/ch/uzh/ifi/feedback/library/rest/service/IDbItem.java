package ch.uzh.ifi.feedback.library.rest.service;

public interface IDbItem<T> {
	
	Integer getId();
	
	void setId(Integer id);
	
	boolean hasChanges();
	
	T Merge(T original);
}
