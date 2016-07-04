package ch.uzh.ifi.feedback.repository;

import java.nio.file.Path;

public class Screenshot {
	
	private String fileName;
	private String path;
	private int size;
	
	public Screenshot(String fileName, String path, int size)
	{
		setFileName(fileName);
		setPath(path);
		setSize(size);
	}
	
	public Screenshot(){
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
