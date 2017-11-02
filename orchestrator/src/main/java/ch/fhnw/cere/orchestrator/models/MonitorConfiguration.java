package ch.fhnw.cere.orchestrator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import eu.supersede.integration.api.monitoring.manager.types.Method;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.json.JSONObject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@JsonInclude(Include.NON_NULL)
public class MonitorConfiguration {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "monitor_tool_id")
    private MonitorTool monitorTool;

    @Column(name="monitor_manager_id")
    private long monitorManagerId;
    
    @NotNull
    @Column(name="config_sender")
    private String configSender;
    @NotNull
    @Column(name="timestamp")
    private String timeStamp;
    @NotNull
    @Column(name="time_slot")
    private String timeSlot;
    @Column(name="kafka_endpoint")
    private String kafkaEndpoint;
    @NotNull
    @Column(name="kafka_topic")
    private String kafkaTopic;
    @NotNull
    private String state;

    /*
     * SocialNetwork params
     */
    @Column(name="keyword_expression")
    private String keywordExpression;
    //private List<String> accounts;

    /*
     * MarketPlaces params
     */
    @Column(name="package_name")
    private String packageName;
    @Column(name="app_id")
    private String appId;
    
    /*
     * QoS params
     */
    @Column(name="url")
    private String url;
    @Column(name="method")
    private Method method;

    public MonitorConfiguration() {
    }

    public MonitorConfiguration(MonitorTool monitorTool, String configSender, String timeStamp, String timeSlot, String kafkaEndpoint, String kafkaTopic, String state, String keywordExpression, String packageName, String appId, String url) {
        this.monitorTool = monitorTool;
        this.configSender = configSender;
        this.timeStamp = timeStamp;
        this.timeSlot = timeSlot;
        this.kafkaEndpoint = kafkaEndpoint;
        this.kafkaTopic = kafkaTopic;
        this.state = state;
        this.keywordExpression = keywordExpression;
        this.packageName = packageName;
        this.appId = appId;
        this.url = url;
    }

    @Override
    public String toString() {
        return String.format(
                "MonitorConfiguration[id=%d, monitorTool='%s', configSender='%s', " +
                        "timeStamp='%s', timeSlot='%s', kafkaEndpoint='%s', " +
                        "kafkaTopic='%s', state='%s', keywordExpression='%s', packageName='%s', appId='%s', url='%s']",
                id, monitorTool, configSender,
                timeStamp, timeSlot, kafkaEndpoint,
                kafkaTopic, state, keywordExpression, packageName, appId, url);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MonitorTool getMonitorTool() {
        return monitorTool;
    }

    public void setMonitorTool(MonitorTool monitorTool) {
        this.monitorTool = monitorTool;
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

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getKafkaEndpoint() {
        return kafkaEndpoint;
    }

    public void setKafkaEndpoint(String kafkaEndpoint) {
        this.kafkaEndpoint = kafkaEndpoint;
    }

    public String getKafkaTopic() {
        return kafkaTopic;
    }

    public void setKafkaTopic(String kafkaTopic) {
        this.kafkaTopic = kafkaTopic;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getKeywordExpression() {
        return keywordExpression;
    }

    public void setKeywordExpression(String keywordExpression) {
        this.keywordExpression = keywordExpression;
    }

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
    
    public String getUrl() {
    	return url;
    }
    
    public void setUrl(String url) {
    	this.url = url;
    }
    
    public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}
    
    public long getMonitorManagerId() {
    	return monitorManagerId;
    }
    
    public void setMonitorManagerId(long id) {
    	this.monitorManagerId = id;
    }
}
