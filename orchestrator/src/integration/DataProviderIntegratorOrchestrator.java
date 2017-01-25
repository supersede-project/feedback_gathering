package integration;

import org.json.JSONObject;

import ch.uzh.ifi.feedback.library.rest.integration.DataProviderIntegrator;;

public class DataProviderIntegratorOrchestrator {
    private DataProviderIntegrator dataProviderIntegrator;

    public DataProviderIntegratorOrchestrator() {
    	this.dataProviderIntegrator = new DataProviderIntegrator();
    }

    public void ingestJsonData(JSONObject jsonData, String topic) throws Exception{
    	dataProviderIntegrator.ingestJsonData(jsonData, topic);
    }
}
