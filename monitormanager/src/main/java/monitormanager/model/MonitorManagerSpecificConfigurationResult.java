package monitormanager.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MonitorManagerSpecificConfigurationResult {
	Integer idConf;
	String message;
	String status;

	public Integer getIdConf() {
		return idConf;
	}

	public void setIdConf(Integer idConf) {
		this.idConf = idConf;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
