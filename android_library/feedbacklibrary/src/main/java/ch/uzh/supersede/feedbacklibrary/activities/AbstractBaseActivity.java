package ch.uzh.supersede.feedbacklibrary.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.*;
import android.view.View;
import android.widget.*;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.beans.LocalConfigurationBean;
import ch.uzh.supersede.feedbacklibrary.utils.*;
import ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.EXTRA_KEY_APPLICATION_CONFIGURATION;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.LOCKED;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public abstract class AbstractBaseActivity extends AppCompatActivity {
    protected USER_LEVEL userLevel = LOCKED;
    protected final String[] preAllocatedStringStorage = new String[]{null};
    protected int screenWidth;
    protected int screenHeight;
    protected LocalConfigurationBean configuration;

    protected <T> T getView(int id, Class<T> classType) {
        return classType.cast(findViewById(id));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLevel = PermissionUtility.getUserLevel(getApplicationContext());
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        configuration = (LocalConfigurationBean) getIntent().getSerializableExtra(EXTRA_KEY_APPLICATION_CONFIGURATION);
    }

    protected void onPostCreate() {
        invokeNullSafe(getSupportActionBar(), "hide", null);
    }

    public void onButtonClicked(View view) {
        Toast.makeText(getApplicationContext(), "Button Clicked.", Toast.LENGTH_SHORT).show();
    }

    protected <T extends Activity> void startActivity(T startActivity, Class<?> activityToStart) {
        Intent intent = new Intent(startActivity.getApplicationContext(), activityToStart);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        handOverConfigurationToIntent(intent);
        startActivity.startActivity(intent);
        startActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    protected Object invokeNullSafe(Object o, String methodName, Object valueIfNull, Object... params) {
        if (o == null) {
            return valueIfNull;
        }
        Class[] paramClasses = new Class[params != null ? params.length : 0];
        for (int p = 0; p < (params != null ? params.length : 0); p++) {
            paramClasses[p] = params[p].getClass();
        }
        Object returnObject = null;
        try {
            Method method = (params == null ? o.getClass().getMethod(methodName) : o.getClass().getMethod(methodName, paramClasses));
            if (method == null) {
                return valueIfNull;
            }
            returnObject = method.invoke(o, params);
        } catch (SecurityException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            Log.e("invokeNullSafe", e.getMessage());
        }
        return ObjectUtility.nvl(returnObject, valueIfNull);
    }

    protected void handOverConfigurationToIntent(Intent intent) {
        Serializable configuration = getIntent().getSerializableExtra(EXTRA_KEY_APPLICATION_CONFIGURATION);
        intent.putExtra(EXTRA_KEY_APPLICATION_CONFIGURATION, configuration);
    }

    public LocalConfigurationBean getConfiguration() {
        return configuration;
    }

    protected int getTopColor(int colorIndex) {
        if (configuration.getTopColors().length >= colorIndex) {
            return ObjectUtility.nvl(configuration.getTopColors()[colorIndex], 0);
        }
        return 0;
    }

    protected void colorViews(int colorIndex, View... views) {
        if (configuration.getTopColors().length >= colorIndex) {
            Integer color = configuration.getTopColors()[colorIndex];
            for (View v : color != null ? views : new View[0]) {
                if (v instanceof TextView && ColorUtility.isDark(color)) {
                    ((TextView) v).setTextColor(ContextCompat.getColor(this, R.color.white));
                } else if (v instanceof TextView) {
                    ((TextView) v).setTextColor(ContextCompat.getColor(this, R.color.black));
                }
                v.setBackgroundColor(color);
            }
        }
    }

    protected void colorShape(int colorIndex, View... views) {
        if (configuration.getTopColors().length >= colorIndex) {
            Integer color = configuration.getTopColors()[colorIndex];
            for (View view : color != null ? views : new View[0]) {
                Drawable background = view.getBackground();
                if (background instanceof ShapeDrawable) {
                    ((ShapeDrawable) background).getPaint().setColor(color);
                } else if (background instanceof GradientDrawable) {
                    ((GradientDrawable) background).setColor(color);
                } else if (background instanceof ColorDrawable) {
                    ((ColorDrawable) background).setColor(color);
                } else if (background instanceof StateListDrawable) {
                    background.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                }
                if (view instanceof TextView && ColorUtility.isDark(color)) {
                    ((TextView) view).setTextColor(ContextCompat.getColor(this, R.color.white));
                } else if (view instanceof TextView) {
                    ((TextView) view).setTextColor(ContextCompat.getColor(this, R.color.black));
                }
            }
        }
    }
}
