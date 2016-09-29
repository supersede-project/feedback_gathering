package monitoring.model;

public class MonitoringData {
	
	private String id;
	private String timeStamp;
	private String message;
	private String author;
	private String link;
	
	public MonitoringData(String id, String timeStamp, String message, String author, String link) {
		this.id = id;
		this.timeStamp = timeStamp;
		this.message = message;
		this.author = author;
		this.link = link;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

}
