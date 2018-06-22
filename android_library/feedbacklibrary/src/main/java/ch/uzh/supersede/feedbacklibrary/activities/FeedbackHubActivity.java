package ch.uzh.supersede.feedbacklibrary.activities;


import android.content.DialogInterface.OnClickListener;
import android.annotation.SuppressLint;
import android.content.*;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.view.*;
import android.widget.*;

import java.util.List;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.beans.LocalFeedbackBean;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.models.AndroidUser;
import ch.uzh.supersede.feedbacklibrary.models.AuthenticateRequest;
import ch.uzh.supersede.feedbacklibrary.models.AuthenticateResponse;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService;
import ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener;
import ch.uzh.supersede.feedbacklibrary.utils.ColorUtility;
import ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility;
import ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL;
import ch.uzh.supersede.feedbacklibrary.utils.PopUp;
import ch.uzh.supersede.feedbacklibrary.utils.Utils;
import ch.uzh.supersede.feedbacklibrary.utils.VersionUtility;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.ActivitiesConstants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
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
        if (getColorCount() == 2) {
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
        updateUserLevel(false);
        invokeVersionControl(2, R.id.hub_button_list, R.id.hub_button_settings);
        FeedbackService.getInstance(this).authenticate(this, new AuthenticateRequest("test", "123")); //TODO [jfo] parse credentials
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void createInfoBubbles() {
        boolean tutorialFinished = getSharedPreferences(SHARED_PREFERENCES_ID, MODE_PRIVATE).getBoolean(SHARED_PREFERENCES_TUTORIAL_HUB, false);
        if (!tutorialFinished) {
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
                    Toast.makeText(v.getContext(), R.string.hub_tutorial_finished, Toast.LENGTH_SHORT).show();
                    getSharedPreferences(SHARED_PREFERENCES_ID, MODE_PRIVATE).edit().putBoolean(SHARED_PREFERENCES_TUTORIAL_HUB, true).apply();
                    return false;
                }
            });
            colorShape(1, lrLayout, llLayout, mLayout, urLayout, ulLayout, umLayout);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUserLevel(false);
        restoreHostApplicationNameToPreferences();
    }

    @Override
    public void onEventCompleted(EventType eventType, Object response) {
        switch (eventType) {
            case AUTHENTICATE:
                if (response instanceof AuthenticateResponse) {
                    FeedbackService.getInstance(this).setToken(((AuthenticateResponse) response).getToken());
                }
                FeedbackService.getInstance(this).setApplicationId(configuration.getHostApplicationLongId()); //TODO [jfo] maybe this id is returned with authentication
                FeedbackService.getInstance(this).setLanguage(configuration.getHostApplicationLanguage());
                break;
            case CREATE_USER:
                if (response instanceof AndroidUser) {
                    AndroidUser androidUser = (AndroidUser) response;
                    FeedbackDatabase.getInstance(this).writeString(USER_NAME, androidUser.getName());
                    FeedbackDatabase.getInstance(this).writeBoolean(IS_DEVELOPER, androidUser.isDeveloper());
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
        switch (eventType) {
            case AUTHENTICATE:
                //FIXME [jfo] remove block as soon as possible
                FeedbackService.getInstance(this).setToken(LIFETIME_TOKEN);
                FeedbackService.getInstance(this).setApplicationId(configuration.getHostApplicationLongId()); //TODO [jfo] maybe this id is returned with authentication
                FeedbackService.getInstance(this).setLanguage(configuration.getHostApplicationLanguage());
                break;
            default:
        }
    }

    @Override
    public void onConnectionFailed(EventType eventType) {
        //TODO
    }

    private void updateUserLevel(boolean ignoreDatabaseCheck) {
        userLevel = PermissionUtility.getUserLevel(getApplicationContext(), ignoreDatabaseCheck);
        levelButton.setText(getResources().getString(R.string.hub_feedback_user_level, userLevel.getLevel()));

        if (getColorCount() == 2) {
            enableView(levelButton, 1);
            disableViews(settingsButton, feedbackButton, listButton);
        } else if (getColorCount() == 3) {
            if (getConfiguration().isColoringVertical()) {
                enableView(levelButton, 0);
            } else {
                enableView(levelButton, 2);
            }
        }
        statusText.setText(null);
        if (PASSIVE.check(getApplicationContext(), ignoreDatabaseCheck)) {
            enableView(listButton, 1, VersionUtility.getDateVersion() > 1);
        }
        if (ACTIVE.check(getApplicationContext(), ignoreDatabaseCheck)) {
            Utils.persistScreenshot(this, cachedScreenshot);
            int ownFeedbackBeans = FeedbackDatabase.getInstance(this).getFeedbackBeans(OWN).size();
            int upVotedFeedbackBeans = FeedbackDatabase.getInstance(this).getFeedbackBeans(UP_VOTED).size();
            int downVotedFeedbackBeans = FeedbackDatabase.getInstance(this).getFeedbackBeans(DOWN_VOTED).size();
            int respondedFeedbackBeans = FeedbackDatabase.getInstance(this).getFeedbackBeans(RESPONDED).size();
            int allFeedbackBeans = FeedbackDatabase.getInstance(this).getFeedbackBeans(ALL).size();
            if (configuration.hasAtLeastNTopColors(2)) {
                statusText.setText(Html.fromHtml(getString(R.string.hub_status, userName,
                        allFeedbackBeans,
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
                        allFeedbackBeans,
                        ownFeedbackBeans,
                        respondedFeedbackBeans,
                        upVotedFeedbackBeans,
                        downVotedFeedbackBeans)
                        .replace(PRIMARY_COLOR_STRING, BLACK_HEX)
                        .replace(SECONDARY_COLOR_STRING, DARK_BLUE)));
            }
            if (getColorCount() == 2) {
                enableView(feedbackButton, 1);
                enableView(settingsButton, 1, VersionUtility.getDateVersion() > 1);
            } else if (getColorCount() == 3) {
                if (getConfiguration().isColoringVertical()) {
                    enableView(feedbackButton, 2);
                    enableView(settingsButton, 2, VersionUtility.getDateVersion() > 1);
                } else {
                    enableView(feedbackButton, 0);
                    enableView(settingsButton, 2, VersionUtility.getDateVersion() > 1);
                }
            }
        }
        if (ADVANCED.check(getApplicationContext(), ignoreDatabaseCheck)) {
            disableViews(levelButton);
        }
    }

    @Override
    public void onButtonClicked(View view) {
        if (view != null) {
            int i = view.getId();
            if (i == R.id.hub_button_list) {
                startActivity(this, FeedbackListActivity.class,false);
            } else if (i == R.id.hub_button_feedback) {
                startActivity(this, FeedbackIdentityActivity.class,false);
            } else if (i == R.id.hub_button_settings) {
                startActivity(this, FeedbackSettingsActivity.class,false);
            } else if (i == R.id.hub_button_user_level) {
                switch (userLevel.getLevel()) {
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
                                .withCustomOk(getString(R.string.hub_confirm), getClickListener(ACTIVE, nameInputText)).buildAndShow();
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
                    FeedbackDatabase.getInstance(this).writeBoolean(IS_DEVELOPER, false);
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
}
