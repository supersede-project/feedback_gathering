package ch.uzh.supersede.feedbacklibrary.beans;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.io.Serializable;
import java.util.Locale;

import ch.uzh.supersede.feedbacklibrary.entrypoint.*;
import ch.uzh.supersede.feedbacklibrary.entrypoint.IFeedbackStyleConfiguration.FEEDBACK_STYLE;
import ch.uzh.supersede.feedbacklibrary.utils.*;

import static ch.uzh.supersede.feedbacklibrary.entrypoint.IFeedbackStyleConfiguration.FEEDBACK_STYLE.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.ActivitiesConstants.*;

public class LocalConfigurationBean implements Serializable {
    // Application
    private String hostApplicationName;
    private String hostApplicationId;
    private Long hostApplicationLongId;
    private String hostApplicationLanguage;
    // IFeedbackBehaviorConfiguration
    private int pullIntervalMinutes;
    // IFeedbackDeveloperConfiguration
    private boolean isDeveloper;
    private String repositoryLogin;
    private String repositoryPass;
    // IFeedbackLayoutConfiguration
    private int audioOrder;
    private double maxAudioTime;
    private int labelOrder;
    private int maxLabelCount;
    private int minLabelCount;
    private int ratingOrder;
    private String ratingTitle;
    private String ratingIcon;
    private int maxRatingValue;
    private int defaultRatingValue;
    private int screenshotOrder;
    private boolean isScreenshotEditable;
    private int textOrder;
    private String textHint;
    private String textLabel;
    private int maxTextLength;
    private int minTextLength;
    // IFeedbackSettingsConfiguration
    private int minUserNameLength;
    private int maxUserNameLength;
    private int minResponseLength;
    private int maxResponseLength;
    private int minTitleLength;
    private int maxTitleLength;
    private int minTagLength;
    private int maxTagLength;
    private int minTagNumber;
    private int maxTagNumber;
    private int maxReportLength;
    private int minReportLength;
    private boolean reportEnabled;
    private int maxTagRecommendationNumber;
    // IFeedbackStyleConfiguration
    private FEEDBACK_STYLE style;
    private Integer[] topColors;
    private boolean isColoringVertical;

    private LocalConfigurationBean() {
        //nop
    }

    public LocalConfigurationBean(Activity activity, Integer[] topColors) {
        this.topColors = topColors;
        if (activity instanceof IFeedbackBehaviorConfiguration) {
            readConfiguration((IFeedbackBehaviorConfiguration) activity);
        }
        if (activity instanceof IFeedbackDeveloperConfiguration) {
            readConfiguration((IFeedbackDeveloperConfiguration) activity);
        }
        if (activity instanceof IFeedbackConfiguration) {
            readConfiguration((IFeedbackConfiguration) activity);
        }
        if (activity instanceof IFeedbackSettingsConfiguration) {
            readConfiguration((IFeedbackSettingsConfiguration) activity);
        }
        if (activity instanceof IFeedbackStyleConfiguration) {
            readConfiguration((IFeedbackStyleConfiguration) activity);
        }
        hostApplicationName = getApplicationName(activity);
        hostApplicationId = getApplicationId(activity);
        hostApplicationLongId = NumberUtility.createApplicationIdFromString(hostApplicationId);
        hostApplicationLanguage = Locale.getDefault().getLanguage();
    }

    private void readConfiguration(IFeedbackBehaviorConfiguration configuration) {
        this.pullIntervalMinutes = configuration.getConfiguredPullIntervalMinutes();
    }

    private void readConfiguration(IFeedbackDeveloperConfiguration configuration) {
        this.repositoryLogin = configuration.getConfiguredRepositoryLogin();
        this.repositoryPass = configuration.getConfiguredRepositoryPassword();
        this.isDeveloper = configuration.isDeveloper();
    }

    private void readConfiguration(IFeedbackConfiguration configuration) {
        if (configuration instanceof ISimpleFeedbackConfiguration) {
            ISimpleFeedbackConfiguration simpleFeedbackConfiguration = (ISimpleFeedbackConfiguration) configuration;
            this.audioOrder = simpleFeedbackConfiguration.getConfiguredAudioFeedbackOrder();
            this.maxAudioTime = DefaultConfiguration.getInstance().getConfiguredAudioFeedbackMaxTime();
            this.labelOrder = simpleFeedbackConfiguration.getConfiguredLabelFeedbackOrder();
            this.maxLabelCount = DefaultConfiguration.getInstance().getConfiguredLabelFeedbackMaxCount();
            this.minLabelCount = DefaultConfiguration.getInstance().getConfiguredLabelFeedbackMinCount();
            this.ratingOrder = simpleFeedbackConfiguration.getConfiguredRatingFeedbackOrder();
            this.ratingTitle = DefaultConfiguration.getInstance().getConfiguredRatingFeedbackTitle();
            this.ratingIcon = DefaultConfiguration.getInstance().getConfiguredRatingFeedbackIcon();
            this.maxRatingValue = DefaultConfiguration.getInstance().getConfiguredRatingFeedbackMaxValue();
            this.defaultRatingValue = DefaultConfiguration.getInstance().getConfiguredRatingFeedbackDefaultValue();
            this.screenshotOrder = simpleFeedbackConfiguration.getConfiguredScreenshotFeedbackOrder();
            this.isScreenshotEditable = DefaultConfiguration.getInstance().getConfiguredScreenshotFeedbackIsEditable();
            this.textOrder = simpleFeedbackConfiguration.getConfiguredTextFeedbackOrder();
            this.textHint = DefaultConfiguration.getInstance().getConfiguredTextFeedbackHint();
            this.textLabel = DefaultConfiguration.getInstance().getConfiguredTextFeedbackLabel();
            this.maxTextLength = DefaultConfiguration.getInstance().getConfiguredTextFeedbackMaxLength();
            this.minTextLength = DefaultConfiguration.getInstance().getConfiguredTextFeedbackMinLength();
            this.minTitleLength = DefaultConfiguration.getInstance().getConfiguredMinTitleLength();
            this.maxTitleLength = DefaultConfiguration.getInstance().getConfiguredMaxTitleLength();
            this.minTagLength = DefaultConfiguration.getInstance().getConfiguredMinTagLength();
            this.maxTagLength = DefaultConfiguration.getInstance().getConfiguredMaxTagLength();
            this.minTagNumber = DefaultConfiguration.getInstance().getConfiguredMinTagNumber();
            this.maxTagNumber = DefaultConfiguration.getInstance().getConfiguredMaxTagNumber();
            this.maxTagRecommendationNumber = DefaultConfiguration.getInstance().getConfiguredMaxTagRecommendationNumber();
        }
        if (configuration instanceof IAudioFeedbackConfiguration) {
            IAudioFeedbackConfiguration audioConfiguration = (IAudioFeedbackConfiguration) configuration;
            this.audioOrder = audioConfiguration.getConfiguredAudioFeedbackOrder();
            this.maxAudioTime = audioConfiguration.getConfiguredAudioFeedbackMaxTime();
        }
        if (configuration instanceof ILabelFeedbackConfiguration) {
            ILabelFeedbackConfiguration labelConfiguration = (ILabelFeedbackConfiguration) configuration;
            this.labelOrder = labelConfiguration.getConfiguredLabelFeedbackOrder();
            this.maxLabelCount = labelConfiguration.getConfiguredLabelFeedbackMaxCount();
            this.minLabelCount = labelConfiguration.getConfiguredLabelFeedbackMinCount();
        }
        if (configuration instanceof IRatingFeedbackConfiguration) {
            IRatingFeedbackConfiguration ratingConfiguration = (IRatingFeedbackConfiguration) configuration;
            this.ratingOrder = ratingConfiguration.getConfiguredRatingFeedbackOrder();
            this.ratingTitle = ratingConfiguration.getConfiguredRatingFeedbackTitle();
            this.ratingIcon = ratingConfiguration.getConfiguredRatingFeedbackIcon();
            this.maxRatingValue = ratingConfiguration.getConfiguredRatingFeedbackMaxValue();
            this.defaultRatingValue = ratingConfiguration.getConfiguredRatingFeedbackDefaultValue();
        }
        if (configuration instanceof IScreenshotFeedbackConfiguration) {
            IScreenshotFeedbackConfiguration screenshotConfiguration = (IScreenshotFeedbackConfiguration) configuration;
            this.screenshotOrder = screenshotConfiguration.getConfiguredScreenshotFeedbackOrder();
            this.isScreenshotEditable = screenshotConfiguration.getConfiguredScreenshotFeedbackIsEditable();
        }
        if (configuration instanceof ITextFeedbackConfiguration) {
            ITextFeedbackConfiguration textConfiguration = (ITextFeedbackConfiguration) configuration;
            this.textOrder = textConfiguration.getConfiguredTextFeedbackOrder();
            this.textHint = textConfiguration.getConfiguredTextFeedbackHint();
            this.textLabel = textConfiguration.getConfiguredTextFeedbackLabel();
            this.maxTextLength = textConfiguration.getConfiguredTextFeedbackMaxLength();
            this.minTextLength = textConfiguration.getConfiguredTextFeedbackMinLength();
        }
        if (configuration instanceof ITitleAndTagFeedbackConfiguration) {
            ITitleAndTagFeedbackConfiguration titleAndTagConfiguration = (ITitleAndTagFeedbackConfiguration) configuration;
            this.minTitleLength = titleAndTagConfiguration.getConfiguredMinTitleLength();
            this.maxTitleLength = titleAndTagConfiguration.getConfiguredMaxTitleLength();
            this.minTagLength = titleAndTagConfiguration.getConfiguredMinTagLength();
            this.maxTagLength = titleAndTagConfiguration.getConfiguredMaxTagLength();
            this.minTagNumber = titleAndTagConfiguration.getConfiguredMinTagNumber();
            this.maxTagNumber = titleAndTagConfiguration.getConfiguredMaxTagNumber();
            this.maxTagRecommendationNumber = titleAndTagConfiguration.getConfiguredMaxTagRecommendationNumber();
        }
    }


    private void readConfiguration(IFeedbackSettingsConfiguration configuration) {
        this.minUserNameLength = configuration.getConfiguredMinUserNameLength();
        this.maxUserNameLength = configuration.getConfiguredMaxUserNameLength();
        this.minResponseLength = configuration.getConfiguredMinResponseLength();
        this.maxResponseLength = configuration.getConfiguredMaxResponseLength();
        this.minReportLength = configuration.getConfiguredMinReportLength();
        this.maxReportLength = configuration.getConfiguredMaxReportLength();
        this.reportEnabled = configuration.getConfiguredReportEnabled();
    }

    private void readConfiguration(IFeedbackStyleConfiguration configuration) {
        this.style = configuration.getConfiguredFeedbackStyle();
        isColoringVertical = true;
        if (style == DARK) {
            topColors = new Integer[]{
                    ANTHRACITE_DARK, GRAY_DARK, GRAY
            };
        } else if (style == LIGHT) {
            topColors = new Integer[]{
                    GRAY, SILVER, WHITE
            };
        } else if (style == SWITZERLAND) {
            topColors = new Integer[]{
                    WHITE, SWISS_RED, WHITE
            };
        } else if (style == GERMANY) {
            topColors = new Integer[]{
                    BLACK, GERMANY_RED, GERMANY_GOLD
            };
        } else if (style == AUSTRIA) {
            topColors = new Integer[]{
                    AUSTRIA_RED, WHITE, AUSTRIA_RED
            };
        } else if (style == FRANCE) {
            isColoringVertical = false;
            topColors = new Integer[]{
                    FRANCE_BLUE, WHITE, FRANCE_RED
            };
        } else if (style == ITALY) {
            isColoringVertical = false;
            topColors = new Integer[]{
                    ITALY_GREEN, WHITE, ITALY_RED
            };
        } else if (style == YUGOSLAVIA) {
            topColors = new Integer[]{
                    YUGOSLAVIA_BLUE, WHITE, YUGOSLAVIA_RED
            };
        } else if (style == WINDOWS95) {
            topColors = new Integer[]{
                    WIN95_GRAY,WIN95_BLUE,WHITE
            };
        }
    }

    private static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    private static String getApplicationId(Context context) {
        return context.getPackageName().concat("." + getApplicationName(context)).toLowerCase();
    }

    public boolean hasAtLeastNTopColors(int n) {
        if (topColors.length < n) {
            return false;
        }
        for (Integer i : topColors) {
            if (i == null) {
                return false;
            }
        }
        return true;
    }

    public static class Builder {
        private String hostApplicationId;
        private Long hostApplicationLongId;
        private String hostApplicationName;
        private String hostApplicationLanguage;
        private Integer pullIntervalMinutes;
        private Boolean isDeveloper;
        private Integer audioOrder;
        private Double maxAudioTime;
        private Integer screenshotOrder;
        private Boolean isScreenshotEditable;
        private Integer ratingOrder;
        private String ratingTitle;
        private String ratingIcon;
        private Integer ratingDefaultValue;
        private Integer maxRatingValue;
        private Integer labelOrder;
        private Integer maxLabelCount;
        private Integer minLabelCount;
        private Integer textOrder;
        private String textLabel;
        private String textHint;
        private Integer maxTextLength;
        private Integer minTextLength;
        private Integer maxTagLength;
        private Integer minTagLength;
        private Integer maxTagNumber;
        private Integer minTagNumber;
        private Integer maxTagRecommendationNumber;
        private Integer maxTitleLength;
        private Integer minTitleLength;
        private Integer maxResponseLength;
        private Integer minResponseLength;
        private Integer maxUserNameLength;
        private Integer minUserNameLength;
        private FEEDBACK_STYLE style;
        private Boolean isColoringVertical;
        private Integer[] topColors;
        private String repositoryLogin;
        private String repositoryPassword;

        public Builder() {
            //nop
        }

        public Builder withHostApplicationId(String hostApplicationId) {
            this.hostApplicationId = hostApplicationId;
            return this;
        }

        public Builder withHostApplicationLongId(Long hostApplicationLongId) {
            this.hostApplicationLongId = hostApplicationLongId;
            return this;
        }

        public Builder withHostApplicationName(String hostApplicationName) {
            this.hostApplicationName = hostApplicationName;
            return this;
        }

        public Builder withHostApplicationLanguage(String hostApplicationLanguage) {
            this.hostApplicationLanguage = hostApplicationLanguage;
            return this;
        }

        public Builder withPullIntervalMinutes(Integer pullIntervalMinutes) {
            this.pullIntervalMinutes = pullIntervalMinutes;
            return this;
        }

        public Builder withIsDeveloper(Boolean isDeveloper) {
            this.isDeveloper = isDeveloper;
            return this;
        }

        public Builder withAudioOrder(Integer audioOrder) {
            this.audioOrder = audioOrder;
            return this;
        }

        public Builder withMaxAudioTime(Double maxAudioTime) {
            this.maxAudioTime = maxAudioTime;
            return this;
        }

        public Builder withScreenshotOrder(Integer screenshotOrder) {
            this.screenshotOrder = screenshotOrder;
            return this;
        }

        public Builder withIsScreenshotEditable(Boolean isScreenshotIsEditable) {
            this.isScreenshotEditable = isScreenshotIsEditable;
            return this;
        }

        public Builder withRatingOrder(Integer ratingOrder) {
            this.ratingOrder = ratingOrder;
            return this;
        }

        public Builder withRatingTitle(String ratingTitle) {
            this.ratingTitle = ratingTitle;
            return this;
        }

        public Builder withRatingIcon(String ratingIcon) {
            this.ratingIcon = ratingIcon;
            return this;
        }

        public Builder withRatingDefaultValue(Integer ratingDefaultValue) {
            this.ratingDefaultValue = ratingDefaultValue;
            return this;
        }

        public Builder withMaxRatingValue(Integer maxRatingValue) {
            this.maxRatingValue = maxRatingValue;
            return this;
        }

        public Builder withLabelOrder(Integer labelOrder) {
            this.labelOrder = labelOrder;
            return this;
        }

        public Builder withMaxLabelCount(Integer maxLabelCount) {
            this.maxLabelCount = maxLabelCount;
            return this;
        }

        public Builder withMinLabelCount(Integer minLabelCount) {
            this.minLabelCount = minLabelCount;
            return this;
        }

        public Builder withTextOrder(Integer textOrder) {
            this.textOrder = textOrder;
            return this;
        }

        public Builder withTextLabel(String textLabel) {
            this.textLabel = textLabel;
            return this;
        }

        public Builder withTextHint(String textHint) {
            this.textHint = textHint;
            return this;
        }

        public Builder withMaxTextLength(Integer maxTextLength) {
            this.maxTextLength = maxTextLength;
            return this;
        }

        public Builder withMinTextLength(Integer minTextLength) {
            this.minTextLength = minTextLength;
            return this;
        }

        public Builder withMaxTagLength(Integer maxTagLength) {
            this.maxTagLength = maxTagLength;
            return this;
        }

        public Builder withMinTagLength(Integer minTagLength) {
            this.minTagLength = minTagLength;
            return this;
        }

        public Builder withMaxTagNumber(Integer maxTagNumber) {
            this.maxTagNumber = maxTagNumber;
            return this;
        }

        public Builder withMinTagNumber(Integer minTagNumber) {
            this.minTagNumber = minTagNumber;
            return this;
        }

        public Builder withMaxTagRecommendationNumber(Integer maxTagRecommendationNumber) {
            this.maxTagRecommendationNumber = maxTagRecommendationNumber;
            return this;
        }

        public Builder withMaxTitleLength(Integer maxTitleLength) {
            this.maxTitleLength = maxTitleLength;
            return this;
        }

        public Builder withMinTitleLength(Integer minTitleLength) {
            this.minTitleLength = minTitleLength;
            return this;
        }

        public Builder withMaxResponseLength(Integer maxResponseLength) {
            this.maxResponseLength = maxResponseLength;
            return this;
        }

        public Builder withMinResponseLength(Integer minResponseLength) {
            this.minResponseLength = minResponseLength;
            return this;
        }

        public Builder withMaxUserNameLength(Integer maxUserNameLength) {
            this.maxUserNameLength = maxUserNameLength;
            return this;
        }

        public Builder withMinUserNameLength(Integer minUserNameLength) {
            this.minUserNameLength = minUserNameLength;
            return this;
        }

        public Builder withStyle(FEEDBACK_STYLE style) {
            this.style = style;
            return this;
        }

        public Builder withIsColoringVertical(Boolean isColoringVertical) {
            this.isColoringVertical = isColoringVertical;
            return this;
        }

        public Builder withTopColors(Integer[] topColors) {
            this.topColors = topColors;
            return this;
        }

        public Builder withRepositoryLogin(String repositoryLogin) {
            this.repositoryLogin = repositoryLogin;
            return this;
        }

        public Builder withRepositoryPassword(String repositoryPassword) {
            this.repositoryPassword = repositoryPassword;
            return this;
        }

        public LocalConfigurationBean build() {
            if (CompareUtility.notNull(hostApplicationId, hostApplicationName, hostApplicationLanguage, topColors, repositoryLogin, repositoryPassword)) {
                LocalConfigurationBean config = new LocalConfigurationBean();
                config.hostApplicationId = this.hostApplicationId;
                config.hostApplicationLongId = this.hostApplicationLongId;
                config.hostApplicationName = this.hostApplicationName;
                config.hostApplicationLanguage = this.hostApplicationLanguage;
                config.pullIntervalMinutes = this.pullIntervalMinutes;
                config.isDeveloper = this.isDeveloper;
                config.audioOrder = this.audioOrder;
                config.maxAudioTime = this.maxAudioTime;
                config.screenshotOrder = this.screenshotOrder;
                config.isScreenshotEditable = this.isScreenshotEditable;
                config.ratingOrder = this.ratingOrder;
                config.ratingTitle = this.ratingTitle;
                config.ratingIcon = this.ratingIcon;
                config.defaultRatingValue = this.ratingDefaultValue;
                config.maxRatingValue = this.maxRatingValue;
                config.labelOrder = this.labelOrder;
                config.maxLabelCount = this.maxLabelCount;
                config.minLabelCount = this.minLabelCount;
                config.textOrder = this.textOrder;
                config.textLabel = this.textLabel;
                config.textHint = this.textHint;
                config.maxTextLength = this.maxTextLength;
                config.minTextLength = this.minTextLength;
                config.maxTagLength = this.maxTagLength;
                config.minTagLength = this.minTagLength;
                config.maxTagNumber = this.maxTagNumber;
                config.minTagNumber = this.minTagNumber;
                config.maxTagRecommendationNumber = this.maxTagRecommendationNumber;
                config.maxTitleLength = this.maxTitleLength;
                config.minTitleLength = this.minTitleLength;
                config.maxResponseLength = this.maxResponseLength;
                config.minResponseLength = this.minResponseLength;
                config.maxUserNameLength = this.maxUserNameLength;
                config.minUserNameLength = this.minUserNameLength;
                config.style = this.style;
                config.isColoringVertical = this.isColoringVertical;
                config.topColors = this.topColors;
                config.repositoryLogin = this.repositoryLogin;
                config.repositoryPass = this.repositoryPassword;
                return config;
            }
            return null;
        }
    }

    public int getPullIntervalMinutes() {
        return pullIntervalMinutes;
    }

    public boolean isDeveloper() {
        return isDeveloper;
    }

    public int getAudioOrder() {
        return audioOrder;
    }

    public double getMaxAudioTime() {
        return maxAudioTime;
    }

    public int getLabelOrder() {
        return labelOrder;
    }

    public int getMaxLabelCount() {
        return maxLabelCount;
    }

    public int getMinLabelCount() {
        return minLabelCount;
    }

    public int getRatingOrder() {
        return ratingOrder;
    }

    public String getRatingTitle() {
        return ratingTitle;
    }

    public String getRatingIcon() {
        return ratingIcon;
    }

    public int getMaxRatingValue() {
        return maxRatingValue;
    }

    public int getDefaultRatingValue() {
        return defaultRatingValue;
    }

    public int getScreenshotOrder() {
        return screenshotOrder;
    }

    public boolean isScreenshotEditable() {
        return isScreenshotEditable;
    }

    public int getTextOrder() {
        return textOrder;
    }

    public String getTextHint() {
        return textHint;
    }

    public String getTextLabel() {
        return textLabel;
    }

    public int getMaxTextLength() {
        return maxTextLength;
    }

    public int getMinTextLength() {
        return minTextLength;
    }

    public int getMinUserNameLength() {
        return minUserNameLength;
    }

    public int getMaxUserNameLength() {
        return maxUserNameLength;
    }

    public int getMinResponseLength() {
        return minResponseLength;
    }

    public int getMaxResponseLength() {
        return maxResponseLength;
    }

    public int getMinTagLength() {
        return minTagLength;
    }

    public int getMaxTagLength() {
        return maxTagLength;
    }

    public int getMinTagNumber() {
        return minTagNumber;
    }

    public int getMaxTagNumber() {
        return maxTagNumber;
    }

    public int getMaxTagRecommendationNumber() {
        return maxTagRecommendationNumber;
    }

    public int getMinTitleLength() {
        return minTitleLength;
    }

    public int getMaxTitleLength() {
        return maxTitleLength;
    }

    public FEEDBACK_STYLE getStyle() {
        return style;
    }

    public String getHostApplicationName() {
        return hostApplicationName;
    }

    public String getHostApplicationId() {
        return hostApplicationId;
    }

    public Long getHostApplicationLongId() {
        return hostApplicationLongId;
    }

    public String getHostApplicationLanguage() {
        return hostApplicationLanguage;
    }

    public Integer[] getTopColors() {
        return topColors;
    }

    public boolean isColoringVertical() {
        return isColoringVertical;
    }

    public String getRepositoryLogin(){
        return repositoryLogin;
    }

    public String getRepositoryPass(){
        return repositoryPass;
    }

    public int getMinReportLength() {
        return minReportLength;
    }

    public boolean isReportEnabled() {
        return reportEnabled;
    }

    public int getMaxReportLength() {
        return maxReportLength;
    }
}
