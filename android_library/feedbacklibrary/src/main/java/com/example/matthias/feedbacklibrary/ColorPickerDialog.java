package com.example.matthias.feedbacklibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Color picker dialog class
 */
public class ColorPickerDialog extends DialogFragment {

    // Use this instance of the interface to deliver action events
    OnColorChangeDialogListener mListener;
    private int changedColor;

    public int getChangedColor() {
        return changedColor;
    }

    // Override the Fragment.onAttach() method to instantiate the OnColorChangeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the OnColorChangeDialogListener so we can send events to the host
            mListener = (OnColorChangeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        changedColor = getArguments().getInt("mInitialColor");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.color_picker_dialog, null); // Inflating the custom layout
        LinearLayout linearLayout = (LinearLayout) view;

        TextView textView = new TextView(view.getContext());
        textView.setText(R.string.supersede_feedbacklibrary_chosencolor_string);
        textView.setTextSize(20F);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(changedColor);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(50, 50, 0, 0);
        textView.setLayoutParams(lp);

        final ColorPickerView cpv = new ColorPickerView(view.getContext(), changedColor, textView);
        cpv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));

        linearLayout.addView(cpv);
        linearLayout.addView(textView);

        builder.setView(linearLayout) // Setting the view to the custom layout
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User wants to change the color
                        changedColor = cpv.getChangedColor();
                        mListener.onDialogPositiveClick(ColorPickerDialog.this);
                    }
                })
                .setNegativeButton("DISCARD", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface OnColorChangeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
    }

    private class ColorPickerView extends View {
        private static final int CENTER_X = 100;
        private static final int CENTER_Y = 100;
        private static final int CENTER_RADIUS = 32;
        private static final float PI = 3.1415926f;
        private final int[] mColors;
        private TextView textView;
        private Paint mPaint;
        private Paint mCenterPaint;
        private RectF newRectF;
        private int changedColor;

        ColorPickerView(Context c, int initialColor, TextView textView) {
            super(c);
            this.textView = textView;
            changedColor = initialColor;
            mColors = new int[]{
                    0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF, 0xFF00FF00,
                    0xFFFFFF00, 0xFFFF0000
            };
            Shader s = new SweepGradient(0, 0, mColors, null);

            newRectF = new RectF();

            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setShader(s);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(32);

            mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mCenterPaint.setColor(initialColor);
            mCenterPaint.setStrokeWidth(5);
        }

        private int ave(int s, int d, float p) {
            return s + Math.round(p * (d - s));
        }

        public int getChangedColor() {
            return changedColor;
        }

        private int interpolateColor(int colors[], float unit) {
            if (unit <= 0) {
                return colors[0];
            }
            if (unit >= 1) {
                return colors[colors.length - 1];
            }

            float p = unit * (colors.length - 1);
            int i = (int) p;
            p -= i;

            // now p is just the fractional part [0...1) and i is the index
            int c0 = colors[i];
            int c1 = colors[i + 1];
            int a = ave(Color.alpha(c0), Color.alpha(c1), p);
            int r = ave(Color.red(c0), Color.red(c1), p);
            int g = ave(Color.green(c0), Color.green(c1), p);
            int b = ave(Color.blue(c0), Color.blue(c1), p);

            return Color.argb(a, r, g, b);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            float r = CENTER_X - mPaint.getStrokeWidth() * 0.5f;
            canvas.translate(CENTER_X, CENTER_X);
            newRectF.set(-r, -r, r, r);
            canvas.drawOval(newRectF, mPaint);
            canvas.drawCircle(0, 0, CENTER_RADIUS, mCenterPaint);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(CENTER_X * 2, CENTER_Y * 2);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX() - CENTER_X;
            float y = event.getY() - CENTER_Y;

            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    float angle = (float) Math.atan2(y, x);
                    // need to turn angle [-PI ... PI] into unit [0....1]
                    float unit = angle / (2 * PI);
                    if (unit < 0) {
                        unit += 1;
                    }
                    mCenterPaint.setColor(interpolateColor(mColors, unit));
                    textView.setTextColor(mCenterPaint.getColor());
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    changedColor = mCenterPaint.getColor();
                    break;
            }
            return true;
        }
    }
}
