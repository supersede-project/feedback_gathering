package ch.uzh.ifi.feedback.repository;

import java.nio.file.Path;

public class Screenshot {
	
	private String fileName;
	private Path path;
	private int size;
	
	public Screenshot(String fileName, Path path, int size)
	{
		setFileName(fileName);
		setPath(path);
		setSize(size);
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
