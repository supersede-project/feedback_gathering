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
    private RatingBar ratingBar;

    public RatingMechanismView(LayoutInflater layoutInflater, Mechanism mechanism) {
        super(layoutInflater);
        this.ratingMechanism = (RatingMechanism) mechanism;
        setEnclosingLayout(getLayoutInflater().inflate(R.layout.rating_feedback_layout, null));
        initView();
    }

    private void initView() {
        ((TextView) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_rating_feedback_title)).setText(ratingMechanism.getTitle());
        this.ratingBar = ((RatingBar) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_rating_feedback_rating));
        this.ratingBar.setNumStars(ratingMechanism.getMaxRating());
        this.ratingBar.setStepSize(1.0f);
    }

    @Override
    public void updateModel() {
        ratingMechanism.setInputRating(((RatingBar) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_rating_feedback_rating)).getRating());
    }

    public float getRating(){
        return ratingBar.getRating();
    }
}
