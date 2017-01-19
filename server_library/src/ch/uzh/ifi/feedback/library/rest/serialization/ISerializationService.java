package ch.uzh.ifi.feedback.library.rest.serialization;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

public interface ISerializationService<T> {
	
	String Serialize(T object);
	T Deserialize(HttpServletRequest request);
}
