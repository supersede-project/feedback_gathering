package ch.uzh.supersede.feedbacklibrary.components.buttons;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import java.util.*;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.activities.*;
import ch.uzh.supersede.feedbacklibrary.beans.*;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.interfaces.ISortableFeedback;
import ch.uzh.supersede.feedbacklibrary.utils.*;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.FEEDBACK_SORTING.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.FEEDBACK_STATUS.DUPLICATE;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ACTIVE;

public class FeedbackListItem extends LinearLayout implements Comparable, ISortableFeedback {
    private TextView titleView;
    private TextView dateView;
    private TextView statusView;
    private TextView pointView;
    private FeedbackBean feedbackBean;
    private Enums.FEEDBACK_SORTING sorting = NONE;
    private String ownUser = USER_NAME_ANONYMOUS;
    private LocalConfigurationBean configuration;

    public FeedbackListItem(Context context, int visibleTiles, FeedbackBean feedbackBean, LocalConfigurationBean configuration, int backgroundColor) {
        super(context);
        this.configuration = configuration;
        this.feedbackBean = feedbackBean;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                                 .getDefaultDisplay()
                                 .getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;
        int padding = 10;
        int partHeight = NumberUtility.divide(screenHeight, visibleTiles + 3);
        int innerLayoutWidth = NumberUtility.multiply(screenWidth, 0.905f); //weighted 20/22
        LinearLayoutCompat.LayoutParams masterParams = new LinearLayoutCompat.LayoutParams(screenWidth, partHeight);
        masterParams.setMargins(5, 5, 5, 5);
        setLayoutParams(masterParams);
        setOrientation(VERTICAL);
        LinearLayoutCompat.LayoutParams longParams = new LinearLayoutCompat.LayoutParams(innerLayoutWidth, partHeight / 2);
        LinearLayoutCompat.LayoutParams shortParams = new LinearLayoutCompat.LayoutParams(innerLayoutWidth / 2, partHeight / 2);
        int textColor = 0;
        if (ColorUtility.isDark(backgroundColor)){
            textColor = ContextCompat.getColor(context, R.color.white);
        }else{
            textColor = ContextCompat.getColor(context, R.color.black);
        }
        LinearLayout upperWrapperLayout = createWrapperLayout(longParams, context, HORIZONTAL);
        LinearLayout lowerWrapperLayout = createWrapperLayout(longParams, context, HORIZONTAL);
        if (ACTIVE.check(context)) {
            ownUser = FeedbackDatabase.getInstance(getContext()).readString(USER_NAME, null);
        }
        titleView = createTextView(shortParams, context, feedbackBean.getTitle(), Gravity.START, padding, textColor);
        dateView = createTextView(shortParams, context, context.getString(R.string.list_date, DateUtility.getDateFromLong(feedbackBean.getTimeStamp())), Gravity.END, padding, textColor);
        int statusColor = ColorUtility.adjustColorToBackground(backgroundColor,feedbackBean.getFeedbackStatus().getColor(),0.4);
        statusView = createTextView(shortParams, context, feedbackBean.getFeedbackStatus().getLabel()
                .concat(SPACE + context.getString(R.string.list_responses, feedbackBean.getResponses())), Gravity.START, padding, statusColor);
        pointView = createTextView(shortParams, context, feedbackBean.getVotesAsText(), Gravity.END, padding, textColor);
        updatePercentageColor();
        setBackgroundColor(backgroundColor);
        upperWrapperLayout.addView(titleView);
        upperWrapperLayout.addView(dateView);
        lowerWrapperLayout.addView(statusView);
        lowerWrapperLayout.addView(pointView);
        addView(upperWrapperLayout);
        addView(lowerWrapperLayout);
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startFeedbackDetailsActivity();
                return false;
            }
        });
    }

    private void startFeedbackDetailsActivity() {
        Intent intent = new Intent(getContext(), FeedbackDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(EXTRA_KEY_FEEDBACK_BEAN, feedbackBean);
        intent.putExtra(EXTRA_KEY_APPLICATION_CONFIGURATION, configuration);
        getContext().startActivity(intent);
        ((Activity) getContext()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public FeedbackBean getFeedbackBean() {
        return feedbackBean;
    }

    private LinearLayout createWrapperLayout(LinearLayoutCompat.LayoutParams layoutParams, Context context, int orientation) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(orientation);
        return linearLayout;
    }

    private TextView createTextView(LinearLayoutCompat.LayoutParams layoutParams, Context context, String text, int gravity,int padding, int textColor) {
        TextView textView = new TextView(context);
        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        textView.setGravity(gravity);
        textView.setPadding(padding, padding, padding, padding);
        textView.setTextColor(textColor);
        return textView;
    }

    @Override
    @SuppressWarnings({"squid:S3358", "squid:S1210", "squid:S3776"})
    public int compareTo(@NonNull Object o) {
        if (o instanceof FeedbackListItem) {
            if (sorting == MINE || sorting == NEW) {
                long comparedTimestamp = ((FeedbackListItem) o).getFeedbackBean().getTimeStamp();
                return comparedTimestamp > feedbackBean.getTimeStamp() ? 1 : comparedTimestamp == feedbackBean.getTimeStamp() ? 0 : -1;
            } else if (sorting == HOT) {
                int comparedResponses = ((FeedbackListItem) o).getFeedbackBean().getResponses();
                return comparedResponses > feedbackBean.getResponses() ? 1 : comparedResponses == feedbackBean.getResponses() ? 0 : -1;
            } else if (sorting == TOP) {
                int comparedUpVotes = ((FeedbackListItem) o).getFeedbackBean().getUpVotes();
                return comparedUpVotes > feedbackBean.getUpVotes() ? 1 : comparedUpVotes == feedbackBean.getUpVotes() ? 0 : -1;
            }
        }
        return 0;
    }

    public void updatePercentageColor() {
        float percent;
        if (feedbackBean.getUpVotes() < 0) {
            percent = 1f / (2 * feedbackBean.getMinUpVotes()) * (feedbackBean.getMinUpVotes() - feedbackBean.getUpVotes());
        } else if (feedbackBean.getUpVotes() == 0) {
            pointView.setTextColor(DUPLICATE.getColor());
            return;
        } else {
            percent = 1f / (2 * feedbackBean.getMaxUpVotes()) * (feedbackBean.getMaxUpVotes() + feedbackBean.getUpVotes());
        }
        pointView.setTextColor(ColorUtility.percentToColor(percent));
    }

    @Override
    public void setSorting(Enums.FEEDBACK_SORTING sorting, List<Enums.FEEDBACK_STATUS> allowedStatuses) {
        if (sorting != MINE || StringUtility.equals(feedbackBean.getUserName(), ownUser)) {
            this.setVisibility(GONE);
            for (Enums.FEEDBACK_STATUS status : allowedStatuses){
                if (status == feedbackBean.getFeedbackStatus()){
                    this.setVisibility(VISIBLE);
                }
            }
        } else {
            this.setVisibility(GONE);
        }
        this.sorting = sorting;
    }
}
