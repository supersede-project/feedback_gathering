package com.example.matthias.hostapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.matthias.feedbacklibrary.FeedbackActivity;
import com.example.matthias.feedbacklibrary.utils.Utils;

import java.util.Calendar;

public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Calendar c = Calendar.getInstance();
        TextView textView = (TextView) findViewById(R.id.time_display);
        String display = c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
        textView.setText(display);
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