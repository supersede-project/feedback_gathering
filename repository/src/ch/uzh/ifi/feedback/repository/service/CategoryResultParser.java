package ch.uzh.ifi.feedback.repository.service;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.repository.model.CategoryFeedback;

public class CategoryResultParser extends DbResultParser<CategoryFeedback>{

	public CategoryResultParser() {
		super(CategoryFeedback.class);
	}
}
