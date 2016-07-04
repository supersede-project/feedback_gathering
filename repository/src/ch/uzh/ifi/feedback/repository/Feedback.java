package ch.uzh.ifi.feedback.repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Feedback {
	
	private String title;
	private String application;
	private String user;
	private String text;
	private Timestamp created;
	private Timestamp lastUpdated;
	private Double configVersion;
	private List<Rating> ratings;
	private List<Screenshot> screenshots;
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	public Timestamp getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Timestamp lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Double getConfigVersion() {
		return configVersion;
	}
	public void setConfigVersion(Double configVersion) {
		this.configVersion = configVersion;
	}
	public List<Rating> getRatings() {
		if(ratings == null)
			ratings = new ArrayList<>();
		
		return ratings;
	}
	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}
	public List<Screenshot> getScreenshots() {
		if(screenshots == null)
			screenshots = new ArrayList<>();
		
		return screenshots;
	}
	public void setScreenshots(List<Screenshot> screenshots) {
		this.screenshots = screenshots;
	}
}
