package com.example.matthias.hostapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Actual trigger
        //Utils.triggerPotentialPullFeedback(this);

        // Only for demo purposes
        Button noPopup = (Button) findViewById(R.id.pull_no_popup_button);
        if (noPopup != null) {
            noPopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String jsonString;
                    jsonString = Utils.readFileAsString("configuration_material_design_pull_no_popup.json", getAssets());
                    startFeedbackActivity(jsonString, false, 0);
                }
            });
        }
        Button popup = (Button) findViewById(R.id.pull_popup_button);
        if (popup != null) {
            popup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String jsonString;
                    jsonString = Utils.readFileAsString("configuration_material_design_pull_popup.json", getAssets());
                    DialogUtils.FeedbackPopupDialog d = DialogUtils.FeedbackPopupDialog.newInstance(getResources().getString(com.example.matthias.feedbacklibrary.R.string.supersede_feedbacklibrary_pull_feedback_question_string), jsonString, 0);
                    d.show(getFragmentManager(), "feedbackPopupDialog");
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

        Intent intent = new Intent(this, FeedbackActivity.class);
        if (id == R.id.action_feedback) {
            boolean result = Utils.checkPermission_READ_EXTERNAL_STORAGE(StartActivity.this, Utils.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            if (result) {
                // Permission is granted --> Open the FeedbackActivity from the feedback library and taking a screenshot automatically before opening it
                String defaultImagePath = Utils.captureScreenshot(this);
                intent.putExtra(FeedbackActivity.DEFAULT_IMAGE_PATH, defaultImagePath);
                startActivity(intent);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utils.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                Intent intent = new Intent(this, FeedbackActivity.class);
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted --> Open the FeedbackActivity from the feedback library and taking a screenshot automatically before opening it
                    String defaultImagePath = Utils.captureScreenshot(this);
                    intent.putExtra(FeedbackActivity.DEFAULT_IMAGE_PATH, defaultImagePath);
                    startActivity(intent);
                } else {
                    // Permission is denied --> Open the FeedbackActivity from the feedback library without taking a screenshot automatically before opening it
                    startActivity(intent);
                }
                break;
        }
    }

    private void startFeedbackActivity(String jsonString, boolean isPush, int selectedPullConfigurationIndex) {
        Intent intent = new Intent(this, FeedbackActivity.class);
        intent.putExtra(FeedbackActivity.JSON_CONFIGURATION_STRING, jsonString);
        intent.putExtra(FeedbackActivity.IS_PUSH_STRING, isPush);
        intent.putExtra(FeedbackActivity.SELECTED_PULL_CONFIGURATION_INDEX_STRING, selectedPullConfigurationIndex);
        startActivity(intent);
    }
}