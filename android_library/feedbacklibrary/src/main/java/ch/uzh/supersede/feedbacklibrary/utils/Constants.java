package ch.uzh.supersede.feedbacklibrary.utils;

/**
 * Created by Marco on 08.03.2018.
 */

public class Constants {

    public static final String PATH_DELIMITER = "/";

    public static class FeedbackActivityConstants {
        public static final String ANNOTATED_IMAGE_NAME_WITHOUT_STICKERS = "annotatedImageWithoutStickers.png";
        public static final String ANNOTATED_IMAGE_NAME_WITH_STICKERS = "annotatedImageWithStickers.png";
        public static final String CONFIGURATION_DIR = "configDir";
        public static final String DEFAULT_IMAGE_PATH = "defaultImagePath";
        public static final String IS_PUSH_STRING = "isPush";
        public static final String JSON_CONFIGURATION_FILE_NAME = "currentConfiguration.json";
        public static final String JSON_CONFIGURATION_STRING = "jsonConfigurationString";
        public static final String SELECTED_PULL_CONFIGURATION_INDEX_STRING = "selectedPullConfigurationIndex";
        // Microphone permission (android.permission-group.MICROPHONE)
        public static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 101;
        // Initialization
        public static final String EXTRA_KEY_APPLICATION_ID = "applicationId";
        public static final String EXTRA_KEY_BASE_URL = "baseURL";
        public static final String EXTRA_KEY_LANGUAGE = "language";
        public static final int REQUEST_CAMERA = 10;
        public static final int REQUEST_PHOTO = 11;
        public static final int REQUEST_ANNOTATE = 12;
        public static final String MULTIPART_FORM_DATA = "multipart/form-data";
        // Storage permission (android.permission-group.STORAGE)
        public static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;
        public static final String TAG = "FeedbackActivity";
    }
}
