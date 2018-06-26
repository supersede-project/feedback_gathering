package ch.uzh.supersede.feedbacklibrary.components.buttons;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackBean;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackVoteBean;
import ch.uzh.supersede.feedbacklibrary.beans.LocalConfigurationBean;
import ch.uzh.supersede.feedbacklibrary.beans.LocalFeedbackBean;
import ch.uzh.supersede.feedbacklibrary.stubs.RepositoryStub;

public class VoteListItem extends AbstractSettingsListItem {

    public VoteListItem(Context context, int visibleTiles, LocalFeedbackBean localFeedbackBean, LocalConfigurationBean configuration, int backgroundColor) {
        super(context, visibleTiles, localFeedbackBean, configuration, backgroundColor);

        LinearLayout upperWrapperLayout = getUpperWrapperLayout();
        LinearLayout lowerWrapperLayout = getLowerWrapperLayout();

        ImageView voteView = createVoteView(getShortParams(), context, localFeedbackBean, PADDING);

        upperWrapperLayout.addView(getTitleView());
        upperWrapperLayout.addView(getDateView());

        lowerWrapperLayout.addView(voteView);
        addView(getUpperWrapperLayout());
        addView(lowerWrapperLayout);
    }

    private ImageView createVoteView(LinearLayoutCompat.LayoutParams layoutParams, Context context, LocalFeedbackBean localFeedbackBean, int padding) {
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(layoutParams);

        if (localFeedbackBean.getVoted() > 0) {
            imageView.setImageResource(R.drawable.ic_thumb_up_black_48dp);
            imageView.getDrawable().mutate().setColorFilter(getForegroundColor(), PorterDuff.Mode.SRC_IN);
        } else if (localFeedbackBean.getVoted() < 0){
            imageView.setImageResource(R.drawable.ic_thumb_down_black_48dp);
            imageView.getDrawable().mutate().setColorFilter(getForegroundColor(), PorterDuff.Mode.SRC_IN);
        }

        imageView.setPadding(padding, padding, padding, padding);

        return imageView;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof VoteListItem){
            long comparedTimestamp = ((VoteListItem) o).getFeedbackBean().getCreationDate();
            return comparedTimestamp > getFeedbackBean().getCreationDate() ? 1 : comparedTimestamp == getFeedbackBean().getCreationDate() ? 0 : -1;
        }
        return 0;
    }
}
