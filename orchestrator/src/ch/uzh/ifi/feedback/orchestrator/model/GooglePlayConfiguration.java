package ch.uzh.ifi.feedback.orchestrator.model;

public class GooglePlayConfiguration extends MonitorConfigurationParams {

	private String packageName;
	
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

}
