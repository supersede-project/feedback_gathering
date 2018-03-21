package ch.uzh.supersede.feedbacklibrary.activities;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Marco on 05.03.2018.
 */

public abstract class AbstractBaseActivity extends AppCompatActivity {

    protected <T> T getView(int id, Class<T> classType) {
        return classType.cast(findViewById(id));
    }
}
