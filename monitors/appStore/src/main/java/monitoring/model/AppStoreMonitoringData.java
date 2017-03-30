/*******************************************************************************
 * Copyright (c) 2016 Universitat Politécnica de Catalunya (UPC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * 	Quim Motger (UPC) - main development
 * 	
 * Initially developed in the context of SUPERSEDE EU project
 * www.supersede.eu
 *******************************************************************************/
package monitoring.model;

import org.json.JSONObject;

public class AppStoreMonitoringData implements MonitoringData {

	private String reviewID;
	private String authorName;
	private String timeStamp;
	private String appVersion;
	private String device;
	private String reviewerLanguage;
	private String starRating;
	private String reviewTitle;
	private String reviewText;
	private String link;

	public String getReviewID() {
		return reviewID;
	}
	public void setReviewID(String reviewID) {
		this.reviewID = reviewID;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getReviewerLanguage() {
		return reviewerLanguage;
	}
	public void setReviewerLanguage(String reviewerLanguage) {
		this.reviewerLanguage = reviewerLanguage;
	}
	public String getStarRating() {
		return starRating;
	}
	public void setStarRating(String starRating) {
		this.starRating = starRating;
	}
	public String getReviewTitle() {
		return reviewTitle;
	}
	public void setReviewTitle(String reviewTitle) {
		this.reviewTitle = reviewTitle;
	}
	public String getReviewText() {
		return reviewText;
	}
	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	@Override
	public JSONObject toJsonObject() {
		JSONObject json = new JSONObject();
		json.put("reviewID", reviewID);
		json.put("authorName", authorName);
		json.put("timeStamp", timeStamp);
		json.put("appVersion", appVersion);
		json.put("device", device);
		json.put("reviewerLanguage", reviewerLanguage);
		json.put("starRating", starRating);
		json.put("reviewTitle", reviewTitle);
		json.put("reviewText", reviewText);
		json.put("link", link);
		return json;
	}
	
}
