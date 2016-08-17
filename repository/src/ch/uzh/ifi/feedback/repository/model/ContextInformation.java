package ch.uzh.ifi.feedback.repository.model;

import java.sql.Date;

import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.annotations.DbIgnore;
import ch.uzh.ifi.feedback.library.rest.validation.Id;
import ch.uzh.ifi.feedback.library.rest.validation.NotNull;

public class ContextInformation {

	@Id
	private long id;	

	private String resolution;
	
	@DbAttribute("user_agent")
	private String userAgent;
	
	@DbAttribute("android_version")	
	private String androidVersion;

	@DbAttribute("local_time")
	private Date localTime;
	
	@DbAttribute("time_zone")
	private String timeZone;
	
	@DbAttribute("device_pixel_ratio")
	private Float devicePixelRatio;
	
	private String country;
	
	private String region;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getAndroidVersion() {
		return androidVersion;
	}

	public void setAndroidVersion(String androidVersion) {
		this.androidVersion = androidVersion;
	}

	public Date getLocalTime() {
		return localTime;
	}

	public void setLocalTime(Date localTime) {
		this.localTime = localTime;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public Float getDevicePixelRatio() {
		return devicePixelRatio;
	}

	public void setDevicePixelRatio(Float devicePixelRatio) {
		this.devicePixelRatio = devicePixelRatio;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}
}