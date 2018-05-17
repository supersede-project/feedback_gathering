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
import ch.uzh.supersede.feedbacklibrary.components.views.AbstractMechanismView;
import ch.uzh.supersede.feedbacklibrary.components.views.AudioMechanismView;
import ch.uzh.supersede.feedbacklibrary.components.views.CategoryMechanismView;
import ch.uzh.supersede.feedbacklibrary.components.views.RatingMechanismView;
import ch.uzh.supersede.feedbacklibrary.components.views.ScreenshotMechanismView;
import ch.uzh.supersede.feedbacklibrary.components.views.TextMechanismView;
import ch.uzh.supersede.feedbacklibrary.models.AbstractMechanism;
import ch.uzh.supersede.feedbacklibrary.models.AudioMechanism;
import ch.uzh.supersede.feedbacklibrary.models.CategoryMechanism;
import ch.uzh.supersede.feedbacklibrary.models.RatingMechanism;
import ch.uzh.supersede.feedbacklibrary.models.ScreenshotMechanism;
import ch.uzh.supersede.feedbacklibrary.models.TextMechanism;
import ch.uzh.supersede.feedbacklibrary.utils.Utils;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.StubsConstants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ADVANCED;


public class OrchestratorStub {
    private ArrayList<AbstractMechanismView> mechanismViews;
    private ArrayList<AbstractMechanism> mechanisms;
    private int id;

    private OrchestratorStub() {
    }

    public List<AbstractMechanismView> getMechanismViews() {
        return this.mechanismViews;
    }

    public List<AbstractMechanism> getMechanisms() {
        return mechanisms;
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

    public static class MechanismBuilder<T extends Activity> {
        private LocalConfigurationBean configuration;
        private List<AbstractMechanismView> mechanismViews;
        private List<AbstractMechanism> mechanisms;
        private Context context;
        private LayoutInflater layoutInflater;
        private LinearLayout rootLayout;
        private Resources resources;
        private T activity;
        private int id;

        public MechanismBuilder(T activity, Context context, Resources resources, LocalConfigurationBean configuration, LinearLayout rootLayout, LayoutInflater layoutInflater) {
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

        @Deprecated
        public MechanismBuilder withAttachment() {
            resolve(ATTACHMENT_TYPE, 0);
            return this;
        }

        public MechanismBuilder withAudio() {
            if (ADVANCED.check(context) && configuration.getAudioOrder() != -1) {
                resolve(AUDIO_TYPE, configuration.getAudioOrder());
            }
            return this;
        }

        public MechanismBuilder withCategory() {
            if (configuration.getCategoryOrder() != -1) {
                resolve(CATEGORY_TYPE, configuration.getCategoryOrder());
            }
            return this;
        }

        @Deprecated
        public MechanismBuilder withDialog() {
            resolve(DIALOG_TYPE, 0);
            return this;
        }

        @Deprecated
        public MechanismBuilder withImage() {
            resolve(IMAGE_TYPE, 0);
            return this;
        }

        public MechanismBuilder withRating() {
            if (configuration.getRatingOrder() != -1) {
                resolve(RATING_TYPE, configuration.getRatingOrder());
            }
            return this;
        }

        public MechanismBuilder withScreenshot() {
            if (configuration.getScreenshotOrder() != -1) {
                resolve(SCREENSHOT_TYPE, configuration.getScreenshotOrder());
            }
            return this;
        }

        public MechanismBuilder withText() {
            if (configuration.getTextOrder() != -1) {
                resolve(TEXT_TYPE, configuration.getTextOrder());
            }
            return this;
        }

        public OrchestratorStub build(List<AbstractMechanismView> mechanismViews) {
            OrchestratorStub stub = new OrchestratorStub();
            Collections.sort(this.mechanismViews);
            for (AbstractMechanismView view : this.mechanismViews) {
                mechanismViews.add(view);
                rootLayout.addView(view.getEnclosingLayout());
            }
            stub.mechanisms = new ArrayList<>(this.mechanisms);
            stub.mechanismViews = new ArrayList<>(this.mechanismViews);
            stub.id = this.id;
            return stub;
        }

        private void resolve(String type, int order) {
            switch (type) {
                case AUDIO_TYPE:
                    AudioMechanism audioMechanism = new AudioMechanism(order, order);
                    AudioMechanismView audioMechanismView = new AudioMechanismView(layoutInflater, audioMechanism, resources, activity, context);
                    mechanisms.add(audioMechanism);
                    mechanismViews.add(audioMechanismView);
                    break;
                case CATEGORY_TYPE:
                    CategoryMechanism categoryMechanism = new CategoryMechanism(order, order);
                    CategoryMechanismView categoryMechanismView = new CategoryMechanismView(layoutInflater, categoryMechanism);
                    mechanisms.add(categoryMechanism);
                    mechanismViews.add(categoryMechanismView);
                    break;
                case RATING_TYPE:
                    RatingMechanism ratingMechanism = new RatingMechanism(order, order);
                    RatingMechanismView ratingMechanismView = new RatingMechanismView(layoutInflater, ratingMechanism);
                    mechanisms.add(ratingMechanism);
                    mechanismViews.add(ratingMechanismView);
                    break;
                case SCREENSHOT_TYPE:
                    ScreenshotMechanism screenshotMechanism = new ScreenshotMechanism(order, order);
                    ScreenshotMechanismView screenshotMechanismView = new ScreenshotMechanismView(layoutInflater, activity, screenshotMechanism, id);
                    mechanisms.add(screenshotMechanism);
                    mechanismViews.add(screenshotMechanismView);
                    break;
                case TEXT_TYPE:
                    TextMechanism textMechanism = new TextMechanism(order, order);
                    TextMechanismView textMechanismView = new TextMechanismView(layoutInflater, textMechanism);
                    mechanisms.add(textMechanism);
                    mechanismViews.add(textMechanismView);
                    break;
                default:
                    break;
            }
            id++;
        }
    }

    private static class OrchestratorParamBuilder {
        private List<Map<String, Object>> list;

        private OrchestratorParamBuilder() {
            this.list = new ArrayList<>();
        }

        public OrchestratorParamBuilder(List<Map<String, Object>> list) {
            this.list = list;
        }

        public static OrchestratorParamBuilder instance() {
            return new OrchestratorParamBuilder();
        }

        public Builder key(Object key) {
            HashMap<String, Object> map = new HashMap<>();
            map.put(ORCHESTRATOR_KEY, key);
            return new Builder(list, map);
        }

        public List<Map<String, Object>> get() {
            return list;
        }

        private static class Builder {
            private List<Map<String, Object>> list;
            Map<String, Object> map;

            public Builder(List<Map<String, Object>> list, Map<String, Object> map) {
                this.list = list;
                this.map = map;
            }

            public OrchestratorParamBuilder value(Object value) {
                map.put(ORCHESTRATOR_VALUE, value);
                list.add(map);
                return new OrchestratorParamBuilder(list);
            }
        }
    }
}
