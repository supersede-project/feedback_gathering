package com.example.matthias.feedbacklibrary.models;

import com.example.matthias.feedbacklibrary.configurations.MechanismConfigurationItem;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Attachment mechanism model
 */
public class AttachmentMechanism extends Mechanism {
    private String title;

    public AttachmentMechanism(MechanismConfigurationItem item) {
        super(ATTACHMENT_TYPE, item);
        initAttachmentMechanism(item);
    }

    public String getTitle() {
        return title;
    }

    private void initAttachmentMechanism(MechanismConfigurationItem item) {
        for (Map<String, Object> param : item.getParameters()) {
            String key = (String) param.get("key");
            // Title
            if (key.equals("title")) {
                setTitle((String) param.get("value"));
            }
        }
    }

    @Override
    public boolean isValid(List<String> errorMessage) {
        return true;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
