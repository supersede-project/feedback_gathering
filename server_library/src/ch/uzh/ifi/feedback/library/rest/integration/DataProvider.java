package ch.uzh.ifi.feedback.library.rest.integration;


import eu.supersede.integration.api.analysis.proxies.DataProviderProxy;
import org.json.JSONObject;


// TODO use this in orchestrator and repository
public class DataProvider {
    private DataProviderProxy proxy;

    public DataProvider() {
        proxy = new DataProviderProxy();
    }

    // TODO after this call check with WP2 people whether they received something
    public void ingestJsonData(String topic, JSONObject jsonData) throws Exception{
        proxy.ingestData(jsonData, topic);
    }
}
