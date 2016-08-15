package ch.uzh.ifi.feedback.library.rest.Service;

public interface IDbItem<T> {
	Integer getId();
	void setId(Integer id);
	T Merge(T original);
}
