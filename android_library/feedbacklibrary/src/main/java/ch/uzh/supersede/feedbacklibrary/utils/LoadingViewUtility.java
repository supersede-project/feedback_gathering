package ch.uzh.supersede.feedbacklibrary.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.widget.TextView;

import ch.uzh.supersede.feedbacklibrary.R;

public class LoadingViewUtility {
    private LoadingViewUtility() {
    }

    public static TextView createLoadingView(Context context, int width, int height, int backgroundColor) {
        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(width, height);
        TextView loadingTextView = new TextView(context);
        loadingTextView.setLayoutParams(params);
        loadingTextView.setText(R.string.loading_text);
        loadingTextView.setGravity(Gravity.CENTER);
        loadingTextView.setPadding(0, 0, 0, 0);
        loadingTextView.setTextColor(ColorUtility.getTextColor(context, backgroundColor));
        return loadingTextView;
    }
}
