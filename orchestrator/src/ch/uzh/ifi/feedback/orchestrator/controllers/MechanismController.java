package ch.uzh.ifi.feedback.orchestrator.controllers;

import ch.uzh.ifi.feedback.library.rest.ISerializationService;
import ch.uzh.ifi.feedback.library.transaction.IDbService;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackMechanism;


public class MechanismController extends FeedbackGatheringController<FeedbackMechanism>{

	public MechanismController(
			IDbService<FeedbackMechanism> dbService,
			ISerializationService<FeedbackMechanism> serializationService) {
		super(dbService, serializationService);
		
	}

}
