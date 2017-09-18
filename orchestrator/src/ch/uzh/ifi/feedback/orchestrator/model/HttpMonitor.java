package ch.uzh.ifi.feedback.orchestrator.model;

public class HttpMonitor extends MonitorConfigurationParams {
	
	private String url;
	
	public String getUrl() {
		return this.url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

}
