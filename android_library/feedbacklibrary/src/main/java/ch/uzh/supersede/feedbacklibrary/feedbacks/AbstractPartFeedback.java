package ch.uzh.supersede.feedbacklibrary.feedbacks;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;

import java.io.File;
import java.io.Serializable;

import ch.uzh.supersede.feedbacklibrary.models.AbstractMechanism;

/**
 * Base class of feedback which need to be sent as multipart such as attachment, audio or screenshot.
 */
public abstract class AbstractPartFeedback implements Serializable {
    @Nullable
    @Expose
    private String fileExtension;
    @Expose
    private long mechanismId;
    @Expose
    private String name;
    @Expose
    private String part;

    public AbstractPartFeedback(AbstractMechanism mechanism, String filePath, int partId) {
        initPartFeedback(mechanism, filePath, partId);
    }

    public abstract String getPartString();

    private void initPartFeedback(AbstractMechanism mechanism, String filePath, int partId) {
        setMechanismId(mechanism.getId());
        File file = new File(filePath);
        String[] split = (file.getName()).split("\\.");
        if (split.length == 1) {
            // The file has no file extension
            setName(split[0]);
            setFileExtension(null);
        } else if (split.length > 1) {
            //FIXME: WTF!
            setName(split[split.length - 2]);
            setFileExtension(split[split.length - 1]);
        }
        setPart(getPartString() + partId);
    }

    @Nullable
    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(@Nullable String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public long getMechanismId() {
        return mechanismId;
    }

    public void setMechanismId(long mechanismId) {
        this.mechanismId = mechanismId;
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
