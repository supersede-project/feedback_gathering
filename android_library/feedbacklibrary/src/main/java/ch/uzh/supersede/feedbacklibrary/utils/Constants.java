package ch.uzh.supersede.feedbacklibrary.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Constants {

    private Constants() {
    }

    public static final int PERMISSION_REQUEST_ALL = 800;
    public static final String FEEDBACK_CONTRIBUTOR = "feedbackContributor";
    public static final String SHARED_PREFERENCES = "feedbackSharedPreferences";
    public static final String USER_NAME = "userName";
    public static final String IS_DEVELOPER = "isDeveloper";
    public static final String USER_NAME_ANONYMOUS = "anonymous";

    public static final String SPACE = " ";
    public static final String PATH_DELIMITER = "/";
    public static final String SUPERSEDE_BASE_URL = "https://platform.supersede.eu:8443/";

    public static final String SEPARATOR = "::;;::;;";
    public static final String IS_PUSH_STRING = "isPush";

    public static final String ATTACHMENT_TYPE = "ATTACHMENT_TYPE";
    public static final String AUDIO_TYPE = "AUDIO_TYPE";
    public static final String CATEGORY_TYPE = "CATEGORY_TYPE";
    public static final String RATING_TYPE = "RATING_TYPE";
    public static final String SCREENSHOT_TYPE = "SCREENSHOT_TYPE";
    public static final String TEXT_TYPE = "TEXT_TYPE";
    public static final String IMAGE_TYPE = "IMAGE_TYPE";

    public static final String JSON_CONFIGURATION_STRING = "jsonConfigurationString";
    public static final String SELECTED_PULL_CONFIGURATION_INDEX = "selectedPullConfigurationIndex";

    public static final String EXTRA_KEY_APPLICATION_ID = "applicationId";
    public static final String EXTRA_KEY_BASE_URL = "baseURL";
    public static final String EXTRA_KEY_LANGUAGE = "language";
    public static final String EXTRA_KEY_HOST_APPLICATION_NAME = "hostApplicationName";
    public static final String EXTRA_KEY_FEEDBACK_BEAN = "feedbackBean";
    public static final String EXTRA_KEY_CACHED_SCREENSHOT = "cachedScreenshot";

    public static final String SHARED_PREFERENCES_ID = "ch.uzh.supersede.feedbacklibrary.feedback";
    public static final String SHARED_PREFERENCES_IS_DEVELOPER = "isDeveloper";
    public static final String SHARED_PREFERENCES_HOST_APPLICATION_NAME = "hostApplicationName";
    public static final String SHARED_PREFERENCES_HOST_APPLICATION_ID = "hostApplicationId";
    public static final String SHARED_PREFERENCES_SETTINGS_USER_NAME_MIN_LENGTH = "settingsUserNameMin";
    public static final String SHARED_PREFERENCES_SETTINGS_USER_NAME_MAX_LENGTH = "settingsUserNameMax";
    public static final String SHARED_PREFERENCES_SETTINGS_RESPONSE_MIN_LENGTH = "settingsResponseMin";
    public static final String SHARED_PREFERENCES_SETTINGS_RESPONSE_MAX_LENGTH = "settingsResponseMax";

    public static final int REQUEST_PHOTO = 11;
    public static final int REQUEST_ANNOTATE = 12;

    public static final String IMAGE_ANNOTATED_DATA_DB_KEY = "imageAnnotatedData";
    public static final String EXTRA_KEY_ALL_STICKER_ANNOTATIONS = "allStickerAnnotations";
    public static final String EXTRA_KEY_HAS_STICKER_ANNOTATIONS = "hasStickerAnnotations";

    public static class SettingsConstants {
        private SettingsConstants() {
        }

        //SettingsConstants

        public static final int SETTINGS_USER_NAME_MIN_LENGTH = 4;
        public static final int SETTINGS_USER_NAME_MAX_LENGTH = 16;
        public static final int SETTINGS_RESPONSE_MIN_LENGTH = 20;
        public static final int SETTINGS_RESPONSE_MAX_LENGTH = 200;
    }
    
    public static class UtilsConstants {
        private UtilsConstants() {
        }

        //UtilsConstants
        public static final String UTILS_TAG = "UtilsConstants";
        public static final String IMAGE_DATA_DB_KEY = "imageData";
    }

    public static class ViewsConstants {
        private ViewsConstants() {
        }

        //ColorPickerView
        public static final int CENTER_X = 100;
        public static final int CENTER_Y = 100;
        public static final int CENTER_RADIUS = 32;
        public static final int[] COLORS = new int[]{0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF, 0xFF00FF00, 0xFFFFFF00, 0xFFFF0000};

        //AbstractAnnotationView
        public static final int BUTTON_SIZE_DP = 30;
        public static final int SELF_SIZE_DP = 100;

        //ScrenshotMechanismView
        public static final String TEXT_ANNOTATION_COUNTER_MAXIMUM = "textAnnotationCounterMaximum";
    }

    public static class ActivitiesConstants {
        private ActivitiesConstants() {
        }

        //FeedbackHubActivity
        public static final String COLOR_STRING = "colorString";
        public static final String DARK_BLUE = "303F9F";

        //FeedbackAcitivity
        public static final String FEEDBACK_ACTIVITY_TAG = "FeedbackActivity";
        public static final String JSON_CONFIGURATION_FILE_NAME = "currentConfiguration.json";
    }

    public static class ServicesConstants {
        private ServicesConstants() {
        }

        public static final String FEEDBACK_SERVICE_TAG = "FeedbackService";
        public static final String CONFIGURATION_REAUEST_WRAPPER_TAG = "ConfigurationRequestWra";
        public static final String EMAIL_SERVICE_TAG = "EmailService";
    }

    public static class ModelsConstants {
        private ModelsConstants() {
        }

        // AudioMechanism
        public static final String AUDIO_MECHANISM_VIEW_TAG = "AudioMechanismView";
        public static final String AUDIO_DIR = "audioDir";
        public static final String AUDIO_EXTENSION = "m4a";
        public static final String AUDIO_FILENAME = "audioFile";
    }

    public static class StubsConstants {
        private StubsConstants() {
        }

        public static final String DIALOG_TYPE = "DIALOG_TYPE";

        //General
        public static final String ORCHESTRATOR_KEY = "key";
        public static final String ORCHESTRATOR_VALUE = "value";
        //Audio
        public static final String AUDIO_TITLE_KEY = "title";
        public static final String AUDIO_TITLE_VALUE = "Audio-Mechanism";
        public static final String AUDIO_MAX_TIME_KEY = "maxTime";
        public static final String AUDIO_MAX_TIME_VALUE = "15.0";
        //Text
        public static final String TEXT_TITLE_KEY = "title";
        public static final String TEXT_HINT_KEY = "hint";
        public static final String TEXT_LABEL_KEY = "label";
        public static final String TEXT_FONT_COLOR_KEY = "textareaFontColor";
        public static final String TEXT_FONT_SIZE_KEY = "fieldFontSize";
        public static final String TEXT_FONT_TYPE_KEY = "fieldFontType";
        public static final String TEXT_ALIGN_KEY = "fieldTextAlignment";
        public static final String TEXT_MAX_LENGTH_KEY = "maxLength";
        public static final String TEXT_MAX_LENGTH_VISIBLE_KEY = "maxLengthVisible";
        public static final String TEXT_LENGTH_VISIBLE_KEY = "textLengthVisible";
        public static final String TEXT_MANDATORY_KEY = "mandatory";
        public static final String TEXT_REMINDER_KEY = "mandatoryReminder";
        public static final String TEXT_TITLE_VALUE = "Text-Mechanism";
        public static final String TEXT_HINT_VALUE = "Enter a Feedback-Text here...";
        public static final String TEXT_LABEL_VALUE = "Text";
        public static final String TEXT_FONT_COLOR_VALUE = "#1511FF";
        public static final String TEXT_FONT_SIZE_VALUE = "12";
        public static final String TEXT_FONT_TYPE_VALUE = "italic";
        public static final String TEXT_ALIGN_VALUE = "left";
        public static final String TEXT_MAX_LENGTH_VALUE = "200";
        public static final String TEXT_MAX_LENGTH_VISIBLE_VALUE = "150";
        public static final String TEXT_LENGTH_VISIBLE_VALUE = "100";
        public static final String TEXT_MANDATORY_VALUE = "1";
        public static final String TEXT_REMINDER_VALUE = "Default mandatory reminder text";
        //Screenshot
        public static final String SCREENSHOT_TITLE_KEY = "title";
        public static final String SCREENSHOT_DEFAULT_KEY = "defaultPicture";
        public static final String SCREENSHOT_MAX_TEXT_KEY = "maxTextAnnotation";
        public static final String SCREENSHOT_TITLE_VALUE = "Screenshot";
        public static final String SCREENSHOT_DEFAULT_VALUE = "";
        public static final String SCREENSHOT_MAX_TEXT_VALUE = "1000";
        //Category
        public static final String CATEGORY_TITLE_KEY = "title";
        public static final String CATEGORY_MANDATORY_KEY = "mandatory";
        public static final String CATEGORY_MANDATORY_REMINDER_KEY = "mandatoryReminder";
        public static final String CATEGORY_MULTIPLE_KEY = "multiple";
        public static final String CATEGORY_OWN_KEY = "ownAllowed";
        public static final String CATEGORY_OPTIONS_KEY = "options";
        public static final String CATEGORY_TITLE_VALUE = "Screenshot-Mechanism";
        public static final String CATEGORY_MANDATORY_VALUE = "1";
        public static final String CATEGORY_MANDATORY_REMINDER_VALUE = "Default mandatory reminder text";
        public static final String CATEGORY_MULTIPLE_VALUE = "1";
        public static final String CATEGORY_OWN_VALUE = "1";
        public static final List<Map<String, Object>> CATEGORY_OPTIONS_VALUE = new ArrayList<>();
        //Rating
        public static final String RATING_TITLE_KEY = "title";
        public static final String RATING_ICON_KEY = "ratingIcon";
        public static final String RATING_MAX_KEY = "maxRating";
        public static final String RATING_DEFAULT_KEY = "defaultRating";
        public static final String RATING_TITLE_VALUE = "Please select a Rating:";
        public static final String RATING_ICON_VALUE = "";
        public static final String RATING_MAX_VALUE = "5.0";
        public static final String RATING_DEFAULT_VALUE = "3.0";
    }
}
