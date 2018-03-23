package monitoring.model;

import org.json.JSONObject;

import monitoring.model.MonitoringData;

public class JsonLogsMonitoringData implements MonitoringData {
	
	private String serial_no;
	private String Date;
	private String level;
	private String type;
	private String class_name;
	private String method_name;
	private String line_number;
	private String Location;
	private String message;
	private String sessionID;
		
	public JsonLogsMonitoringData() {
		super();
	}

	public String getSerial_no() {
		return serial_no;
	}

	public void setSerial_no(String serial_no) {
		this.serial_no = serial_no;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getClass_name() {
		return class_name;
	}

	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}

	public String getMethod_name() {
		return method_name;
	}

	public void setMethod_name(String method_name) {
		this.method_name = method_name;
	}

	public String getLine_number() {
		return line_number;
	}

	public void setLine_number(String line_number) {
		this.line_number = line_number;
	}

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	@Override
	public JSONObject toJsonObject() {
		JSONObject json = new JSONObject();
		json.put("serial_no", this.serial_no);
		json.put("Date", this.Date);
		json.put("level", this.level);
		json.put("type",  this.type);
		json.put("class_name", this.class_name);
		json.put("method_name", this.method_name);
		json.put("line_number", this.line_number);
		json.put("Location", this.Location);
		json.put("message", this.message);
		json.put("sessionID", this.sessionID);
		return json;
	}

}
