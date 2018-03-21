package ch.uzh.supersede.hostapplication;

import android.Manifest;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import ch.uzh.supersede.feedbacklibrary.activities.AbstractBaseActivity;
import ch.uzh.supersede.feedbacklibrary.activities.FeedbackActivity;
import ch.uzh.supersede.feedbacklibrary.entrypoint.FeedbackConnector;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService.ConfigurationRequestWrapper;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService.ConfigurationRequestWrapper
        .ConfigurationRequestWrapperBuilder;
import ch.uzh.supersede.feedbacklibrary.utils.Utils;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.SUPERSEEDE_BASE_URL;

/**
 * This class represents an activity of the host application,
 * i.e., StartActivity integrates the feedback library.
 */
public class StartActivity extends AbstractBaseActivity {
    /*
     * This integer is used in the onRequestPermissionsResult method.
     * The value of this integer can be arbitrary. The only requirement is
     * that it should be unique regarding the switch case in the onRequestPermissionsResult method.
     */
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO [jfo]: remove me
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_start);
        Toolbar toolbar = getView(R.id.toolbar, Toolbar.class);
        setSupportActionBar(toolbar);
    }

    public void onButtonClicked(View view){
        switch (view.getId()){
            case R.id.trigger_random_pull_config_v1_button:{
                //TODO: Missing Implementation
                break;
            }
            case R.id.trigger_specific_pull_config_v1_id21_button:{
                ConfigurationRequestWrapper wrapper = new ConfigurationRequestWrapperBuilder(StartActivity.this,FeedbackActivity.class)
                        .withApplicationId(-1)
                        .withPullConfigurationId(-1)
                        .withUrl(SUPERSEEDE_BASE_URL)
                        .withLanguage("en")
                        .withIntermediateDialog("Intermediate dialog text for pull configuration with id = 22")
                        .build();
                FeedbackService.getInstance().pullConfigurationAndStartActivity(wrapper);
                break;
            }
            case R.id.trigger_specific_pull_config_v1_id22_button:{
                ConfigurationRequestWrapper wrapper = new ConfigurationRequestWrapperBuilder(StartActivity.this,FeedbackActivity.class)
                        .withApplicationId(-1)
                        .withPullConfigurationId(-1)
                        .withUrl(SUPERSEEDE_BASE_URL)
                        .withLanguage("en")
                        .withIntermediateDialog("Intermediate dialog text for pull configuration with id = 22")
                        .build();
                FeedbackService.getInstance().pullConfigurationAndStartActivity(wrapper);
                break;
            }
            default:{
                break;
            }
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

        if (id == R.id.action_single_mechanism_feedback) {
            boolean result = Utils.checkSinglePermission(this, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE, Manifest
                    .permission.READ_EXTERNAL_STORAGE, null, null, false);
            if (result) {
                /*
                 * The permission is already granted.
                 * The library takes a screenshot of the current screen automatically and opens the
                 * FeedbackActivityConstants from the feedback library.
                 */
                FeedbackService
                        .getInstance()
                        .startFeedbackActivityWithScreenshotCapture(SUPERSEEDE_BASE_URL, this, 29L, "en");
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
                /*
                 * In case the permission was not already granted, a rationale is shown to the user asking for the
                 * permission.
                 * The result of the request permission, i.e., if the user allowed or denied the permission,
                 * is handled in the onRequestPermissionsResultCase method.
                 */
            Utils.onRequestPermissionsResultCase(requestCode, grantResults, this, Manifest.permission
                    .READ_EXTERNAL_STORAGE, R.string.supersede_feedbacklibrary_permission_request_title, R.string
                    .supersede_feedbacklibrary_external_storage_permission_text_automatic_screenshot_rationale, 29L,
                    SUPERSEEDE_BASE_URL, "en");
        }
    }

    public void onConnect(View view){
        FeedbackConnector.getInstance().connect(view,this);
    }
}