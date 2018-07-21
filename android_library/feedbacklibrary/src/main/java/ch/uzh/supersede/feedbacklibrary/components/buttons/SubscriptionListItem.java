package ch.uzh.supersede.feedbacklibrary.components.buttons;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Gravity;
import android.widget.*;

import ch.uzh.supersede.feedbacklibrary.*;
import ch.uzh.supersede.feedbacklibrary.beans.*;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.services.*;
import ch.uzh.supersede.feedbacklibrary.stubs.RepositoryStub;

public class SubscriptionListItem extends AbstractSettingsListItem implements IFeedbackServiceEventListener {

    public IFeedbackServiceEventListener getListener() {
        return this;
    }

    public SubscriptionListItem(Context context, int visibleTiles, LocalFeedbackBean feedbackBean, LocalConfigurationBean configuration, int backgroundColor) {
        super(context, visibleTiles, feedbackBean, configuration, backgroundColor);

        LinearLayout upperWrapperLayout = getUpperWrapperLayout();
        LinearLayout lowerWrapperLayout = getLowerWrapperLayout();

        Switch subscribeToggle = createSwitch(getShortParams(), context, Gravity.START, feedbackBean, PADDING);

        upperWrapperLayout.addView(getTitleView());
        upperWrapperLayout.addView(getDateView());

        lowerWrapperLayout.addView(subscribeToggle);
        addView(getUpperWrapperLayout());
        addView(lowerWrapperLayout);
    }

    private Switch createSwitch(LinearLayoutCompat.LayoutParams layoutParams, final Context context, int gravity, final LocalFeedbackBean feedbackBean, int padding) {
        Switch toggle = new Switch(context);
        toggle.setLayoutParams(layoutParams);
        toggle.setPadding(padding, padding, padding, padding);
        toggle.setChecked(feedbackBean.getSubscribed() == 1);
        toggle.setGravity(gravity);

        toggle.getThumbDrawable().setColorFilter(getForegroundColor(), PorterDuff.Mode.MULTIPLY);
        toggle.getTrackDrawable().setColorFilter(getForegroundColor(), PorterDuff.Mode.MULTIPLY);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isSubscribed) {
                FeedbackBean feedback = RepositoryStub.getFeedback(context, feedbackBean);
                RepositoryStub.sendSubscriptionChange(context, feedback, isSubscribed);
                FeedbackService.getInstance(context).createSubscription(getListener(), feedback, isSubscribed);
            }
        });

        return toggle;
    }

    @Override
    public void onEventCompleted(EventType eventType, Object response) {
        switch (eventType) {
            case CREATE_FEEDBACK_SUBSCRIPTION:
                if (response instanceof FeedbackBean) {
                    boolean isSubscribed = FeedbackDatabase.getInstance(getContext()).getFeedbackState((FeedbackBean) response).isSubscribed();
                    if (isSubscribed) {
                        Toast.makeText(getContext(), "Subscribed to " + getFeedbackBean().getTitle(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Unsubscribed to " + getFeedbackBean().getTitle(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onEventFailed(EventType eventType, Object response) {
        Log.e(getClass().getSimpleName(), getResources().getString(R.string.api_service_event_failed, eventType, response.toString()));
    }

    @Override
    public void onConnectionFailed(EventType eventType) {
        Log.e(getClass().getSimpleName(), getResources().getString(R.string.api_service_connection_failed, eventType));
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return 0;
    }
}
