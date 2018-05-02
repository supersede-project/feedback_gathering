package ch.uzh.supersede.feedbacklibrary.beans;

import android.app.Activity;
import android.util.Log;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.ServicesConstants.CONFIGURATION_REAUEST_WRAPPER_TAG;


public class ConfigurationRequestBean {
    private String url;
    private String language;
    private String intermediateDialogText;
    private Activity startingActivity;
    private Class<?> activityToStart;
    private long applicationId;
    private long pullConfigurationId;

    private ConfigurationRequestBean() {
    }

    public String getUrl() {
        return url;
    }

    public String getLanguage() {
        return language;
    }

    public String getIntermediateDialogText() {
        return intermediateDialogText;
    }

    public Activity getStartingActivity() {
        return startingActivity;
    }

    public Class<?> getActivityToStart() {
        return activityToStart;
    }

    public long getApplicationId() {
        return applicationId;
    }

    public long getPullConfigurationId() {
        return pullConfigurationId;
    }

    public static class Builder {
        private String url;
        private String language;
        private String intermediateDialogText;
        private Activity startingActivity;
        private Class<?> activityToStart;
        private long applicationId;
        private long pullConfigurationId;

        public Builder(Activity startingActivity, Class<?> activityToStart) {
            this.startingActivity = startingActivity;
            this.activityToStart = activityToStart;
        }

        public Builder withApplicationId(long applicationId) {
            this.applicationId = applicationId;
            return this;
        }

        public Builder withPullConfigurationId(long pullConfigurationId) {
            this.pullConfigurationId = pullConfigurationId;
            return this;
        }

        public Builder withLanguage(String language) {
            this.language = language;
            return this;
        }

        public Builder withUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder withIntermediateDialog(String intermediateDialogText) {
            this.intermediateDialogText = intermediateDialogText;
            return this;
        }

        public ConfigurationRequestBean build() {
            if (checkNullAndLog(url, "url") || checkNullAndLog(language, "language") || checkNullAndLog
                    (intermediateDialogText, "intermediateDialogText") || checkNullAndLog(activityToStart, "activityToStart")
                    || checkNullAndLog(startingActivity, "startingActivity") || checkNullAndLog(applicationId, "applicationId")
                    || checkNullAndLog(pullConfigurationId, "pullConfigurationId")) {
                throw new RuntimeException("Could not create ConfigurationRequestBean!");
            }
            ConfigurationRequestBean wrapper = new ConfigurationRequestBean();
            wrapper.url = this.url;
            wrapper.language = this.language;
            wrapper.intermediateDialogText = this.intermediateDialogText;
            wrapper.startingActivity = this.startingActivity;
            wrapper.activityToStart = this.activityToStart;
            wrapper.applicationId = this.applicationId;
            wrapper.pullConfigurationId = this.pullConfigurationId;
            return wrapper;
        }

        private boolean checkNullAndLog(Object o, String label) {
            if (o == null) {
                Log.e(CONFIGURATION_REAUEST_WRAPPER_TAG, "Could not create ConfigurationRequestBean because " + label + " is null.");
                return true;
            }
            return false;
        }
    }
}
