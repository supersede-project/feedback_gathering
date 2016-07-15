package com.example.matthias.feedbacklibrary.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Feedback configuration used in the feedback activity
 */
public class FeedbackConfiguration {
    // TODO: Set name of the actual application
    private String application = null;
    // TODO: Set actual configuration version
    private float configVersion = -1.0f;
    // TODO: Set actual user of the feedback application --> possibly a user class in the future
    private String user = null;

    private List<Mechanism> allMechanisms = null;

    public FeedbackConfiguration(List<FeedbackConfigurationItem> configuration) {
        setApplication("Android Application");
        setConfigVersion(1.0f);
        setUser("Android User");
        initMechanism(configuration);
    }

    private Mechanism createMechanism(FeedbackConfigurationItem item) {
        TextMechanism toCreateText;
        RatingMechanism toCreateRating;
        ScreenshotMechanism toCreateScreenshot;
        AudioMechanism toCreateAudio;
        String type = item.getType();

        if (type.equals("TEXT_TYPE")) {
            return new TextMechanism(item);
        } else if (type.equals("RATING_TYPE")) {
            return new RatingMechanism(item);
        } else if (type.equals("AUDIO_TYPE")) {
            // TODO: Implement
            return new AudioMechanism(item);
        } else if (type.equals("SCREENSHOT_TYPE")) {
            return new ScreenshotMechanism(item);
        } else {
            // should never happen!
        }

        return null; // should never happen!
    }

    public List<Mechanism> getAllMechanisms() {
        return allMechanisms;
    }

    public String getApplication() {
        return application;
    }

    public float getConfigVersion() {
        return configVersion;
    }

    public String getUser() {
        return user;
    }

    private void initMechanism(List<FeedbackConfigurationItem> configuration) {
        allMechanisms = new ArrayList<>();

        for (FeedbackConfigurationItem item : configuration) {
            allMechanisms.add(createMechanism(item));
        }

        Collections.sort(allMechanisms, new Comparator<Mechanism>() {
            @Override
            public int compare(Mechanism a, Mechanism b) {
                return ((Integer) a.getOrder()).compareTo(b.getOrder());
            }
        });
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public void setConfigVersion(float configVersion) {
        this.configVersion = configVersion;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
