package ch.uzh.supersede.feedbacklibrary.utils;

public final class Constants {
    public static final String EXTRA_KEY_APPLICATION_ID = "applicationId";
    public static final String EXTRA_KEY_BASE_URL = "baseURL";
    public static final String EXTRA_KEY_LANGUAGE = "language";

    public static final String ANNOTATED_IMAGE_NAME_WITHOUT_STICKERS = "annotatedImageWithoutStickers.png";
    public static final String ANNOTATED_IMAGE_NAME_WITH_STICKERS = "annotatedImageWithStickers.png";
    public static final String CONFIGURATION_DIR = "configDir";
    public static final String DEFAULT_IMAGE_PATH = "defaultImagePath";
    public static final String IS_PUSH_STRING = "isPush";
    public static final String JSON_CONFIGURATION_FILE_NAME = "currentConfiguration.json";
    public static final String JSON_CONFIGURATION_STRING = "jsonConfigurationString";
    public static final String SELECTED_PULL_CONFIGURATION_INDEX_STRING = "selectedPullConfigurationIndex";
    public static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 101;  // Microphone permission (android.permission-group.MICROPHONE)

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

    public static final String PATH_DELIMITER = "/";
    public static final String SUPERSEEDE_BASE_URL = "https://platform.supersede.eu:8443/";
    private Constants(){}
}
