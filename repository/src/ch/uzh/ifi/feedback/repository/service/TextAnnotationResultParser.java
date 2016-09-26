package ch.uzh.ifi.feedback.repository.service;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.repository.model.TextAnnotation;

public class TextAnnotationResultParser extends DbResultParser<TextAnnotation> {

	public TextAnnotationResultParser() {
		super(TextAnnotation.class);
	}

}
