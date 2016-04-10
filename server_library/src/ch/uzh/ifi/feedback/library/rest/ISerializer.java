package ch.uzh.ifi.feedback.library.rest;

public interface ISerializer<T> {
	String Serialize(T object);
}
