package com.example.matthias.hostapplication;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.matthias.feedbacklibrary.utils.Utils;

/**
 * This class represents an activity of the host application,
 * i.e., StartActivity integrates the feedback library.
 */
public class StartActivity extends AppCompatActivity {
    /*
     * This integer is used in the onRequestPermissionsResult method.
     * The value of this integer can be arbitrary. The only requirement is
     * that it should be unique regarding the switch case in the onRequestPermissionsResult method.
     */
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button triggerRandomPullV1 = (Button) findViewById(R.id.trigger_random_pull_config_v1_button);
        if (triggerRandomPullV1 != null) {
            triggerRandomPullV1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                     * This method randomly triggers a potential PULL feedback of a specific application.
                     * If any and which PULL feedback is triggered depends on the likelihood parameter of the each PULL configuration
                     * (the likelihood is a general configuration parameter of a PULL configuration).
                     * In this example, a PULL feedback from the application with id = 5 might be triggered.
                     */
                    Utils.triggerRandomPullFeedback("http://ec2-54-175-37-30.compute-1.amazonaws.com/", StartActivity.this, 5L, "en");
                }
            });
        }
        Button triggerSpecificPullV1Id14 = (Button) findViewById(R.id.trigger_specific_pull_config_v1_id11_button);
        if (triggerSpecificPullV1Id14 != null) {
            triggerSpecificPullV1Id14.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                     * This method triggers a PULL feedback of a specific application.
                     * Which PULL feedback is triggered depends on the given PULL configuration id.
                     * The intermediate dialog text represents the dialog text which is shown to the user if a PULL feedback is triggered.
                     * If an intermediate dialog is shown depends on the showIntermediateDialog parameter of the PULL configuration.
                     * (the showIntermediateDialog is a general configuration parameter of a PULL configuration).
                     * In this example, a PULL feedback with id = 11 from the application with id = 5 will be triggered.
                     */
                    Utils.triggerSpecificPullFeedback("http://ec2-54-175-37-30.compute-1.amazonaws.com/", StartActivity.this, 5L, "en", 11L, "Intermediate dialog text for pull configuration with id = 11");
                }
            });
        }

        Button triggerSpecificPullV1Id15 = (Button) findViewById(R.id.trigger_specific_pull_config_v1_id12_button);
        if (triggerSpecificPullV1Id15 != null) {
            triggerSpecificPullV1Id15.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                     * In this example, a PULL feedback with id = 12 from the application with id = 5 will be triggered.
                     */
                    Utils.triggerSpecificPullFeedback("http://ec2-54-175-37-30.compute-1.amazonaws.com/", StartActivity.this, 5L, "en", 12L, "Intermediate dialog text for pull configuration with id = 12");
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_single_mechanism_feedback) {
            boolean result = Utils.checkSinglePermission(this, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, null, null, false);
            if (result) {
                /*
                 * The permission is already granted.
                 * The library takes a screenshot of the current screen automatically and opens the FeedbackActivity from the feedback library.
                 */
                Utils.startActivityWithScreenshotCapture("http://ec2-54-175-37-30.compute-1.amazonaws.com/", this, 5L, "en");
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                /*
                 * In case the permission was not already granted, a rationale is shown to the user asking for the permission.
                 * The result of the request permission, i.e., if the user allowed or denied the permission,
                 * is handled in the onRequestPermissionsResultCase method.
                 */
                Utils.onRequestPermissionsResultCase(requestCode, permissions, grantResults, this, Manifest.permission.READ_EXTERNAL_STORAGE,
                        com.example.matthias.feedbacklibrary.R.string.supersede_feedbacklibrary_permission_request_title,
                        com.example.matthias.feedbacklibrary.R.string.supersede_feedbacklibrary_external_storage_permission_text_automatic_screenshot_rationale,
                        5L, "http://ec2-54-175-37-30.compute-1.amazonaws.com/", "en");
                break;
        }
    }
}