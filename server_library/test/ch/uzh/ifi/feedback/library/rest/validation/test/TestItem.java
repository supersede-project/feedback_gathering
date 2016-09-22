package ch.uzh.ifi.feedback.library.rest.validation.test;

import ch.uzh.ifi.feedback.library.rest.Service.ItemBase;
import ch.uzh.ifi.feedback.library.rest.validation.Id;
import ch.uzh.ifi.feedback.library.rest.validation.NotNull;
import ch.uzh.ifi.feedback.library.rest.validation.Unique;

public class TestItem extends ItemBase<TestItem>{

	@Id
	private Integer id;
	
	@Unique
	private String uniqueField;
	
	@NotNull
	private String notNullField;
	
	public TestItem(int id, String unique, String notNull) {
		this.id = id;
		this.uniqueField = unique;
		this.notNullField = notNull;
	}
	
	public TestItem() {
	}
	
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public String getNotNullField() {
		return notNullField;
	}

	public void setNotNullField(String notNullField) {
		this.notNullField = notNullField;
	}

	public String getUniqueField() {
		return uniqueField;
	}

	public void setUniqueField(String uniqueField) {
		this.uniqueField = uniqueField;
	}

}
