package ch.uzh.supersede.feedbacklibrary.activities;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public abstract class AbstractBaseActivity extends AppCompatActivity {

    protected <T> T getView(int id, Class<T> classType) {
        return classType.cast(findViewById(id));
    }

    public void onButtonClicked(View view){
        if (view != null){
            switch (view.getId()){
                default:{
                    Toast.makeText(getApplicationContext(),"Button Clicked.",Toast.LENGTH_SHORT).show();
                    //NOP
                    break;
                }
            }
        }
    }
}
