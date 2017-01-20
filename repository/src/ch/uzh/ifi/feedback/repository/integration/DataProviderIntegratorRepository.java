package ch.uzh.ifi.feedback.repository.integration;


import eu.supersede.integration.api.analysis.proxies.DataProviderProxy;
import org.json.JSONObject;


public class DataProviderIntegratorRepository {
    private DataProviderProxy proxy;

    public DataProviderIntegratorRepository() {
        proxy = new DataProviderProxy();
    }

    public void ingestJsonData(String topic, JSONObject jsonData) throws Exception{
        proxy.ingestData(jsonData, topic);
    }
}
