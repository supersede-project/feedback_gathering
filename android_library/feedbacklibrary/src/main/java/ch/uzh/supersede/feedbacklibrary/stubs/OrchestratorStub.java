package ch.uzh.supersede.feedbacklibrary.stubs;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.beans.LocalConfigurationBean;
import ch.uzh.supersede.feedbacklibrary.components.views.AbstractFeedbackPartView;
import ch.uzh.supersede.feedbacklibrary.components.views.AudioFeedbackView;
import ch.uzh.supersede.feedbacklibrary.components.views.RatingFeedbackView;
import ch.uzh.supersede.feedbacklibrary.components.views.ScreenshotFeedbackView;
import ch.uzh.supersede.feedbacklibrary.components.views.TextFeedbackView;
import ch.uzh.supersede.feedbacklibrary.models.AbstractFeedbackPart;
import ch.uzh.supersede.feedbacklibrary.models.AudioFeedback;
import ch.uzh.supersede.feedbacklibrary.models.RatingFeedback;
import ch.uzh.supersede.feedbacklibrary.models.ScreenshotFeedback;
import ch.uzh.supersede.feedbacklibrary.models.TextFeedback;
import ch.uzh.supersede.feedbacklibrary.utils.ImageUtility;

import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ADVANCED;


public final class OrchestratorStub {
    private ArrayList<AbstractFeedbackPartView> feedbackPartViews;
    private ArrayList<AbstractFeedbackPart> feedbackParts;
    private int id;

    private OrchestratorStub() {
    }

    public List<AbstractFeedbackPartView> getFeedbackPartViews() {
        return this.feedbackPartViews;
    }

    public List<AbstractFeedbackPart> getFeedbackParts() {
        return feedbackParts;
    }

    public int getId() {
        return id;
    }

    public static void receiveFeedback(Activity activity) {
        Toast toast = Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.feedback_success), Toast.LENGTH_SHORT);
        toast.show();
        ImageUtility.wipeImages(activity);
        activity.onBackPressed();
    }

    public static class FeedbackBuilder<T extends Activity> {
        private LocalConfigurationBean configuration;
        private List<AbstractFeedbackPartView> feedbackPartViews;
        private List<AbstractFeedbackPart> feedbackParts;
        private Context context;
        private LayoutInflater layoutInflater;
        private LinearLayout rootLayout;
        private Resources resources;
        private T activity;
        private int id;

        public FeedbackBuilder(T activity, Context context, Resources resources, LocalConfigurationBean configuration, LinearLayout rootLayout, LayoutInflater layoutInflater) {
            this.feedbackPartViews = new ArrayList<>();
            this.feedbackParts = new ArrayList<>();
            this.configuration = configuration;
            this.id = 0;
            this.context = context;
            this.layoutInflater = layoutInflater;
            this.resources = resources;
            this.rootLayout = rootLayout;
            this.activity = activity;
        }

        public FeedbackBuilder withAudio() {
            if (ADVANCED.check(context) && configuration.getAudioOrder() != -1) {
                AudioFeedback audioFeedback = new AudioFeedback(configuration);
                AudioFeedbackView audioMechanismView = new AudioFeedbackView(layoutInflater, audioFeedback, resources, activity, context);
                audioMechanismView.colorView(configuration.getTopColors());
                feedbackParts.add(audioFeedback);
                feedbackPartViews.add(audioMechanismView);
                id++;
            }
            return this;
        }

        public FeedbackBuilder withRating() {
            if (configuration.getRatingOrder() != -1) {
                RatingFeedback ratingFeedback = new RatingFeedback(configuration);
                RatingFeedbackView ratingFeedbackView = new RatingFeedbackView(layoutInflater, ratingFeedback);
                ratingFeedbackView.colorView(configuration.getTopColors());
                feedbackParts.add(ratingFeedback);
                feedbackPartViews.add(ratingFeedbackView);
                id++;
            }
            return this;
        }

        public FeedbackBuilder withScreenshot() {
            if (configuration.getScreenshotOrder() != -1) {
                ScreenshotFeedback screenshotFeedback = new ScreenshotFeedback(configuration);
                ScreenshotFeedbackView screenshotFeedbackView = new ScreenshotFeedbackView(layoutInflater, activity, screenshotFeedback);
                screenshotFeedbackView.colorView(configuration.getTopColors());
                feedbackParts.add(screenshotFeedback);
                feedbackPartViews.add(screenshotFeedbackView);
                id++;
            }
            return this;
        }

        public FeedbackBuilder withText() {
            if (configuration.getTextOrder() != -1) {
                TextFeedback textFeedback = new TextFeedback(configuration);
                TextFeedbackView textFeedbackView = new TextFeedbackView(layoutInflater, textFeedback);
                textFeedbackView.colorView(configuration.getTopColors());
                feedbackParts.add(textFeedback);
                feedbackPartViews.add(textFeedbackView);
                id++;
            }
            return this;
        }

        public FeedbackBuilder withTitle() {
            throw new UnsupportedOperationException("Not yet implemented!");
        }

        @SuppressWarnings("unhecked")
        public OrchestratorStub build(List<AbstractFeedbackPartView> feedbackPartViews) {
            OrchestratorStub stub = new OrchestratorStub();
            Collections.sort(this.feedbackPartViews);
            for (AbstractFeedbackPartView view : this.feedbackPartViews) {
                feedbackPartViews.add(view);
                rootLayout.addView(view.getEnclosingLayout());
            }
            stub.feedbackParts = new ArrayList<>(this.feedbackParts);
            stub.feedbackPartViews = new ArrayList<>(this.feedbackPartViews);
            stub.id = this.id;
            return stub;
        }
    }
}
