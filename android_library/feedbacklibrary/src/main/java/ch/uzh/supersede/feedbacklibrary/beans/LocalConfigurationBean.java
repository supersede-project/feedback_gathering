package ch.uzh.supersede.feedbacklibrary.beans;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;

import java.io.Serializable;

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
    private int categoryOrder;
    private int textOrder;
    private int audioOrder;
    private int screenshotOrder;
    private int ratingOrder;
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
    private Integer[] topColors;

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
        this.categoryOrder = configuration.getConfiguredCategoryFeedbackOrder();
        this.textOrder = configuration.getConfiguredTextFeedbackOrder();
        this.audioOrder = configuration.getConfiguredAudioFeedbackOrder();
        this.screenshotOrder = configuration.getConfiguredScreenshotFeedbackOrder();
        this.ratingOrder = configuration.getConfiguredRatingFeedbackOrder();
    }

    private void readConfiguration(IFeedbackSettings configuration) {
        this.minUserNameLength = configuration.getConfiguredMinUserNameLength();
        this.maxUserNameLength = configuration.getConfiguredMaxUserNameLength();
        this.minResponseLength = configuration.getConfiguredMinResponseLength();
        this.maxResponseLength = configuration.getConfiguredMaxResponseLength();
    }

    private void readConfiguration(IFeedbackStyle configuration) {
        this.style = configuration.getConfiguredFeedbackStyle();
        if (style == DARK){
            topColors = new Integer[]{
                    ANTHRAZIT_DARK,GRAY_DARK
            };
        }else if (style == BRIGHT){
            topColors = new Integer[]{
                    GRAY,SILVER
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

    public boolean hasAtLeastNTopColors(int n){
        if (topColors.length < n){
            return false;
        }
        for (Integer i : topColors){
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

    public int getCategoryOrder() {
        return categoryOrder;
    }

    public int getTextOrder() {
        return textOrder;
    }

    public int getAudioOrder() {
        return audioOrder;
    }

    public int getRatingOrder() {
        return ratingOrder;
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

    public Integer[] getTopColors() {
        return topColors;
    }
}
