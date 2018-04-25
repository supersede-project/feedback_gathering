package ch.uzh.supersede.hostapplication;

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
        LinearLayout lLayout = getView(R.id.root_layout,LinearLayout.class);
        CoordinatorLayout cLayout = getView(R.id.coordinator_layout,CoordinatorLayout.class);
        double colorDouble = ((double)Color.BLACK)*Math.random();
        int color = (int)colorDouble;
        lLayout.setBackgroundColor(color);
        cLayout.setBackgroundColor(color);
    }

    public void onFeedbackClicked(View view){
        FeedbackConnector.getInstance().connect(view,this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}