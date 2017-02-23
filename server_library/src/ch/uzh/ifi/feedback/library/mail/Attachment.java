package ch.uzh.ifi.feedback.library.mail;

public class Attachment {

	private String filePath;
	private String fileNameAndExtension;
	
	public Attachment(String filePath, String fileNameAndExtension) {
		super();
		this.filePath = filePath;
		this.fileNameAndExtension = fileNameAndExtension;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileNameAndExtension() {
		return fileNameAndExtension;
	}

	public void setFileNameAndExtension(String fileNameAndExtension) {
		this.fileNameAndExtension = fileNameAndExtension;
	}
}
