package ch.uzh.ifi.feedback.orchestrator.model;

import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.annotations.Id;
import ch.uzh.ifi.feedback.library.rest.annotations.Serialize;
import ch.uzh.ifi.feedback.library.rest.annotations.Unique;
import ch.uzh.ifi.feedback.orchestrator.serialization.MonitorConfigurationSerializationService;

@Serialize(MonitorConfigurationSerializationService.class)
public class MonitorConfiguration extends OrchestratorItem<MonitorConfiguration> {

	@Id
	@DbAttribute("monitor_configurations_id")
	private Integer id;
	
	@DbAttribute("monitor_tool_name")
	private String monitorToolName;
	
	@DbAttribute("monitor_tool_monitor")
	private String monitor;
	
	private String configSender;

	private String timeStamp;
	
	//Configuration candidates, only one of them is deserialized
	private AppStoreConfiguration AppStoreConfiguration;
	private GooglePlayConfiguration GooglePlayConfiguration;
	private SocialNetworksMonitorConfiguration SocialNetworksMonitorConfiguration;
	
	public String getMonitor() {
		return monitor;
	}

	public void setMonitor(String monitor) {
		this.monitor = monitor;
	}

	public String getMonitorToolName() {
		return monitorToolName;
	}

	public void setMonitorToolName(String monitorToolName) {
		this.monitorToolName = monitorToolName;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getConfigSender() {
		return configSender;
	}

	public void setConfigSender(String configSender) {
		this.configSender = configSender;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public AppStoreConfiguration getAppStoreConfiguration() {
		return AppStoreConfiguration;
	}

	public void setAppStoreConfiguration(AppStoreConfiguration appStoreConfiguration) {
		AppStoreConfiguration = appStoreConfiguration;
	}

	public GooglePlayConfiguration getGooglePlayConfiguration() {
		return GooglePlayConfiguration;
	}

	public void setGooglePlayConfiguration(GooglePlayConfiguration googlePlayConfiguration) {
		GooglePlayConfiguration = googlePlayConfiguration;
	}

	public SocialNetworksMonitorConfiguration getSocialNetworksMonitorConfiguration() {
		return SocialNetworksMonitorConfiguration;
	}

	public void setSocialNetworksMonitorConfiguration(
			SocialNetworksMonitorConfiguration socialNetworksMonitorConfiguration) {
		SocialNetworksMonitorConfiguration = socialNetworksMonitorConfiguration;
	}

}
