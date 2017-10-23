package monitormanager.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitterMonitorManagerConfiguration extends MonitorManagerSpecificConfiguration{
	
	String keywordExpression;
	List<String> accounts;
	
	public String getKeywordExpression() {
		return keywordExpression;
	}
	public void setKeywordExpression(String keywordExpression) {
		this.keywordExpression = keywordExpression;
	}
	public List<String> getAccounts() {
		if (accounts == null)
			return new ArrayList<>();
		return accounts;
	}
	public void setAccounts(List<String> accounts) {
		this.accounts = accounts;
	}
	
}
