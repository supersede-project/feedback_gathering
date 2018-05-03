package ch.uzh.supersede.host;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.LinearLayout;

import ch.uzh.supersede.feedbacklibrary.activities.AbstractBaseActivity;
import ch.uzh.supersede.feedbacklibrary.entrypoint.*;

public class HostActivity extends AbstractBaseActivity implements IFeedbackBehavior, IFeedbackStyle, IFeedbackSettings, IFeedbackLayoutConfiguration, IFeedbackDeveloper {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        LinearLayout linearLayout = getView(R.id.root_layout,LinearLayout.class);
        CoordinatorLayout cLayout = getView(R.id.coordinator_layout,CoordinatorLayout.class);
        double colorDouble = ((double)Color.BLACK)*Math.random();
        int color = (int)colorDouble;
        linearLayout.setBackgroundColor(color);
        cLayout.setBackgroundColor(color);
    }

    public void onFeedbackClicked(View view){
        FeedbackConnector.getInstance().connect(view,this);
    }

    //Feedback Layout
    @Override
    public int getConfiguredAudioFeedbackOrder() {
        return -1;
    }

    @Override
    public int getConfiguredScreenshotFeedbackOrder() {
        return 2;
    }

    @Override
    public int getConfiguredCategoryFeedbackOrder() {
        return -1;
    }

    @Override
    public int getConfiguredTextFeedbackOrder() {
        return -1;
    }

    @Override
    public int getConfiguredRatingFeedbackOrder() {
        return 1;
    }

    //Feedback Settings
    @Override
    public int getConfiguredMinUserNameLength() {
        return 3;
    }

    @Override
    public int getConfiguredMaxUserNameLength() {
        return 10;
    }

    @Override
    public int getConfiguredMinResponseLength() {
        return 10;
    }

    @Override
    public int getConfiguredMaxResponseLength() {
        return 30;
    }

    //Feedback Developer
    @Override
    public boolean isDeveloper() {
        return true;
    }

    //Feedback Behaviour
    @Override
    public int getConfiguredPullIntervalMinutes() {
        return 0;
    }

    //Feedback Layout
    @Override
    public FEEDBACK_STYLE getConfiguredFeedbackStyle() {
        return null;
    }
}