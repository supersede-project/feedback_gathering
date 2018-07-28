package ch.uzh.supersede.feedbacklibrary.components.buttons;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutCompat;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.beans.*;

public class VoteListItem extends AbstractSettingsListItem {

    public VoteListItem(Context context) {
        super(context);
    }

    public VoteListItem(Context context, int visibleTiles, FeedbackDetailsBean feedbackDetailsBean, LocalConfigurationBean configuration, int backgroundColor) {
        super(context, visibleTiles, feedbackDetailsBean, configuration, backgroundColor);

        LinearLayout upperWrapperLayout = getUpperWrapperLayout();
        LinearLayout lowerWrapperLayout = getLowerWrapperLayout();

        ImageView voteView = createVoteView(getShortParams(), context, feedbackDetailsBean, PADDING);

        upperWrapperLayout.addView(getTitleView());
        upperWrapperLayout.addView(getDateView());

        lowerWrapperLayout.addView(voteView);
        addView(getUpperWrapperLayout());
        addView(lowerWrapperLayout);
    }

    private ImageView createVoteView(LinearLayoutCompat.LayoutParams layoutParams, Context context, FeedbackDetailsBean feedbackDetailsBean, int padding) {
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(layoutParams);

        if (feedbackDetailsBean.getUpVotes() > 0) {
            imageView.setImageResource(R.drawable.ic_vote_up);
            imageView.getDrawable().mutate().setColorFilter(getForegroundColor(), PorterDuff.Mode.SRC_IN);
        } else if (feedbackDetailsBean.getUpVotes() < 0) {
            imageView.setImageResource(R.drawable.ic_vote_down);
            imageView.getDrawable().mutate().setColorFilter(getForegroundColor(), PorterDuff.Mode.SRC_IN);
        }

        imageView.setPadding(padding, padding, padding, padding);

        return imageView;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof VoteListItem) {
            long comparedTimestamp = ((VoteListItem) o).getFeedbackDetailsBean().getTimeStamp();
            return comparedTimestamp > getFeedbackDetailsBean().getTimeStamp() ? 1 : comparedTimestamp == getFeedbackDetailsBean().getTimeStamp() ? 0 : -1;
        }
        return 0;
    }
}
