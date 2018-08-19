package ch.uzh.supersede.feedbacklibrary.components.views;

import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.design.widget.*;
import android.text.*;
import android.view.*;
import android.widget.*;

import java.lang.reflect.*;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.models.TextFeedback;

public final class TextFeedbackView extends AbstractFeedbackPartView {
    private TextFeedback textFeedback;

    @Override
    protected void colorPrimary(int color) {
        getEnclosingLayout().setBackgroundColor(color);
        int childCount = ((LinearLayout) getEnclosingLayout()).getChildCount();
        for (int p = 0; p < childCount; p++) {
            handleChildLayoutsForegrounds(((LinearLayout) getEnclosingLayout()).getChildAt(p), color);
        }
    }

    @Override
    protected void colorSecondary(int color) {
        int childCount = ((LinearLayout) getEnclosingLayout()).getChildCount();
        for (int p = 0; p < childCount; p++) {
            handleChildLayoutsBackgrounds(((LinearLayout) getEnclosingLayout()).getChildAt(p), color);
        }
    }

    @Override
    protected void colorTertiary(int color) {

    }

    private void handleChildLayoutsBackgrounds(View v, int color) {
        if (v instanceof ViewGroup) {
            v.setBackgroundColor(color);
            int childCount = ((ViewGroup) v).getChildCount();
            for (int p = 0; p < childCount; p++) {
                handleChildLayoutsBackgrounds(((ViewGroup) v).getChildAt(p), color);
            }
        }
    }

    private void handleChildLayoutsForegrounds(View v, int color) {
        if (v instanceof ViewGroup) {
            int childCount = ((ViewGroup) v).getChildCount();
            for (int p = 0; p < childCount; p++) {
                handleChildLayoutsForegrounds(((ViewGroup) v).getChildAt(p), color);
            }
        }
        if (v instanceof TextView) {
            ((TextView) v).setTextColor(color);
            if (v instanceof TextInputEditText) {
                ColorStateList colorStateList = ColorStateList.valueOf(color);
                ((TextInputEditText) v).setSupportBackgroundTintList(colorStateList);
            }
        }
        if (v instanceof TextInputLayout) {
            setUpperHintColor(color, (TextInputLayout) v);
        }
    }

    private void setUpperHintColor(int color, TextInputLayout textInputLayout) {
        try {
            for (Field field : new Field[]{textInputLayout.getClass().getDeclaredField("mFocusedTextColor"), textInputLayout.getClass().getDeclaredField("mDefaultTextColor")}) {
                field.setAccessible(true);
                int[][] states = new int[][]{{0}};
                int[] colors = new int[]{
                        color
                };
                ColorStateList colorStateList = new ColorStateList(states, colors);
                field.set(textInputLayout, colorStateList);
            }
            Method method = textInputLayout.getClass().getDeclaredMethod("updateLabelState", boolean.class);
            method.setAccessible(true);
            method.invoke(textInputLayout, true);
        } catch (Exception e) {
            //NOP
        }
    }

    public TextFeedbackView(LayoutInflater layoutInflater, TextFeedback feedbackPart) {
        super(layoutInflater);
        this.viewOrder = feedbackPart.getOrder();
        this.textFeedback = feedbackPart;
        setEnclosingLayout(getLayoutInflater().inflate(R.layout.text_feedback_enclosing, null));
        initView();
    }


    private void initView() {
        final TextInputLayout textInputLayout = (TextInputLayout) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_text_feedback_input_layout);
        final TextInputEditText textInputEditText = (TextInputEditText) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_text_feedback_text);

        // Set the hint and enable it
        textInputLayout.setHintEnabled(true);
        textInputLayout.setHint(textFeedback.getHint());

        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                //nop
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //nop
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    textInputLayout.setHint(textFeedback.getLabel());
                } else if (s.length() == 0) {
                    textInputLayout.setHint(textFeedback.getHint());
                }

                if (textFeedback.getMaxLength() != null && textInputLayout.isErrorEnabled()) {
                    if (s.length() > textFeedback.getMaxLength()) {
                        textInputLayout.setError(getEnclosingLayout().getResources().getString(R.string.feedback_text_warning, textFeedback.getMaxLength()));
                    } else {
                        textInputLayout.setError(null);
                    }
                }
            }
        });
    }

    @Override
    public void updateModel() {
        textFeedback.setText(((TextInputEditText) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_text_feedback_text)).getText().toString());
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof AbstractFeedbackPartView) {
            int comparedViewOrder = ((AbstractFeedbackPartView) o).getViewOrder();
            return comparedViewOrder > getViewOrder() ? -1 : comparedViewOrder == getViewOrder() ? 0 : 1;
        }
        return 0;
    }
}
