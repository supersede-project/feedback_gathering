package ch.uzh.supersede.feedbacklibrary.activities;


import android.os.Bundle;
import android.support.v7.widget.ContentFrameLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackDetailsBean;
import ch.uzh.supersede.feedbacklibrary.components.buttons.FeedbackListItem;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.models.Feedback;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService;
import ch.uzh.supersede.feedbacklibrary.utils.*;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.UserConstants.USER_IS_DEVELOPER;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.UserConstants.USER_NAME;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.FEEDBACK_SORTING.*;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ACTIVE;


@SuppressWarnings("squid:MaximumInheritanceDepth")
public class FeedbackListActivity extends AbstractFeedbackListActivity {
    private String searchTerm;
    private ArrayList<FeedbackListItem> activeFeedbackList = new ArrayList<>();
    private ArrayList<FeedbackListItem> allFeedbackList = new ArrayList<>();
    private TextView loadingTextView;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_list);
        loadingTextView = LoadingViewUtility.createLoadingView(this, screenWidth, screenHeight, getTopColor(0));
        ContentFrameLayout rootLayout = getView(R.id.list_root, ContentFrameLayout.class);
        rootLayout.addView(loadingTextView);
        setScrollListLayout(getView(R.id.list_layout_scroll, LinearLayout.class));
        setScrollView(getView(R.id.list_view_scroll, ScrollView.class));

        getButtons().put(MINE, setOnClickListener(getView(R.id.list_button_mine, Button.class)));
        getButtons().put(TOP, setOnClickListener(getView(R.id.list_button_top, Button.class)));
        getButtons().put(HOT, setOnClickListener(getView(R.id.list_button_hot, Button.class)));
        getButtons().put(NEW, setOnClickListener(getView(R.id.list_button_new, Button.class)));

        Button filterButton = getView(R.id.list_button_filter, Button.class);
        filterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilteringOptions();
            }
        });
        setSearchText(addTextChangedListener(getView(R.id.list_edit_search, EditText.class)));
        setFocusSink(getView(R.id.list_edit_focus_sink, LinearLayout.class));

        colorShape(0, getButtons().get(TOP), getButtons().get(HOT), getButtons().get(NEW));
        colorShape(1, getButtons().get(MINE));
        colorViews(0, filterButton);
        colorViews(1,
                getView(R.id.list_layout_color_1, LinearLayout.class),
                getView(R.id.list_layout_color_2, LinearLayout.class),
                getView(R.id.list_layout_color_3, LinearLayout.class),
                getView(R.id.list_layout_color_4, LinearLayout.class),
                getView(R.id.list_layout_color_5, LinearLayout.class));
        colorViews(2, getView(R.id.list_root, ContentFrameLayout.class));

        if (ACTIVE.check(this)){
            userName = FeedbackDatabase.getInstance(this).readString(USER_NAME, null);
        }
        toggleButtons(getButtons().get(MINE));

        onPostCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        allFeedbackList.clear();
        loadingTextView.setVisibility(View.VISIBLE);
        if (!ACTIVE.check(getApplicationContext()) && !getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE).getBoolean(SHARED_PREFERENCES_ONLINE, false)) {
            //userlvl 1 and offline
            FeedbackService.getInstance(this, true).getFeedbackList(this, this, userName);
        } else {
            FeedbackService.getInstance(this).getFeedbackList(this, this, userName);
        }
        doSearch(getSearchText().getText().toString());
        sort();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onEventCompleted(EventType eventType, Object response) {
        switch (eventType) {
            case GET_FEEDBACK_LIST:
                if (response instanceof List) {
                    allFeedbackList.addAll(FeedbackUtility.createFeedbackListItems((List<Feedback>) response, this, configuration, getTopColor(1), getClass()));
                    activeFeedbackList = new ArrayList<>(allFeedbackList);
                    doSearch(getSearchText().getText().toString());
                    loadingTextView.setVisibility(View.INVISIBLE);
                    sort();
                }
                break;
            case GET_FEEDBACK_LIST_MOCK:
                if (response instanceof ArrayList) {
                    allFeedbackList = (ArrayList<FeedbackListItem>) response;
                    activeFeedbackList = new ArrayList<>(allFeedbackList);
                    doSearch(getSearchText().getText().toString());
                    loadingTextView.setVisibility(View.INVISIBLE);
                    sort();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onEventFailed(EventType eventType, Object response) {
        super.onEventFailed(eventType, response);
        handleServiceFailed(eventType);
    }

    @Override
    public void onConnectionFailed(EventType eventType) {
        super.onConnectionFailed(eventType);
        handleServiceFailed(eventType);
    }

    private void handleServiceFailed(EventType eventType) {
        switch (eventType) {
            case GET_FEEDBACK_LIST:
            case GET_FEEDBACK_LIST_MOCK:
                loadingTextView.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), R.string.list_alert_event, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void handleButtonToggle(View v) {
        if (v.getId() == getButtons().get(MINE).getId()) {
            loadMyFeedback();
        } else if (v.getId() == getButtons().get(TOP).getId()) {
            loadTopFeedback();
        } else if (v.getId() == getButtons().get(HOT).getId()) {
            loadHotFeedback();
        } else if (v.getId() == getButtons().get(NEW).getId()) {
            loadNewFeedback();
        }
    }

    @Override
    protected List<FeedbackListItem> getActiveList() {
        return activeFeedbackList;
    }

    @Override
    protected void doSearch(String s) {
        activeFeedbackList.clear();
        if (getSorting() == MINE && ACTIVE.check(getApplicationContext()) && FeedbackDatabase
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

    private void loadNewFeedback() {
        setSorting(NEW);
        doSearch(searchTerm);
    }

    private void loadHotFeedback() {
        setSorting(HOT);
        doSearch(searchTerm);
    }

    private void loadTopFeedback() {
        setSorting(TOP);
        doSearch(searchTerm);
    }

    private void loadMyFeedback() {
        setSorting(MINE);
        doSearch(searchTerm);
    }
}
