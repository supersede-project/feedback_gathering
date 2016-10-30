package ch.uzh.ifi.feedback.orchestrator;

import ch.uzh.ifi.feedback.library.rest.IRestManager;
import ch.uzh.ifi.feedback.library.rest.ServletBase;
import ch.uzh.ifi.feedback.orchestrator.transaction.MonitoringDatabaseConfiguration;

public class MonitoringServlet extends ServletBase {
	
	private static final long serialVersionUID = 1L;

	public MonitoringServlet(IRestManager restManager, MonitoringDatabaseConfiguration config) {
		super(restManager, config);
	}

	@Override
	protected void InitController() {
		try{
        	_restManager.Init("ch.uzh.ifi.feedback.orchestrator");
        }
        catch(Exception ex){
        	System.out.println(ex.getMessage());
        	ex.printStackTrace();
        	super.destroy();
        }
	}

}
