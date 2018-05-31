package ch.uzh.supersede.feedbacklibrary.utils;

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

    public static final String JSON_CONFIGURATION_STRING = "jsonConfigurationString";
    public static final String SELECTED_PULL_CONFIGURATION_INDEX = "selectedPullConfigurationIndex";

    public static final String EXTRA_KEY_BASE_URL = "baseURL";
    public static final String EXTRA_KEY_LANGUAGE = "language";
    public static final String EXTRA_KEY_HOST_APPLICATION_NAME = "hostApplicationName";
    public static final String EXTRA_KEY_APPLICATION_CONFIGURATION = "applicationConfiguration";
    public static final String EXTRA_KEY_FEEDBACK_BEAN = "feedbackBean";
    public static final String EXTRA_KEY_FEEDBACK_DETAIL_BEAN = "feedbackDetailBean";
    public static final String EXTRA_KEY_CACHED_SCREENSHOT = "cachedScreenshot";

    public static final String SHARED_PREFERENCES_ID = "ch.uzh.supersede.feedbacklibrary.feedback";
    public static final String SHARED_PREFERENCES_HOST_APPLICATION_NAME = "hostApplicationName";

    public static final int REQUEST_PHOTO = 11;
    public static final int REQUEST_ANNOTATE = 12;

    public static final String IMAGE_ANNOTATED_DATA_DB_KEY = "imageAnnotatedData";
    public static final String EXTRA_KEY_ALL_STICKER_ANNOTATIONS = "allStickerAnnotations";
    public static final String EXTRA_KEY_HAS_STICKER_ANNOTATIONS = "hasStickerAnnotations";

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
        public static final int ANTHRAZIT_DARK = -11711155;
        public static final int GRAY_DARK = -8947849;
        public static final int GRAY = -4473925;
        public static final int SILVER = -1118482;
        public static final int BLACK = -16777216;
        public static final int WHITE = -1;
        public static final int SWISS_RED = -65536;
        public static final int YUGOSLAVIA_RED = -2359296;
        public static final int YUGOSLAVIA_BLUE = -16697197;
        public static final int ITALY_GREEN = -16739770;
        public static final int ITALY_RED= -3265737;
        public static final int GERMANY_RED= -2293760;
        public static final int GERMANY_GOLD= -12800;
        public static final int AUSTRIA_RED= -1234631;
        public static final int FRANCE_BLUE= -16768107;
        public static final int FRANCE_RED= -1234631;
        public static final int WIN95_GRAY= -4144960;
        public static final int WIN95_BLUE= -16777088;
        public static final int DISABLED_BACKGROUND= -3355444;
        public static final int DISABLED_FOREGROUND= -8750470;

        //FeedbackAcitivity
        public static final String FEEDBACK_ACTIVITY_TAG = "FeedbackActivity";
    }

    public static class ServicesConstants {
        private ServicesConstants() {
        }

        public static final String FEEDBACK_SERVICE_TAG = "FeedbackService";
        public static final String CONFIGURATION_REAUEST_WRAPPER_TAG = "ConfigurationRequestWra";
    }

    public static class ModelsConstants {
        private ModelsConstants() {
        }

        // AudioFeedback
        public static final String AUDIO_MECHANISM_VIEW_TAG = "AudioMechanismView";
        public static final String AUDIO_DIR = "audioDir";
        public static final String AUDIO_EXTENSION = "m4a";
        public static final String AUDIO_FILENAME = "audioFile";
    }
}
