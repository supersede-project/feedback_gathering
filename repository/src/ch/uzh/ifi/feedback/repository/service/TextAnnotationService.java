package ch.uzh.ifi.feedback.repository.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.service.ServiceBase;
import ch.uzh.ifi.feedback.library.transaction.DatabaseConfiguration;
import ch.uzh.ifi.feedback.library.transaction.IDatabaseConfiguration;
import ch.uzh.ifi.feedback.repository.model.TextAnnotation;

@Singleton
public class TextAnnotationService extends ServiceBase<TextAnnotation>{

	@Inject
	public TextAnnotationService(
			TextAnnotationResultParser resultParser,
			IDatabaseConfiguration config) 
	{
		super(resultParser, TextAnnotation.class, "text_annotations", config.getDatabase());
	}

}
