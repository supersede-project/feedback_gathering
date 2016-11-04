package ch.uzh.ifi.feedback.orchestrator.model;

public class MarketPlaces extends MonitorConfigurationParams {

	private String packageName;
	
	private String appId;
	
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

}
