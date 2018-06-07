package ch.uzh.supersede.feedbacklibrary.components.views;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.RatingBar;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.models.RatingFeedback;

public class RatingFeedbackView extends AbstractFeedbackPartView {
    private RatingFeedback ratingFeedback;
    private RatingBar ratingBar;

    public RatingFeedbackView(LayoutInflater layoutInflater, RatingFeedback ratingFeedback) {
        super(layoutInflater);
        this.viewOrder = ratingFeedback.getOrder();
        this.ratingFeedback = ratingFeedback;
        setEnclosingLayout(getLayoutInflater().inflate(R.layout.rating_feedback, null));
        initView();
    }

    private void initView() {
        this.ratingBar = ((RatingBar) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_rating_feedback_rating));
        this.ratingBar.setNumStars(ratingFeedback.getMaxRating());
        this.ratingBar.setStepSize(1.0f);
    }

    @Override
    public void updateModel() {
        ratingFeedback.setRating((long) ((RatingBar) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_rating_feedback_rating)).getRating());
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
