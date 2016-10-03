package ch.uzh.ifi.feedback.repository.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.Service.DatabaseConfiguration;
import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.repository.model.ScreenshotFeedback;
import ch.uzh.ifi.feedback.repository.model.TextAnnotation;
import javassist.NotFoundException;

import static java.util.Arrays.asList;

@Singleton
public class ScreenshotFeedbackService extends ServiceBase<ScreenshotFeedback> {

	private TextAnnotationService annotationService;
	
	@Inject
	public ScreenshotFeedbackService(ScreenshotFeedbackResultParser resultParser, DatabaseConfiguration dbConfig, TextAnnotationService annotationService) 
	{
		super(resultParser, ScreenshotFeedback.class, "screenshot_feedbacks", dbConfig.getRepositoryDb());
		this.annotationService = annotationService;
	}
	
	@Override
	public List<ScreenshotFeedback> GetAll() throws SQLException {
		List<ScreenshotFeedback> feedbacks = super.GetAll();
		for(ScreenshotFeedback feedback : feedbacks)
		{
			feedback.getTextAnnotations().addAll(annotationService.GetWhere(asList(feedback.getId()), "screenshot_feedbacks_id = ?"));
		}
		
		return feedbacks;
	}
	
	@Override
	public ScreenshotFeedback GetById(int id) throws SQLException, NotFoundException {
		ScreenshotFeedback feedback =  super.GetById(id);
		feedback.getTextAnnotations().addAll(annotationService.GetWhere(asList(feedback.getId()), "screenshot_feedbacks_id = ?"));
		return feedback;
	}
	
	@Override
	public List<ScreenshotFeedback> GetWhere(List<Object> values, String... conditions) throws SQLException {
		List<ScreenshotFeedback> feedbacks = super.GetWhere(values, conditions);
		for(ScreenshotFeedback feedback : feedbacks)
		{
			feedback.getTextAnnotations().addAll(annotationService.GetWhere(asList(feedback.getId()), "screenshot_feedbacks_id = ?"));
		}
		
		return feedbacks;
	}
	
	@Override
	public int Insert(Connection con, ScreenshotFeedback feedback)
			throws SQLException, NotFoundException, UnsupportedOperationException {
		int feedbackId = super.Insert(con, feedback);
		
		for(TextAnnotation annotation : feedback.getTextAnnotations())
		{
			annotation.setScreenshotId(feedbackId);
			annotationService.Insert(con, annotation);
		}
		
		return feedbackId;
	}
	
	@Override
	public void Update(Connection con, ScreenshotFeedback feedback)
			throws SQLException, NotFoundException, UnsupportedOperationException {
		super.Update(con, feedback);
		
		for(TextAnnotation annotation : feedback.getTextAnnotations())
		{
			if(annotation.getId() != null)
			{
				annotationService.Update(con, annotation);
			}
			else{
				annotationService.Insert(con, annotation);
			}
		}
	}
}
