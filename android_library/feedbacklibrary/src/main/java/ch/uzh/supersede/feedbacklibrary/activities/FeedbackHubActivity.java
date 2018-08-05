package ch.uzh.supersede.feedbacklibrary.activities;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.text.InputFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import java.util.List;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.beans.LocalFeedbackBean;
import ch.uzh.supersede.feedbacklibrary.database.DatabaseMigration;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.models.*;
import ch.uzh.supersede.feedbacklibrary.services.*;
import ch.uzh.supersede.feedbacklibrary.utils.*;
import ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL;

import static ch.uzh.supersede.feedbacklibrary.entrypoint.IFeedbackStyleConfiguration.FEEDBACK_STYLE.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.ActivitiesConstants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.UserConstants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.FETCH_MODE.*;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.*;

@SuppressWarnings({"squid:MaximumInheritanceDepth", "squid:S1170"})
public class FeedbackHubActivity extends AbstractBaseActivity implements IFeedbackServiceEventListener {
    private Button listButton;
    private Button levelButton;
    private Button feedbackButton;
    private Button settingsButton;
    private LinearLayout backgroundLayout;
    private TextView spaceTop;
    private TextView spaceLeft;
    private TextView spaceRight;
    private TextView spaceBottom;
    private TextView statusText;
    private String userName;
    private boolean tutorialInitialized = false;
    private int tapCounter = 0;
    private byte[] cachedScreenshot = null;
    private String hostApplicationName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_hub);
        //Layout
        listButton = getView(R.id.hub_button_list, Button.class);
        levelButton = getView(R.id.hub_button_user_level, Button.class);
        feedbackButton = getView(R.id.hub_button_feedback, Button.class);
        settingsButton = getView(R.id.hub_button_settings, Button.class);
        backgroundLayout = getView(R.id.hub_layout_background, LinearLayout.class);
        spaceTop = getView(R.id.hub_space_color_top, TextView.class);
        spaceBottom = getView(R.id.hub_space_color_bottom, TextView.class);
        spaceLeft = getView(R.id.hub_space_color_left, TextView.class);
        spaceRight = getView(R.id.hub_space_color_right, TextView.class);
        if (getColorCount() == 2 || CollectionUtility.oneOf(getConfiguration().getStyle(), DARK, LIGHT, SWITZERLAND, WINDOWS95)) {
            colorViews(0, backgroundLayout);
            colorViews(1, listButton, levelButton, feedbackButton, settingsButton);
        } else if (getColorCount() == 3) {
            if (getConfiguration().isColoringVertical()) {
                colorViews(0, listButton, levelButton, spaceTop);
                colorViews(1, backgroundLayout);
                colorViews(2, feedbackButton, settingsButton, spaceBottom);
            } else {
                colorViews(0, listButton, spaceLeft, feedbackButton);
                colorViews(1, backgroundLayout);
                colorViews(2, levelButton, spaceRight, settingsButton);
            }
        }
        statusText = getView(R.id.hub_text_status, TextView.class);
        //Cache
        cachedScreenshot = getIntent().getByteArrayExtra(EXTRA_KEY_CACHED_SCREENSHOT);
        restoreHostApplicationNameToPreferences();
        onPostCreate();
        if (ACTIVE.check(this)) {
            userName = FeedbackDatabase.getInstance(this).readString(USER_NAME, null);
        }
        execDatabaseMigration();
        updateUserLevel(false);
        invokeVersionControl(2, R.id.hub_button_list, R.id.hub_button_settings);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void createInfoBubbles() {
        boolean tutorialFinished = getSharedPreferences(SHARED_PREFERENCES_ID, MODE_PRIVATE).getBoolean(SHARED_PREFERENCES_TUTORIAL_HUB, false);
        if (!tutorialFinished && !tutorialInitialized) {
            RelativeLayout root = getView(R.id.hub_root, RelativeLayout.class);
            RelativeLayout mLayout = infoUtility.addInfoBox(root, getString(R.string.hub_feedback_status_label), getString(R.string.hub_feedback_status_info), this, statusText);
            RelativeLayout llLayout = infoUtility.addInfoBox(root, getString(R.string.hub_feedback_create_label), getString(R.string.hub_feedback_create_info), this, feedbackButton, mLayout);
            RelativeLayout lrLayout = infoUtility.addInfoBox(root, getString(R.string.hub_feedback_settings_label), getString(R.string.hub_feedback_settings_info), this, settingsButton, llLayout);
            RelativeLayout ulLayout = infoUtility.addInfoBox(root, getString(R.string.hub_feedback_list_label), getString(R.string.hub_feedback_list_info), this, listButton, lrLayout);
            RelativeLayout urLayout = infoUtility.addInfoBox(root, getString(R.string.hub_feedback_user_lvl_label), getString(R.string.hub_feedback_user_lvl_info), this, levelButton, ulLayout);
            RelativeLayout umLayout = infoUtility.addInfoBox(root, getString(R.string.hub_feedback_info_label), getString(R.string.hub_feedback_info_info), this, spaceTop, urLayout);
            mLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Toast.makeText(v.getContext(), R.string.tutorial_finished, Toast.LENGTH_SHORT).show();
                    getSharedPreferences(SHARED_PREFERENCES_ID, MODE_PRIVATE).edit().putBoolean(SHARED_PREFERENCES_TUTORIAL_HUB, true).apply();
                    return false;
                }
            });
            colorShape(1, lrLayout, llLayout, mLayout, urLayout, ulLayout, umLayout);
            tutorialInitialized = true;
        }
    }

    private void restoreHostApplicationNameToPreferences() {
        String extraHostApplicationName = getIntent().getStringExtra(EXTRA_KEY_HOST_APPLICATION_NAME);
        String preferencesHostApplicationName = getSharedPreferences(SHARED_PREFERENCES_ID, MODE_PRIVATE).getString(SHARED_PREFERENCES_HOST_APPLICATION_NAME, null);
        if (preferencesHostApplicationName == null && extraHostApplicationName != null) {
            getSharedPreferences(SHARED_PREFERENCES_ID, MODE_PRIVATE).edit().putString(SHARED_PREFERENCES_HOST_APPLICATION_NAME, extraHostApplicationName).apply();
        } else if (preferencesHostApplicationName == null && hostApplicationName != null) {
            getSharedPreferences(SHARED_PREFERENCES_ID, MODE_PRIVATE).edit().putString(SHARED_PREFERENCES_HOST_APPLICATION_NAME, hostApplicationName).apply();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUserLevel(false);
        restoreHostApplicationNameToPreferences();
        authenticateAndStartService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUserLevel(false);
        restoreHostApplicationNameToPreferences();
    }

    private void execDatabaseMigration() {
        new DatabaseMigration(getApplicationContext(), configuration).run();
    }

    private void authenticateAndStartService() {
        FeedbackService.getInstance(this).authenticate(this, new AuthenticateRequest(configuration.getEndpointLogin(), configuration.getEndpointPass()));
        ServiceUtility.startService(NotificationService.class, this, new ServiceUtility.Extra(EXTRA_KEY_APPLICATION_CONFIGURATION, configuration));
    }

    private void updateUserLevel(boolean ignoreDatabaseCheck) {
        userLevel = PermissionUtility.getUserLevel(getApplicationContext(), ignoreDatabaseCheck);
        levelButton.setText(getResources().getString(R.string.hub_feedback_user_level, userLevel.getLevel()));

        disableViews(settingsButton, feedbackButton, listButton);

        enableView(levelButton, viewToColorMap.get(levelButton));
        statusText.setText(null);
        if (PASSIVE.check(getApplicationContext(), ignoreDatabaseCheck)) {
            enableView(listButton, viewToColorMap.get(listButton), VersionUtility.getDateVersion() > 1);
        }
        if (ACTIVE.check(getApplicationContext(), ignoreDatabaseCheck)) {
            ConfigurationUtility.execStoreStateToDatabase(this, configuration);
            ImageUtility.persistScreenshot(this, cachedScreenshot);
            int ownFeedbackBeans = FeedbackDatabase.getInstance(this).getFeedbackBeans(OWN).size();
            int upVotedFeedbackBeans = FeedbackDatabase.getInstance(this).getFeedbackBeans(UP_VOTED).size();
            int downVotedFeedbackBeans = FeedbackDatabase.getInstance(this).getFeedbackBeans(DOWN_VOTED).size();
            int respondedFeedbackBeans = FeedbackDatabase.getInstance(this).getFeedbackBeans(RESPONDED).size();
            int userKarma = FeedbackDatabase.getInstance(this).readInteger(USER_KARMA, 0);
            if (configuration.hasAtLeastNTopColors(2)) {
                statusText.setText(Html.fromHtml(getString(R.string.hub_status, userName,
                        userKarma,
                        ownFeedbackBeans,
                        respondedFeedbackBeans,
                        upVotedFeedbackBeans,
                        downVotedFeedbackBeans)
                        .replace(PRIMARY_COLOR_STRING, ColorUtility.isDark(ColorUtility.getBackgroundColorOfView(backgroundLayout)) ? WHITE_HEX : BLACK_HEX)
                        .replace(SECONDARY_COLOR_STRING, ColorUtility.toHexString(ColorUtility.adjustColorToBackground(
                                ColorUtility.getBackgroundColorOfView(backgroundLayout),
                                configuration.getTopColors()[1],
                                0.3)))));
            } else {
                statusText.setText(Html.fromHtml(getString(R.string.hub_status, userName,
                        userKarma,
                        ownFeedbackBeans,
                        respondedFeedbackBeans,
                        upVotedFeedbackBeans,
                        downVotedFeedbackBeans)
                        .replace(PRIMARY_COLOR_STRING, BLACK_HEX)
                        .replace(SECONDARY_COLOR_STRING, DARK_BLUE)));
            }

            boolean userIsDeveloper = FeedbackDatabase.getInstance(this).readBoolean(USER_IS_DEVELOPER, false);
            if (userIsDeveloper) {
                feedbackButton.setText(R.string.hub_feedback_developer_list);
            } else {
                feedbackButton.setText(R.string.hub_feedback_create);
            }

            enableView(feedbackButton, viewToColorMap.get(feedbackButton));
            enableView(settingsButton, viewToColorMap.get(settingsButton));
        }
        if (ADVANCED.check(getApplicationContext(), ignoreDatabaseCheck)) {
            disableViews(levelButton);
        }
    }

    @Override
    public void onButtonClicked(View view) {
        boolean tutorialFinished = getSharedPreferences(SHARED_PREFERENCES_ID, MODE_PRIVATE).getBoolean(SHARED_PREFERENCES_TUTORIAL_HUB, false);
        if (!tutorialFinished) {
            Toast.makeText(getApplicationContext(), R.string.tutorial_alert, Toast.LENGTH_SHORT).show();
            return;
        }
        if (view != null) {
            int i = view.getId();
            if (i == R.id.hub_button_list) {
                startActivity(this, FeedbackListActivity.class, false);
            } else if (i == R.id.hub_button_feedback) {
                boolean userIsDeveloper = FeedbackDatabase.getInstance(this).readBoolean(USER_IS_DEVELOPER, false);
                if (userIsDeveloper) {
                    startActivity(this, FeedbackListDeveloperActivity.class, false);
                } else {
                    startActivity(this, FeedbackIdentityActivity.class, false);
                }
            } else if (i == R.id.hub_button_settings) {
                startActivity(this, FeedbackSettingsActivity.class, false);
            } else if (i == R.id.hub_button_user_level) {
                switch (userLevel.getLevel()) {
                    case 0:
                        new PopUp(this)
                                .withTitle(getString(R.string.hub_access_1))
                                .withMessage(getString(R.string.hub_access_1_description))
                                .withCustomOk(getString(R.string.hub_confirm), getClickListener(PASSIVE)).buildAndShow();
                        break;
                    case 1:
                        if (getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE).getBoolean(SHARED_PREFERENCES_ONLINE, false)) {
                            //der callback ob der server antwortet. generell speichern dieses status in einem state?
                            final EditText nameInputText = new EditText(this);
                            nameInputText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(configuration.getMaxUserNameLength()
                            )});
                            nameInputText.setSingleLine();
                            nameInputText.setMaxLines(1);
                            new PopUp(this)
                                    .withTitle(getString(R.string.hub_access_2))
                                    .withMessage(getString(R.string.hub_access_2_description, configuration.getMinUserNameLength(), configuration.getMaxUserNameLength()))
                                    .withInput(nameInputText)
                                    .withCustomOk(getString(R.string.hub_confirm), getClickListener(ACTIVE, nameInputText)).buildAndShow();
                        } else {
                            new PopUp(this)
                                    .withTitle(getString(R.string.hub_access_2))
                                    .withMessage(getString(R.string.hub_access_2_fail))
                                    .withoutCancel()
                                    .withCustomOk(getString(R.string.hub_confirm)).buildAndShow();
                        }
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
                if (tapCounter >= 5 && ACTIVE.check(this)) {
                    tapCounter = 0;
                    FeedbackDatabase.getInstance(this).writeString(USER_NAME, null);
                    FeedbackDatabase.getInstance(this).writeBoolean(USER_IS_DEVELOPER, false);
                    List<LocalFeedbackBean> ownFeedbackBeans = FeedbackDatabase.getInstance(this).getFeedbackBeans(OWN);
                    List<LocalFeedbackBean> subscribedFeedbackBeans = FeedbackDatabase.getInstance(this).getFeedbackBeans(SUBSCRIBED);
                    List<LocalFeedbackBean> votedFeedbackBeans = FeedbackDatabase.getInstance(this).getFeedbackBeans(VOTED);
                    List<LocalFeedbackBean> respondedFeedbackBeans = FeedbackDatabase.getInstance(this).getFeedbackBeans(RESPONDED);
                    List<LocalFeedbackBean> allFeedbackBeans = FeedbackDatabase.getInstance(this).getFeedbackBeans(ALL);
                    FeedbackDatabase.getInstance(this).wipeAllStoredFeedback();
                    Toast.makeText(this, "DEVELOPER: Username resetted!\n" +
                                    allFeedbackBeans.size() + " Local Feedback-Entries deleted, containing\n" +
                                    ownFeedbackBeans.size() + " own Feedback-Entries\n" +
                                    subscribedFeedbackBeans.size() + " subscribed Feedback-Entries\n" +
                                    votedFeedbackBeans.size() + " voted Feedback-Entries\n" +
                                    respondedFeedbackBeans.size() + " responded Feedback-Entries.",
                            Toast.LENGTH_SHORT).show();
                    userName = null;
                    statusText.setText(Html.fromHtml(getString(R.string.hub_status, userName, 0, 0, 0, 0, 0).replace(PRIMARY_COLOR_STRING, DARK_BLUE)));
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
                    getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE).edit().putBoolean(FEEDBACK_CONTRIBUTOR, true).apply();
                    updateUserLevel(true);
                }
            };
        } else if (userLevel == ACTIVE) {
            return new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String inputString = inputEditText[0].getText().toString();
                    if (inputString.length() < configuration.getMinUserNameLength()) {
                        Toast.makeText(getApplicationContext(), R.string.hub_warning_username_too_short, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (inputString.length() > configuration.getMaxUserNameLength()) {
                        Toast.makeText(getApplicationContext(), R.string.hub_warning_username_too_short, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    preAllocatedStringStorage[0] = inputString;
                    dialog.cancel();
                    if (ACTIVE.getMissing(getApplicationContext()).length == 0) {
                        handleAllPermissionsGranted(true); //Can only happen when Developer reset their UserName
                    } else {
                        ActivityCompat.requestPermissions(FeedbackHubActivity.this, ACTIVE.getMissing(getApplicationContext()), PERMISSION_REQUEST_ALL);
                    }
                }
            };
        } else if (userLevel == ADVANCED) {
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
        if (ACTIVE.check(this, true) && preAllocatedStringStorage[0] != null) {
            String name = FeedbackDatabase.getInstance(this).readString(USER_NAME, null);
            if (name == null) {
                Toast.makeText(this, getString(configuration.isDeveloper() ? R.string.hub_developer_registered : R.string.hub_username_registered, preAllocatedStringStorage[0]), Toast.LENGTH_SHORT)
                     .show();
                FeedbackService.getInstance(this).createUser(this, new AndroidUser(preAllocatedStringStorage[0], configuration.isDeveloper()));
                userName = USER_NAME_CREATING;
                levelButton.setEnabled(false);
            } else {
                userName = name;
                Toast.makeText(this, getString(configuration.isDeveloper() ? R.string.hub_developer_registered : R.string.hub_username_restored, name), Toast.LENGTH_SHORT).show();
                preAllocatedStringStorage[0] = null;
            }
        }
        if (reload) {
            updateUserLevel(true);
        }
    }

    @Override
    public void onEventCompleted(EventType eventType, Object response) {
        switch (eventType) {
            case AUTHENTICATE:
                if (response instanceof AuthenticateResponse) {
                    FeedbackService.getInstance(this).setToken(((AuthenticateResponse) response).getToken());
                }
                break;
            case CREATE_USER:
                if (response instanceof AndroidUser) {
                    AndroidUser androidUser = (AndroidUser) response;
                    FeedbackDatabase.getInstance(this).writeString(USER_NAME, androidUser.getName());
                    FeedbackDatabase.getInstance(this).writeInteger(USER_KARMA, androidUser.getKarma());
                    FeedbackDatabase.getInstance(this).writeBoolean(USER_IS_DEVELOPER, androidUser.isDeveloper());
                    FeedbackDatabase.getInstance(this).writeBoolean(USER_IS_BLOCKED, androidUser.isBlocked());
                    userName = androidUser.getName();
                }
                preAllocatedStringStorage[0] = null;
                updateUserLevel(true);
                levelButton.setEnabled(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onEventFailed(EventType eventType, Object response) {
        Log.w(getClass().getSimpleName(), getResources().getString(R.string.api_service_event_failed, eventType, response.toString()));
    }

    @Override
    public void onConnectionFailed(EventType eventType) {
        Log.w(getClass().getSimpleName(), getResources().getString(R.string.api_service_connection_failed, eventType));
    }
}
