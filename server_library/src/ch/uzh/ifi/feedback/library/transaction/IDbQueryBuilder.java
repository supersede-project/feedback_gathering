package ch.uzh.ifi.feedback.library.transaction;

public interface IDbQueryBuilder {
	
	void BuildQuery(Object queryObject);
	String GetQuery();
	
}
