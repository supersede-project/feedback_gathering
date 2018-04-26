package ch.uzh.supersede.feedbacklibrary.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.BuildConfig;
import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.components.buttons.AbstractSettingsListItem;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService;
import ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener;

import static ch.uzh.supersede.feedbacklibrary.utils.Enums.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.SETTINGS_VIEW.*;


@SuppressWarnings("squid:MaximumInheritanceDepth")
public class FeedbackSettingsActivity extends AbstractBaseActivity implements IFeedbackServiceEventListener {

    private LinearLayout scrollListLayout;
    private SETTINGS_VIEW currentViewState = MINE;

    private Button myButton;
    private Button othersButton;
    private Button settingsButton;

    private ArrayList<AbstractSettingsListItem> myFeedbackList = new ArrayList<>();
    private ArrayList<AbstractSettingsListItem> othersFeedbackList = new ArrayList<>();
    private ArrayList<AbstractSettingsListItem> settingsFeedbackList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_settings);

        scrollListLayout = getView(R.id.settings_layout_scroll, LinearLayout.class);

        myButton = setOnClickListener(getView(R.id.settings_button_mine, Button.class));
        othersButton = setOnClickListener(getView(R.id.settings_button_others, Button.class));
        settingsButton = setOnClickListener(getView(R.id.settings_button_settings, Button.class));
        onPostCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        execFillFeedbackList();
        toggleButtons(getViewByState(currentViewState));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onEventCompleted(EventType eventType, Object response) {
        if (BuildConfig.DEBUG) {
            switch (eventType) {
                case GET_MINE_FEEDBACK_VOTES:
                    myFeedbackList = (ArrayList<AbstractSettingsListItem>) response;
                    break;
                case GET_OTHERS_FEEDBACK_VOTES:
                    othersFeedbackList = (ArrayList<AbstractSettingsListItem>) response;
                    break;
                case GET_FEEDBACK_SETTINGS:
                    settingsFeedbackList = (ArrayList<AbstractSettingsListItem>) response;
                    break;
                default:
            }
        }
        //TODO [jfo] real implementation
    }

    @Override
    public void onEventFailed(EventType eventType, Object response) {
        //TODO [jfo] implementation
    }

    @Override
    public void onConnectionFailed(EventType eventType) {
        //TODO [jfo] implementation
    }

    private void execFillFeedbackList() {
        FeedbackService.getInstance().getOthersFeedbackVotes(this, this);
        FeedbackService.getInstance().getMineFeedbackVotes(this, this);
        FeedbackService.getInstance().getFeedbackSettings(this, this);
    }

    private Button setOnClickListener(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButtons(v);
            }
        });
        return button;
    }

    private void toggleButtons(View v) {
        setInactive(myButton, othersButton, settingsButton);
        v.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.blue_tab));
        ((Button) v).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

        if (v.getId() == myButton.getId()) {
            load(myFeedbackList);
            currentViewState = MINE;
        } else if (v.getId() == othersButton.getId()) {
            load(othersFeedbackList);
            currentViewState = OTHERS;
        } else if (v.getId() == settingsButton.getId()) {
            load(settingsFeedbackList);
            currentViewState = SUBSCRIPTIONS;
        }
    }

    private View getViewByState(SETTINGS_VIEW state) {
        switch (state) {
            case OTHERS:
                return othersButton;
            case SUBSCRIPTIONS:
                return settingsButton;
            case MINE:
            default:
                return myButton;
        }
    }

    private void setInactive(Button... buttons) {
        for (Button b : buttons) {
            b.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.gray_tab));
            b.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.indigo));
        }
    }

    private void load(List<? extends LinearLayout> currentList) {
        scrollListLayout.removeAllViews();
        getView(R.id.settings_view_scroll, ScrollView.class).scrollTo(0, 0);
        for (LinearLayout item : currentList) {
            scrollListLayout.addView(item);
        }
    }
}
