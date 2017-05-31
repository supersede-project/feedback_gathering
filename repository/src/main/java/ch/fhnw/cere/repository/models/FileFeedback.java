package ch.fhnw.cere.repository.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Entity
public class FileFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;
    protected String path;
    protected int size;
    @Transient
    protected String part;
    protected String fileExtension;

    public FileFeedback() {

    }

    public FileFeedback(String path, int size, String part, String fileExtension) {
        this.path = path;
        this.size = size;
        this.part = part;
        this.fileExtension = fileExtension;
    }

    public FileFeedback(String path, String part, String fileExtension) {
        this.path = path;
        this.size = size;
        this.part = part;
        this.fileExtension = fileExtension;
    }

    @Override
    public String toString() {
        return String.format(
                "FileFeedback[id=%d, path=%s, size=%d, part=%s, fileExtension=%s]",
                id, path, size, part, fileExtension);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }
}
