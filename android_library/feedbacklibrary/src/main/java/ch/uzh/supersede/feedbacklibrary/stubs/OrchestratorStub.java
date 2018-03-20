package ch.uzh.supersede.feedbacklibrary.stubs;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.uzh.supersede.feedbacklibrary.configurations.MechanismConfigurationItem;
import ch.uzh.supersede.feedbacklibrary.models.AudioMechanism;
import ch.uzh.supersede.feedbacklibrary.models.CategoryMechanism;
import ch.uzh.supersede.feedbacklibrary.models.Mechanism;
import ch.uzh.supersede.feedbacklibrary.models.RatingMechanism;
import ch.uzh.supersede.feedbacklibrary.models.ScreenshotMechanism;
import ch.uzh.supersede.feedbacklibrary.models.TextMechanism;
import ch.uzh.supersede.feedbacklibrary.views.AudioMechanismView;
import ch.uzh.supersede.feedbacklibrary.views.CategoryMechanismView;
import ch.uzh.supersede.feedbacklibrary.views.MechanismView;
import ch.uzh.supersede.feedbacklibrary.views.RatingMechanismView;
import ch.uzh.supersede.feedbacklibrary.views.ScreenshotMechanismView;
import ch.uzh.supersede.feedbacklibrary.views.ScreenshotMechanismView.OnImageChangedListener;
import ch.uzh.supersede.feedbacklibrary.views.TextMechanismView;

import static ch.uzh.supersede.feedbacklibrary.models.Mechanism.ATTACHMENT_TYPE;
import static ch.uzh.supersede.feedbacklibrary.models.Mechanism.AUDIO_TYPE;
import static ch.uzh.supersede.feedbacklibrary.models.Mechanism.CATEGORY_TYPE;
import static ch.uzh.supersede.feedbacklibrary.models.Mechanism.DIALOG_TYPE;
import static ch.uzh.supersede.feedbacklibrary.models.Mechanism.IMAGE_TYPE;
import static ch.uzh.supersede.feedbacklibrary.models.Mechanism.RATING_TYPE;
import static ch.uzh.supersede.feedbacklibrary.models.Mechanism.SCREENSHOT_TYPE;
import static ch.uzh.supersede.feedbacklibrary.models.Mechanism.TEXT_TYPE;

/**
 * Created by Marco on 15.03.2018.
 */

public class OrchestratorStub {
    private ArrayList<MechanismView> mechanismViews;
    private OrchestratorStub() {
        mechanismViews = new ArrayList<>();
    }

    public List<MechanismView> getMechanismViews(){
        return this.mechanismViews;
    }


    public void addAll(LinearLayout linearLayout, List<MechanismView> mechanismViews) {
    }

    public static class MechanismBuilder <T extends Activity & OnImageChangedListener> {
        private ArrayList<MechanismView> viewList;
        private Context context;
        private LayoutInflater layoutInflater;
        private LinearLayout rootLayout;
        private Resources resources;
        private T activity;
        private String imagePath;
        private int id;

        public  MechanismBuilder(T activity, Context context, Resources resources, LinearLayout rootLayout, String imagePath) {
            viewList = new ArrayList<>();
            id = 0;
            this.context = context;
            this.layoutInflater = LayoutInflater.from(context);
            this.resources = resources;
            this.rootLayout = rootLayout;
            this.activity = activity;
            this.imagePath = imagePath;
        }

        public MechanismBuilder withAttachment() {
            resolve(ATTACHMENT_TYPE);
            return this;
        }

        public MechanismBuilder withAudio() {
            resolve(AUDIO_TYPE);
            return this;
        }

        public MechanismBuilder withCategory() {
            resolve(CATEGORY_TYPE);
            return this;
        }

        public MechanismBuilder withDialog() {
            resolve(DIALOG_TYPE);
            return this;
        }

        public MechanismBuilder withImage() {
            resolve(IMAGE_TYPE);
            return this;
        }

        public MechanismBuilder withRating() {
            resolve(RATING_TYPE);
            return this;
        }

        public MechanismBuilder withScreenshot() {
            resolve(SCREENSHOT_TYPE);
            return this;
        }

        public MechanismBuilder withText() {
            resolve(TEXT_TYPE);
            return this;
        }

        public OrchestratorStub build(List<MechanismView> mechanismViews) {
            OrchestratorStub stub = new OrchestratorStub();
            for (MechanismView view : viewList){
                mechanismViews.add(view);
                rootLayout.addView(view.getEnclosingLayout());
            }
            return stub;
        }

        private void resolve(String type) {
            MechanismConfigurationItem configurationItem = new MechanismConfigurationItem();
            configurationItem.setCanBeActivated(true);
            configurationItem.setActive(true);
            configurationItem.setOrder(id);
            configurationItem.setId(id++);
            switch (type) {
                case ATTACHMENT_TYPE:
                    //TODO: Gab es nie, implementieren!
                    break;
                case AUDIO_TYPE:
                    configurationItem.setType(AUDIO_TYPE);
                    AudioMechanism audioMechanism = new AudioMechanism(configurationItem);
                    //DIESER CODE TUT WEH -->
                    Map<String,Object> params = new HashMap<>();
                    params.put("key","maxTime");
                    params.put("value","Audio-Mechanism");
                    List<Map<String,Object>> paramList = new ArrayList<>();
                    paramList.add(params);
                    configurationItem.setParameters(paramList);
                    //<---
                    AudioMechanismView audioMechanismView = new AudioMechanismView(layoutInflater, audioMechanism, resources, activity, context);
                    viewList.add(audioMechanismView);
                    break;
                case CATEGORY_TYPE:
                    configurationItem.setType(CATEGORY_TYPE);
                    CategoryMechanism categoryMechanism = new CategoryMechanism(configurationItem);
                    CategoryMechanismView categoryMechanismView = new CategoryMechanismView(layoutInflater, categoryMechanism);
                    viewList.add(categoryMechanismView);
                    break;
                case DIALOG_TYPE:
                    //TODO: Gab es nie, implementieren!
                    break;
                case IMAGE_TYPE:
                    //TODO: Gab es nie, implementieren!
                    break;
                case RATING_TYPE:
                    configurationItem.setType(RATING_TYPE);
                    RatingMechanism ratingMechanism = new RatingMechanism(configurationItem);
                    RatingMechanismView ratingMechanismView = new RatingMechanismView(layoutInflater, ratingMechanism);
                    viewList.add(ratingMechanismView);
                    break;
                case SCREENSHOT_TYPE:
                    configurationItem.setType(SCREENSHOT_TYPE);
                    ScreenshotMechanism screenshotMechanism = new ScreenshotMechanism(configurationItem);
                    ScreenshotMechanismView screenshotMechanismView = new ScreenshotMechanismView(layoutInflater, screenshotMechanism, activity, id, imagePath);
                    viewList.add(screenshotMechanismView);
                    break;
                case TEXT_TYPE:
                    configurationItem.setType(TEXT_TYPE);
                    TextMechanism textMechanism = new TextMechanism(configurationItem);
                    TextMechanismView textMechanismView = new TextMechanismView(layoutInflater, textMechanism);
                    viewList.add(textMechanismView);
                    break;
                default:
                    break;
            }
        }
    }
}
