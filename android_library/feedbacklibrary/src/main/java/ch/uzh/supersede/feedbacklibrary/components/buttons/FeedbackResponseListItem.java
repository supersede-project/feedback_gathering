package ch.uzh.supersede.feedbacklibrary.components.buttons;

import android.app.Activity;
import android.content.*;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.*;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.activities.FeedbackDetailsActivity;
import ch.uzh.supersede.feedbacklibrary.beans.*;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.utils.*;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ACTIVE;

public class FeedbackResponseListItem extends LinearLayout implements Comparable {
    private TextView userView;
    private TextView dateView;
    private TextView contentView;
    private FeedbackResponseBean feedbackResponseBean;

    public FeedbackResponseListItem(Context context, FeedbackResponseBean feedbackResponseBean) {
        super(context);
        this.feedbackResponseBean = feedbackResponseBean;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                                 .getDefaultDisplay()
                                 .getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;
        int padding = 10;
        int partHeight = 300; // Todo change according to textsize
        int innerLayoutWidth = NumberUtility.multiply(screenWidth, 0.905f); //weighted 20/22
        LinearLayoutCompat.LayoutParams masterParams = new LinearLayoutCompat.LayoutParams(screenWidth, partHeight);
        masterParams.setMargins(5, 5, 5, 5);
        setLayoutParams(masterParams);
        setOrientation(VERTICAL);
        LinearLayoutCompat.LayoutParams longParams = new LinearLayoutCompat.LayoutParams(screenWidth, partHeight / 2);
        LinearLayoutCompat.LayoutParams shortParams = new LinearLayoutCompat.LayoutParams(innerLayoutWidth / 2, partHeight / 2);
        int textColor = 0;
        int backgroundColor = 0;
        if (feedbackResponseBean.isDeveloper()){
            textColor = ContextCompat.getColor(context, R.color.gold_2);
            backgroundColor = ContextCompat.getColor(context, R.color.gold_3);
        }else if (feedbackResponseBean.isFeedbackOwner()){
            textColor = ContextCompat.getColor(context, R.color.accent);
            backgroundColor = ContextCompat.getColor(context, R.color.pink);
        }else{
            textColor = ContextCompat.getColor(context, R.color.cyan);
            backgroundColor = ContextCompat.getColor(context, R.color.indigo);
        }
        setBackgroundColor(backgroundColor);
        LinearLayout upperWrapperLayout = createWrapperLayout(longParams, context, HORIZONTAL);
        LinearLayout lowerWrapperLayout = createWrapperLayout(longParams, context, HORIZONTAL);
        userView = createTextView(shortParams, context, feedbackResponseBean.getUserName(), Gravity.START, padding, textColor,backgroundColor);
        dateView = createTextView(shortParams, context, context.getString(R.string.list_date, DateUtility.getDateFromLong(feedbackResponseBean.getTimeStamp())), Gravity.END, padding, textColor,backgroundColor);
        contentView = createTextView(longParams, context, feedbackResponseBean.getContent(), Gravity.START, padding, textColor,backgroundColor);
        upperWrapperLayout.addView(userView);
        upperWrapperLayout.addView(dateView);
        lowerWrapperLayout.addView(contentView);
        addView(upperWrapperLayout);
        addView(lowerWrapperLayout);
    }

    private void startFeedbackDetailsActivity() {
        Intent intent = new Intent(getContext(), FeedbackDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(EXTRA_KEY_FEEDBACK_BEAN, feedbackResponseBean);
        getContext().startActivity(intent);
        ((Activity) getContext()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


    private LinearLayout createWrapperLayout(LinearLayoutCompat.LayoutParams layoutParams, Context context, int orientation) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(orientation);
        return linearLayout;
    }

    private TextView createTextView(LinearLayoutCompat.LayoutParams layoutParams, Context context, String text, int gravity,  int padding, int textColor, int backgroundColor) {
        TextView textView = new TextView(context);
        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        textView.setGravity(gravity);
        textView.setBackgroundColor(backgroundColor);
        textView.setPadding(padding, padding, padding, padding);
        textView.setTextColor(textColor);
        return textView;
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
}
