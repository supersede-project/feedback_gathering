package monitor_manager.models;

import com.google.gson.JsonObject;

public class ConfigurationParser {

	public JsonObject getTwitterConfiguration(JsonObject json) {
		JsonObject socialNetworks = new JsonObject();
		JsonObject params = new JsonObject();
		addMainConfigurationParams(json,params);
		if (json.get("keywordEpxression") != null) params.addProperty("keywordExpression", json.get("keywordExpression").getAsString());
		if (json.get("accounts") != null) params.add("accounts", json.get("accounts"));
		
		socialNetworks.add("SocialNetworksMonitoringConfProf", params);
		return socialNetworks;
	}
	
	public JsonObject getGooglePlayConfiguration(JsonObject json) {
		JsonObject googlePlay = new JsonObject();
		JsonObject params = new JsonObject();
		addMainConfigurationParams(json,params);
		params.addProperty("packageName", json.get("packageName").getAsString());
		
		googlePlay.add("GooglePlayConfProf", params);
		return googlePlay;
	}
	
	public JsonObject getAppStoreConfiguration(JsonObject json) {
		JsonObject appStore = new JsonObject();
		JsonObject params = new JsonObject();
		addMainConfigurationParams(json,params);
		params.addProperty("appId", json.get("appId").getAsString());
		
		appStore.add("AppStoreConfProf", params);
		return appStore;
	}
	
	private void addMainConfigurationParams(JsonObject json, JsonObject params) {
		params.addProperty("toolName", json.get("tool").getAsString());
		params.addProperty("timeSlot", json.get("timeSlot").getAsString());
		params.addProperty("kafkaEndpoint", json.get("kafkaEndpoint").getAsString());
		params.addProperty("kafkaTopic", json.get("kafkaTopic").getAsString());
	}
	
}
