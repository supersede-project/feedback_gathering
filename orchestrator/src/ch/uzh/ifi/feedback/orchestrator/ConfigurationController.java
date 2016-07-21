package ch.uzh.ifi.feedback.orchestrator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.uzh.ifi.feedback.library.rest.*;
import ch.uzh.ifi.feedback.library.transaction.*;


@Controller
(Route = "/{Application}/configuration")
public class ConfigurationController extends RestController<List<FeedbackMechanism>>{
    
	private ISerializationService<List<FeedbackMechanism>> serializationService;
	private ConfigurationService dbService;
	
    public ConfigurationController() {
		super();
		
		dbService = new ConfigurationService(new TransactionManager(), new ConfigurationParser());
		serializationService = new DefaultSerializer<>(this.getSerializationType());
	}

	@Override
	public List<FeedbackMechanism> Get(HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		String application = (String)request.getAttribute("Application");
		return dbService.Read(application);
	}

/*	@Override
	public void Post(HttpServletRequest request, HttpServletResponse response, List<FeedbackMechanism> configuration) throws Exception)
	{
		String application = (String)request.getAttribute("Application");
		
	}*/

	@Override
	public ISerializationService<List<FeedbackMechanism>> getSerializationService() {
		return serializationService;
	}
}
