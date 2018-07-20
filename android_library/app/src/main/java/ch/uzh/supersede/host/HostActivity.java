package ch.uzh.supersede.host;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import ch.uzh.supersede.feedbacklibrary.activities.AbstractBaseActivity;
import ch.uzh.supersede.feedbacklibrary.entrypoint.*;
import ch.uzh.supersede.feedbacklibrary.utils.DefaultConfiguration;

@SuppressWarnings({"squid:MaximumInheritanceDepth", "squid:S1170"})
public class HostActivity extends AbstractBaseActivity implements
        IFeedbackBehaviorConfiguration,
        IFeedbackStyleConfiguration,
        IFeedbackSettingsConfiguration,
        IFeedbackDeveloperConfiguration,
        ISimpleFeedbackConfiguration {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        LinearLayout linearLayout = getView(R.id.root_layout, LinearLayout.class);
        Button feedbackButton = getView(R.id.button_host_feedback, Button.class);
        Button dummyButton1 = getView(R.id.button_host_trigger_1, Button.class);
        Button dummyButton2 = getView(R.id.button_host_trigger_2, Button.class);
        CoordinatorLayout cLayout = getView(R.id.coordinator_layout, CoordinatorLayout.class);
        double primaryColorDouble = ((double) Color.BLACK) * Math.random();
        int primaryColor = (int) primaryColorDouble;
        linearLayout.setBackgroundColor(primaryColor);
        cLayout.setBackgroundColor(primaryColor);

        double secondaryColorDouble = ((double) Color.BLACK) * Math.random();
        int secondaryColor = (int) secondaryColorDouble;
        feedbackButton.setBackgroundColor(secondaryColor);
        dummyButton1.setBackgroundColor(secondaryColor);
        dummyButton2.setBackgroundColor(secondaryColor);
    }

    public void onFeedbackClicked(View view) {
        FeedbackConnector.getInstance().connect(view, this);
    }

    //Feedback Layout
    @Override
    public int getConfiguredAudioFeedbackOrder() {
        return DefaultConfiguration.getInstance().getConfiguredAudioFeedbackOrder();
    }

    @Override
    public int getConfiguredScreenshotFeedbackOrder() {
        return DefaultConfiguration.getInstance().getConfiguredScreenshotFeedbackOrder();
    }

    @Override
    public int getConfiguredTextFeedbackOrder() {
        return DefaultConfiguration.getInstance().getConfiguredTextFeedbackOrder();
    }

    @Override
    public int getConfiguredRatingFeedbackOrder() {
        return DefaultConfiguration.getInstance().getConfiguredRatingFeedbackOrder();
    }

    //Feedback Settings
    @Override
    public int getConfiguredMinUserNameLength() {
        return 3;
    }

    @Override
    public int getConfiguredMaxUserNameLength() {
        return 100;
    }

    @Override
    public int getConfiguredMinResponseLength() {
        return 10;
    }

    @Override
    public int getConfiguredMaxResponseLength() {
        return 30;
    }

    @Override
    public int getConfiguredMinReportLength() {
        return 5;
    }

    @Override
    public int getConfiguredMaxReportLength() {
        return 100;
    }

    @Override
    public boolean getConfiguredReportEnabled() {
        return true;
    }

    //Feedback Developer
    @Override
    public boolean isDeveloper() {
        return false;
    }

    @Override
    public String getConfiguredRepositoryLogin() {
        return "super_admin";
    }

    @Override
    public String getConfiguredRepositoryPassword() {
        return "password";
    }

    //Feedback Behavior
    @Override
    public int getConfiguredPullIntervalMinutes() {
        return 1;
    }

    //Feedback Layout
    @Override
    public FEEDBACK_STYLE getConfiguredFeedbackStyle() {
        return FEEDBACK_STYLE.DARK;
    }
}