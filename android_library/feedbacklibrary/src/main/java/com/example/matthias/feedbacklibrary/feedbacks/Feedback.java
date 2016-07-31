package com.example.matthias.feedbacklibrary.feedbacks;

import com.example.matthias.feedbacklibrary.models.Mechanism;
import com.example.matthias.feedbacklibrary.models.RatingMechanism;
import com.example.matthias.feedbacklibrary.models.TextMechanism;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Feedback
 */
public class Feedback implements Serializable {
    private String text;
    private String application;
    private String title;
    private float configVersion;
    private String user;

    private List<RatingFeedback> ratings;

    public Feedback(List<Mechanism> allMechanisms) {
        ratings = new ArrayList<>();
        for (Mechanism mechanism : allMechanisms) {
            if (mechanism.isActive()) {
                String type = mechanism.getType();
                switch (type) {
                    case Mechanism.AUDIO_TYPE:
                        // TODO: Implement it here (multipart)?
                        break;
                    case Mechanism.CHOICE_TYPE:
                        break;
                    case Mechanism.RATING_TYPE:
                        RatingFeedback ratingFeedback = new RatingFeedback();
                        ratingFeedback.setTitle(((RatingMechanism) mechanism).getTitle());
                        ratingFeedback.setRating((int) (((RatingMechanism) mechanism).getInputRating()));
                        ratings.add(ratingFeedback);
                        break;
                    case Mechanism.SCREENSHOT_TYPE:
                        // TODO: Implement it here (multipart)?
                        break;
                    case Mechanism.TEXT_TYPE:
                        this.title = ((TextMechanism) mechanism).getTitle();
                        this.text = ((TextMechanism) mechanism).getInputText();
                        break;
                    default:
                        // should never happen!
                        break;
                }

            }
        }
    }

    public String getApplication() {
        return application;
    }

    public float getConfigVersion() {
        return configVersion;
    }

    public List<RatingFeedback> getRatings() {
        return ratings;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public String getUser() {
        return user;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public void setConfigVersion(float configVersion) {
        this.configVersion = configVersion;
    }

    public void setRatings(List<RatingFeedback> ratings) {
        this.ratings = ratings;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
