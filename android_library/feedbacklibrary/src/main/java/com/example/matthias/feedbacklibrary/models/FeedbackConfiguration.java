package com.example.matthias.feedbacklibrary.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by Matthias on 24.04.2016.
 */
public class FeedbackConfiguration {
    private String application = null; // TODO: Set name of the actual application
    private float configVersion = -1.0f; // TODO: Set actual configuration version
    private String user = null; // TODO: Set actual user of the feedback application --> maybe a user class in the future
    private String title = null; // Will this be the title of the text feedback?
    private String text = null;

    private List<Mechanism> allMechanisms = null;

    public FeedbackConfiguration(List<FeedbackConfigurationItem> configuration) {
        setApplication("Android Application");
        setConfigVersion(1.0f);
        setUser("Android User");
        initMechanism(configuration);
    }


    private void initMechanism(List<FeedbackConfigurationItem> configuration) {
        allMechanisms = new ArrayList<>();

        for(FeedbackConfigurationItem item : configuration) {
            allMechanisms.add(createMechanism(item));
        }

        Collections.sort(allMechanisms, new Comparator<Mechanism>() {
            @Override
            public int compare(Mechanism a, Mechanism b) {
                return ((Integer) a.getOrder()).compareTo((Integer) b.getOrder());
            }
        });
    }

    private Mechanism createMechanism(FeedbackConfigurationItem item) {
        TextMechanism toCreateText = null;
        RatingMechanism toCreateRating = null;
        ScreenshotMechanism toCreateScreenshot = null;
        AudioMechanism toCreateAudio = null;
        String type = item.getType();

        if(type.equals("TEXT_TYPE")) {
            toCreateText = new TextMechanism(item);
            for (Map<String, Object> param : item.getParameters()) {
                String key = (String) param.get("key");

                // Title
                if(key.equals("title")) {
                    toCreateText.setTitle((String) param.get("value"));
                }
                // Hint
                if(key.equals("hint")) {
                    toCreateText.setHint((String) param.get("value"));
                }
                // Maximum length
                if(key.equals("maxLength")) {
                    toCreateText.setMaxLength(((Double) param.get("value")).intValue());
                }
            }
            return toCreateText;
        } else if(type.equals("RATING_TYPE")) {
            toCreateRating = new RatingMechanism(item);
            for (Map<String, Object> param : item.getParameters()) {
                String key = (String) param.get("key");
                // Title
                if(key.equals("title")) {
                    toCreateRating.setTitle((String) param.get("value"));
                }
                // Rating icon
                if(key.equals("ratingIcon")) {
                    toCreateRating.setRatingIcon((String) param.get("value"));
                }
                // Maximum rating
                if(key.equals("maxRating")) {
                    toCreateRating.setMaxRating(((Double) param.get("value")).intValue());
                }
                // Default rating
                if(key.equals("defaultRating")) {
                    toCreateRating.setDefaultRating(((Double) param.get("value")).floatValue());
                }
            }
            return toCreateRating;
        } else if(type.equals("AUDIO_TYPE")) {
            toCreateAudio = new AudioMechanism(item);
            for (Map<String, Object> param : item.getParameters()) {

            }

            // TODO
            return toCreateAudio;
        } else if(type.equals("SCREENSHOT_TYPE")) {
            toCreateScreenshot = new ScreenshotMechanism(item);
            for (Map<String, Object> param : item.getParameters()) {

            }

            // TODO
            return toCreateScreenshot;
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
    public void setApplication(String application) {
        this.application = application;
    }

    public float getConfigVersion() {
        return configVersion;
    }
    public void setConfigVersion(float configVersion) {
        this.configVersion = configVersion;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
}
