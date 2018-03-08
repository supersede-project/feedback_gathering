package ch.uzh.supersede.hostapplication;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Marco on 05.03.2018.
 */

public class AbstractBaseActivity extends AppCompatActivity {

    protected <T> T getView(int id, Class<T> classType) {
        return classType.cast(findViewById(id));
    }
}
