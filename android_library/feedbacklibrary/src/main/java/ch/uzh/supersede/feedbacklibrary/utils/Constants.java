package ch.uzh.supersede.feedbacklibrary.utils;

public class Constants {

    private Constants(){
    }

    public static final String PATH_DELIMITER = "/";
    public static final String SUPERSEEDE_BASE_URL = "https://platform.supersede.eu:8443/";

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

    // Audio
    public static final String AUDIO_DIR = "audioDir";
    public static final String AUDIO_EXTENSION = "m4a";
    public static final String AUDIO_FILENAME = "audioFile";

    // Screenshot
    public static final String EXTRA_KEY_ALL_STICKER_ANNOTATIONS = "allStickerAnnotations";
    public static final String EXTRA_KEY_ALL_TEXT_ANNOTATIONS = "allTextAnnotations";
    public static final String EXTRA_KEY_ANNOTATED_IMAGE_PATH_WITHOUT_STICKERS = "annotatedImagePathWithoutStickers";
    public static final String EXTRA_KEY_ANNOTATED_IMAGE_PATH_WITH_STICKERS = "annotatedImagePathWithStickers";
    public static final String EXTRA_KEY_HAS_STICKER_ANNOTATIONS = "hasStickerAnnotations";
    public static final String EXTRA_KEY_HAS_TEXT_ANNOTATIONS = "hasTextAnnotations";
    public static final String EXTRA_KEY_IMAGE_PATCH = "imagePath";
    public static final String EXTRA_KEY_MECHANISM_VIEW_ID = "mechanismViewID";
    public static final String SEPARATOR = "::;;::;;";
    public static final String TEXT_ANNOTATION_COUNTER_MAXIMUM = "textAnnotationCounterMaximum";
}
