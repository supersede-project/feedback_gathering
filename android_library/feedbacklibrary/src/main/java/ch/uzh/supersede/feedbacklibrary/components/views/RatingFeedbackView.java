package ch.uzh.supersede.feedbacklibrary.components.views;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RatingBar;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.models.RatingFeedback;
import ch.uzh.supersede.feedbacklibrary.utils.ColorUtility;

public final class RatingFeedbackView extends AbstractFeedbackPartView {
    private RatingFeedback ratingFeedback;
    private RatingBar ratingBar;

    public RatingFeedbackView(LayoutInflater layoutInflater, RatingFeedback ratingFeedback) {
        super(layoutInflater);
        this.viewOrder = ratingFeedback.getOrder();
        this.ratingFeedback = ratingFeedback;
        setEnclosingLayout(getLayoutInflater().inflate(R.layout.rating_feedback, null));
        initView();
    }

    @Override
    protected void colorPrimary(int color) {
        setRatingStarColor(color);
        getEnclosingLayout().setBackgroundColor(color);
    }

    @Override
    protected void colorSecondary(int color) {
        ((FrameLayout) ratingBar.getParent()).setBackgroundColor(color);
    }

    @Override
    protected void colorTertiary(int color) {
        //NOP
    }

    private void setRatingStarColor(int color) {
        LayerDrawable progressLayerDrawable = (LayerDrawable) ratingBar.getProgressDrawable();
        Drawable progressDrawable1 = progressLayerDrawable.getDrawable(1);
        progressDrawable1.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_ATOP);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ratingBar.setProgressTintList(ColorStateList.valueOf(color));
        } else {
            Drawable backgroundDrawable = progressLayerDrawable.getDrawable(0);
            Drawable progressDrawable2 = progressLayerDrawable.getDrawable(2);
            backgroundDrawable.setColorFilter(ColorUtility.isDark(color) ? Color.WHITE : Color.BLACK, PorterDuff.Mode.SRC_ATOP);
            progressDrawable2.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
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
