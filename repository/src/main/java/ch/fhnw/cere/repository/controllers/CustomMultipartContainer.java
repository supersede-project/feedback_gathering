package ch.fhnw.cere.repository.controllers;


import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public class CustomMultipartContainer {
    private List<MultipartFile> files;

    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }
}