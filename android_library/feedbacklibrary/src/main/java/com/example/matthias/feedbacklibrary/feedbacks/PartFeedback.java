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

    /**
     * This method returns the file extension.
     *
     * @return the file extension
     */
    public String getFileExtension() {
        return fileExtension;
    }

    /**
     * This method returns the mechanism id.
     *
     * @return the mechanism id
     */
    public long getMechanismId() {
        return mechanismId;
    }

    /**
     * This method returns the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * This method returns the part.
     *
     * @return the part
     */
    public String getPart() {
        return part;
    }

    /**
     * This method returns the part string.
     *
     * @return the part string
     */
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

    /**
     * This method sets the file extension.
     *
     * @param fileExtension the file extension
     */
    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    /**
     * This method sets the mechanism id.
     *
     * @param mechanismId the mechanism id
     */
    public void setMechanismId(long mechanismId) {
        this.mechanismId = mechanismId;
    }

    /**
     * This method sets the name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method sets the part.
     *
     * @param part the part
     */
    public void setPart(String part) {
        this.part = part;
    }
}
