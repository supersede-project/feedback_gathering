package ch.uzh.supersede.feedbacklibrary.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.*;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.beans.LocalConfigurationBean;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService;
import ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener;
import ch.uzh.supersede.feedbacklibrary.utils.*;
import ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.ActivitiesConstants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.LOCKED;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public abstract class AbstractBaseActivity extends AppCompatActivity {
    protected USER_LEVEL userLevel = LOCKED;
    protected final String[] preAllocatedStringStorage = new String[]{null};
    protected int screenWidth;
    protected int screenHeight;
    protected LocalConfigurationBean configuration;
    protected InfoUtility infoUtility;
    protected HashMap<View, Integer> viewToColorMap = new HashMap<>();

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
        configuration = ConfigurationUtility.getConfigurationFromActivity(this);
        infoUtility = new InfoUtility(screenWidth, screenHeight);

        if (configuration == null) {
            configuration = ConfigurationUtility.getConfigurationFromDatabase(this);
        }
        getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE).edit().putLong(SHARED_PREFERENCES_HOST_APPLICATION_ID, configuration.getHostApplicationLongId()).apply();
        getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE).edit().putString(SHARED_PREFERENCES_HOST_APPLICATION_LANGUAGE, configuration.getHostApplicationLanguage()).apply();
        getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE).edit().putString(SHARED_PREFERENCES_ENDPOINT_URL, configuration.getEndpointUrl()).apply();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putLong(SHARED_PREFERENCES_HOST_APPLICATION_ID, configuration.getHostApplicationLongId()).apply();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(SHARED_PREFERENCES_HOST_APPLICATION_LANGUAGE, configuration.getHostApplicationLanguage()).apply();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(SHARED_PREFERENCES_ENDPOINT_URL, configuration.getEndpointUrl()).apply();
    }

    protected void onPostCreate() {
        invokeNullSafe(getSupportActionBar(), "hide", null);
        checkConnectivity();
    }

    private void checkConnectivity() {
        NetworkInfo activeNetworkInfo = null;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Exception e) {
            Log.e("Network", e.getMessage());
        }
        if (!(activeNetworkInfo != null && activeNetworkInfo.isConnected())) {
            //Offline, don't ping Repository
            getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE).edit().putBoolean(SHARED_PREFERENCES_ONLINE, false).apply();
        } else {
            getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE).edit().putBoolean(SHARED_PREFERENCES_ONLINE, true).apply();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkConnectivity();
    }

    public void onButtonClicked(View view) {
        if (view instanceof Button) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.abstract_button_clicked, ((Button) view).getText().toString().replace("\n", " ")), Toast.LENGTH_SHORT).show();
        }
    }

    protected <T extends Activity> void startActivity(T startActivity, Class<?> activityToStart, boolean destruction, Intent... handoverIntent) {
        Intent intent = null;
        if (handoverIntent == null || handoverIntent.length == 0) {
            intent = new Intent(startActivity.getApplicationContext(), activityToStart);
        } else {
            intent = handoverIntent[0];
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        if (destruction) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        handOverConfigurationToIntent(intent);
        startActivity.startActivity(intent);
        startActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        if (destruction) {
            startActivity.finish();
        }
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
            for (View view : color != null ? views : new View[0]) {
                if (view == null) {
                    continue;
                }
                if (!viewToColorMap.containsKey(view)) {
                    viewToColorMap.put(view, colorIndex);
                }
                colorizeText(color, view);
                view.setBackgroundColor(color);
            }
        }
    }

    protected void colorLayouts(int colorIndex, ViewGroup... layouts) {
        if (configuration.getTopColors().length >= colorIndex) {
            Integer color = configuration.getTopColors()[colorIndex];
            for (ViewGroup layout : color != null ? layouts : new ViewGroup[0]) {
                if (layout == null) {
                    continue;
                }
                layout.setBackgroundColor(color);
            }
        }
    }

    protected void colorShape(int colorIndex, View... views) {
        colorShape(colorIndex, false, views);
    }

    protected void colorShape(int colorIndex, boolean isShapeDisabled, View... views) {
        if (configuration.getTopColors().length >= colorIndex) {
            Integer color = configuration.getTopColors()[colorIndex];
            for (View view : color != null ? views : new View[0]) {
                if (view == null) {
                    continue;
                }
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
                colorizeText(color, view, isShapeDisabled);
                if (view instanceof RelativeLayout) {
                    for (int v = 0; v < ((RelativeLayout) view).getChildCount(); v++) {
                        colorizeText(color, ((RelativeLayout) view).getChildAt(v), isShapeDisabled);
                    }
                }
            }
        }
    }

    public void colorTextOnly(int backgroundColorIndex, View... views) {
        if (configuration.getTopColors().length >= backgroundColorIndex) {
            Integer color = configuration.getTopColors()[backgroundColorIndex];
            for (View view : color != null ? views : new View[0]) {
                colorizeText(color, view);
            }
        }
    }

    private void colorizeText(Integer color, View view, boolean isTextDisabled) {
        if (view instanceof TextView && ColorUtility.isDark(color)) {
            if (isTextDisabled) {
                ((TextView) view).setTextColor(ContextCompat.getColor(this, R.color.anthrazit));
            } else {
                ((TextView) view).setTextColor(ContextCompat.getColor(this, R.color.white));
            }
        } else if (view instanceof TextView) {
            if (isTextDisabled) {
                ((TextView) view).setTextColor(ContextCompat.getColor(this, R.color.anthrazitDark));
            } else {
                ((TextView) view).setTextColor(ContextCompat.getColor(this, R.color.black));
            }
        }
    }

    private void colorizeText(Integer color, View view) {
        colorizeText(color, view, false);
    }

    protected final int getColorCount() {
        return configuration.getTopColors().length;
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

    protected void invokeVersionControl(int lockBelowVersion, int... viewIds) {
        if (VersionUtility.getDateVersion() < lockBelowVersion) {
            for (int id : viewIds) {
                View view = findViewById(id);
                disableViews(view);
            }
        }
    }

    protected void disableViews(View... views) {
        for (View view : (views != null && views.length > 0) ? views : new View[0]) {
            if (view != null) {
                view.setEnabled(false);
                view.setClickable(false);
                view.setBackgroundColor(DISABLED_BACKGROUND);
                if (view instanceof TextView) {
                    ((TextView) view).setTextColor(DISABLED_FOREGROUND);
                }
            }
        }
    }

    protected void enableView(View view, Integer colorIndex, Boolean... conditionals) {
        colorIndex = (colorIndex == null ? 0 : colorIndex);
        if (conditionals != null && conditionals.length > 0) {
            for (Boolean b : conditionals) {
                if (!b) {
                    return;
                }
            }
        }
        if (view != null && getColorCount() >= colorIndex) {
            view.setEnabled(true);
            view.setClickable(true);
            view.setBackgroundColor(getTopColor(colorIndex));
            colorizeText(getTopColor(colorIndex), view);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        createInfoBubbles();
    }

    protected void createInfoBubbles() {
        //NOP
    }
}
