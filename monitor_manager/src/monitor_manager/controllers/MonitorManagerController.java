package monitor_manager.controllers;

import java.io.IOException;
import java.net.URI;

import javax.inject.Singleton;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import monitor_manager.models.ConfigurationParser;

@Singleton
public class MonitorManagerController {
	
	private String monitorHost = "localhost:8080";
	
	private ConfigurationParser parser = new ConfigurationParser();

	@POST
	@Path("addConfiguration")
	public void addConfiguration(String json) throws Exception {
		JsonObject jsonObj = getJson(json);
		
		JsonObject configuration = null;
		
		switch (jsonObj.get("monitor").getAsString()) {
			case "TwitterAPI":
				configuration = parser.getTwitterConfiguration(jsonObj);
				break;
			case "GooglePlay":
				configuration = parser.getGooglePlayConfiguration(jsonObj);
				break;
			case "AppStore":
				configuration = parser.getAppStoreConfiguration(jsonObj);
				break;
		}
		
		CloseableHttpClient client = HttpClientBuilder.create().build();
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost(monitorHost)
			.setPath("/configuration")
		    .setParameter("configuration", configuration.toString());
		URI uri = builder.build();
		HttpPost httppost = new HttpPost(uri);
		client.execute(httppost);
	}
	
	@POST
	@Path("updateConfiguration")
	public void updateConfiguration(String json) {
		JsonObject jsonObj = getJson(json);
		//TODO still not implemented in monitor's side
		//CloseableHttpClient client = HttpClientBuilder.create().build();
		//String url = monitorManagerHost + "updateConfiguration";
		//HttpPut request = new HttpPut(url);
		//request.addHeader("content-type", "application/json");
		//request.setEntity(new StringEntity(getConfigurationJson(monitorConfiguration).toString()));
		//client.execute(request);
	}
	
	@POST
	@Path("deleteConfiguration")
	public void deleteConfiguration(String json) throws Exception {
		JsonObject jsonObj = getJson(json);
		
		CloseableHttpClient client = HttpClientBuilder.create().build();
		String url = monitorHost + 
				"/" + jsonObj.get("monitor") + 
				"/configuration/" + jsonObj.get("id");
		HttpDelete request = new HttpDelete(url);
		client.execute(request);
	}
	
	private JsonObject getJson(String configuration) {
		JsonParser jsonParser = new JsonParser();
		JsonObject json = (JsonObject)jsonParser.parse(configuration);
		return json;
	}
	
}
