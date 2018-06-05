package ch.uzh.supersede.feedbacklibrary.beans;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.io.Serializable;
import java.util.Locale;

import ch.uzh.supersede.feedbacklibrary.entrypoint.*;
import ch.uzh.supersede.feedbacklibrary.entrypoint.IFeedbackStyle.FEEDBACK_STYLE;
import ch.uzh.supersede.feedbacklibrary.utils.*;

import static ch.uzh.supersede.feedbacklibrary.entrypoint.IFeedbackStyle.FEEDBACK_STYLE.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.ActivitiesConstants.*;

public class LocalConfigurationBean implements Serializable {
    // IFeedbackBehavior
    private int pullIntervalMinutes;
    // IFeedbackDeveloper
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
    // IFeedbackSettings
    private int minUserNameLength;
    private int maxUserNameLength;
    private int minResponseLength;
    private int maxResponseLength;
    // IFeedbackStyle
    private FEEDBACK_STYLE style;
    // Application
    private String hostApplicationName;
    private String hostApplicationId;
    private Long hostApplicationLongId;
    private String hostApplicationLanguage;
    private Integer[] topColors;
    private boolean coloringVertical;

    private LocalConfigurationBean() {
    }

    public LocalConfigurationBean(Activity activity, Integer[] topColors) {
        this.topColors = topColors;
        if (activity instanceof IFeedbackBehavior) {
            readConfiguration((IFeedbackBehavior) activity);
        }
        if (activity instanceof IFeedbackDeveloper) {
            readConfiguration((IFeedbackDeveloper) activity);
        }
        if (activity instanceof IFeedbackConfiguration) {
            readConfiguration((IFeedbackConfiguration) activity);
        }
        if (activity instanceof IFeedbackSettings) {
            readConfiguration((IFeedbackSettings) activity);
        }
        if (activity instanceof IFeedbackStyle) {
            readConfiguration((IFeedbackStyle) activity);
        }
        hostApplicationName = getApplicationName(activity);
        hostApplicationId = getApplicationId(activity);
        hostApplicationLongId = NumberUtility.createApplicationIdFromString(hostApplicationId);
        hostApplicationLanguage = Locale.getDefault().getLanguage();
    }

    private void readConfiguration(IFeedbackBehavior configuration) {
        this.pullIntervalMinutes = configuration.getConfiguredPullIntervalMinutes();
    }

    private void readConfiguration(IFeedbackDeveloper configuration) {
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
        if (configuration instanceof ITitleFeedbackConfiguration) {
                //TODO
        }
    }


    private void readConfiguration(IFeedbackSettings configuration) {
        this.minUserNameLength = configuration.getConfiguredMinUserNameLength();
        this.maxUserNameLength = configuration.getConfiguredMaxUserNameLength();
        this.minResponseLength = configuration.getConfiguredMinResponseLength();
        this.maxResponseLength = configuration.getConfiguredMaxResponseLength();
    }

    private void readConfiguration(IFeedbackStyle configuration) {
        this.style = configuration.getConfiguredFeedbackStyle();
        coloringVertical =true;
        if (style == DARK) {
            topColors = new Integer[]{
                    ANTHRACITE_DARK, GRAY_DARK
            };
        } else if (style == LIGHT) {
            topColors = new Integer[]{
                    GRAY, SILVER
            };
        } else if (style == SWITZERLAND) {
            topColors = new Integer[]{
                    WHITE, SWISS_RED
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
                    WIN95_GRAY,WIN95_BLUE
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
}
