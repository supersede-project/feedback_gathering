package ch.uzh.supersede.host;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.LinearLayout;

import ch.uzh.supersede.feedbacklibrary.activities.AbstractBaseActivity;
import ch.uzh.supersede.feedbacklibrary.entrypoint.*;

public class HostActivity extends AbstractBaseActivity implements IFeedbackBehavior, IFeedbackDeveloper, IFeedbackStyle, IFeedbackSettings{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        LinearLayout linearLayout = getView(R.id.root_layout,LinearLayout.class);
        CoordinatorLayout cLayout = getView(R.id.coordinator_layout,CoordinatorLayout.class);
        double colorDouble = ((double)Color.BLACK)*Math.random();
        int color = (int)colorDouble;
        linearLayout.setBackgroundColor(color);
        cLayout.setBackgroundColor(color);
    }

    public void onFeedbackClicked(View view){
        FeedbackConnector.getInstance().connect(view,this);
    }


    @Override
    public boolean isDeveloper() {
        return false;
    }

    @Override
    public int getConfiguredMinUserNameLength() {
        return 3;
    }

    @Override
    public int getConfiguredMaxUserNameLength() {
        return 10;
    }

    @Override
    public int getConfiguredMinResponseLength() {
        return 10;
    }

    @Override
    public int getConfiguredMaxResponseLength() {
        return 30;
    }
}