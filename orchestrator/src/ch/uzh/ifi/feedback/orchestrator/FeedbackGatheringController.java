package ch.uzh.ifi.feedback.orchestrator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.uzh.ifi.feedback.library.rest.*;
import ch.uzh.ifi.feedback.library.transaction.*;
import ch.uzh.ifi.feedback.orchestrator.Model.Application;
import ch.uzh.ifi.feedback.orchestrator.Model.FeedbackMechanism;


@Controller
(Route = "/application/{application_id}/configuration")
public class FeedbackGatheringController extends RestController<Application>{
    
	private ISerializationService<Application> serializationService;
	private ConfigurationService dbService;
	
    public FeedbackGatheringController() {
		super();
		
		dbService = new ConfigurationService(new TransactionManager(), new ConfigurationParser());
		serializationService = new ConfigurationSerializer(this.getSerializationType());
	}

	@Override
	public Application Get(HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		String application = (String)request.getAttribute("Application");
		return dbService.GetConfiguration(application);
	}

	@Override
	public void Post(HttpServletRequest request, HttpServletResponse response, Application configuration) throws Exception
	{
		String application = (String)request.getAttribute("Application");
		configuration.setName(application);
		dbService.InsertConfiguration(configuration);
		response.getWriter().append(serializationService.Serialize(dbService.GetConfiguration(configuration.getName())));
	}

	@Override
	public void Put(HttpServletRequest request, HttpServletResponse response, Application configuration) throws Exception
	{
		String application = (String)request.getAttribute("Application");
		configuration.setName(application);
		dbService.UpdateConfiguration(configuration);
		response.getWriter().append(serializationService.Serialize(dbService.GetConfiguration(configuration.getName())));
	}
	
	@Override
	public ISerializationService<Application> getSerializationService() {
		return serializationService;
	}
}
