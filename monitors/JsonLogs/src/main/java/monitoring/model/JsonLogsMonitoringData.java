package monitoring.model;

import java.math.BigInteger;

import org.json.JSONObject;

import monitoring.model.MonitoringData;

public class JsonLogsMonitoringData implements MonitoringData {
	
	private BigInteger timeMillis;
	private String thread;
	private String level;
	private String loggerName;
	private String message;
	private Boolean endOfBatch;
	private String loggerFqcn;
	private int threadId;
	private int threadPriority;	
	
		
	public JsonLogsMonitoringData() {
		super();
	}

	public BigInteger getTimeMillis() {
		return timeMillis;
	}

	public void setTimeMillis(BigInteger timeMillis) {
		this.timeMillis = timeMillis;
	}

	public String getThread() {
		return thread;
	}

	public void setThread(String thread) {
		this.thread = thread;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getLoggerName() {
		return loggerName;
	}

	public void setLoggerName(String loggerName) {
		this.loggerName = loggerName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getEndOfBatch() {
		return endOfBatch;
	}

	public void setEndOfBatch(Boolean endOfBatch) {
		this.endOfBatch = endOfBatch;
	}

	public String getLoggerFqcn() {
		return loggerFqcn;
	}

	public void setLoggerFqcn(String loggerFqcn) {
		this.loggerFqcn = loggerFqcn;
	}

	public int getThreadId() {
		return threadId;
	}

	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}

	public int getThreadPriority() {
		return threadPriority;
	}

	public void setThreadPriority(int threadPriority) {
		this.threadPriority = threadPriority;
	}




	@Override
	public JSONObject toJsonObject() {
		JSONObject json = new JSONObject();
		json.put("timeMillis", this.timeMillis);
		json.put("thread", this.thread);
		json.put("level", this.level);
		json.put("loggerName", this.loggerName);
		json.put("message", this.message);
		json.put("endOfBatch", this.endOfBatch);
		json.put("loggerFqcn", this.loggerFqcn);
		json.put("threadId", this.threadId);
		json.put("threadPriority", this.threadPriority);
		return json;
	}

}
