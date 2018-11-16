package uzh.ch.supersede.host;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

import ch.uzh.supersede.feedbacklibrary.entrypoint.*;
import ch.uzh.supersede.feedbacklibrary.utils.ColorUtility;

@SuppressWarnings({"squid:MaximumInheritanceDepth", "squid:S1170"})
public class HostActivity extends AppCompatActivity implements
        IFeedbackStyleConfiguration,
        IFeedbackBehaviorConfiguration,
        IFeedbackEndpointConfiguration,
        IFeedbackDeveloperConfiguration{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.root_layout);
        Button feedbackButton = (Button) findViewById(R.id.host_button_feedback);
        TextView mode = (TextView) findViewById(R.id.host_text_mode);
        TextView karma = (TextView) findViewById(R.id.host_text_karma);
        CoordinatorLayout cLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        double primaryColorDouble = ((double) Color.BLACK) * Math.random();
        int primaryColor = (int) primaryColorDouble;
        linearLayout.setBackgroundColor(primaryColor);
        cLayout.setBackgroundColor(primaryColor);

        double secondaryColorDouble = ((double) Color.BLACK) * Math.random();
        int secondaryColor = (int) secondaryColorDouble;
        feedbackButton.setBackgroundColor(secondaryColor);
        feedbackButton.setTextColor(ColorUtility.isDark(secondaryColor)? Color.WHITE:Color.BLACK);
        mode.setBackgroundColor(secondaryColor);
        mode.setTextColor(ColorUtility.isDark(secondaryColor)? Color.WHITE:Color.BLACK);
        mode.setText(getString(R.string.host_mode,isDeveloper()?getString(R.string.host_developer):getString(R.string.host_user)));
        karma.setBackgroundColor(secondaryColor);
        karma.setTextColor(ColorUtility.isDark(secondaryColor)? Color.WHITE:Color.BLACK);
        karma.setText(getString(R.string.host_karma,0));



        Integer currentUserKarma = FeedbackConnector.getInstance().getCurrentUserKarma(this);
        if (currentUserKarma != null) {
            karma.setText(getString(R.string.host_karma,currentUserKarma));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView karma = (TextView) findViewById(R.id.host_text_karma);
        Integer currentUserKarma = FeedbackConnector.getInstance().getCurrentUserKarma(this);
        if (currentUserKarma != null) {
            karma.setText(getString(R.string.host_karma,currentUserKarma));
        }
    }

    public void onFeedbackClicked(View view) {
        FeedbackConnector.getInstance().connect(view, this);
    }


    @Override
    public String getConfiguredEndpointLogin() {
        return "admin";
    }

    @Override
    public String getConfiguredEndpointPassword() {
        return "password";
    }

    @Override
    public FEEDBACK_STYLE getConfiguredFeedbackStyle() {
        return FEEDBACK_STYLE.CUSTOM;
    }

    @Override
    public int[] getConfiguredCustomStyle() {
        return new int[]{-16047514, -9992786, -5126707};
    }

    @Override
    public String getConfiguredEndpointUrl() {
        return "http://server1108.cs.technik.fhnw.ch/feedback_repository/";
    }

    @Override
    public int getConfiguredPullIntervalMinutes() {
        return 1;
    }

    @Override
    public boolean isDeveloper() {
        return BuildConfig.IS_DEVELOPER;
    }
}