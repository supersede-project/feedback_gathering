package ch.uzh.ifi.feedback.library.rest;

public interface IDeserializer<T> {
	T Deserialize(String content);
}
