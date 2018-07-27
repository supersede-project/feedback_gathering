package ch.uzh.supersede.feedbacklibrary.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.ContentFrameLayout;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import java.util.*;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackDetailsBean;
import ch.uzh.supersede.feedbacklibrary.beans.LocalFeedbackBean;
import ch.uzh.supersede.feedbacklibrary.components.buttons.*;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.models.Feedback;
import ch.uzh.supersede.feedbacklibrary.services.*;
import ch.uzh.supersede.feedbacklibrary.utils.FeedbackUtility;
import ch.uzh.supersede.feedbacklibrary.utils.ServiceUtility;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.SETTINGS_VIEW;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.SETTINGS_VIEW.*;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ADVANCED;


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

        ToggleButton enableNotificationsToggle = getView(R.id.settings_toggle_enable_notifications, ToggleButton.class);
        enableNotificationsToggle.setChecked(ServiceUtility.isServiceEnabled(this));
        enableNotificationsToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isEnabled) {
                ServiceUtility.setServiceEnabled(compoundButton.getContext(), isEnabled);
                execStartNotificationService(isEnabled);
            }
        });

        colorLayouts(0, getView(R.id.settings_root, ContentFrameLayout.class));
        colorTextOnly(0,
                getView(R.id.settings_text_use_stubs, TextView.class),
                getView(R.id.settings_text_enable_notifications, TextView.class),
                getView(R.id.settings_text_feature_3, TextView.class),
                getView(R.id.settings_text_title_general, TextView.class),
                getView(R.id.settings_text_title_feedback, TextView.class));
        colorViews(1,
                getView(R.id.settings_toggle_use_stubs, ToggleButton.class),
                getView(R.id.settings_toggle_enable_notifications, ToggleButton.class),
                getView(R.id.settings_toggle_feature_3, ToggleButton.class));
        colorShape(0, myButton, othersButton, settingsButton);
        colorShape(1, myButton);
        colorViews(1,
                getView(R.id.settings_layout_color_1, LinearLayout.class),
                getView(R.id.settings_layout_color_2, LinearLayout.class),
                getView(R.id.settings_layout_color_3, LinearLayout.class),
                getView(R.id.settings_layout_color_4, LinearLayout.class));
        invokeVersionControl(5, R.id.settings_toggle_feature_3);
        if (!ADVANCED.check(this)) {
            disableViews(enableNotificationsToggle);
        }
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
            case GET_FEEDBACK_LIST_VOTED:
                othersFeedbackList.clear();
                othersFeedbackList.addAll(FeedbackUtility.createFeedbackVotesListItems((List<Feedback>) response, this, configuration, getTopColor(0)));
                break;
            case GET_MINE_FEEDBACK_VOTES_MOCK:
                myFeedbackList.clear();
                for (FeedbackDetailsBean bean : (ArrayList<FeedbackDetailsBean>) response) {
                    myFeedbackList.add(new VoteListItem(this, 8, bean, configuration, getTopColor(0)));
                }
                Collections.sort(myFeedbackList);
                break;
            case GET_FEEDBACK_LIST_OWN:
                myFeedbackList.clear();
                myFeedbackList.addAll(FeedbackUtility.createFeedbackVotesListItems((List<Feedback>) response, this, configuration, getTopColor(0)));
                break;
            case GET_OTHERS_FEEDBACK_VOTES_MOCK:
                othersFeedbackList.clear();
                for (FeedbackDetailsBean bean : (ArrayList<FeedbackDetailsBean>) response) {
                    othersFeedbackList.add(new VoteListItem(this, 8, bean, configuration, getTopColor(0)));
                }
                Collections.sort(othersFeedbackList);
                break;
            case GET_LOCAL_FEEDBACK_LIST_SUBSCRIBED:
                settingsFeedbackList.clear();
                for (FeedbackDetailsBean bean : (ArrayList<FeedbackDetailsBean>) response) {
                    settingsFeedbackList.add(new SubscriptionListItem(this, 8, bean, configuration, getTopColor(0)));
                }
                Collections.sort(settingsFeedbackList);
                break;
            default:
        }
    }

    @Override
    public void onEventFailed(EventType eventType, Object response) {
        Log.e(getClass().getSimpleName(), getResources().getString(R.string.api_service_event_failed, eventType, response.toString()));
    }

    @Override
    public void onConnectionFailed(EventType eventType) {
        Log.e(getClass().getSimpleName(), getResources().getString(R.string.api_service_connection_failed, eventType));
    }

    private void execFillFeedbackList() {
        FeedbackService.getInstance(this).getFeedbackListVoted(this, this);
        FeedbackService.getInstance(this).getFeedbackListOwn(this, this);
        FeedbackService.getInstance(this).getLocalFeedbackListSubscribed(this, this);
    }

    private void execStartNotificationService(boolean isEnabled) {
        if (isEnabled) {
            ServiceUtility.startService(NotificationService.class, this, new ServiceUtility.Extra(EXTRA_KEY_APPLICATION_CONFIGURATION, configuration));
        } else {
            ServiceUtility.stopService(NotificationService.class, this);
        }
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
            currentViewState = VOTED;
        } else if (v.getId() == settingsButton.getId()) {
            load(settingsFeedbackList);
            currentViewState = SUBSCRIBED;
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
            case VOTED:
                return othersButton;
            case SUBSCRIBED:
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
