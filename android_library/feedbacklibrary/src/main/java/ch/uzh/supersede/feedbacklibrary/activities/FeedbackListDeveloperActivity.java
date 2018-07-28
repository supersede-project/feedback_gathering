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
import ch.uzh.supersede.feedbacklibrary.models.Feedback;
import ch.uzh.supersede.feedbacklibrary.models.FeedbackReport;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService;
import ch.uzh.supersede.feedbacklibrary.utils.*;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.FEEDBACK_SORTING.*;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ACTIVE;


@SuppressWarnings("squid:MaximumInheritanceDepth")
public class FeedbackListDeveloperActivity extends AbstractFeedbackListActivity {
    private Enums.FEEDBACK_SORTING currentViewState = PRIVATE;

    private String searchTerm;
    private ArrayList<FeedbackListItem> privateFeedbackList = new ArrayList<>();
    private ArrayList<FeedbackListItem> reportedFeedbackList = new ArrayList<>();
    private ArrayList<FeedbackListItem> activeFeedbackList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_list);
        setScrollListLayout(getView(R.id.dev_list_layout_scroll, LinearLayout.class));
        setScrollView(getView(R.id.dev_list_view_scroll, ScrollView.class));

        getButtons().put(PRIVATE, setOnClickListener(getView(R.id.dev_list_button_private, Button.class)));
        getButtons().put(REPORTED, setOnClickListener(getView(R.id.dev_list_button_reported, Button.class)));

        Button feature1Button = getView(R.id.dev_list_button_feature_1, Button.class);
        Button feature2Button = getView(R.id.dev_list_button_feature_2, Button.class);
        Button filterButton = getView(R.id.dev_list_button_filter, Button.class);

        filterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilteringOptions();
            }
        });
        setSearchText(addTextChangedListener(getView(R.id.dev_list_edit_search, EditText.class)));
        setFocusSink(getView(R.id.dev_list_edit_focus_sink, LinearLayout.class));

        colorShape(0, getButtons().get(REPORTED));
        colorShape(0, true, feature1Button, feature2Button);
        colorShape(1, getButtons().get(PRIVATE));
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
        getActiveList().clear();
        if (!ACTIVE.check(getApplicationContext()) && !getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE).getBoolean(SHARED_PREFERENCES_ONLINE, false)) {
            //userlvl 1 and offline
            FeedbackService.getInstance(this, true).getFeedbackListPrivate(this);
            FeedbackService.getInstance(this, true).getFeedbackReportList(this);
        } else {
            FeedbackService.getInstance(this).getFeedbackListPrivate(this);
            FeedbackService.getInstance(this).getFeedbackReportList(this);
        }
        doSearch(getSearchText().getText().toString());
        sort();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onEventCompleted(EventType eventType, Object response) {
        switch (eventType) {
            case GET_FEEDBACK_LIST_PRIVATE:
                if (response instanceof List) {
                    handleListUpdate(privateFeedbackList, (List<Feedback>) response);
                }
                break;
            case GET_FEEDBACK_REPORT_LIST:
                if (response instanceof List) {
                    handleListUpdate(reportedFeedbackList, (List<Feedback>) response);
                }
                break;
            default:
                break;
        }
    }

    private void handleListUpdate(List<FeedbackListItem> feedbackListItems, List<Feedback> feedbackList) {
        feedbackListItems.clear();

        for (Feedback feedback : feedbackList) {
            FeedbackDetailsBean feedbackDetailsBean = FeedbackUtility.feedbackToFeedbackDetailsBean(this, feedback);
            feedbackListItems.add(new FeedbackListItem(this, 8, feedbackDetailsBean, configuration, getTopColor(0)));
        }
        doSearch(getSearchText().getText().toString());
        sort();
    }

    @Override
    protected void handleButtonToggle(View v) {
        if (v.getId() == getButtons().get(PRIVATE).getId()) {
            currentViewState = PRIVATE;
            setSorting(PRIVATE);
        } else if (v.getId() == getButtons().get(REPORTED).getId()) {
            currentViewState = REPORTED;
            setSorting(REPORTED);
        }
        activeFeedbackList = new ArrayList<>(getActiveList());
        doSearch(searchTerm);
    }

    @Override
    protected List<FeedbackListItem> getActiveList() {
        return getActiveListByState(currentViewState);
    }

    private List<FeedbackListItem> getActiveListByState(Enums.FEEDBACK_SORTING state) {
        switch (state) {
            case REPORTED:
                return reportedFeedbackList;
            case PRIVATE:
                return privateFeedbackList;
            default:
                return new ArrayList<>();
        }
    }

    @Override
    protected void doSearch(String s) {
        List<FeedbackListItem> sortedFeedbackList = new ArrayList<>(getActiveList());
        activeFeedbackList.clear();
        if (!getString(R.string.list_edit_search).equals(s) && StringUtility.hasText(s)) {
            for (FeedbackListItem item : sortedFeedbackList) {
                if (item.getFeedbackBean().getTitle().toLowerCase().contains(s.toLowerCase())) {
                    activeFeedbackList.add(item);
                }
            }
            searchTerm = s;
        } else {
            activeFeedbackList = new ArrayList<>(getActiveList());
            searchTerm = null;
        }
        sort();
    }
}
