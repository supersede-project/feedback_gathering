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
    // IFeedbackBehaviorConfiguration
    private int pullIntervalMinutes;
    // IFeedbackDeveloperConfiguration
    private boolean isDeveloper;
    // IFeedbackLayoutConfiguration
    private int audioOrder;
    private double audioMaxTime;
    private int labelOrder;
    private int labelMaxCount;
    private int labelMinCount;
    private int ratingOrder;
    private String ratingTitle;
    private String ratingIcon;
    private int ratingMaxValue;
    private int ratingDefaultValue;
    private int screenshotOrder;
    private boolean screenshotIsEditable;
    private int textOrder;
    private String textHint;
    private String textLabel;
    private int textMaxLength;
    private int textMinLength;
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
    // Application
    private String hostApplicationName;
    private String hostApplicationId;
    private Long hostApplicationLongId;
    private String hostApplicationLanguage;
    private Integer[] topColors;
    private boolean coloringVertical;

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
        this.isDeveloper = configuration.isDeveloper();
    }

    private void readConfiguration(IFeedbackConfiguration configuration) {
        if (configuration instanceof ISimpleFeedbackConfiguration){
            ISimpleFeedbackConfiguration simpleFeedbackConfiguration = (ISimpleFeedbackConfiguration) configuration;
            this.audioOrder = simpleFeedbackConfiguration.getConfiguredAudioFeedbackOrder();
            this.audioMaxTime = DefaultConfiguration.getInstance().getConfiguredAudioFeedbackMaxTime();
            this.labelOrder = simpleFeedbackConfiguration.getConfiguredLabelFeedbackOrder();
            this.labelMaxCount = DefaultConfiguration.getInstance().getConfiguredLabelFeedbackMaxCount();
            this.labelMinCount = DefaultConfiguration.getInstance().getConfiguredLabelFeedbackMinCount();
            this.ratingOrder = simpleFeedbackConfiguration.getConfiguredRatingFeedbackOrder();
            this.ratingTitle = DefaultConfiguration.getInstance().getConfiguredRatingFeedbackTitle();
            this.ratingIcon = DefaultConfiguration.getInstance().getConfiguredRatingFeedbackIcon();
            this.ratingMaxValue = DefaultConfiguration.getInstance().getConfiguredRatingFeedbackMaxValue();
            this.ratingDefaultValue = DefaultConfiguration.getInstance().getConfiguredRatingFeedbackDefaultValue();
            this.screenshotOrder = simpleFeedbackConfiguration.getConfiguredScreenshotFeedbackOrder();
            this.screenshotIsEditable = DefaultConfiguration.getInstance().getConfiguredScreenshotFeedbackIsEditable();
            this.textOrder = simpleFeedbackConfiguration.getConfiguredTextFeedbackOrder();
            this.textHint = DefaultConfiguration.getInstance().getConfiguredTextFeedbackHint();
            this.textLabel = DefaultConfiguration.getInstance().getConfiguredTextFeedbackLabel();
            this.textMaxLength = DefaultConfiguration.getInstance().getConfiguredTextFeedbackMaxLength();
            this.textMinLength = DefaultConfiguration.getInstance().getConfiguredTextFeedbackMinLength();
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
                this.audioMaxTime = audioConfiguration.getConfiguredAudioFeedbackMaxTime();
        }
        if (configuration instanceof ILabelFeedbackConfiguration) {
                ILabelFeedbackConfiguration labelConfiguration = (ILabelFeedbackConfiguration) configuration;
                this.labelOrder = labelConfiguration.getConfiguredLabelFeedbackOrder();
                this.labelMaxCount = labelConfiguration.getConfiguredLabelFeedbackMaxCount();
                this.labelMinCount = labelConfiguration.getConfiguredLabelFeedbackMinCount();
        }
        if (configuration instanceof IRatingFeedbackConfiguration) {
                IRatingFeedbackConfiguration ratingConfiguration = (IRatingFeedbackConfiguration) configuration;
                this.ratingOrder = ratingConfiguration.getConfiguredRatingFeedbackOrder();
                this.ratingTitle = ratingConfiguration.getConfiguredRatingFeedbackTitle();
                this.ratingIcon = ratingConfiguration.getConfiguredRatingFeedbackIcon();
                this.ratingMaxValue = ratingConfiguration.getConfiguredRatingFeedbackMaxValue();
                this.ratingDefaultValue = ratingConfiguration.getConfiguredRatingFeedbackDefaultValue();
        }
        if (configuration instanceof IScreenshotFeedbackConfiguration) {
                IScreenshotFeedbackConfiguration screenshotConfiguration = (IScreenshotFeedbackConfiguration) configuration;
                this.screenshotOrder = screenshotConfiguration.getConfiguredScreenshotFeedbackOrder();
                this.screenshotIsEditable = screenshotConfiguration.getConfiguredScreenshotFeedbackIsEditable();
        }
        if (configuration instanceof ITextFeedbackConfiguration) {
                ITextFeedbackConfiguration textConfiguration = (ITextFeedbackConfiguration) configuration;
                this.textOrder = textConfiguration.getConfiguredTextFeedbackOrder();
                this.textHint = textConfiguration.getConfiguredTextFeedbackHint();
                this.textLabel = textConfiguration.getConfiguredTextFeedbackLabel();
                this.textMaxLength = textConfiguration.getConfiguredTextFeedbackMaxLength();
                this.textMinLength = textConfiguration.getConfiguredTextFeedbackMinLength();
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
        coloringVertical =true;
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
            coloringVertical = false;
            topColors = new Integer[]{
                    FRANCE_BLUE, WHITE, FRANCE_RED
            };
        } else if (style == ITALY) {
            coloringVertical = false;
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

    public int getPullIntervalMinutes() {
        return pullIntervalMinutes;
    }

    public boolean isDeveloper() {
        return isDeveloper;
    }

    public int getAudioOrder() {
        return audioOrder;
    }

    public double getAudioMaxTime() {
        return audioMaxTime;
    }

    public int getLabelOrder() {
        return labelOrder;
    }

    public int getLabelMaxCount() {
        return labelMaxCount;
    }

    public int getLabelMinCount() {
        return labelMinCount;
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

    public int getRatingMaxValue() {
        return ratingMaxValue;
    }

    public int getRatingDefaultValue() {
        return ratingDefaultValue;
    }

    public int getScreenshotOrder() {
        return screenshotOrder;
    }

    public boolean isScreenshotIsEditable() {
        return screenshotIsEditable;
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

    public int getTextMaxLength() {
        return textMaxLength;
    }

    public int getTextMinLength() {
        return textMinLength;
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
        return coloringVertical;
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
