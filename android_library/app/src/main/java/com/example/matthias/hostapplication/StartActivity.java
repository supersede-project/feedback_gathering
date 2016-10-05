package com.example.matthias.hostapplication;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.matthias.feedbacklibrary.FeedbackActivity;
import com.example.matthias.feedbacklibrary.utils.DialogUtils;
import com.example.matthias.feedbacklibrary.utils.Utils;

public class StartActivity extends AppCompatActivity {
    // Storage permission (android.permission-group.STORAGE)
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;
    // baseURL = "http://ec2-54-175-37-30.compute-1.amazonaws.com/"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO: Uncomment before release
        // Actual trigger
        //Utils.triggerPotentialPullFeedback("http://ec2-54-175-37-30.compute-1.amazonaws.com/", this, 6L, "en");

        // TODO: Remove before release
        // Only for demo purposes
        Button triggerPullV1 = (Button) findViewById(R.id.trigger_pull_v1_button);
        if (triggerPullV1 != null) {
            triggerPullV1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.triggerPotentialPullFeedback("http://ec2-54-175-37-30.compute-1.amazonaws.com/", StartActivity.this, 6L, "en");
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

        if (id == R.id.action_feedback) {
            /*
            boolean result = Utils.checkSinglePermission(this, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, null, null, false);
            if (result) {
                // Permission is already granted. Taking a screenshot of the current screen automatically and open the FeedbackActivity from the feedback library
                Utils.startActivityWithScreenshotCapture("http://ec2-54-175-37-30.compute-1.amazonaws.com/", this, 6L, "en");
                // 5L --> multiple mechanisms of the same type
                // 6L --> only one mechanisms of the same type
            }
            */
            return true;
        }

        if (id == R.id.action_single_mechanism_feedback) {
            boolean result = Utils.checkSinglePermission(this, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, null, null, false);
            if (result) {
                // Permission is already granted. Taking a screenshot of the current screen automatically and open the FeedbackActivity from the feedback library
                Utils.startActivityWithScreenshotCapture("http://ec2-54-175-37-30.compute-1.amazonaws.com/", this, 6L, "en");
                // 5L --> multiple mechanisms of the same type
                // 6L --> only one mechanisms of the same type
            }
        }

        if (id == R.id.action_multiple_mechanism_feedback) {
            boolean result = Utils.checkSinglePermission(this, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, null, null, false);
            if (result) {
                // Permission is already granted. Taking a screenshot of the current screen automatically and open the FeedbackActivity from the feedback library
                Utils.startActivityWithScreenshotCapture("http://ec2-54-175-37-30.compute-1.amazonaws.com/", this, 5L, "en");
                // 5L --> multiple mechanisms of the same type
                // 6L --> only one mechanisms of the same type
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                Utils.onRequestPermissionsResultCase(requestCode, permissions, grantResults, this, Manifest.permission.READ_EXTERNAL_STORAGE,
                        com.example.matthias.feedbacklibrary.R.string.supersede_feedbacklibrary_permission_request_title,
                        com.example.matthias.feedbacklibrary.R.string.supersede_feedbacklibrary_external_storage_permission_text_automatic_screenshot_rationale,
                        6L, "http://ec2-54-175-37-30.compute-1.amazonaws.com/", "en");
                break;
        }
    }

    // TODO: Remove before release
    // Only for demo purposes
    private void startFeedbackActivity(String jsonString, boolean isPush, long selectedPullConfigurationIndex, String baseURL, String language) {
        Intent intent = new Intent(this, FeedbackActivity.class);
        intent.putExtra(FeedbackActivity.JSON_CONFIGURATION_STRING, jsonString);
        intent.putExtra(FeedbackActivity.IS_PUSH_STRING, isPush);
        intent.putExtra(FeedbackActivity.SELECTED_PULL_CONFIGURATION_INDEX_STRING, selectedPullConfigurationIndex);
        intent.putExtra(FeedbackActivity.EXTRA_KEY_BASE_URL, baseURL);
        intent.putExtra(FeedbackActivity.EXTRA_KEY_LANGUAGE, language);
        startActivity(intent);
    }
}