package ch.uzh.supersede.feedbacklibrary.components.buttons;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.activities.*;
import ch.uzh.supersede.feedbacklibrary.beans.*;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.utils.*;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.UserConstants.USER_IS_DEVELOPER;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ACTIVE;

public abstract class AbstractSettingsListItem extends LinearLayout  implements Comparable {
    public static final int PADDING = 10;
    private FeedbackDetailsBean feedbackDetailsBean;
    private LocalConfigurationBean configuration;
    private LinearLayout upperWrapperLayout;
    private LinearLayout lowerWrapperLayout;
    private TextView dateView;
    private TextView titleView;
    private Integer[] colors;

    public AbstractSettingsListItem(Context context){
        super(context);
    }

    public FeedbackDetailsBean getFeedbackDetailsBean() {
        return feedbackDetailsBean;
    }

    public LinearLayoutCompat.LayoutParams getShortParams() {
        return shortParams;
    }

    private LinearLayoutCompat.LayoutParams shortParams;

    public LinearLayout getUpperWrapperLayout() {
        return upperWrapperLayout;
    }

    public LinearLayout getLowerWrapperLayout() {
        return lowerWrapperLayout;
    }

    public TextView getDateView() {
        return dateView;
    }

    public TextView getTitleView() {
        return titleView;
    }

    public AbstractSettingsListItem(Context context, int visibleTiles, FeedbackDetailsBean feedbackDetailsBean, LocalConfigurationBean configuration, int backgroundColor) {
        super(context);
        this.feedbackDetailsBean = feedbackDetailsBean;
        this.colors = configuration.getTopColors();
        this.configuration = configuration;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                                 .getDefaultDisplay()
                                 .getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;
        int partHeight = NumberUtility.divide(screenHeight, visibleTiles + 3);
        int innerLayoutWidth = NumberUtility.multiply(screenWidth, 0.905f); //weighted 20/22
        LayoutParams masterParams = new LayoutParams(screenWidth, partHeight);
        masterParams.setMargins(5, 5, 5, 5);
        setLayoutParams(masterParams);
        setOrientation(VERTICAL);
        LinearLayoutCompat.LayoutParams longParams = new LinearLayoutCompat.LayoutParams(screenWidth, partHeight / 2);
        shortParams = new LinearLayoutCompat.LayoutParams(innerLayoutWidth / 2, partHeight / 2);

        int textColor;
        if (ColorUtility.isDark(backgroundColor)) {
            textColor = ContextCompat.getColor(context, R.color.white);
        } else {
            textColor = ContextCompat.getColor(context, R.color.black);
        }

        upperWrapperLayout = createWrapperLayout(longParams, context, HORIZONTAL);
        lowerWrapperLayout = createWrapperLayout(longParams, context, HORIZONTAL);
        titleView = createTextView(shortParams, context, feedbackDetailsBean.getTitle(), Gravity.START, PADDING, textColor);
        dateView = createTextView(shortParams, context, context.getString(R.string.list_date, DateUtility.getDateFromLong(feedbackDetailsBean.getTimeStamp())), Gravity.END, PADDING, textColor);
        int statusColor = ColorUtility.adjustColorToBackground(backgroundColor, feedbackDetailsBean.getFeedbackStatus().getColor(), 0.4);
        TextView statusView = createTextView(shortParams, context, feedbackDetailsBean.getFeedbackStatus().getLabel(),
                Gravity.START, PADDING, statusColor);

        setBackgroundColor(backgroundColor);
        lowerWrapperLayout.addView(statusView);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startFeedbackDetailsActivity();
            }
        });
    }

    private void startFeedbackDetailsActivity() {
        Intent intent;
        if (ACTIVE.check(getContext())){
            if (FeedbackDatabase.getInstance(getContext()).readBoolean(USER_IS_DEVELOPER,false)){
                intent = new Intent(getContext(), FeedbackDetailsDeveloperActivity.class);
            }else{
                intent = new Intent(getContext(), FeedbackDetailsActivity.class);
            }
        }else {
            Toast.makeText(getContext(),R.string.list_alert_user_level,Toast.LENGTH_SHORT).show();
            return;
        }
        intent.putExtra(EXTRA_KEY_CALLER_CLASS, FeedbackSettingsActivity.class.getName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(EXTRA_KEY_FEEDBACK_DETAIL_BEAN, feedbackDetailsBean);
        intent.putExtra(EXTRA_KEY_APPLICATION_CONFIGURATION, configuration);
        getContext().startActivity(intent);
        ((Activity) getContext()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private LinearLayout createWrapperLayout(LinearLayoutCompat.LayoutParams layoutParams, Context context, int orientation) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(orientation);
        return linearLayout;
    }

    private TextView createTextView(LinearLayoutCompat.LayoutParams layoutParams, Context context, String text, int gravity, int padding, int textColor) {
        TextView textView = new TextView(context);
        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        textView.setGravity(gravity);
        textView.setPadding(padding, padding, padding, padding);
        textView.setTextColor(textColor);
        return textView;
    }


    protected int getForegroundColor() {
        if (colors != null && colors.length > 1) {
            return colors[1];
        }
        return Color.BLACK;
    }
}
