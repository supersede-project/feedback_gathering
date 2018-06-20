package ch.uzh.supersede.feedbacklibrary.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.beans.LocalFeedbackBean;
import ch.uzh.supersede.feedbacklibrary.components.buttons.AbstractSettingsListItem;
import ch.uzh.supersede.feedbacklibrary.components.buttons.SubscriptionListItem;
import ch.uzh.supersede.feedbacklibrary.components.buttons.VoteListItem;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService;
import ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.USE_STUBS;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.SETTINGS_VIEW;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.SETTINGS_VIEW.*;


@SuppressWarnings("squid:MaximumInheritanceDepth")
public class FeedbackSettingsActivity extends AbstractBaseActivity implements IFeedbackServiceEventListener {

    private LinearLayout scrollListLayout;
    private SETTINGS_VIEW currentViewState = MINE;

    private Button myButton;
    private Button othersButton;
    private Button settingsButton;

    private LinearLayout focusSink;

    private ArrayList<AbstractSettingsListItem> myFeedbackList = new ArrayList<>();
    private ArrayList<AbstractSettingsListItem> othersFeedbackList = new ArrayList<>();
    private ArrayList<AbstractSettingsListItem> settingsFeedbackList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_settings);

        scrollListLayout = getView(R.id.settings_layout_scroll, LinearLayout.class);
        focusSink = getView(R.id.list_edit_focus_sink, LinearLayout.class);

        myButton = setOnClickListener(getView(R.id.settings_button_mine, Button.class));
        othersButton = setOnClickListener(getView(R.id.settings_button_others, Button.class));
        settingsButton = setOnClickListener(getView(R.id.settings_button_settings, Button.class));

        ToggleButton useStubsToggle = getView(R.id.settings_toggle_use_stubs, ToggleButton.class);
        useStubsToggle.setChecked(FeedbackDatabase.getInstance(this).readBoolean(USE_STUBS, false));
        useStubsToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isEnabled) {
                FeedbackDatabase.getInstance(compoundButton.getContext()).writeBoolean(USE_STUBS, isEnabled);
                execFillFeedbackList();
            }
        });

        colorShape(0, myButton, othersButton, settingsButton);
        colorShape(1, myButton);
        colorViews(1,
                getView(R.id.settings_layout_color_1, LinearLayout.class),
                getView(R.id.settings_layout_color_2, LinearLayout.class),
                getView(R.id.settings_layout_color_3, LinearLayout.class),
                getView(R.id.settings_layout_color_4, LinearLayout.class));

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
        switch (eventType) {
            case GET_MINE_FEEDBACK_VOTES_MOCK:
                myFeedbackList.clear();
                for (LocalFeedbackBean bean : (ArrayList<LocalFeedbackBean>) response) {
                    myFeedbackList.add(new VoteListItem(this, 8, bean, configuration, getTopColor(0)));
                }
                break;
            case GET_OTHERS_FEEDBACK_VOTES_MOCK:
                othersFeedbackList.clear();
                for (LocalFeedbackBean bean : (ArrayList<LocalFeedbackBean>) response) {
                    othersFeedbackList.add(new VoteListItem(this, 8, bean, configuration, getTopColor(0)));
                }
                break;
            case GET_FEEDBACK_SUBSCRIPTIONS_MOCK:
                settingsFeedbackList.clear();
                for (LocalFeedbackBean bean : (ArrayList<LocalFeedbackBean>) response) {
                    settingsFeedbackList.add(new SubscriptionListItem(this, 8, bean, configuration, getTopColor(0)));
                }
                break;
            default:
        }
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
        FeedbackService.getInstance(this).getOthersFeedbackVotes(this, this);
        FeedbackService.getInstance(this).getMineFeedbackVotes(this, this);
        FeedbackService.getInstance(this).getFeedbackSubscriptions(this, this);
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
        colorShape(1, v);

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

        //handle focus and keyboard
        focusSink.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(focusSink.getWindowToken(), 0);
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
        colorShape(0, buttons);
    }

    private void load(List<? extends LinearLayout> currentList) {
        scrollListLayout.removeAllViews();
        getView(R.id.settings_view_scroll, ScrollView.class).scrollTo(0, 0);
        for (LinearLayout item : currentList) {
            scrollListLayout.addView(item);
        }
    }
}
