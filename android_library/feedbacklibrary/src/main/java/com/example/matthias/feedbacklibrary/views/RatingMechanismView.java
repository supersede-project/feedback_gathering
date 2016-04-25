package com.example.matthias.feedbacklibrary.views;

import android.view.LayoutInflater;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.matthias.feedbacklibrary.R;
import com.example.matthias.feedbacklibrary.models.Mechanism;
import com.example.matthias.feedbacklibrary.models.RatingMechanism;

/**
 * Created by Matthias on 24.04.2016.
 */
public class RatingMechanismView extends MechanismView {
    private RatingMechanism ratingMechanism = null;

    public RatingMechanismView(LayoutInflater layoutInflater, Mechanism mechanism) {
        super(layoutInflater);
        this.ratingMechanism = (RatingMechanism) mechanism;
        setEnclosingLayout(getLayoutInflater().inflate(R.layout.rating_feedback_layout, null));
        initView();
    }

    private void initView() {
        ((TextView) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_rating_feedback_title)).setText(ratingMechanism.getTitle());
        RatingBar bar = ((RatingBar) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_rating_feedback_rating));
        bar.setNumStars(ratingMechanism.getMaxRating());
        bar.setStepSize(1.0f);
        bar.setRating(ratingMechanism.getDefaultRating());
    }

    @Override
    public void updateModel() {
        ratingMechanism.setInputRating(((RatingBar) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_rating_feedback_rating)).getRating());
    }
}
