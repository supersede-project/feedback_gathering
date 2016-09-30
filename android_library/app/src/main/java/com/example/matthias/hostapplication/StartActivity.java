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
        //Utils.triggerPotentialPullFeedback("http://ec2-54-175-37-30.compute-1.amazonaws.com/", this, 8L, "en");

        // TODO: Remove before release
        // Only for demo purposes
        Button popup = (Button) findViewById(R.id.pull_popup_button);
        if (popup != null) {
            popup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String jsonString;
                    jsonString = Utils.readFileAsString("feedback_orchestrator_adapted_single_selection.json", getAssets());
                    DialogUtils.PullFeedbackIntermediateDialog d = DialogUtils.PullFeedbackIntermediateDialog.newInstance(getResources().getString(com.example.matthias.feedbacklibrary.R.string.supersede_feedbacklibrary_pull_feedback_question_string), jsonString, 9, "http://ec2-54-175-37-30.compute-1.amazonaws.com/", "en");
                    d.show(getFragmentManager(), "feedbackPopupDialog");
                }
            });
        }
        Button noPopup = (Button) findViewById(R.id.pull_no_popup_button);
        if (noPopup != null) {
            noPopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String jsonString;
                    jsonString = Utils.readFileAsString("feedback_orchestrator_adapted_multiple_selection.json", getAssets());
                    startFeedbackActivity(jsonString, false, 10, "http://ec2-54-175-37-30.compute-1.amazonaws.com/", "en");
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
            boolean result = Utils.checkSinglePermission(this, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, null, null, false);
            if (result) {
                // Permission is already granted. Taking a screenshot of the current screen automatically and open the FeedbackActivity from the feedback library
                Utils.startActivityWithScreenshotCapture("http://ec2-54-175-37-30.compute-1.amazonaws.com/", this, 8L, "en");
            }
            return true;
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
                        8L, "http://ec2-54-175-37-30.compute-1.amazonaws.com/", "en");
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