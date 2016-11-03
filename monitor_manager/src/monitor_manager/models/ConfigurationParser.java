package monitor_manager.models;

import com.google.gson.JsonObject;

public class ConfigurationParser {

	public JsonObject getTwitterConfiguration(JsonObject json) {
		return json.getAsJsonObject("SocialNetworksMonitorConfiguration");
	}
	
	public JsonObject getGooglePlayConfiguration(JsonObject json) {
		return json.getAsJsonObject("GooglePlayConfiguration");
	}
	
	public JsonObject getAppStoreConfiguration(JsonObject json) {
		return json.getAsJsonObject("AppStoreConfiguration");
	}
	
}
