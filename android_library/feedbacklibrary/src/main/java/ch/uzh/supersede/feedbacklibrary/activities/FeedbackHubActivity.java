package ch.uzh.supersede.feedbacklibrary.activities;


import android.content.DialogInterface;
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

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility;
import ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL;
import ch.uzh.supersede.feedbacklibrary.utils.PopUp;
import ch.uzh.supersede.feedbacklibrary.utils.Utils;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.FEEDBACK_CONTRIBUTOR;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.FeedbackActivityConstants.EXTRA_KEY_CACHED_SCREENSHOT;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.FeedbackColorConstants.COLOR_STRING;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.FeedbackColorConstants.DARK_BLUE;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.PERMISSION_REQUEST_ALL;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.SHARED_PREFERENCES;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.USER_NAME;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ACTIVE;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ADVANCED;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.LOCKED;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.PASSIVE;

@SuppressWarnings({"squid:MaximumInheritanceDepth","squid:S1170"})
public class FeedbackHubActivity extends AbstractBaseActivity {
    private Button listButton;
    private Button levelButton;
    private Button feedbackButton;
    private Button settingsButton;
    private TextView statusText;
    private String userName;
    private final String[] preAllocatedStringStorage = new String[]{null};
    private int tapCounter = 0;
    private byte[] cachedScreenshot = null;

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
        onPostCreate();
        if (ACTIVE.check(this)) {
            userName = FeedbackDatabase.getInstance(this).readString(USER_NAME, null);
        }
        updateUserLevel();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUserLevel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUserLevel();
    }

    private void updateUserLevel() {
        userLevel = PermissionUtility.getUserLevel(getApplicationContext());
        levelButton.setText(getResources().getString(R.string.hub_feedback_user_level, userLevel.getLevel()));
        levelButton.setEnabled(true);
        settingsButton.setEnabled(false);
        feedbackButton.setEnabled(false);
        listButton.setEnabled(false);
        statusText.setEnabled(false);
        if (userLevel.getLevel()>LOCKED.getLevel()){
            listButton.setEnabled(true);
        }
        if (userLevel.getLevel()>PASSIVE.getLevel()){
            feedbackButton.setEnabled(true);
            Utils.persistScreenshot(this,cachedScreenshot);
            statusText.setText(Html.fromHtml(getString(R.string.hub_status,userName,0,0,0,0,0).replace(COLOR_STRING,DARK_BLUE)));
        }
        if (userLevel.getLevel()>ACTIVE.getLevel()){
            settingsButton.setEnabled(true);
        }
        if (userLevel.getLevel()>=ADVANCED.getLevel()){
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
                if (tapCounter >= 5){
                    tapCounter = 0;
                    FeedbackDatabase.getInstance(this).writeString(USER_NAME,null);
                    Toast.makeText(this, "DEVELOPER: Username resetted!", Toast.LENGTH_SHORT).show();
                    userName = null;
                    statusText.setText(Html.fromHtml(getString(R.string.hub_status,userName,0,0,0,0,0).replace(COLOR_STRING,DARK_BLUE)));
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
                    updateUserLevel();
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
                    ActivityCompat.requestPermissions(FeedbackHubActivity.this, ACTIVE.getMissing(getApplicationContext()), PERMISSION_REQUEST_ALL);
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
                    handleAllPermissionsGranted();
                    return;
                }
            }
            Toast.makeText(this, R.string.hub_warning_permission_not_granted, Toast.LENGTH_SHORT).show();
            //Not (fully) granted
        }
    }

    private void handleAllPermissionsGranted() {
        updateUserLevel();
        if (ACTIVE.check(this) && preAllocatedStringStorage[0] != null){
            String name = FeedbackDatabase.getInstance(this).readString(USER_NAME,null);
            if (name == null){
                Toast.makeText(this,getString(R.string.hub_username_registered,preAllocatedStringStorage[0]),Toast.LENGTH_SHORT).show();
                FeedbackDatabase.getInstance(this).writeString(USER_NAME,preAllocatedStringStorage[0]);
                userName = preAllocatedStringStorage[0];
            }else{
                userName = name;
                Toast.makeText(this,getString(R.string.hub_username_restored,name),Toast.LENGTH_SHORT).show();
            }
            preAllocatedStringStorage[0] = null;
        }
    }
}
