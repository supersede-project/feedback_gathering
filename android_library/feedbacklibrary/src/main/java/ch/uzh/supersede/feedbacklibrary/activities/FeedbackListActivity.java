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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackDetailsBean;
import ch.uzh.supersede.feedbacklibrary.components.buttons.FeedbackListItem;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.models.Feedback;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService;
import ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener;
import ch.uzh.supersede.feedbacklibrary.utils.ColorUtility;
import ch.uzh.supersede.feedbacklibrary.utils.Enums;
import ch.uzh.supersede.feedbacklibrary.utils.FeedbackUtility;
import ch.uzh.supersede.feedbacklibrary.utils.LoadingViewUtility;
import ch.uzh.supersede.feedbacklibrary.utils.StringUtility;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.EXTRA_KEY_FEEDBACK_DELETION;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.IS_DEVELOPER;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.FEEDBACK_SORTING.*;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ACTIVE;


@SuppressWarnings("squid:MaximumInheritanceDepth")
public class FeedbackListActivity extends AbstractBaseActivity implements IFeedbackServiceEventListener {
    private LinearLayout scrollListLayout;
    private Button myButton;
    private Button topButton;
    private Button hotButton;
    private Button newButton;
    private Button filterButton;
    private LinearLayout focusSink;
    private EditText searchText;
    private String searchTerm;
    private ArrayList<FeedbackListItem> activeFeedbackList = new ArrayList<>();
    private ArrayList<FeedbackListItem> allFeedbackList = new ArrayList<>();
    private Enums.FEEDBACK_SORTING sorting = MINE;
    private ArrayList<Enums.FEEDBACK_STATUS> allowedStatuses = new ArrayList<>();
    private TextView loadingTextView;
    private boolean returnedFromDeletion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_list);
        loadingTextView = LoadingViewUtility.createLoadingView(this, screenWidth, screenHeight, getTopColor(0));
        ContentFrameLayout rootLayout = getView(R.id.list_root, ContentFrameLayout.class);
        rootLayout.addView(loadingTextView);
        returnedFromDeletion = getIntent().getBooleanExtra(EXTRA_KEY_FEEDBACK_DELETION,false);


        scrollListLayout = getView(R.id.list_layout_scroll, LinearLayout.class);
        myButton = setOnClickListener(getView(R.id.list_button_mine, Button.class));
        topButton = setOnClickListener(getView(R.id.list_button_top, Button.class));
        hotButton = setOnClickListener(getView(R.id.list_button_hot, Button.class));
        newButton = setOnClickListener(getView(R.id.list_button_new, Button.class));
        filterButton = getView(R.id.list_button_filter, Button.class);
        filterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilteringOptions();
            }
        });
        Collections.addAll(allowedStatuses, Enums.FEEDBACK_STATUS.values());
        searchText = addTextChangedListener(getView(R.id.list_edit_search, EditText.class));
        focusSink = getView(R.id.list_edit_focus_sink, LinearLayout.class);

        colorShape(0, topButton, hotButton, newButton);
        colorShape(1, myButton);
        colorViews(0, filterButton);
        colorViews(1,
                getView(R.id.list_layout_color_1, LinearLayout.class),
                getView(R.id.list_layout_color_2, LinearLayout.class),
                getView(R.id.list_layout_color_3, LinearLayout.class),
                getView(R.id.list_layout_color_4, LinearLayout.class),
                getView(R.id.list_layout_color_5, LinearLayout.class));
        colorViews(2,getView(R.id.list_root,ContentFrameLayout.class));
        onPostCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        allFeedbackList.clear();
        loadingTextView.setVisibility(View.VISIBLE);
        FeedbackService.getInstance(this).getFeedbackList(this, this, configuration, getTopColor(0));
        doSearch(searchText.getText().toString());
        sort();
    }

    @Override
    public void onEventCompleted(EventType eventType, Object response) {
        switch (eventType) {
            case GET_FEEDBACK_LIST:
                if (response instanceof List) {
                    List<FeedbackDetailsBean> feedbackDetailsBeans = new ArrayList<>();
                    for (Feedback feedback : (List<Feedback>) response) {
                        FeedbackDetailsBean feedbackDetailsBean = FeedbackUtility.feedbackToFeedbackDetailsBean(this, feedback);
                        if (feedbackDetailsBean != null){ //Avoid NP caused by old Repository Feedback
                            feedbackDetailsBeans.add(feedbackDetailsBean);
                            allFeedbackList.add(new FeedbackListItem(this, 8, feedbackDetailsBean.getFeedbackBean(), configuration, getTopColor(0)));
                        }
                    }
                    activeFeedbackList = new ArrayList<>(allFeedbackList);
                    doSearch(searchText.getText().toString());
                    sort();
                }
                break;
            case GET_FEEDBACK_LIST_MOCK:
                if (response instanceof ArrayList) {
                    allFeedbackList = (ArrayList<FeedbackListItem>) response;
                    activeFeedbackList = new ArrayList<>(allFeedbackList);
                    doSearch(searchText.getText().toString());
                    sort();
                }
                break;
            default:
                break;
        }
        loadingTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onEventFailed(EventType eventType, Object response) {
        loadingTextView.setVisibility(View.INVISIBLE);
        Toast.makeText(getApplicationContext(), R.string.list_alert_event,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(EventType eventType) {
        loadingTextView.setVisibility(View.INVISIBLE);
        Toast.makeText(getApplicationContext(), R.string.list_alert_server,Toast.LENGTH_SHORT).show();
    }

    private void sort() {
        for (FeedbackListItem item : activeFeedbackList) {
            item.setSorting(sorting, allowedStatuses);
        }
        Collections.sort(activeFeedbackList);
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
        setInactive(myButton, topButton, hotButton, newButton);
        colorShape(1, v);
        if (v.getId() == myButton.getId()) {
            loadMyFeedback();
        } else if (v.getId() == topButton.getId()) {
            loadTopFeedback();
        } else if (v.getId() == hotButton.getId()) {
            loadHotFeedback();
        } else if (v.getId() == newButton.getId()) {
            loadNewFeedback();
        }
        //handle focus and keyboard
        focusSink.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(focusSink.getWindowToken(), 0);
        }
    }

    private void setInactive(Button... buttons) {
        colorShape(0, buttons);
    }

    private void doSearch(String s) {
        activeFeedbackList.clear();
        if (sorting==MINE && ACTIVE.check(getApplicationContext()) && FeedbackDatabase.getInstance(getApplicationContext()).readBoolean(IS_DEVELOPER,false)){
            addDeveloperContext();
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

    private void addDeveloperContext() {
        //TODO: connect new views with specific players (negatively contributing, often contributing, often replying etc.)
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

    private void loadNewFeedback() {
        sorting = NEW;
        doSearch(searchTerm);
    }

    private void loadHotFeedback() {
        sorting = HOT;
        doSearch(searchTerm);
    }

    private void loadTopFeedback() {
        sorting = TOP;
        doSearch(searchTerm);
    }

    private void loadMyFeedback() {
        sorting = MINE;
        doSearch(searchTerm);
    }

    private void load() {
        scrollListLayout.removeAllViews();
        getView(R.id.list_view_scroll, ScrollView.class).scrollTo(0, 0);
        for (FeedbackListItem item : activeFeedbackList) {
            scrollListLayout.addView(item);
        }
    }
}
