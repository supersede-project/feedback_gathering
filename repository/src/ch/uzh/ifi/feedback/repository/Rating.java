package ch.uzh.ifi.feedback.repository;

public class Rating {
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	private String title;
	private int rating;
	
}
