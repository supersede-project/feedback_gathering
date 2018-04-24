package ch.uzh.supersede.feedbacklibrary.activities;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.*;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import java.util.*;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.components.buttons.FeedbackListItem;
import ch.uzh.supersede.feedbacklibrary.interfaces.ISortableFeedback.FEEDBACK_SORTING;
import ch.uzh.supersede.feedbacklibrary.stubs.RepositoryStub;
import ch.uzh.supersede.feedbacklibrary.utils.*;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackBean;

import static ch.uzh.supersede.feedbacklibrary.interfaces.ISortableFeedback.FEEDBACK_SORTING.MINE;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class FeedbackListActivity extends AbstractBaseActivity {
    private LinearLayout scrollListLayout;
    private Button myButton;
    private Button topButton;
    private Button hotButton;
    private Button newButton;
    private LinearLayout focusSink;
    private EditText searchText;
    private String searchTerm;
    private ArrayList<FeedbackListItem> activeFeedbackList = new ArrayList<>();
    private ArrayList<FeedbackListItem> allFeedbackList = new ArrayList<>();
    private FEEDBACK_SORTING sorting = MINE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_list);
        scrollListLayout = getView(R.id.list_layout_scroll, LinearLayout.class);
        myButton = setOnClickListener(getView(R.id.list_button_mine, Button.class));
        topButton = setOnClickListener(getView(R.id.list_button_top, Button.class));
        hotButton = setOnClickListener(getView(R.id.list_button_hot, Button.class));
        newButton = setOnClickListener(getView(R.id.list_button_new, Button.class));
        searchText = addTextChangedListener(getView(R.id.list_edit_search, EditText.class));
        focusSink = getView(R.id.list_edit_focus_sink, LinearLayout.class);
        for (FeedbackBean bean : RepositoryStub.getFeedback(this, 50, -30, 50, 0.1f)) {
            allFeedbackList.add(new FeedbackListItem(this, 8, bean));
        }
        activeFeedbackList = new ArrayList<>(allFeedbackList);
        sort();
        onPostCreate();
    }

    private void sort() {
        for (FeedbackListItem item : activeFeedbackList) {
            item.sort(sorting);
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
        v.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.blue_tab));
        ((Button) v).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
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
        for (Button b : buttons) {
            b.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.gray_tab));
            b.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.indigo));
        }
    }

    private void doSearch(String s) {
        activeFeedbackList.clear();
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
        sorting = FEEDBACK_SORTING.NEW;
        doSearch(searchTerm);
    }

    private void loadHotFeedback() {
        sorting = FEEDBACK_SORTING.HOT;
        doSearch(searchTerm);
    }

    private void loadTopFeedback() {
        sorting = FEEDBACK_SORTING.TOP;
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
