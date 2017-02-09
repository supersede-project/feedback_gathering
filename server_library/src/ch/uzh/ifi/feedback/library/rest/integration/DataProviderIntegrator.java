package ch.uzh.ifi.feedback.library.rest.integration;


import eu.supersede.integration.api.analysis.proxies.DataProviderProxy;
import org.json.JSONObject;


// TODO use this in orchestrator and repository
public class DataProviderIntegrator {
    private DataProviderProxy proxy;

    public DataProviderIntegrator() {
        proxy = new DataProviderProxy();
    }

    // TODO after this call check with WP2 people whether they received something
    public void ingestJsonData(JSONObject jsonData, String topic) throws Exception{
        proxy.ingestData(jsonData, topic);
    }
}
