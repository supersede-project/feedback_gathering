package ch.uzh.supersede.feedbacklibrary.components.buttons;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.*;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.utils.*;

public class FeedbackListItem extends LinearLayout implements Comparable {
    private TextView titleView;
    private TextView dateView;
    private TextView statusView;
    private TextView pointView;
    private int points;
    private String title;
    public enum FEEDBACK_STATUS{
        OPEN("Open",Color.rgb(0,150,255)),
        IN_PROGRESS("In Progress",Color.rgb(222,222,0)),
        REJECTED("Rejected",Color.rgb(222,0,0)),
        CLOSED("Closed",Color.rgb(0,222,100));
        private int color;
        private String label;
        FEEDBACK_STATUS(String label, int color){
            this.label = label;
            this.color = color;
        }

        public int getColor() {
            return color;
        }

        public String getLabel() {
            return label;
        }
    }

    public FeedbackListItem(Context context, int visibleTiles, String title, String date, FEEDBACK_STATUS status, int points) {
        super(context);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                                 .getDefaultDisplay()
                                 .getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;
        this.points = points;
        this.title = title;
        int partHeight = NumberUtility.divide(screenHeight, visibleTiles + 3);
        int innerLayoutWidth = NumberUtility.multiply(screenWidth, 0.905f); //weighted 20/22
        LinearLayoutCompat.LayoutParams masterParams = new LinearLayoutCompat.LayoutParams(screenWidth, partHeight);
        masterParams.setMargins(5, 5, 5, 5);
        setLayoutParams(masterParams);
        setOrientation(VERTICAL);
        LinearLayoutCompat.LayoutParams longParams = new LinearLayoutCompat.LayoutParams(screenWidth, partHeight / 2);
        LinearLayoutCompat.LayoutParams shortParams = new LinearLayoutCompat.LayoutParams(innerLayoutWidth / 2, partHeight / 2);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.dark_blue_square);
        int white = ContextCompat.getColor(context,R.color.white);
        LinearLayout upperWrapperLayout = createWrapperLayout(longParams, context, HORIZONTAL);
        LinearLayout lowerWrapperLayout = createWrapperLayout(longParams, context, HORIZONTAL);
        int padding = 10;
        titleView = createTextView(shortParams, context, title, Gravity.START, drawable, padding,white );
        dateView = createTextView(shortParams, context, date, Gravity.END, drawable, padding, white);
        statusView = createTextView(shortParams, context, status.getLabel(), Gravity.START, drawable, padding, status.getColor());
        pointView = createTextView(shortParams, context, "+" + points, Gravity.END, drawable, padding, white);
        upperWrapperLayout.addView(titleView);
        upperWrapperLayout.addView(dateView);
        lowerWrapperLayout.addView(statusView);
        lowerWrapperLayout.addView(pointView);
        addView(upperWrapperLayout);
        addView(lowerWrapperLayout);
    }

    private LinearLayout createWrapperLayout(LinearLayoutCompat.LayoutParams layoutParams, Context context, int orientation) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(orientation);
        return linearLayout;
    }

    private TextView createTextView(LinearLayoutCompat.LayoutParams layoutParams, Context context, String text, int gravity, Drawable background, int padding, int textColor) {
        TextView textView = new TextView(context);
        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        textView.setGravity(gravity);
        textView.setBackground(background);
        textView.setPadding(padding, padding, padding, padding);
        textView.setTextColor(textColor);
        return textView;
    }

    public int getPoints() {
        return points;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof FeedbackListItem) {
            int comparedPoints = ((FeedbackListItem) o).getPoints();
            return comparedPoints > getPoints() ? 1 : comparedPoints == getPoints() ? 0 : -1;
        }
        return 0;
    }

    public void updatePercentageColor(int maxPoints){
        float percent = 1f/maxPoints*points;
        pointView.setTextColor(ColorUtility.percentToColor(percent));
    }
}
