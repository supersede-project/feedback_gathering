package ch.uzh.supersede.feedbacklibrary.components.views;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.widget.LinearLayout;

import ch.uzh.supersede.feedbacklibrary.R;

public class SpacerListItem extends LinearLayout {
    public SpacerListItem(Context context, int height, int color) {
        super(context);
        setLayoutParams(new LinearLayoutCompat.LayoutParams(LayoutParams.MATCH_PARENT, height));
        GradientDrawable gradientDrawable=new GradientDrawable();
        gradientDrawable.setStroke(1,getResources().getColor(R.color.white));
        setBackground(gradientDrawable);
    }
}
