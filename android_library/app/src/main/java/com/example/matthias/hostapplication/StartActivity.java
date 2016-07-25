package com.example.matthias.hostapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.matthias.feedbacklibrary.FeedbackActivity;
import com.example.matthias.feedbacklibrary.utils.DialogUtils;
import com.example.matthias.feedbacklibrary.utils.Utils;

import java.util.ArrayList;

public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView countdownTextView = (TextView) findViewById(R.id.countdown_textview);
        final String message = getResources().getString(R.string.supersede_feedbacklibrary_pull_feedback_question_string);

        Button b = (Button) findViewById(R.id.pull_start_button);
        if (b != null) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new CountDownTimer(6000, 1000) {
                        @Override
                        public void onFinish() {
                            if (countdownTextView != null) {
                                countdownTextView.setText("0");
                            }
                            ArrayList<String> messages = new ArrayList<String>();
                            messages.add(message);
                            DialogUtils.FeedbackPopupDialog d = DialogUtils.FeedbackPopupDialog.newInstance(messages);
                            d.show(getFragmentManager(), "feedbackPopupDialog");
                        }

                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (countdownTextView != null) {
                                countdownTextView.setText(String.valueOf(millisUntilFinished / 1000));
                            }
                        }
                    }.start();
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
}