package ch.uzh.supersede.host;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

import ch.uzh.supersede.feedbacklibrary.entrypoint.*;
import ch.uzh.supersede.feedbacklibrary.utils.DefaultConfiguration;

@SuppressWarnings({"squid:MaximumInheritanceDepth", "squid:S1170"})
public class HostActivity extends AppCompatActivity implements
        IFeedbackBehaviorConfiguration,
        IFeedbackStyleConfiguration,
        IFeedbackSettingsConfiguration,
        IFeedbackDeveloperConfiguration,
        ISimpleFeedbackConfiguration,
        IFeedbackEndpointConfiguration{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.root_layout);
        Button feedbackButton = (Button)findViewById(R.id.button_host_feedback);
        Button dummyButton1 = (Button)findViewById(R.id.button_host_trigger_1);
        Button dummyButton2 = (Button)findViewById(R.id.button_host_trigger_2);
        CoordinatorLayout cLayout = (CoordinatorLayout)findViewById(R.id.coordinator_layout);
        double primaryColorDouble = ((double) Color.BLACK) * Math.random();
        int primaryColor = (int) primaryColorDouble;
        linearLayout.setBackgroundColor(primaryColor);
        cLayout.setBackgroundColor(primaryColor);

        double secondaryColorDouble = ((double) Color.BLACK) * Math.random();
        int secondaryColor = (int) secondaryColorDouble;
        feedbackButton.setBackgroundColor(secondaryColor);
        dummyButton1.setBackgroundColor(secondaryColor);
        dummyButton2.setBackgroundColor(secondaryColor);

        Integer currentUserKarma = FeedbackConnector.getInstance().getCurrentUserKarma(this);
        if (currentUserKarma != null) {
            Toast.makeText(getApplicationContext(), "Karma of current user = " + currentUserKarma, Toast.LENGTH_LONG).show();
        }
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
    public String getConfiguredEndpointLogin() {
        return "admin";
    }

    @Override
    public String getConfiguredEndpointPassword() {
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
        return FEEDBACK_STYLE.CUSTOM;
    }

    @Override
    public int[] getConfiguredCustomStyle() {
        return new int[]{-179768107,-12768107};
    }

    @Override
    public String getConfiguredEndpointUrl() {
        return "http://mt.ronnieschaniel.com:8080/feedback_repository/";
    }
}