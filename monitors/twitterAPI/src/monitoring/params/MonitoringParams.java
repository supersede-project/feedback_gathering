package monitoring.params;

import java.util.ArrayList;
import java.util.List;

public class MonitoringParams {
	
	private String timeSlot;
	private String kafkaEndpoint;
	private String kafkaTopic;
	private String keywordExpression;
	private List<String> accounts;
	
	public MonitoringParams() {
		accounts = new ArrayList<>();
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

	public String getKeywordExpression() {
		return keywordExpression;
	}

	public void setKeywordExpression(String keywordExpression) {
		this.keywordExpression = keywordExpression;
	}

	public List<String> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<String> accounts) {
		this.accounts = accounts;
	}
	
	public void addAcount(String account) {
		this.accounts.add(account);
	}
	

}
