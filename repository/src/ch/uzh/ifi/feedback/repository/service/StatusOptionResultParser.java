package ch.uzh.ifi.feedback.repository.service;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.transaction.DbResultParser;
import ch.uzh.ifi.feedback.repository.model.StatusOption;

@Singleton
public class StatusOptionResultParser extends DbResultParser<StatusOption>{
	public StatusOptionResultParser() {
		super(StatusOption.class);
	}
}
