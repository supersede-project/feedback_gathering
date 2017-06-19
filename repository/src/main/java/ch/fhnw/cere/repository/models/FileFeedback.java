package ch.fhnw.cere.repository.models;


public interface FileFeedback {
    String getPart();
    void setPath(String path);
    void setSize(int size);
    String getFileExtension();
}
