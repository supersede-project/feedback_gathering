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
    public static final String TECHNICAL_USER_NAME = "technicalUserName";


    public static final String SPACE = " ";
    public static final String PATH_DELIMITER = "/";
    public static final String SUPERSEEDE_BASE_URL = "https://platform.supersede.eu:8443/";
    public static final int OK_RESPONSE = 200;


    public static class  FeedbackColorConstants {
        private FeedbackColorConstants(){
        }

        public static final String COLOR_STRING = "colorString";
        public static final String DARK_BLUE = "303F9F";
    }

    public static class FeedbackActivityConstants {

        private FeedbackActivityConstants() {
        }

        public static final String IS_PUSH_STRING = "isPush";
        public static final String JSON_CONFIGURATION_FILE_NAME = "currentConfiguration.json";
        public static final String JSON_CONFIGURATION_STRING = "jsonConfigurationString";
        public static final String SELECTED_PULL_CONFIGURATION_INDEX_STRING = "selectedPullConfigurationIndex";

        // Initialization
        public static final String EXTRA_KEY_APPLICATION_ID = "applicationId";
        public static final String EXTRA_KEY_BASE_URL = "baseURL";
        public static final String EXTRA_KEY_LANGUAGE = "language";
        public static final String EXTRA_KEY_CACHED_SCREENSHOT = "cachedScreenshot";
        public static final int REQUEST_PHOTO = 11;
        public static final int REQUEST_ANNOTATE = 12;

        // Storage permission (android.permission-group.STORAGE)
        public static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;
        public static final String TAG = "FeedbackActivity";
    }

    public static class AudioMechanismConstants {
        private AudioMechanismConstants() {
        }

        public static final String AUDIO_DIR = "audioDir";
        public static final String AUDIO_EXTENSION = "m4a";
        public static final String AUDIO_FILENAME = "audioFile";
    }

    public static class ScreenshotConstants {
        private ScreenshotConstants() {
        }
        public static final String TAG = "Utils";
        public static final String IMAGE_DATA_DB_KEY = "imageData";
        public static final String IMAGE_ANNOTATED_DATA_DB_KEY = "imageAnnotatedData";

        public static final String EXTRA_KEY_ALL_STICKER_ANNOTATIONS = "allStickerAnnotations";
        public static final String EXTRA_KEY_HAS_STICKER_ANNOTATIONS = "hasStickerAnnotations";
        public static final String SEPARATOR = "::;;::;;";
        public static final String TEXT_ANNOTATION_COUNTER_MAXIMUM = "textAnnotationCounterMaximum";
    }

    public static class OrchestratorConstants{
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
