package ch.uzh.supersede.feedbacklibrary.models;

import java.util.List;

import ch.uzh.supersede.feedbacklibrary.configurations.MechanismConfigurationItem;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.MechanismConstants.ATTACHMENT_TYPE;

public class AttachmentMechanism extends AbstractMechanism {
    private List<String> attachmentPaths;

    public AttachmentMechanism(MechanismConfigurationItem item) {
        super(ATTACHMENT_TYPE, item);
    }

    @Override
    public boolean isValid(List<String> errorMessage) {
        return true;
    }

    public List<String> getAttachmentPaths() {
        return attachmentPaths;
    }

    public void setAttachmentPaths(List<String> attachmentPaths) {
        this.attachmentPaths = attachmentPaths;
    }

}
