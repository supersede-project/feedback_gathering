package com.example.matthias.feedbacklibrary.feedbacks;

import java.io.Serializable;
import java.util.List;

/**
 * Attachment feedback.
 */
public class AttachmentFeedback implements Serializable {
    private List<String> attachmentPaths;

    public List<String> getAttachmentPaths() {
        return attachmentPaths;
    }

    public void setAttachmentPaths(List<String> attachmentPaths) {
        this.attachmentPaths = attachmentPaths;
    }
}
