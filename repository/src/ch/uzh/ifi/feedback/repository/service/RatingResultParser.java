package ch.uzh.ifi.feedback.repository.service;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.repository.model.Rating;

public class RatingResultParser extends DbResultParser<Rating> {

	public RatingResultParser() {
		super(Rating.class);
	}

}
