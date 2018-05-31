package ch.uzh.supersede.feedbacklibrary.models;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;

import java.io.File;
import java.io.Serializable;

import ch.uzh.supersede.feedbacklibrary.utils.Utils;

/**
 * Base class of feedback which need to be sent as multipart such as audio or screenshot.
 */
public abstract class AbstractMultipartFeedback extends AbstractFeedbackPart implements Serializable {
    @Nullable
    @Expose
    private String fileExtension;
    @Expose
    private String name;
    @Expose
    private String part;

    public AbstractMultipartFeedback(long mechanismId, int order) {
        super(mechanismId, order);
    }

    public abstract String getPartString();

    protected void initPartFeedback(String filePath) {
        if (filePath == null) {
            return;
        }
        File file = new File(filePath);
        String[] split = Utils.splitFileNameExtension(file.getName());
        setFileExtension(split[1]);
        setPart(getPartString());
    }

    @Nullable
    public String getFileExtension() {
        return fileExtension;
    }

    public abstract String getFilePath();

    public void setFileExtension(@Nullable String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }
}
