package ch.uzh.supersede.feedbacklibrary.services;

import android.app.Activity;
import android.util.Log;

public class ConfigurationRequestWrapper {
    private static final String TAG = "ConfigurationRequestWrapper";
    private String url;
    private String language;
    private String intermediateDialogText;
    private Activity startingActivity;
    private Class<?> activityToStart;
    private long applicationId;
    private long pullConfigurationId;

    private ConfigurationRequestWrapper() {
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

    public static class ConfigurationRequestWrapperBuilder {
        private String url;
        private String language;
        private String intermediateDialogText;
        private Activity startingActivity;
        private Class<?> activityToStart;
        private long applicationId;
        private long pullConfigurationId;

        public ConfigurationRequestWrapperBuilder(Activity startingActivity, Class<?> activityToStart) {
            this.startingActivity = startingActivity;
            this.activityToStart = activityToStart;
        }

        public ConfigurationRequestWrapperBuilder withApplicationId(long applicationId) {
            this.applicationId = applicationId;
            return this;
        }

        public ConfigurationRequestWrapperBuilder withPullConfigurationId(long pullConfigurationId) {
            this.pullConfigurationId = pullConfigurationId;
            return this;
        }

        public ConfigurationRequestWrapperBuilder withLanguage(String language) {
            this.language = language;
            return this;
        }

        public ConfigurationRequestWrapperBuilder withUrl(String url) {
            this.url = url;
            return this;
        }

        public ConfigurationRequestWrapperBuilder withIntermediateDialog(String intermediateDialogText) {
            this.intermediateDialogText = intermediateDialogText;
            return this;
        }

        public ConfigurationRequestWrapper build() {
            if (checkNullAndLog(url, "url") || checkNullAndLog(language, "language") || checkNullAndLog
                    (intermediateDialogText, "intermediateDialogText") || checkNullAndLog(activityToStart, "activityToStart")
                    || checkNullAndLog(startingActivity, "startingActivity") || checkNullAndLog(applicationId, "applicationId")
                    || checkNullAndLog(pullConfigurationId, "pullConfigurationId")) {
                throw new RuntimeException("Could not create ConfigurationRequestWrapper!");
            }
            ConfigurationRequestWrapper wrapper = new ConfigurationRequestWrapper();
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
                Log.e(TAG, "Could not create ConfigurationRequestWrapper because " + label + " is null.");
                return true;
            }
            return false;
        }
    }
}
