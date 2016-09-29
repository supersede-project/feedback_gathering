package com.example.matthias.feedbacklibrary.views;

import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.matthias.feedbacklibrary.R;
import com.example.matthias.feedbacklibrary.models.Mechanism;
import com.example.matthias.feedbacklibrary.models.ScreenshotMechanism;

/**
 * Screenshot mechanism view
 */
public class ScreenshotMechanismView extends MechanismView {
    private ScreenshotMechanism screenshotMechanism = null;

    public ScreenshotMechanismView(LayoutInflater layoutInflater, Mechanism mechanism) {
        super(layoutInflater);
        this.screenshotMechanism = (ScreenshotMechanism) mechanism;
        setEnclosingLayout(getLayoutInflater().inflate(R.layout.screenshot_feedback_layout, null));
        initView();
    }

    private void initView() {
        ((TextView) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_screenshot_feedback_title)).setText(screenshotMechanism.getTitle());
    }

    public void setAnnotatedImagePath(String annotatedImagePath) {
        screenshotMechanism.setImagePath(annotatedImagePath);
    }

    @Override
    public void updateModel() {
    }
}
