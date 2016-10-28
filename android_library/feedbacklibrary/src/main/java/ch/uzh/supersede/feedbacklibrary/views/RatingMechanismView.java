package ch.uzh.supersede.feedbacklibrary.views;

import android.view.LayoutInflater;
import android.widget.RatingBar;
import android.widget.TextView;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.models.Mechanism;
import ch.uzh.supersede.feedbacklibrary.models.RatingMechanism;

/**
 * Rating mechanism view
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
