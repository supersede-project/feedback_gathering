package ch.uzh.supersede.feedbacklibrary.utils;

import android.graphics.Color;

import ch.uzh.supersede.feedbacklibrary.entrypoint.*;

public final class DefaultConfiguration implements
        IFeedbackBehaviorConfiguration,
        IFeedbackStyleConfiguration,
        IFeedbackSettingsConfiguration,
        IFeedbackDeveloperConfiguration,
        IFeedbackAudioConfiguration,
        IFeedbackRatingConfiguration,
        IFeedbackScreenshotConfiguration,
        IFeedbackTextConfiguration,
        IFeedbackTitleAndTagConfiguration,
        IFeedbackEndpointConfiguration {
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
        return 3;
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
        return FEEDBACK_STYLE.LIGHT;
    }

    @Override
    public int[] getConfiguredCustomStyle() {
        return new int[]{Color.WHITE,Color.BLACK,Color.WHITE};
    }

    @Override
    public double getConfiguredAudioFeedbackMaxTime() {
        return 15.0;
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
        return "Enter your description here...";
    }

    @Override
    public String getConfiguredTextFeedbackLabel() {
        return "Description";
    }

    @Override
    public int getConfiguredTextFeedbackMaxLength() {
        return 999;
    }

    @Override
    public int getConfiguredTextFeedbackMinLength() {
        return 10;
    }

    @Override
    public int getConfiguredPullIntervalMinutes() {
        return 5;
    }

    @Override
    public String getConfiguredEndpointUrl() {
        return "https://www.supersede.eu/";
    }

    @Override
    public String getConfiguredEndpointLogin() {
        return "username";
    }

    @Override
    public String getConfiguredEndpointPassword() {
        return "password";
    }
}
