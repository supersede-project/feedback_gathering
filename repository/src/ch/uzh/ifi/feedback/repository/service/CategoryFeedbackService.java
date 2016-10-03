package ch.uzh.ifi.feedback.repository.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.Service.DatabaseConfiguration;
import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.repository.model.CategoryFeedback;

@Singleton
public class CategoryFeedbackService extends ServiceBase<CategoryFeedback>{

	@Inject
	public CategoryFeedbackService(
			CategoryResultParser resultParser,
			String tableName, 
			String dbName,
			DatabaseConfiguration config) {
		super(resultParser, CategoryFeedback.class, "category_feedbacks", config.getRepositoryDb());
	}
}
