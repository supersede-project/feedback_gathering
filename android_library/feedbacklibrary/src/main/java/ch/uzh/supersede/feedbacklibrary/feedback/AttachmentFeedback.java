package ch.uzh.supersede.feedbacklibrary.feedback;

import java.io.Serializable;
import java.util.List;

public class AttachmentFeedback implements Serializable {
    private List<String> attachmentPaths;

    public List<String> getAttachmentPaths() {
        return attachmentPaths;
    }

    public void setAttachmentPaths(List<String> attachmentPaths) {
        this.attachmentPaths = attachmentPaths;
    }
}
