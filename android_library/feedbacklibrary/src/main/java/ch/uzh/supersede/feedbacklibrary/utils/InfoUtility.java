package ch.uzh.supersede.feedbacklibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;

import ch.uzh.supersede.feedbacklibrary.R;

/**
 * Created by Marco Bonzanigo on 18.11.2016.
 * Utility to generate dynamic Info-Bubbles.
 */

public class InfoUtility {
    private static ArrayList<Integer> idList = new ArrayList<>();
    private int screenWidth;
    private int screenHeight;

    public InfoUtility(int screenWidth, int screenHeight) {
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }

    public RelativeLayout addInfoBox(RelativeLayout layout, String title, String text,Activity activity, View anchorView, final View... chainedViews) {
        RelativeLayout infoBox = createInfoBox(title,text,activity,anchorView,chainedViews);
        if (infoBox != null){
            layout.addView(infoBox);
            layout.bringChildToFront(infoBox);
            infoBox.bringToFront();
            layout.invalidate();
        }
        return infoBox;
    }
    private RelativeLayout createInfoBox(String title, String text, Activity activity, View anchorView, final View... chainedViews) {
        if (idList.contains(anchorView.getId())){
            return null;
        }else{
            idList.add(anchorView.getId());
        }
        //Text-Size
        int titleWidth = measureTextWidth(title,20,getDensity(activity));
        //X-Y Locations
        int[] xyLocations = new int[2];
        anchorView.getLocationOnScreen(xyLocations);
        int x = xyLocations[0];
        int y = xyLocations[1];
        //View-Handling
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.infobox_layout, null, false);
        RelativeLayout infoBox = (RelativeLayout) view.findViewById(R.id.layout_info_box);
        //Params
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(titleWidth,ViewGroup.LayoutParams.WRAP_CONTENT);
        infoBox.setLayoutParams(layoutParams);
        //Set Text
        ((TextView)view.findViewById(R.id.info_box_title)).setText(title);
        ((TextView)view.findViewById(R.id.info_box_title)).setPaintFlags(((TextView)view.findViewById(R.id.info_box_title)).getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        ((TextView)view.findViewById(R.id.info_box_text)).setText(text);
        //Layouting
        int anchorWidth = anchorView.getWidth();
        int anchorHeight = anchorView.getHeight();
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        if (x < screenWidth / 2) {
            infoBox.setX(x+anchorWidth/2);
            if (y < screenHeight/2){ //upper-left
                infoBox.setBackground(ResourcesCompat.getDrawable(view.getResources(), R.drawable.info_box_left_upper_neutral, null));
                infoBox.setY(y+anchorHeight/2);
            }else{ //lower-left
                infoBox.setBackground(ResourcesCompat.getDrawable(view.getResources(), R.drawable.info_box_left_lower_neutral, null));
                infoBox.setY(y-view.getMeasuredHeight());
            }
        } else {//right info-box
            infoBox.setX(x+anchorWidth/2-titleWidth);
            if (y < screenHeight/2){ //upper-right
                infoBox.setBackground(ResourcesCompat.getDrawable(view.getResources(), R.drawable.info_box_right_upper_neutral, null));
                infoBox.setY(y+anchorHeight/2);
            }else{ //lower-right
                infoBox.setBackground(ResourcesCompat.getDrawable(view.getResources(), R.drawable.info_box_right_lower_neutral, null));
                infoBox.setY(y-view.getMeasuredHeight());
            }
        }

        for (View chainedView : chainedViews){
            chainedView.setVisibility(View.INVISIBLE);
        }
        //ActionListener
        infoBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation fadeOut = new AlphaAnimation(1, 0);
                fadeOut.setInterpolator(new AccelerateInterpolator());
                fadeOut.setDuration(500);
                v.setAnimation(fadeOut);
                v.setVisibility(View.INVISIBLE);
                ((ViewGroup) v.getParent()).removeView(v);
                for (View chainedView : chainedViews){
                    Animation fadeIn = new AlphaAnimation(0, 1);
                    fadeIn.setInterpolator(new AccelerateInterpolator());
                    fadeIn.setDuration(500);
                    chainedView.setAnimation(fadeIn);
                    chainedView.setVisibility(View.VISIBLE);
                }
            }
        });
        return infoBox;
    }

    private static int measureTextWidth(String text, int size, float density) {
        Paint paint = new Paint();
        Rect bounds = new Rect();
        float scaledPx = size * density;

        paint.setTypeface(Typeface.DEFAULT);
        paint.setTextSize(scaledPx);

        paint.getTextBounds(text, 0, text.length(), bounds);
        float measureText = paint.measureText(text);
        return (int) (measureText+75);
    }
    private static float getDensity(Activity activity){
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        return metrics.density;
    }

}