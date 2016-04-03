package com.example.matthias.hostapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import com.example.matthias.feedbacklibrary.FeedbackActivity;

public class StartActivity extends AppCompatActivity {
    private boolean isConfigSelected;
    private String requestURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        if (isConfigSelected) {
            menu.getItem(1).setEnabled(true);
        } else {
            menu.getItem(1).setEnabled(false);
        }
        return true;
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

        // Only for demo purposes, to show different configurations
        Intent intent = new Intent(this, FeedbackActivity.class);
        intent.putExtra("requestURL", requestURL);
        if (id == R.id.action_feedback) {
            // open FeedbackActivity from the feedback library
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Only for demo purposes, to show different configurations
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        isConfigSelected = false;
        requestURL = null;

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.radio_text_rating:
                if (checked) {
                    isConfigSelected = true;
                    requestURL = "text_rating.json";
                }
                break;
            case R.id.radio_text_rating_order:
                if (checked) {
                    isConfigSelected = true;
                    requestURL = "text_rating_order.json";
                }
                break;
            case R.id.radio_text_sc_rating:
                if (checked) {
                    isConfigSelected = true;
                    requestURL = "text_sc_rating.json";
                }
                break;
            case R.id.radio_rating_text:
                if (checked) {
                    isConfigSelected = true;
                    requestURL = "rating_text.json";
                }
                break;
        }
        invalidateOptionsMenu();
    }
}