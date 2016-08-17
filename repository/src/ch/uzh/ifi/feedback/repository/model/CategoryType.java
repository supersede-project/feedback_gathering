package ch.uzh.ifi.feedback.repository.model;

import java.sql.Timestamp;

import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.validation.Id;

public class CategoryType {

	@Id
	private long id;
	
	private String key;
	
	private String language;
	
	private String text;

	@DbAttribute("created_at")
	private Timestamp createdAt;

	@DbAttribute("updated_at")
	private Timestamp updatedAt;

	public CategoryType(long id, String key, String language, String text, Timestamp createdAt, Timestamp updatedAt) {
		super();
		this.id = id;
		this.key = key;
		this.language = language;
		this.text = text;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
}
