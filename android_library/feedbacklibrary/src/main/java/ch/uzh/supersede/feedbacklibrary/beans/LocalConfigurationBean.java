package ch.uzh.supersede.feedbacklibrary.beans;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import java.io.Serializable;
import java.util.Locale;

import ch.uzh.supersede.feedbacklibrary.entrypoint.*;
import ch.uzh.supersede.feedbacklibrary.entrypoint.IFeedbackStyleConfiguration.FEEDBACK_STYLE;
import ch.uzh.supersede.feedbacklibrary.utils.DefaultConfiguration;
import ch.uzh.supersede.feedbacklibrary.utils.NumberUtility;

import static ch.uzh.supersede.feedbacklibrary.entrypoint.IFeedbackStyleConfiguration.FEEDBACK_STYLE.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.ActivitiesConstants.*;

public final class LocalConfigurationBean implements Serializable {
    // Application
    private String hostApplicationName;
    private String hostApplicationId;
    private Long hostApplicationLongId;
    private String hostApplicationLanguage;
    // IFeedbackBehaviorConfiguration
    private int pullIntervalMinutes;
    // IFeedbackDeveloperConfiguration
    private boolean isDeveloper;
    // IFeedbackLayoutConfiguration
    private String endpointUrl;
    private String endpointLogin;
    private String endpointPass;
    // IFeedbackLayoutConfiguration
    private int audioOrder;
    private double audioMaxTime;
    private int ratingOrder;
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
    private Integer[] topColors;
    private boolean isColoringVertical;

    private LocalConfigurationBean() {
        //nop
    }

    public LocalConfigurationBean(Activity activity, Integer[] topColors) {
        this.topColors = topColors;
        readDefaultConfiguration();
        if (activity instanceof ISimpleFeedbackConfiguration) {
            readFeedbackConfiguration((ISimpleFeedbackConfiguration) activity);
        }
        if (activity instanceof IFeedbackAudioConfiguration) {
            readFeedbackConfiguration((IFeedbackAudioConfiguration) activity);
        }
        if (activity instanceof IFeedbackBehaviorConfiguration) {
            readFeedbackConfiguration((IFeedbackBehaviorConfiguration) activity);
        }
        if (activity instanceof IFeedbackDeveloperConfiguration) {
            readFeedbackConfiguration((IFeedbackDeveloperConfiguration) activity);
        }
        if (activity instanceof IFeedbackEndpointConfiguration) {
            readFeedbackConfiguration((IFeedbackEndpointConfiguration) activity);
        }
        if (activity instanceof IFeedbackRatingConfiguration) {
            readFeedbackConfiguration((IFeedbackRatingConfiguration) activity);
        }
        if (activity instanceof IFeedbackScreenshotConfiguration) {
            readFeedbackConfiguration((IFeedbackScreenshotConfiguration) activity);
        }
        if (activity instanceof IFeedbackSettingsConfiguration) {
            readFeedbackConfiguration((IFeedbackSettingsConfiguration) activity);
        }
        if (activity instanceof IFeedbackStyleConfiguration) {
            readFeedbackConfiguration((IFeedbackStyleConfiguration) activity);
        }
        if (activity instanceof IFeedbackTitleAndTagConfiguration) {
            readFeedbackConfiguration((IFeedbackTitleAndTagConfiguration) activity);
        }
        hostApplicationName = getApplicationName(activity);
        hostApplicationId = getApplicationId(activity);
        hostApplicationLongId = NumberUtility.createApplicationIdFromString(hostApplicationId);
        hostApplicationLanguage = Locale.getDefault().getLanguage();
    }

    private static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    private static String getApplicationId(Context context) {
        return context.getPackageName().concat("." + getApplicationName(context)).toLowerCase();
    }

    private void readDefaultConfiguration() {
        //Rating
        this.ratingOrder = DefaultConfiguration.getInstance().getConfiguredRatingFeedbackOrder();
        this.ratingDefaultValue = DefaultConfiguration.getInstance().getConfiguredRatingFeedbackDefaultValue();
        this.ratingMaxValue = DefaultConfiguration.getInstance().getConfiguredRatingFeedbackMaxValue();
        //Text
        this.textOrder = DefaultConfiguration.getInstance().getConfiguredTextFeedbackOrder();
        this.textHint = DefaultConfiguration.getInstance().getConfiguredTextFeedbackHint();
        this.textLabel = DefaultConfiguration.getInstance().getConfiguredTextFeedbackLabel();
        this.textMaxLength = DefaultConfiguration.getInstance().getConfiguredTextFeedbackMaxLength();
        this.textMinLength = DefaultConfiguration.getInstance().getConfiguredTextFeedbackMinLength();
        //Screenshot
        this.screenshotOrder = DefaultConfiguration.getInstance().getConfiguredScreenshotFeedbackOrder();
        this.screenshotIsEditable = DefaultConfiguration.getInstance().getConfiguredScreenshotFeedbackIsEditable();
        //Audio
        this.audioOrder = DefaultConfiguration.getInstance().getConfiguredAudioFeedbackOrder();
        this.audioMaxTime = DefaultConfiguration.getInstance().getConfiguredAudioFeedbackMaxTime();
        //Behavior
        this.pullIntervalMinutes = DefaultConfiguration.getInstance().getConfiguredPullIntervalMinutes();
        //Developer
        this.isDeveloper = DefaultConfiguration.getInstance().isDeveloper();
        //Endpoint
        this.endpointUrl = DefaultConfiguration.getInstance().getConfiguredEndpointUrl();
        this.endpointLogin = DefaultConfiguration.getInstance().getConfiguredEndpointLogin();
        this.endpointPass = DefaultConfiguration.getInstance().getConfiguredEndpointPassword();
        //Settings
        this.minReportLength = DefaultConfiguration.getInstance().getConfiguredMinReportLength();
        this.maxReportLength = DefaultConfiguration.getInstance().getConfiguredMaxReportLength();
        this.minResponseLength = DefaultConfiguration.getInstance().getConfiguredMinResponseLength();
        this.maxResponseLength = DefaultConfiguration.getInstance().getConfiguredMaxResponseLength();
        this.minUserNameLength = DefaultConfiguration.getInstance().getConfiguredMinUserNameLength();
        this.maxUserNameLength = DefaultConfiguration.getInstance().getConfiguredMaxUserNameLength();
        this.reportEnabled = DefaultConfiguration.getInstance().getConfiguredReportEnabled();
        //Style
        this.style = DefaultConfiguration.getInstance().getConfiguredFeedbackStyle();
        //Title and Tag
        this.minTagLength = DefaultConfiguration.getInstance().getConfiguredMinTagLength();
        this.maxTagLength = DefaultConfiguration.getInstance().getConfiguredMaxTagLength();
        this.minTagNumber = DefaultConfiguration.getInstance().getConfiguredMinTagNumber();
        this.maxTagNumber = DefaultConfiguration.getInstance().getConfiguredMaxTagNumber();
        this.maxTagRecommendationNumber = DefaultConfiguration.getInstance().getConfiguredMaxTagRecommendationNumber();
        this.minTitleLength = DefaultConfiguration.getInstance().getConfiguredMinTitleLength();
        this.maxTitleLength = DefaultConfiguration.getInstance().getConfiguredMaxTitleLength();
    }

    private void readFeedbackConfiguration(ISimpleFeedbackConfiguration simpleFeedbackConfiguration) {
        this.audioOrder = simpleFeedbackConfiguration.getConfiguredAudioFeedbackOrder();
        this.ratingOrder = simpleFeedbackConfiguration.getConfiguredRatingFeedbackOrder();
        this.screenshotOrder = simpleFeedbackConfiguration.getConfiguredScreenshotFeedbackOrder();
        this.textOrder = simpleFeedbackConfiguration.getConfiguredTextFeedbackOrder();
    }

    private void readFeedbackConfiguration(IFeedbackAudioConfiguration audioConfiguration) {
        this.audioOrder = audioConfiguration.getConfiguredAudioFeedbackOrder();
        this.audioMaxTime = audioConfiguration.getConfiguredAudioFeedbackMaxTime();
    }

    private void readFeedbackConfiguration(IFeedbackRatingConfiguration ratingConfiguration) {
        this.ratingOrder = ratingConfiguration.getConfiguredRatingFeedbackOrder();
        this.ratingMaxValue = ratingConfiguration.getConfiguredRatingFeedbackMaxValue();
        this.ratingDefaultValue = ratingConfiguration.getConfiguredRatingFeedbackDefaultValue();
    }

    private void readFeedbackConfiguration(IFeedbackScreenshotConfiguration screenshotConfiguration) {
        this.screenshotOrder = screenshotConfiguration.getConfiguredScreenshotFeedbackOrder();
        this.screenshotIsEditable = screenshotConfiguration.getConfiguredScreenshotFeedbackIsEditable();
    }

    private void readFeedbackConfiguration(IFeedbackTitleAndTagConfiguration titleAndTagConfiguration) {
        this.minTitleLength = titleAndTagConfiguration.getConfiguredMinTitleLength();
        this.maxTitleLength = titleAndTagConfiguration.getConfiguredMaxTitleLength();
        this.minTagLength = titleAndTagConfiguration.getConfiguredMinTagLength();
        this.maxTagLength = titleAndTagConfiguration.getConfiguredMaxTagLength();
        this.minTagNumber = titleAndTagConfiguration.getConfiguredMinTagNumber();
        this.maxTagNumber = titleAndTagConfiguration.getConfiguredMaxTagNumber();
        this.maxTagRecommendationNumber = titleAndTagConfiguration.getConfiguredMaxTagRecommendationNumber();
    }

    private void readFeedbackConfiguration(IFeedbackSettingsConfiguration feedbackSettingsConfiguration) {
        this.minUserNameLength = feedbackSettingsConfiguration.getConfiguredMinUserNameLength();
        this.maxUserNameLength = feedbackSettingsConfiguration.getConfiguredMaxUserNameLength();
        this.minResponseLength = feedbackSettingsConfiguration.getConfiguredMinResponseLength();
        this.maxResponseLength = feedbackSettingsConfiguration.getConfiguredMaxResponseLength();
        this.minReportLength = feedbackSettingsConfiguration.getConfiguredMinReportLength();
        this.maxReportLength = feedbackSettingsConfiguration.getConfiguredMaxReportLength();
        this.reportEnabled = feedbackSettingsConfiguration.getConfiguredReportEnabled();
    }

    private void readFeedbackConfiguration(IFeedbackBehaviorConfiguration configuration) {
        this.pullIntervalMinutes = configuration.getConfiguredPullIntervalMinutes();
    }

    private void readFeedbackConfiguration(IFeedbackDeveloperConfiguration configuration) {
        this.isDeveloper = configuration.isDeveloper();
    }

    private void readFeedbackConfiguration(IFeedbackEndpointConfiguration configuration) {
        this.endpointUrl = configuration.getConfiguredEndpointUrl();
        this.endpointLogin = configuration.getConfiguredEndpointLogin();
        this.endpointPass = configuration.getConfiguredEndpointPassword();
    }

    private void readFeedbackConfiguration(IFeedbackStyleConfiguration configuration) {
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
                    SWISS_RED, WHITE, SWISS_RED
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
                    WIN95_GRAY, WIN95_BLUE, WHITE
            };
        } else if (style == CUSTOM) {
            int[] colors = configuration.getConfiguredCustomStyle();
            if (colors.length == 3) {
                topColors = new Integer[]{colors[0], colors[1], colors[2]};
            } else if (colors.length == 2) {
                topColors = new Integer[]{colors[0], colors[1]};
            } else {
                Log.e("Wrong Configuration", "Custom Styles must contain 2 or 3 colors! Fallback to Dark-Theme!");
                topColors = new Integer[]{
                        ANTHRACITE_DARK, GRAY_DARK, GRAY
                };
            }
        }
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

    public int getRatingOrder() {
        return ratingOrder;
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

    public Integer getLastColor() {
        return topColors[topColors.length - 1];
    }

    public boolean isColoringVertical() {
        return isColoringVertical;
    }

    public String getEndpointLogin() {
        return endpointLogin;
    }

    public String getEndpointPass() {
        return endpointPass;
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

    public String getEndpointUrl() {
        return endpointUrl;
    }

    public int getLastColorIndex() {
        return topColors.length - 1;
    }
}
