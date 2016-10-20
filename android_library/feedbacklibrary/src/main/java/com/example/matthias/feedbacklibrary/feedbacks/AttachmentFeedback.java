package com.example.matthias.feedbacklibrary.feedbacks;

import java.io.Serializable;
import java.util.List;

/**
 * Attachment feedback.
 */
public class AttachmentFeedback implements Serializable {
    private List<String> attachmentPaths;

    /**
     * This method returns the attachment paths.
     *
     * @return the attachment paths.
     */
    public List<String> getAttachmentPaths() {
        return attachmentPaths;
    }

    /**
     * This method sets the attachment paths.
     *
     * @param attachmentPaths the attachment paths
     */
    public void setAttachmentPaths(List<String> attachmentPaths) {
        this.attachmentPaths = attachmentPaths;
    }
}
