package ch.uzh.ifi.feedback.repository.integration;

import org.json.JSONObject;

import ch.uzh.ifi.feedback.library.rest.integration.DataProviderIntegrator;;

public class DataProviderIntegratorRepository {
    private DataProviderIntegrator dataProviderIntegrator;

    public DataProviderIntegratorRepository() {
    	this.dataProviderIntegrator = new DataProviderIntegrator();
    }

    public void ingestJsonData(JSONObject jsonData, String topic) throws Exception{
    	dataProviderIntegrator.ingestJsonData(jsonData, topic);
    }
}
