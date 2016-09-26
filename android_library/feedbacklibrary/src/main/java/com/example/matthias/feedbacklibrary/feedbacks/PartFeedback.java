package com.example.matthias.feedbacklibrary.feedbacks;

import android.support.annotation.Nullable;

import com.example.matthias.feedbacklibrary.models.Mechanism;
import com.google.gson.annotations.Expose;

import java.io.File;
import java.io.Serializable;

/**
 * Base class of feedback which need to be sent as multipart such as attachment, audio or screenshot.
 */
public abstract class PartFeedback implements Serializable {
    @Nullable
    @Expose
    private String fileExtension;
    @Expose
    private long mechanismId;
    @Expose
    private String name;
    @Expose
    private String part;

    public PartFeedback(Mechanism mechanism, String filePath, int partId) {
        initPartFeedback(mechanism, filePath, partId);
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public long getMechanismId() {
        return mechanismId;
    }

    public String getName() {
        return name;
    }

    public String getPart() {
        return part;
    }

    public abstract String getPartString();

    private void initPartFeedback(Mechanism mechanism, String filePath, int partId) {
        setMechanismId(mechanism.getId());
        File file = new File(filePath);
        String[] split = (file.getName()).split("\\.");
        if (split.length == 1) {
            // The file has no file extension
            setName(split[1]);
            setFileExtension(null);
        } else if (split.length > 1) {
            setName(split[split.length - 2]);
            setFileExtension(split[split.length - 1]);
        }
        setPart(getPartString() + partId);
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public void setMechanismId(long mechanismId) {
        this.mechanismId = mechanismId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPart(String part) {
        this.part = part;
    }
}
