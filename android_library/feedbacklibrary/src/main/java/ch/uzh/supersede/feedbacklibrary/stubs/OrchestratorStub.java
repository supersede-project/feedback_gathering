package ch.uzh.supersede.feedbacklibrary.stubs;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.*;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.beans.LocalConfigurationBean;
import ch.uzh.supersede.feedbacklibrary.components.views.AbstractFeedbackPartView;
import ch.uzh.supersede.feedbacklibrary.components.views.AudioMechanismView;
import ch.uzh.supersede.feedbacklibrary.components.views.LabelMechanismView;
import ch.uzh.supersede.feedbacklibrary.components.views.RatingMechanismView;
import ch.uzh.supersede.feedbacklibrary.components.views.ScreenshotMechanismView;
import ch.uzh.supersede.feedbacklibrary.components.views.TextMechanismView;
import ch.uzh.supersede.feedbacklibrary.models.AbstractFeedbackPart;
import ch.uzh.supersede.feedbacklibrary.models.AudioFeedback;
import ch.uzh.supersede.feedbacklibrary.models.LabelFeedback;
import ch.uzh.supersede.feedbacklibrary.models.RatingFeedback;
import ch.uzh.supersede.feedbacklibrary.models.ScreenshotFeedback;
import ch.uzh.supersede.feedbacklibrary.models.TextFeedback;
import ch.uzh.supersede.feedbacklibrary.utils.Utils;

import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ADVANCED;


public class OrchestratorStub {
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

    public static void receiveFeedback(Activity activity, View view) {
        //TODO: Evaluation, Store etc
        Toast toast = Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.feedback_success), Toast.LENGTH_SHORT);
        toast.show();
        Utils.wipeImages(activity);
        activity.onBackPressed();
    }

    public static class FeedbackBuilder<T extends Activity> {
        private LocalConfigurationBean configuration;
        private List<AbstractFeedbackPartView> mechanismViews;
        private List<AbstractFeedbackPart> mechanisms;
        private Context context;
        private LayoutInflater layoutInflater;
        private LinearLayout rootLayout;
        private Resources resources;
        private T activity;
        private int id;

        public FeedbackBuilder(T activity, Context context, Resources resources, LocalConfigurationBean configuration, LinearLayout rootLayout, LayoutInflater layoutInflater) {
            this.mechanismViews = new ArrayList<>();
            this.mechanisms = new ArrayList<>();
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
                AudioFeedback audioFeedback = new AudioFeedback(id, configuration);
                AudioMechanismView audioMechanismView = new AudioMechanismView(layoutInflater, audioFeedback, resources, activity, context);
                mechanisms.add(audioFeedback);
                mechanismViews.add(audioMechanismView);
                id++;
            }
            return this;
        }

        public FeedbackBuilder withCategory() {
            if (configuration.getLabelOrder() != -1) {
                LabelFeedback labelFeedback = new LabelFeedback(id, configuration);
                LabelMechanismView labelMechanismView = new LabelMechanismView(layoutInflater, labelFeedback);
                mechanisms.add(labelFeedback);
                mechanismViews.add(labelMechanismView);
                id++;
            }
            return this;
        }

        public FeedbackBuilder withRating() {
            if (configuration.getRatingOrder() != -1) {
                RatingFeedback ratingFeedback = new RatingFeedback(id, configuration);
                RatingMechanismView ratingMechanismView = new RatingMechanismView(layoutInflater, ratingFeedback);
                mechanisms.add(ratingFeedback);
                mechanismViews.add(ratingMechanismView);
                id++;
            }
            return this;
        }

        public FeedbackBuilder withScreenshot() {
            if (configuration.getScreenshotOrder() != -1) {
                ScreenshotFeedback screenshotFeedback = new ScreenshotFeedback(id, configuration);
                ScreenshotMechanismView screenshotMechanismView = new ScreenshotMechanismView(layoutInflater, activity, screenshotFeedback, id);
                mechanisms.add(screenshotFeedback);
                mechanismViews.add(screenshotMechanismView);
                id++;
            }
            return this;
        }

        public FeedbackBuilder withText() {
            if (configuration.getTextOrder() != -1) {
                TextFeedback textFeedback = new TextFeedback(id, configuration);
                TextMechanismView textMechanismView = new TextMechanismView(layoutInflater, textFeedback);
                mechanisms.add(textFeedback);
                mechanismViews.add(textMechanismView);
                id++;
            }
            return this;
        }

        public FeedbackBuilder withTitle(){
            throw new UnsupportedOperationException("Not yet implemented!");
        }

        public OrchestratorStub build(List<AbstractFeedbackPartView> mechanismViews) {
            OrchestratorStub stub = new OrchestratorStub();
            Collections.sort(this.mechanismViews);
            for (AbstractFeedbackPartView view : this.mechanismViews) {
                mechanismViews.add(view);
                rootLayout.addView(view.getEnclosingLayout());
            }
            stub.feedbackParts = new ArrayList<>(this.mechanisms);
            stub.feedbackPartViews = new ArrayList<>(this.mechanismViews);
            stub.id = this.id;
            return stub;
        }
    }
}
