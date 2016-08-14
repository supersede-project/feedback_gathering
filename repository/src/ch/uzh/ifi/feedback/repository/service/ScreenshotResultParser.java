package ch.uzh.ifi.feedback.repository.service;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.repository.model.Screenshot;

public class ScreenshotResultParser extends DbResultParser<Screenshot> {

	public ScreenshotResultParser() {
		super(Screenshot.class);
	}

}
