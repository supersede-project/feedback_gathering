package ch.uzh.supersede.feedbacklibrary.beans;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.io.Serializable;
import java.util.ArrayList;

import ch.uzh.supersede.feedbacklibrary.entrypoint.*;
import ch.uzh.supersede.feedbacklibrary.entrypoint.IFeedbackStyle.FEEDBACK_STYLE;
import ch.uzh.supersede.feedbacklibrary.utils.NumberUtility;

import static ch.uzh.supersede.feedbacklibrary.entrypoint.IFeedbackStyle.FEEDBACK_STYLE.NONE;

public class LocalConfigurationBean implements Serializable {
    // IFeedbackBehavior
    private int pullIntervalMinutes;
    // IFeedbackDeveloper
    private boolean isDeveloper;
    // IFeedbackLayoutConfiguration
    private boolean isLabelEnabled;
    private boolean isTextEnabled;
    private boolean isAudioEnabled;
    private boolean isScreenshotEnabled;
    private boolean isAttachmentEnabled;
    private int labelOrder;
    private int textOrder;
    private int audioOrder;
    private int screenshotOrder;
    // IFeedbackSettings
    private int minUserNameLength;
    private int maxUserNameLength;
    private int minResponseLength;
    private int maxResponseLength;
    // IFeedbackStyle
    private FEEDBACK_STYLE style = NONE;
    // Application
    private String hostApplicationName;
    private String hostApplicationId;
    private Long hostApplicationLongId;

    private LocalConfigurationBean() {
    }

    public LocalConfigurationBean(Activity activity) {
        if (activity instanceof IFeedbackBehavior) {
            readConfiguration((IFeedbackBehavior) activity);
        }
        if (activity instanceof IFeedbackDeveloper) {
            readConfiguration((IFeedbackDeveloper) activity);
        }
        if (activity instanceof IFeedbackLayoutConfiguration) {
            readConfiguration((IFeedbackLayoutConfiguration) activity);
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
    }

    private void readConfiguration(IFeedbackBehavior configuration) {
        this.pullIntervalMinutes = configuration.getConfiguredPullIntervalMinutes();
    }

    private void readConfiguration(IFeedbackDeveloper configuration) {
        this.isDeveloper = configuration.isDeveloper();
    }

    private void readConfiguration(IFeedbackLayoutConfiguration configuration) {
        this.isLabelEnabled = configuration.isLabelFeedbackEnabled();
        this.isTextEnabled = configuration.isLabelFeedbackEnabled();
        this.isAudioEnabled = configuration.isLabelFeedbackEnabled();
        this.isScreenshotEnabled = configuration.isLabelFeedbackEnabled();
        this.isAttachmentEnabled = configuration.isLabelFeedbackEnabled();
        this.labelOrder = configuration.getConfiguredLabelFeedbackOrder();
        this.textOrder = configuration.getConfiguredTextFeedbackOrder();
        this.audioOrder = configuration.getConfiguredAudioFeedbackOrder();
        this.screenshotOrder = configuration.getConfiguredScreenshotFeedbackOrder();
    }

    private void readConfiguration(IFeedbackSettings configuration) {
        this.minUserNameLength = configuration.getConfiguredMinUserNameLength();
        this.maxUserNameLength = configuration.getConfiguredMaxUserNameLength();
        this.minResponseLength = configuration.getConfiguredMinResponseLength();
        this.maxResponseLength = configuration.getConfiguredMaxResponseLength();
    }

    private void readConfiguration(IFeedbackStyle configuration) {
        this.style = configuration.getConfiguredFeedbackStyle();
    }

    private static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    private static String getApplicationId(Context context) {
        return context.getPackageName().concat("." + getApplicationName(context)).toLowerCase();
    }


    public int getPullIntervalMinutes() {
        return pullIntervalMinutes;
    }

    public boolean isDeveloper() {
        return isDeveloper;
    }

    public boolean isLabelEnabled() {
        return isLabelEnabled;
    }

    public boolean isTextEnabled() {
        return isTextEnabled;
    }

    public boolean isAudioEnabled() {
        return isAudioEnabled;
    }

    public boolean isScreenshotEnabled() {
        return isScreenshotEnabled;
    }

    public boolean isAttachmentEnabled() {
        return isAttachmentEnabled;
    }

    public int getLabelOrder() {
        return labelOrder;
    }

    public int getTextOrder() {
        return textOrder;
    }

    public int getAudioOrder() {
        return audioOrder;
    }

    public int getScreenshotOrder() {
        return screenshotOrder;
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
}
