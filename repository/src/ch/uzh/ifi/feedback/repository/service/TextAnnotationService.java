package ch.uzh.ifi.feedback.repository.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.Service.DatabaseConfiguration;
import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.repository.model.TextAnnotation;

@Singleton
public class TextAnnotationService extends ServiceBase<TextAnnotation>{

	@Inject
	public TextAnnotationService(
			TextAnnotationResultParser resultParser,
			DatabaseConfiguration config) 
	{
		super(resultParser, TextAnnotation.class, "text_annotations", config.getRepositoryDb());
	}

}
