package ch.uzh.supersede.feedbacklibrary.components.buttons;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import ch.uzh.supersede.feedbacklibrary.BuildConfig;
import ch.uzh.supersede.feedbacklibrary.beans.LocalFeedbackBean;
import ch.uzh.supersede.feedbacklibrary.beans.LocalFeedbackState;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService;
import ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener;
import ch.uzh.supersede.feedbacklibrary.stubs.RepositoryStub;

public class SubscriptionsListItem extends AbstractSettingsListItem implements IFeedbackServiceEventListener {
    public IFeedbackServiceEventListener getListener() {
        return this;
    }

    public SubscriptionsListItem(Context context, int visibleTiles, LocalFeedbackBean feedbackBean) {
        super(context, visibleTiles, feedbackBean);

        LinearLayout upperWrapperLayout = getUpperWrapperLayout();
        LinearLayout lowerWrapperLayout = getLowerWrapperLayout();

        Switch subscribeToggle = createSwitch(getShortParams(), context, Gravity.START, getColoredBackground(), feedbackBean, PADDING);

        upperWrapperLayout.addView(getTitleView());
        upperWrapperLayout.addView(getDateView());

        lowerWrapperLayout.addView(subscribeToggle);
        addView(getUpperWrapperLayout());
        addView(lowerWrapperLayout);
    }

    private Switch createSwitch(LinearLayoutCompat.LayoutParams layoutParams, final Context context, int gravity, Drawable background, final LocalFeedbackBean feedbackBean, int padding) {
        Switch toggle = new Switch(context);
        toggle.setLayoutParams(layoutParams);
        toggle.setBackground(background);
        toggle.setPadding(padding, padding, padding, padding);
        toggle.setChecked(feedbackBean.getSubscribed() == 1);
        toggle.setGravity(gravity);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FeedbackService.getInstance().createSubscription(getListener(), context, RepositoryStub.getFeedback(context, feedbackBean), isChecked);
            }
        });

        return toggle;
    }

    @Override
    public void onEventCompleted(EventType eventType, Object response) {
        if (BuildConfig.DEBUG && eventType == EventType.CREATE_SUBSCRIPTION) {
            if (((LocalFeedbackState) response).isSubscribed()) {
                Toast.makeText(getContext(), "Subscribed to " + getFeedbackBean().getTitle(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Unsubscribed to " + getFeedbackBean().getTitle(), Toast.LENGTH_SHORT).show();
            }
        }
        //TODO [jfo] real implementation
    }

    @Override
    public void onEventFailed(EventType eventType, Object response) {
        //TODO [jfo] implement
    }

    @Override
    public void onConnectionFailed(EventType eventType) {
        //TODO [jfo] implement
    }
}
