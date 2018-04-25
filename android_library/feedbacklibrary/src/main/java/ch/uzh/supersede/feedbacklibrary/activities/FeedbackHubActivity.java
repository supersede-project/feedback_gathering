package ch.uzh.supersede.feedbacklibrary.activities;


import android.content.*;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.beans.LocalFeedbackBean;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.stubs.RepositoryStub;
import ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility;
import ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL;
import ch.uzh.supersede.feedbacklibrary.utils.PopUp;
import ch.uzh.supersede.feedbacklibrary.utils.Utils;

import static ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase.FETCH_MODE.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.ActivitiesConstants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.*;

@SuppressWarnings({"squid:MaximumInheritanceDepth","squid:S1170"})
public class FeedbackHubActivity extends AbstractBaseActivity {
    private Button listButton;
    private Button levelButton;
    private Button feedbackButton;
    private Button settingsButton;
    private TextView statusText;
    private String userName;
    private int tapCounter = 0;
    private byte[] cachedScreenshot = null;
    private String hostApplicationName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_hub);
        //User Level Setup
        listButton = getView(R.id.hub_button_list, Button.class);
        levelButton = getView(R.id.hub_button_user_level, Button.class);
        feedbackButton = getView(R.id.hub_button_feedback, Button.class);
        settingsButton = getView(R.id.hub_button_settings, Button.class);
        statusText = getView(R.id.hub_text_status, TextView.class);
        //Cache
        cachedScreenshot = getIntent().getByteArrayExtra(EXTRA_KEY_CACHED_SCREENSHOT);
        restoreHostApplicationNameToPreferences();
        onPostCreate();
        if (ACTIVE.check(this)) {
            userName = FeedbackDatabase.getInstance(this).readString(USER_NAME, null);
        }
        updateUserLevel(false);
    }

    private void restoreHostApplicationNameToPreferences() {
        String extraHostApplicationName = getIntent().getStringExtra(EXTRA_KEY_HOST_APPLICATION_NAME);
        String preferencesHostApplicationName = getSharedPreferences(SHARED_PREFERENCES_ID, MODE_PRIVATE).getString(EXTRA_KEY_HOST_APPLICATION_NAME,null);
        if (preferencesHostApplicationName == null && extraHostApplicationName != null){
            getSharedPreferences(SHARED_PREFERENCES_ID, MODE_PRIVATE).edit().putString(EXTRA_KEY_HOST_APPLICATION_NAME, extraHostApplicationName).apply();
        }else if (preferencesHostApplicationName == null && hostApplicationName != null){
            getSharedPreferences(SHARED_PREFERENCES_ID, MODE_PRIVATE).edit().putString(EXTRA_KEY_HOST_APPLICATION_NAME, hostApplicationName).apply();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUserLevel(false);
        restoreHostApplicationNameToPreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUserLevel(false);
        restoreHostApplicationNameToPreferences();
    }

    private void updateUserLevel(boolean ignoreDatabaseCheck) {
        userLevel = PermissionUtility.getUserLevel(getApplicationContext(), ignoreDatabaseCheck);
        levelButton.setText(getResources().getString(R.string.hub_feedback_user_level, userLevel.getLevel()));
        levelButton.setEnabled(true);
        settingsButton.setEnabled(false);
        feedbackButton.setEnabled(false);
        listButton.setEnabled(false);
        statusText.setVisibility(View.GONE);
        if (PASSIVE.check(getApplicationContext(),ignoreDatabaseCheck)){
            listButton.setEnabled(true);
        }
        if (ACTIVE.check(getApplicationContext(),ignoreDatabaseCheck)){
            statusText.setVisibility(View.VISIBLE);
            Utils.persistScreenshot(this,cachedScreenshot);
            int ownFeedbackBeans = FeedbackDatabase.getInstance(this).getFeedbackBeans(OWN).size();
            int upVotedFeedbackBeans = FeedbackDatabase.getInstance(this).getFeedbackBeans(UP_VOTED).size();
            int downVotedFeedbackBeans = FeedbackDatabase.getInstance(this).getFeedbackBeans(DOWN_VOTED).size();
            int respondedFeedbackBeans = FeedbackDatabase.getInstance(this).getFeedbackBeans(RESPONDED).size();
            int allFeedbackBeans = FeedbackDatabase.getInstance(this).getFeedbackBeans(ALL).size();
            statusText.setText(Html.fromHtml(getString(R.string.hub_status,userName,
                    allFeedbackBeans,
                    ownFeedbackBeans,
                    respondedFeedbackBeans,
                    upVotedFeedbackBeans,
                    downVotedFeedbackBeans).replace(COLOR_STRING,DARK_BLUE)));
            feedbackButton.setEnabled(true);
            settingsButton.setEnabled(true);
        }
        if (ADVANCED.check(getApplicationContext(),ignoreDatabaseCheck)){
            levelButton.setEnabled(false);
        }
    }

    @Override
    public void onButtonClicked(View view) {
        if (view != null) {
            int i = view.getId();
            if (i == R.id.hub_button_list) {
                startActivity(this, FeedbackListActivity.class);
            } else if (i == R.id.hub_button_feedback) {
                startActivity(this, FeedbackActivity.class);
            } else if (i == R.id.hub_button_settings) {
                startActivity(this, FeedbackSettingsActivity.class);
            } else if (i == R.id.hub_button_user_level) {
                switch (userLevel.getLevel()){
                    case 0:
                        new PopUp(this)
                            .withTitle(getString(R.string.hub_access_1))
                            .withMessage(getString(R.string.hub_access_1_description))
                            .withCustomOk(getString(R.string.hub_confirm), getClickListener(PASSIVE)).buildAndShow();
                        break;
                    case 1:
                        final EditText nameInputText = new EditText(this);
                        nameInputText.setMaxLines(1);
                        new PopUp(this)
                                .withTitle(getString(R.string.hub_access_2))
                                .withMessage(getString(R.string.hub_access_2_description))
                                .withInput(nameInputText)
                                .withCustomOk(getString(R.string.hub_confirm), getClickListener(ACTIVE,nameInputText)).buildAndShow();
                        break;
                    case 2:
                        new PopUp(this)
                                .withTitle(getString(R.string.hub_access_3))
                                .withMessage(getString(R.string.hub_access_3_description))
                                .withCustomOk(getString(R.string.hub_confirm), getClickListener(ADVANCED)).buildAndShow();
                        break;
                    default:
                        break;
                }
            } else {
                //DEVELOPER MENU, TO BE REMOVED OR HIDDEN
                tapCounter++;
                if (tapCounter >= 5 && ACTIVE.check(this)){
                    tapCounter = 0;
                    FeedbackDatabase.getInstance(this).writeString(USER_NAME,null);
                    List<LocalFeedbackBean> ownFeedbackBeans = FeedbackDatabase.getInstance(this).getFeedbackBeans(OWN);
                    List<LocalFeedbackBean> subscribedFeedbackBeans = FeedbackDatabase.getInstance(this).getFeedbackBeans(SUBSCRIBED);
                    List<LocalFeedbackBean> votedFeedbackBeans = FeedbackDatabase.getInstance(this).getFeedbackBeans(VOTED);
                    List<LocalFeedbackBean> respondedFeedbackBeans = FeedbackDatabase.getInstance(this).getFeedbackBeans(RESPONDED);
                    List<LocalFeedbackBean> allFeedbackBeans = FeedbackDatabase.getInstance(this).getFeedbackBeans(ALL);
                    FeedbackDatabase.getInstance(this).wipeAllStoredFeedback();
                    Toast.makeText(this, "DEVELOPER: Username resetted!\n"+
                            allFeedbackBeans.size()+" Local Feedback-Entries deleted, containing\n"+
                                    ownFeedbackBeans.size()+" own Feedback-Entries\n"+
                                    subscribedFeedbackBeans.size()+" subscribed Feedback-Entries\n"+
                                    votedFeedbackBeans.size()+" voted Feedback-Entries\n"+
                                    respondedFeedbackBeans.size()+" responded Feedback-Entries.",
                            Toast.LENGTH_SHORT).show();
                    userName = null;
                    statusText.setText(Html.fromHtml(getString(R.string.hub_status,userName,0,0,0,0,0).replace(COLOR_STRING,DARK_BLUE)));
                    updateUserLevel(false);
                }
            }
        }
    }

    @NonNull
    private OnClickListener getClickListener(USER_LEVEL userLevel, final EditText... inputEditText) {
        if (userLevel == PASSIVE) {
            return new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE).edit().putBoolean(FEEDBACK_CONTRIBUTOR,true).apply();
                    updateUserLevel(true);
                }
            };
        }else if (userLevel == ACTIVE) {
            return new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String inputString = inputEditText[0].getText().toString();
                    if (inputString.length() < 4){ //TODO: Length as settings
                        Toast.makeText(getApplicationContext(), R.string.hub_warning_username_too_short,Toast.LENGTH_SHORT).show();
                        return;
                    }
                    preAllocatedStringStorage[0] = inputString;
                    dialog.cancel();
                    if (ACTIVE.getMissing(getApplicationContext()).length==0){
                        handleAllPermissionsGranted(true); //Can only happen when Developer reset their UserName
                    }else{
                        ActivityCompat.requestPermissions(FeedbackHubActivity.this, ACTIVE.getMissing(getApplicationContext()), PERMISSION_REQUEST_ALL);
                    }
                }
            };
        }else if (userLevel == ADVANCED) {
            return new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    ActivityCompat.requestPermissions(FeedbackHubActivity.this, ADVANCED.getMissing(getApplicationContext()), PERMISSION_REQUEST_ALL);
                }
            };
        }
        return new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        };
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_ALL) {
            if (grantResults.length > 0 && grantResults.length == permissions.length) {
                //check if all are granted
                boolean allGranted = true;
                for (int p = 0; p < permissions.length; p++) {
                    if (grantResults[p] != PackageManager.PERMISSION_GRANTED) {
                        allGranted = false;
                        break;
                    }
                }
                if (allGranted) {
                    handleAllPermissionsGranted(false);
                    return;
                }
            }
            Toast.makeText(this, R.string.hub_warning_permission_not_granted, Toast.LENGTH_SHORT).show();
            //Not (fully) granted
        }
    }

    private void handleAllPermissionsGranted(boolean reload) {
        updateUserLevel(true);
        if (ACTIVE.check(this,true) && preAllocatedStringStorage[0] != null){
            String name = FeedbackDatabase.getInstance(this).readString(USER_NAME,null);
            String technicalName = FeedbackDatabase.getInstance(this).readString(TECHNICAL_USER_NAME,null);
            if (name == null){
                Toast.makeText(this,getString(R.string.hub_username_registered,preAllocatedStringStorage[0]),Toast.LENGTH_SHORT).show();
                preAllocatedStringStorage[0] = RepositoryStub.getUniqueName(preAllocatedStringStorage[0]);
                FeedbackDatabase.getInstance(this).writeString(USER_NAME,preAllocatedStringStorage[0]);
                userName = preAllocatedStringStorage[0];
            }else{
                userName = name;
                Toast.makeText(this,getString(R.string.hub_username_restored,name),Toast.LENGTH_SHORT).show();
            }
            if (technicalName == null){
                String id = UUID.randomUUID().toString();
                FeedbackDatabase.getInstance(this).writeString(TECHNICAL_USER_NAME,id);
            }
            preAllocatedStringStorage[0] = null;
        }
        if (reload){
            updateUserLevel(true);
        }
    }
}
