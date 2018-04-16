package ch.uzh.supersede.feedbacklibrary.activities;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import java.util.*;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.components.buttons.FeedbackListItem;
import ch.uzh.supersede.feedbacklibrary.utils.*;

import static ch.uzh.supersede.feedbacklibrary.components.buttons.FeedbackListItem.FEEDBACK_STATUS.*;

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
    private int maxPoints = 0;
    private static final Button[] activeButtonAllocation = new Button[]{null};
    private ArrayList<FeedbackListItem> myFeedbackList = new ArrayList<>();
    private ArrayList<FeedbackListItem> topFeedbackList = new ArrayList<>();
    private ArrayList<FeedbackListItem> hotFeedbackList = new ArrayList<>();
    private ArrayList<FeedbackListItem> newFeedbackList = new ArrayList<>();
    private ArrayList<FeedbackListItem> activeFeedbackList = new ArrayList<>();

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
        for (int l = 0; l < 4; l++) {
            String title = l == 0 ? "My Feedback #" : l == 1 ? "Top Feedback #" : l == 2 ? "Hot Feedback #" : "New Feedback #";
            for (int s = 0; s < 4; s++) {
                FeedbackListItem.FEEDBACK_STATUS status = s == 0 ? OPEN : s == 1 ? IN_PROGRESS : s == 2 ? REJECTED : CLOSED;
                for (int i = 0; i < 10; i++) {
                    int id = (10*s)+i;
                    FeedbackListItem listItem = new FeedbackListItem(this, 8, title + id, "Date: ".concat(DateUtility.getDate(false)), status, (int) (50 * Math.random()));
                    if (listItem.getPoints() > maxPoints) {
                        maxPoints = listItem.getPoints();
                    }
                    if (l == 0) {
                        myFeedbackList.add(listItem);
                    } else if (l == 1) {
                        topFeedbackList.add(listItem);
                    }else if (l == 2) {
                        hotFeedbackList.add(listItem);
                    }else if (l == 3) {
                        newFeedbackList.add(listItem);
                    }
                }
            }
        }
        sort(myFeedbackList,topFeedbackList,hotFeedbackList,newFeedbackList);
        load(myFeedbackList,true);
        onPostCreate();
    }

    private void sort(ArrayList<FeedbackListItem>... lists){
        for (ArrayList<FeedbackListItem> list : lists){
            Collections.sort(list);
        }
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
            loadMyFeedbacks();
        } else if (v.getId() == topButton.getId()) {
            loadTopFeedbacks();
        } else if (v.getId() == hotButton.getId()) {
            loadHotFeedbacks();
        } else if (v.getId() == newButton.getId()) {
            loadNewFeedbacks();
        }
        //handle focus and keyboard
        focusSink.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(focusSink.getWindowToken(), 0);
    }

    private void setInactive(Button... buttons) {
        for (Button b : buttons) {
            b.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.gray_tab));
            b.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.indigo));
        }
    }

    private void doSearch(String s) {
        if (!getString(R.string.list_edit_search).equals(s)) {
            if (StringUtility.hasText(s)) {
                ArrayList<FeedbackListItem> remainingItems = new ArrayList<>();
                for (FeedbackListItem item : activeFeedbackList){
                    if (item.getTitle().contains(s)){
                        remainingItems.add(item);
                    }
                }
                load(remainingItems, false);
            }else{
                load(activeFeedbackList, false);
            }
            searchTerm = s;
        }
    }

    private void loadNewFeedbacks() {
        load(newFeedbackList,true);
        doSearch(searchTerm);
    }

    private void loadHotFeedbacks() {
        load(hotFeedbackList,true);
        doSearch(searchTerm);
    }

    private void loadTopFeedbacks() {
        load(topFeedbackList,true);
        doSearch(searchTerm);
    }

    private void loadMyFeedbacks() {
        load(myFeedbackList,true);
        doSearch(searchTerm);
    }

    private void load(ArrayList<FeedbackListItem> arrayList, boolean save){
        scrollListLayout.removeAllViews();
        for (FeedbackListItem item : arrayList) {
            item.updatePercentageColor(maxPoints);
            scrollListLayout.addView(item);
        }
        if (save){
            activeFeedbackList = arrayList;
        }
    }
}
