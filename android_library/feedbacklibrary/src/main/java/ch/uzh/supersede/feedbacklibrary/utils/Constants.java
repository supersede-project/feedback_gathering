package ch.uzh.supersede.feedbacklibrary.utils;


import static ch.uzh.supersede.feedbacklibrary.utils.Enums.RUNNING_MODE_TYPE.*;

public class Constants {
    public static final Enums.RUNNING_MODE_TYPE RUNNING_MODE = TEST;

    private Constants() {
    }

    public static final int PERMISSION_REQUEST_ALL = 800;
    public static final String FEEDBACK_CONTRIBUTOR = "feedbackContributor";
    public static final String SHARED_PREFERENCES = "feedbackSharedPreferences";

    public static final String USE_STUBS = "useStubs";
    public static final String ENABLE_NOTIFICATIONS = "enableNotifications";
    public static final String USER_NAME_ANONYMOUS = "anonymous";
    public static final String USER_NAME_CREATING = "being created ..";
    public static final String CONFIGURATION = "localConfigurationBean";

    public static final String SPACE = " ";
    public static final String PATH_DELIMITER = "/";
    public static final String HTML_LINEBREAK = "<br/>";

    public static final String SUPERSEDE_BASE_URL = RUNNING_MODE == PRODUCTIVE ? "https://platform.supersede.eu:8443/" : "http://mt.ronnieschaniel.com:8080/feedback_repository/";
    public static final String LIFETIME_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImNyZWF0ZWQiOjE1MjQxNDkwNDM1MjcsImV4cCI6MjEyODk0OTA0M30.QCV2GDgD0GSeqjuVL8UjQUDGnxLrYIl5akiFTklUTlOVqmT9mqxuBpK6EDNhUP53-zSBYpieGdZmDwTjt4nDXQ"; //FIXME [jfo] remove as soon as possible
    public static final String SEPARATOR = "::;;::;;";

    public static final String EXTRA_KEY_HOST_APPLICATION_NAME = "hostApplicationName";
    public static final String EXTRA_KEY_APPLICATION_CONFIGURATION = "applicationConfiguration";
    public static final String EXTRA_KEY_FEEDBACK_BEAN = "feedbackBean";
    public static final String EXTRA_KEY_FEEDBACK_DETAIL_BEAN = "feedbackDetailBean";
    public static final String EXTRA_KEY_CACHED_SCREENSHOT = "cachedScreenshot";
    public static final String EXTRA_KEY_FEEDBACK_TITLE = "feedbackTitle";
    public static final String EXTRA_KEY_FEEDBACK_TAGS = "feedbackTags";
    public static final String EXTRA_FROM_CREATION = "fromCreation";
    public static final String EXTRA_KEY_FEEDBACK_DELETION = "feedbackDeletion";
    public static final String EXTRA_KEY_ALL_STICKER_ANNOTATIONS = "allStickerAnnotations";
    public static final String EXTRA_KEY_HAS_STICKER_ANNOTATIONS = "hasStickerAnnotations";

    public static final String SHARED_PREFERENCES_ID = "ch.uzh.supersede.feedbacklibrary.feedback";
    public static final String SHARED_PREFERENCES_HOST_APPLICATION_NAME = "hostApplicationName";
    public static final String SHARED_PREFERENCES_TUTORIAL_HUB = "hubTutorial";
    public static final String SHARED_PREFERENCES_TUTORIAL_IDENTITY = "identityTutorial";
    public static final String SHARED_PREFERENCES_ONLINE = "isOnline";
    public static final String SHARED_PREFERENCES_ENDPOINT_URL = "endpointUrl";
    public static final String SHARED_PREFERENCES_ENDPOINT_URL_FALLBACK = "https://www.google.com";

    public static final int REQUEST_PHOTO = 11;
    public static final int REQUEST_ANNOTATE = 12;

    public static final String IMAGE_ANNOTATED_DATA_DB_KEY = "imageAnnotatedData";
    public static final String NOT_YET_IMPLEMENTED_EXCEPTION = "Not yet implemented";


    public static class UserConstants {
        private UserConstants() {
        }

        public static final String USER_NAME = "userName";
        public static final String USER_IS_DEVELOPER = "isDeveloper";
        public static final String USER_KARMA = "userKarma";
        public static final String USER_IS_BLOCKED = "userIsBlocked";
        public static final String USER_REPORTED_FEEDBACK = "reportedFeedback";
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
    }

    public static class ActivitiesConstants {
        private ActivitiesConstants() {
        }

        //FeedbackHubActivity
        public static final String PRIMARY_COLOR_STRING = "primaryColorString";
        public static final String SECONDARY_COLOR_STRING = "secondaryColorString";
        public static final String DARK_BLUE = "303F9F";
        public static final String BLACK_HEX = "000000";
        public static final String WHITE_HEX = "FFFFFF";
        public static final int ANTHRACITE_DARK = -11711155;
        public static final int GRAY_DARK = -8947849;
        public static final int GRAY = -4473925;
        public static final int SILVER = -1118482;
        public static final int BLACK = -16777216;
        public static final int WHITE = -1;
        public static final int SWISS_RED = -65536;
        public static final int YUGOSLAVIA_RED = -2359296;
        public static final int YUGOSLAVIA_BLUE = -16697197;
        public static final int ITALY_GREEN = -16739770;
        public static final int ITALY_RED = -3265737;
        public static final int GERMANY_RED = -2293760;
        public static final int GERMANY_GOLD = -12800;
        public static final int AUSTRIA_RED = -1234631;
        public static final int FRANCE_BLUE = -16768107;
        public static final int FRANCE_RED = -1234631;
        public static final int WIN95_GRAY = -4144960;
        public static final int WIN95_BLUE = -16777088;
        public static final int DISABLED_BACKGROUND = -3355444;
        public static final int DISABLED_FOREGROUND = -8750470;

        //FeedbackAcitivity
        public static final String FEEDBACK_ACTIVITY_TAG = "FeedbackActivity";
    }

    public static class ModelsConstants {
        private ModelsConstants() {
        }

        // AudioFeedback
        public static final String AUDIO_MECHANISM_VIEW_TAG = "AudioFeedbackView";
        public static final String AUDIO_DIR = "audioDir";
        public static final String AUDIO_EXTENSION = "m4a";
        public static final String AUDIO_FILENAME = "audioFile";
    }
}
