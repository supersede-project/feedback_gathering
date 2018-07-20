package ch.uzh.supersede.feedbacklibrary.utils;

import android.app.Activity;
import android.content.*;

import ch.uzh.supersede.feedbacklibrary.beans.LocalConfigurationBean;
import ch.uzh.supersede.feedbacklibrary.entrypoint.*;
import ch.uzh.supersede.feedbacklibrary.entrypoint.IFeedbackStyleConfiguration;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.EXTRA_KEY_APPLICATION_CONFIGURATION;

public class DefaultConfiguration implements
        IFeedbackBehaviorConfiguration,
        IFeedbackStyleConfiguration,
        IFeedbackSettingsConfiguration,
        IFeedbackDeveloperConfiguration,
        IFeedbackAudioConfiguration,
        IFeedbackLabelConfiguration,
        IFeedbackRatingConfiguration,
        IFeedbackScreenshotConfiguration,
        IFeedbackTextConfiguration,
        IFeedbackTitleAndTagConfiguration,
        IFeedbackEndpointConfiguration{
    private static DefaultConfiguration defaultConfiguration;

    private DefaultConfiguration() {
    }

    public static DefaultConfiguration getInstance() {
        if (defaultConfiguration == null) {
            defaultConfiguration = new DefaultConfiguration();
        }
        return defaultConfiguration;
    }

    //ORDERS

    @Override
    public int getConfiguredLabelFeedbackOrder() {
        return -1;
    }

    @Override
    public int getConfiguredRatingFeedbackOrder() {
        return 1;
    }

    @Override
    public int getConfiguredTextFeedbackOrder() {
        return 2;
    }

    @Override
    public int getConfiguredScreenshotFeedbackOrder() {
        return 3;
    }

    @Override
    public int getConfiguredAudioFeedbackOrder() {
        return 4;
    }

    @Override
    public boolean isDeveloper() {
        return false;
    }

    @Override
    public int getConfiguredMinUserNameLength() {
        return 5;
    }

    @Override
    public int getConfiguredMaxUserNameLength() {
        return 35;
    }

    @Override
    public int getConfiguredMinResponseLength() {
        return 10;
    }

    @Override
    public int getConfiguredMaxResponseLength() {
        return 255;
    }

    @Override
    public int getConfiguredMinReportLength() {
        return 10;
    }

    @Override
    public int getConfiguredMaxReportLength() {
        return 255;
    }

    @Override
    public boolean getConfiguredReportEnabled() {
        return true;
    }

    @Override
    public int getConfiguredMinTitleLength() {
        return 5;
    }

    @Override
    public int getConfiguredMaxTitleLength() {
        return 100;
    }

    @Override
    public int getConfiguredMinTagLength() {
        return 3;
    }

    @Override
    public int getConfiguredMaxTagLength() {
        return 20;
    }

    @Override
    public int getConfiguredMinTagNumber() {
        return 1;
    }

    @Override
    public int getConfiguredMaxTagNumber() {
        return 5;
    }

    @Override
    public int getConfiguredMaxTagRecommendationNumber() {
        return 5;
    }

    @Override
    public FEEDBACK_STYLE getConfiguredFeedbackStyle() {
        return null;
    }

    @Override
    public double getConfiguredAudioFeedbackMaxTime() {
        return 15.0;
    }

    @Override
    public int getConfiguredLabelFeedbackMaxCount() {
        return 5;
    }

    @Override
    public int getConfiguredLabelFeedbackMinCount() {
        return 2;
    }

    @Override
    public String getConfiguredRatingFeedbackTitle() {
        return "Please select a Rating";
    }

    @Override
    public String getConfiguredRatingFeedbackIcon() {
        return "";
    }

    @Override
    public int getConfiguredRatingFeedbackMaxValue() {
        return 5;
    }

    @Override
    public int getConfiguredRatingFeedbackDefaultValue() {
        return 3;
    }

    @Override
    public boolean getConfiguredScreenshotFeedbackIsEditable() {
        return true;
    }

    @Override
    public String getConfiguredTextFeedbackHint() {
        return "Enter a Feedback-Text here...";
    }

    @Override
    public String getConfiguredTextFeedbackLabel() {
        return "Text";
    }

    @Override
    public int getConfiguredTextFeedbackMaxLength() {
        return 999;
    }

    @Override
    public int getConfiguredTextFeedbackMinLength() {
        return 3;
    }

    @Override
    public int getConfiguredPullIntervalMinutes() {
        return 0;
    }

    @Override
    public String getConfiguredEndpointUrl() {
        return "https://platform.supersede.eu:8443/";
    }

    public static class DefaultConfigurationUtility {
        public static LocalConfigurationBean getConfigurationFromActivity(Activity activity){
            return getConfigurationFromIntent(activity.getIntent());
        }
        public static LocalConfigurationBean getConfigurationFromIntent(Intent intent){
            return (LocalConfigurationBean) intent.getSerializableExtra(EXTRA_KEY_APPLICATION_CONFIGURATION);
        }
    }
}
