package ch.uzh.supersede.feedbacklibrary.models;

import ch.uzh.supersede.feedbacklibrary.configurations.MechanismConfigurationItem;

import java.util.List;
import java.util.Map;

/**
 * Attachment mechanism model
 */
public class AttachmentMechanism extends Mechanism {
    private List<String> attachmentPaths;
    private String title;

    public AttachmentMechanism(MechanismConfigurationItem item) {
        super(ATTACHMENT_TYPE, item);
        initAttachmentMechanism(item);
    }

    /**
     * This method returns all the attachment paths.
     *
     * @return the attachment paths
     */
    public List<String> getAttachmentPaths() {
        return attachmentPaths;
    }

    /**
     * This method returns the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    private void initAttachmentMechanism(MechanismConfigurationItem item) {
        for (Map<String, Object> param : item.getParameters()) {
            String key = (String) param.get("key");
            // Title
            if (key == null){
                return;
            }
            if (key.equals("title")) {
                setTitle((String) param.get("value"));
            }
        }
    }

    @Override
    public boolean isValid(List<String> errorMessage) {
        return true;
    }

    /**
     * This method sets all the attachment paths.
     *
     * @param attachmentPaths the attachment paths
     */
    public void setAttachmentPaths(List<String> attachmentPaths) {
        this.attachmentPaths = attachmentPaths;
    }

    /**
     * This method sets the title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
