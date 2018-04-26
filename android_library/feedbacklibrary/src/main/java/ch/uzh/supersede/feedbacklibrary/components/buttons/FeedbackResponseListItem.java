package ch.uzh.supersede.feedbacklibrary.components.buttons;

import android.app.*;
import android.content.*;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.activities.FeedbackDetailsActivity;
import ch.uzh.supersede.feedbacklibrary.beans.*;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.stubs.RepositoryStub;
import ch.uzh.supersede.feedbacklibrary.utils.*;

import static android.content.Context.MODE_PRIVATE;
import static ch.uzh.supersede.feedbacklibrary.components.buttons.FeedbackResponseListItem.RESPONSE_MODE.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.SettingsConstants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.RESPONSE_MODE.EDITING;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.RESPONSE_MODE.READING;

public class FeedbackResponseListItem extends LinearLayout implements Comparable {
    private View upperLeftView;
    private View upperRightView;
    private View bottomView;
    private FeedbackResponseBean feedbackResponseBean;
    private FeedbackBean feedbackBean;
    private RESPONSE_MODE mode;
    private int minResponseLength;
    private int maxResponseLength;

    public enum RESPONSE_MODE {
        FIXED, EDITABLE
    }

    public FeedbackResponseListItem(Context context, FeedbackBean feedbackBean, FeedbackResponseBean feedbackResponseBean, RESPONSE_MODE mode) {
        super(context);
        this.feedbackBean = feedbackBean;
        this.feedbackResponseBean = feedbackResponseBean;
        this.mode = mode;
        minResponseLength = getContext().getSharedPreferences(SHARED_PREFERENCES_ID, MODE_PRIVATE).getInt(SHARED_PREFERENCES_SETTINGS_RESPONSE_MIN_LENGTH, SETTINGS_RESPONSE_MIN_LENGTH);
        maxResponseLength = getContext().getSharedPreferences(SHARED_PREFERENCES_ID, MODE_PRIVATE).getInt(SHARED_PREFERENCES_SETTINGS_RESPONSE_MAX_LENGTH, SETTINGS_RESPONSE_MAX_LENGTH);
        generateListItem(feedbackResponseBean);
    }

    private void generateListItem(FeedbackResponseBean feedbackResponseBean) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager()
                                .getDefaultDisplay()
                                .getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int padding = 10;
        int headerHeight = 150;
        int innerLayoutWidth = NumberUtility.multiply(screenWidth, 0.905f); //weighted 20/22
        LinearLayoutCompat.LayoutParams masterParams = new LinearLayoutCompat.LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        masterParams.setMargins(5, 5, 5, 5);
        setLayoutParams(masterParams);
        setOrientation(VERTICAL);
        LinearLayoutCompat.LayoutParams longParams = new LinearLayoutCompat.LayoutParams(screenWidth, LayoutParams.WRAP_CONTENT);
        LinearLayoutCompat.LayoutParams shortParams = new LinearLayoutCompat.LayoutParams(innerLayoutWidth / 2, headerHeight);
        setBackgroundColor(resolveBackgroundColor(feedbackResponseBean));
        LinearLayout upperWrapperLayout = createWrapperLayout(longParams, getContext(), HORIZONTAL);
        LinearLayout lowerWrapperLayout = createWrapperLayout(longParams, getContext(), HORIZONTAL);
        generateElements(feedbackResponseBean, padding, longParams, shortParams);
        upperWrapperLayout.addView(upperLeftView);
        upperWrapperLayout.addView(upperRightView);
        lowerWrapperLayout.addView(bottomView);
        addView(upperWrapperLayout);
        addView(lowerWrapperLayout);
    }

    @SuppressWarnings({"squid:S2696"})
    private void generateElements(FeedbackResponseBean feedbackResponseBean, int padding, LinearLayoutCompat.LayoutParams longParams, LinearLayoutCompat.LayoutParams shortParams) {
        if (mode == EDITABLE) {
            FeedbackDetailsActivity.mode = EDITING;
            OnClickListener cancelListener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeFeedbackResponse();
                }
            };
            upperLeftView = createButtonView(shortParams,
                    getContext().getString(R.string.details_cancel),
                    cancelListener,
                    new int[]{25, 25, 25, 0},
                    padding,
                    resolveTextColor(feedbackResponseBean),
                    resolveBackgroundColor(feedbackResponseBean));
            OnClickListener sendListener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    prepareFeedbackResponse();
                }
            };
            upperRightView = createButtonView(shortParams,
                    getContext().getString(R.string.details_send_response),
                    sendListener,
                    new int[]{0, 25, 25, 25},
                    padding,
                    resolveTextColor(feedbackResponseBean),
                    resolveBackgroundColor(feedbackResponseBean));
            bottomView = createEditTextView(longParams,
                    Gravity.START,
                    padding,
                    resolveTextColor(feedbackResponseBean),
                    resolveBackgroundColor(feedbackResponseBean));
        } else if (mode == FIXED) {
            upperLeftView = createTextView(shortParams,
                    feedbackResponseBean.getUserName(),
                    Gravity.START,
                    padding,
                    resolveTextColor(feedbackResponseBean),
                    resolveBackgroundColor(feedbackResponseBean));
            upperRightView = createTextView(shortParams,
                    getContext().getString(R.string.list_date, DateUtility.getDateFromLong(mode==FIXED?feedbackResponseBean.getTimeStamp():System.currentTimeMillis())),
                    Gravity.END,
                    padding,
                    resolveTextColor(feedbackResponseBean),
                    resolveBackgroundColor(feedbackResponseBean));
            bottomView = createTextView(longParams,
                    feedbackResponseBean.getContent(),
                    Gravity.START,
                    padding,
                    resolveTextColor(feedbackResponseBean),
                    resolveBackgroundColor(feedbackResponseBean));
        }
    }

    private int resolveTextColor(FeedbackResponseBean feedbackResponseBean) {
        if (feedbackResponseBean != null && feedbackResponseBean.isDeveloper() || mode == EDITABLE && FeedbackDatabase.getInstance(getContext()).readBoolean(IS_DEVELOPER,false)) {
            return ContextCompat.getColor(getContext(), R.color.gold_2);
        } else if (feedbackResponseBean != null && feedbackResponseBean.isFeedbackOwner() || mode == EDITABLE) {
            return ContextCompat.getColor(getContext(), R.color.accent);
        }else{
            return ContextCompat.getColor(getContext(), R.color.cyan);
        }
    }
    private int resolveBackgroundColor(FeedbackResponseBean feedbackResponseBean) {
        if (feedbackResponseBean != null && feedbackResponseBean.isDeveloper() || mode == EDITABLE && FeedbackDatabase.getInstance(getContext()).readBoolean(IS_DEVELOPER,false)) {
            return ContextCompat.getColor(getContext(), R.color.gold_3);
        } else if (feedbackResponseBean != null && feedbackResponseBean.isFeedbackOwner() || mode == EDITABLE) {
            return ContextCompat.getColor(getContext(), R.color.pink);
        }else{
            return ContextCompat.getColor(getContext(), R.color.indigo);
        }
    }

    private LinearLayout createWrapperLayout(LinearLayoutCompat.LayoutParams layoutParams, Context context, int orientation) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(orientation);
        return linearLayout;
    }

    private TextView createTextView(LinearLayoutCompat.LayoutParams layoutParams, String text, int gravity, int padding, int textColor, int backgroundColor) {
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        textView.setGravity(gravity);
        textView.setTextColor(textColor);
        textView.setBackgroundColor(backgroundColor);
        textView.setPadding(padding, padding, padding, padding);
        return textView;
    }
    private EditText createEditTextView(LinearLayoutCompat.LayoutParams layoutParams, int gravity, int padding, int textColor, int backgroundColor) {
        EditText editText = new EditText(getContext());
        editText.setMaxLines(Integer.MAX_VALUE);
        editText.setLayoutParams(layoutParams);
        editText.setGravity(gravity);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxResponseLength)});
        editText.setTextColor(textColor);
        editText.setBackgroundColor(backgroundColor);
        editText.setPadding(padding, padding, padding, padding);
        return editText;
    }

    private Button createButtonView(LinearLayoutCompat.LayoutParams layoutParams, String label, OnClickListener listener, int[] margins, int padding, int textColor, int backgroundColor) {
        Button button = new Button(getContext());
        layoutParams.setMargins(margins[0],margins[1],margins[2],margins[3]);
        button.setLayoutParams(layoutParams);
        button.setGravity(Gravity.CENTER);
        button.setTextColor(backgroundColor);
        button.setBackgroundColor(textColor);
        button.setText(label);
        button.setPadding(padding, padding, padding, padding);
        button.setOnClickListener(listener);
        return button;
    }

    private void prepareFeedbackResponse() {
        if (((EditText)bottomView).getText().length() < minResponseLength){
            Toast.makeText(getContext(),getContext().getString(R.string.details_response_too_short),Toast.LENGTH_SHORT).show();
        }else{
            String response = ((EditText) bottomView).getText().toString();
            RepositoryStub.sendFeedbackResponse(getContext(),feedbackBean, response);
            removeFeedbackResponse();
            FeedbackDetailsActivity.persistFeedbackResponseLocally(getContext(),feedbackBean,response);
        }
    }

    @SuppressWarnings({"squid:S2696"})
    private void removeFeedbackResponse() {
        setVisibility(GONE);
        FeedbackDetailsActivity.mode = READING;
        cancelKeyboardInputOnBottomView();
    }

    private void cancelKeyboardInputOnBottomView() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(bottomView.getWindowToken(), 0);
        }
    }

    public FeedbackResponseBean getFeedbackResponseBean() {
        return feedbackResponseBean;
    }

    @Override
    @SuppressWarnings({"squid:S3358", "squid:S1210", "squid:S3776"})
    public int compareTo(@NonNull Object o) {
        if (o instanceof FeedbackResponseListItem) {
            long comparedTimestamp = ((FeedbackResponseListItem) o).getFeedbackResponseBean().getTimeStamp();
            return comparedTimestamp > feedbackResponseBean.getTimeStamp() ? 1 : comparedTimestamp == feedbackResponseBean.getTimeStamp() ? 0 : -1;
        }
        return 0;
    }

    public void requestInputFocus(){
        bottomView.requestFocus();
        InputMethodManager inputMethodManager =
                (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                bottomView.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
    }
}
