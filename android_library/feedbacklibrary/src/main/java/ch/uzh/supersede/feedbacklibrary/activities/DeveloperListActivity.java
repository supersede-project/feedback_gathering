package ch.uzh.supersede.feedbacklibrary.activities;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.widget.ContentFrameLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import java.util.*;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackDetailsBean;
import ch.uzh.supersede.feedbacklibrary.components.buttons.FeedbackListItem;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.models.Feedback;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService;
import ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener;
import ch.uzh.supersede.feedbacklibrary.utils.*;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.UserConstants.USER_IS_DEVELOPER;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.DEVELOPER_VIEW.PRIVATE;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.DEVELOPER_VIEW.REPORTED;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.FEEDBACK_SORTING.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.SETTINGS_VIEW.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.SETTINGS_VIEW.MINE;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ACTIVE;


@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DeveloperListActivity extends AbstractBaseActivity implements IFeedbackServiceEventListener {
    private LinearLayout scrollListLayout;
    private Enums.DEVELOPER_VIEW currentViewState = PRIVATE;

    private Button privateButton;
    private Button reportedButton;
    private Button feature1Button;
    private Button feature2Button;
    private Button filterButton;
    private LinearLayout focusSink;
    private EditText searchText;
    private String searchTerm;
    private ArrayList<FeedbackListItem> privateFeedbackList = new ArrayList<>();
    private ArrayList<FeedbackListItem> reportedFeedbackList = new ArrayList<>();
    private ArrayList<Enums.FEEDBACK_STATUS> allowedStatuses = new ArrayList<>();
    private TextView loadingTextView;
    private boolean returnedFromDeletion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_list);
        loadingTextView = LoadingViewUtility.createLoadingView(this, screenWidth, screenHeight, getTopColor(0));
        ContentFrameLayout rootLayout = getView(R.id.dev_list_root, ContentFrameLayout.class);
        rootLayout.addView(loadingTextView);

        returnedFromDeletion = getIntent().getBooleanExtra(EXTRA_KEY_FEEDBACK_DELETION, false);

        scrollListLayout = getView(R.id.dev_list_layout_scroll, LinearLayout.class);
        privateButton = setOnClickListener(getView(R.id.dev_list_button_private, Button.class));
        reportedButton = setOnClickListener(getView(R.id.dev_list_button_reported, Button.class));
        feature1Button = setOnClickListener(getView(R.id.dev_list_button_feature_1, Button.class));
        feature2Button = setOnClickListener(getView(R.id.dev_list_button_feature_2, Button.class));
        filterButton = getView(R.id.dev_list_button_filter, Button.class);
        filterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilteringOptions();
            }
        });
        Collections.addAll(allowedStatuses, Enums.FEEDBACK_STATUS.values());
        searchText = addTextChangedListener(getView(R.id.dev_list_edit_search, EditText.class));
        focusSink = getView(R.id.dev_list_edit_focus_sink, LinearLayout.class);

        colorShape(0, reportedButton, feature1Button, feature2Button);
        colorShape(1, privateButton);
        colorViews(0, filterButton);
        colorViews(1,
                getView(R.id.dev_list_layout_color_1, LinearLayout.class),
                getView(R.id.dev_list_layout_color_2, LinearLayout.class),
                getView(R.id.dev_list_layout_color_3, LinearLayout.class),
                getView(R.id.dev_list_layout_color_4, LinearLayout.class),
                getView(R.id.dev_list_layout_color_5, LinearLayout.class));
        colorViews(2, getView(R.id.dev_list_root, ContentFrameLayout.class));
        onPostCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        allFeedbackList.clear();
        loadingTextView.setVisibility(View.VISIBLE);
        if (!ACTIVE.check(getApplicationContext()) && !getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE).getBoolean(SHARED_PREFERENCES_ONLINE, false)) {
            //userlvl 1 and offline
            FeedbackService.getInstance(this, true).getPrivateFeedbackList(this);
            FeedbackService.getInstance(this, true).getReportedFeedbackList(this);
        } else {
            FeedbackService.getInstance(this).getPrivateFeedbackList(this);
            FeedbackService.getInstance(this).getReportedFeedbackList(this);
        }
        doSearch(searchText.getText().toString());
        sort();
    }


    @Override
    public void onEventCompleted(EventType eventType, Object response) {
        switch (eventType) {
            case GET_PRIVATE_FEEDBACK_LIST:
                handleListUpdate(privateFeedbackList, response);
                break;
            case GET_REPORTED_FEEDBACK_LIST:
                handleListUpdate(reportedFeedbackList, response);
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("unchecked")
    private void handleListUpdate(List<FeedbackListItem> feedbackListItems, Object response) {
        if (response instanceof List) {
            feedbackListItems.clear();
            for (Feedback feedback : (List<Feedback>) response) {
                FeedbackDetailsBean feedbackDetailsBean = FeedbackUtility.feedbackToFeedbackDetailsBean(this, feedback);
                feedbackListItems.add(new FeedbackListItem(this, 8, feedbackDetailsBean, configuration, getTopColor(0)));
            }
            doSearch(searchText.getText().toString());
            loadingTextView.setVisibility(View.INVISIBLE);
            sort();
        }
    }

    @Override
    public void onEventFailed(EventType eventType, Object response) {
        super.onEventFailed(eventType, response);
        Log.w(getClass().getSimpleName(), getResources().getString(R.string.api_service_event_failed, eventType, response.toString()));
    }

    @Override
    public void onConnectionFailed(EventType eventType) {
        super.onConnectionFailed(eventType);
        Log.w(getClass().getSimpleName(), getResources().getString(R.string.api_service_connection_failed, eventType));
    }

    private void sort(List<FeedbackListItem> feedbackListItems) {
        for (FeedbackListItem item : feedbackListItems) {
            item.setSorting(currentViewState, allowedStatuses);
        }
        Collections.sort(feedbackListItems);
        load();
    }

    private EditText addTextChangedListener(final EditText editText) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && searchText.getText() != null && searchText.getText().toString().equals(getString(R.string.list_edit_search))) {
                    searchText.setText(null);
                } else if (!hasFocus && !StringUtility.hasText(searchText.getText().toString())) {
                    searchText.setText(getString(R.string.list_edit_search));
                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                doSearch(searchText.getText().toString());
            }
        });
        return editText;
    }


    private Button setOnClickListener(Button button) {
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButtons(v);
            }
        });
        return button;
    }

    private void toggleButtons(View v) {
        setInactive(privateButton, reportedButton, feature1Button, feature2Button);
        colorShape(1, v);

        if (v.getId() == privateButton.getId()) {
            load(privateFeedbackList);
            currentViewState = PRIVATE;
        } else if (v.getId() == reportedButton.getId()) {
            load(othersFeedbackList);
            currentViewState = REPORTED;
        }

        //handle focus and keyboard
        focusSink.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(focusSink.getWindowToken(), 0);
        }
    }

    private View getViewByState(Enums.DEVELOPER_VIEW state) {
        switch (state) {
            case REPORTED:
                return reportedButton;
            case PRIVATE:
            default:
                return privateButton;
        }
    }

    private void setInactive(Button... buttons) {
        colorShape(0, buttons);
    }

    private void doSearch(String s) {
        activeFeedbackList.clear();
        if (sorting == MINE && ACTIVE.check(getApplicationContext()) && FeedbackDatabase
                .getInstance(getApplicationContext())
                .readBoolean(USER_IS_DEVELOPER, false) && VersionUtility.getDateVersion() >= 4) {
            sort();
            return;
        }
        if (!getString(R.string.list_edit_search).equals(s) && StringUtility.hasText(s)) {
            for (FeedbackListItem item : allFeedbackList) {
                if (item.getFeedbackBean().getTitle().toLowerCase().contains(s.toLowerCase())) {
                    activeFeedbackList.add(item);
                }
            }
            searchTerm = s;
        } else {
            searchTerm = null;
            activeFeedbackList = new ArrayList<>(allFeedbackList);
        }
        sort();
    }

    private final CheckBox[] filterCheckBoxArray = new CheckBox[Enums.FEEDBACK_STATUS.values().length];

    private void openFilteringOptions() {
        LinearLayout borderLayout = new LinearLayout(this);
        LinearLayout wrapperLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = 15;
        params.setMargins(margin, margin, margin, margin);
        wrapperLayout.setOrientation(LinearLayout.VERTICAL);
        wrapperLayout.setLayoutParams(params);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        for (int s = 0; s < Enums.FEEDBACK_STATUS.values().length; s++) {
            Enums.FEEDBACK_STATUS status = Enums.FEEDBACK_STATUS.values()[s];
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(status.getLabel());
            CompoundButtonCompat.setButtonTintList(checkBox, ColorStateList.valueOf(getTopColor(1)));
            checkBox.setTextColor(ColorUtility.adjustColorToBackground(getTopColor(0), status.getColor(), 0.4));
            for (Enums.FEEDBACK_STATUS statusAllowed : allowedStatuses) {
                if (statusAllowed == status) {
                    checkBox.setChecked(true);
                }
            }
            filterCheckBoxArray[s] = checkBox;
            wrapperLayout.addView(checkBox);
        }
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        borderLayout.setBackgroundColor(getTopColor(1));
        wrapperLayout.setBackgroundColor(getTopColor(0));
        borderLayout.addView(wrapperLayout);
        builder.setView(borderLayout);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                allowedStatuses.clear();
                for (int s = 0; s < Enums.FEEDBACK_STATUS.values().length; s++) {
                    Enums.FEEDBACK_STATUS status = Enums.FEEDBACK_STATUS.values()[s];
                    if (filterCheckBoxArray[s].isChecked()) {
                        allowedStatuses.add(status);
                    }
                }
                sort();
            }
        });
        AlertDialog alertDialog = builder.show();
        alertDialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(ColorUtility.adjustColorToBackground(getResources().getColor(R.color.white), getTopColor(1), 0.3));
    }

    private void load() {
        scrollListLayout.removeAllViews();
        getView(R.id.dev_list_view_scroll, ScrollView.class).scrollTo(0, 0);
        for (FeedbackListItem item : activeFeedbackList) {
            scrollListLayout.addView(item);
        }
    }
}
