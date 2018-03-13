package ch.uzh.supersede.feedbacklibrary;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO [jfo]: remove me
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_help);
    }
}
