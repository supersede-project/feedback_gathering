package ch.uzh.supersede.feedbacklibrary.services;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import ch.uzh.supersede.feedbacklibrary.API.IFeedbackAPI;
import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.activities.FeedbackActivity;
import ch.uzh.supersede.feedbacklibrary.configurations.ConfigurationItem;
import ch.uzh.supersede.feedbacklibrary.configurations.OrchestratorConfigurationItem;
import ch.uzh.supersede.feedbacklibrary.utils.DialogUtils;
import ch.uzh.supersede.feedbacklibrary.utils.Utils;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.FeedbackActivityConstants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.OK_RESPONSE;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.SUPERSEEDE_BASE_URL;

public class FeedbackService {
    private static final String TAG = "FeedbackService";
    private static FeedbackService instance;
    private static IFeedbackAPI feedbackAPI;

    private FeedbackService() {
    }

    public static FeedbackService getInstance() {
        if (instance == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SUPERSEEDE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            feedbackAPI = retrofit.create(IFeedbackAPI.class);
            instance = new FeedbackService();
        }
        return instance;
    }

    public boolean pingOrchestrator() {
        try {
            Response<ResponseBody> response = feedbackAPI
                    .pingOrchestrator()
                    .execute();
            if (response.code() == OK_RESPONSE) {
                return true;
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return false;
    }

    public boolean pingRepository() {
        try {
            Response<ResponseBody> response = feedbackAPI
                    .pingRepository()
                    .execute();
            if (response.code() == OK_RESPONSE) {
                return true;
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return false;
    }

    public boolean createFeedbackVariant(String language, long applicationId, MultipartBody.Part feedback,
                                         List<MultipartBody.Part> files) {
        try {
            Response<JsonObject> response = feedbackAPI
                    .createFeedbackVariant(language, applicationId, feedback, files)
                    .execute();
            if (response.code() == OK_RESPONSE) {
                return true;
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return false;
    }

    /**
     * Opens the FeedbackActivity from the feedback library in case if a PULL feedback is triggered with a specific PULL configuration.
     */
    public void pullConfigurationAndStartActivity(ConfigurationRequestWrapper configurationRequestWrapper) {
        if (configurationRequestWrapper != null && pingOrchestrator()) {
            new AsyncOrchestratorConfigurationFetcher(configurationRequestWrapper).execute();
        }
    }


    //<Params, Progress, Result>
    private class AsyncOrchestratorConfigurationFetcher extends AsyncTask<Void, Integer, Response<OrchestratorConfigurationItem>> {
        private ConfigurationRequestWrapper configurationRequestWrapper;

        private AsyncOrchestratorConfigurationFetcher() {

        }

        public AsyncOrchestratorConfigurationFetcher(ConfigurationRequestWrapper configurationRequestWrapper) {
            this.configurationRequestWrapper = configurationRequestWrapper;
        }


        @Override
        protected Response<OrchestratorConfigurationItem> doInBackground(Void... voids) {
            if (configurationRequestWrapper != null) {
                try {
                    return feedbackAPI.getConfiguration(configurationRequestWrapper.getLanguage(), configurationRequestWrapper.getApplicationId()).execute();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            Log.e(TAG, "Could not get Configuration from Orchestrator");
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected void onPostExecute(Response<OrchestratorConfigurationItem> response) {
            if (response == null || response.body() == null) {
                return;
            }

            OrchestratorConfigurationItem configuration = response.body();
            List<ConfigurationItem> configurationItems = configuration.getConfigurationItems();
            long selectedPullConfigurationIndex = -1;
            ConfigurationItem selectedConfigurationItem = null;
            Log.d("APPLICATION-ID: "+configurationRequestWrapper.getApplicationId()," APPLICATION-ID "+configurationRequestWrapper.getApplicationId()+" HAT "+configurationItems.size()+ " CONFIGURATIONS!");
            for (ConfigurationItem configurationItem : configurationItems) {
                if (configurationItem
                        .getType()
                        .equals("PULL") && configurationItem.getId() == configurationRequestWrapper
                        .getPullConfigurationId()) {
                    selectedPullConfigurationIndex = configurationItem.getId();
                    selectedConfigurationItem = configurationItem;
                    break;
                }
            }

            if (selectedPullConfigurationIndex != -1) {
                // If no "showIntermediateDialog" is provided, show it
                boolean showIntermediateDialog = true;
                for (Map<String, Object> parameter : selectedConfigurationItem.getGeneralConfigurationItem().getParameters()) {
                    String key = (String) parameter.get("key");
                    // Intermediate dialog
                    if (key.equals("showIntermediateDialog")) {
                        //TODO: [mbo] Refactor this casting-mess
                        showIntermediateDialog = (Utils.intToBool(((Double) parameter.get("value")).intValue()));
                    }
                }

                String jsonConfiguration = new GsonBuilder()
                        .setLenient()
                        .create()
                        .toJson(configuration);

                Intent intent = createFeedbackIntentFromPull(configurationRequestWrapper.getUrl(),
                        configurationRequestWrapper.getStartingActivity(),
                        configurationRequestWrapper.getActivityToStart(),
                        jsonConfiguration,
                        configurationRequestWrapper.getLanguage(),
                        selectedPullConfigurationIndex);

                if (!showIntermediateDialog) {
                    // Start the feedback activity without asking the user
                    configurationRequestWrapper
                            .getStartingActivity()
                            .startActivity(intent);
                } else {
                    // Ask the user if he would like to give feedback or not
                    DialogUtils.PullFeedbackIntermediateDialog d = DialogUtils.PullFeedbackIntermediateDialog.newInstance(configurationRequestWrapper.getIntermediateDialogText(), jsonConfiguration,
                            selectedPullConfigurationIndex, configurationRequestWrapper.getUrl(), configurationRequestWrapper.getLanguage());
                    d.show(configurationRequestWrapper.getStartingActivity().getFragmentManager(), "feedbackPopupDialog");
                }
            } else {
                DialogUtils.showInformationDialog(configurationRequestWrapper.getStartingActivity(), new
                        String[]{configurationRequestWrapper.getStartingActivity().getResources().getString(
                                R.string.supersede_feedbacklibrary_feedback_application_unavailable_text)},true);
            }
        }
    }

    /**
     * Starts an activity with the orchestrator-configuration
     */
    public static Intent createFeedbackIntentFromPull(String baseURL, Activity activity, Class<?> activityToStart, String jsonConfiguration, String language, long selectedPullConfigurationIndex) {
        Intent intent = new Intent(activity, activityToStart);
        intent.putExtra(IS_PUSH_STRING, false);
        intent.putExtra(JSON_CONFIGURATION_STRING, jsonConfiguration);
        intent.putExtra(SELECTED_PULL_CONFIGURATION_INDEX_STRING, selectedPullConfigurationIndex);
        intent.putExtra(EXTRA_KEY_BASE_URL, baseURL);
        intent.putExtra(EXTRA_KEY_LANGUAGE, language);
        return intent;
    }

    /**
     * Takes a screenshot of the current screen automatically and opens the FeedbackActivity from the feedback library in case if a PUSH feedback is triggered.
     */
    public static void startActivityWithScreenshotCapture(@NonNull final String baseURL, @NonNull final Activity activity, final long applicationId, @NonNull final String language) {
        Intent intent = new Intent(activity, FeedbackActivity.class);
        String defaultImagePath = Utils.captureScreenshot(activity);
        intent.putExtra(DEFAULT_IMAGE_PATH, defaultImagePath);
        intent.putExtra(EXTRA_KEY_APPLICATION_ID, applicationId);
        intent.putExtra(EXTRA_KEY_BASE_URL, baseURL);
        intent.putExtra(EXTRA_KEY_LANGUAGE, language);
        activity.startActivity(intent);
    }
    public static class ConfigurationRequestWrapper {
        private String url;
        private String language;
        private String intermediateDialogText;
        private Activity startingActivity;
        private Class<?> activityToStart;
        private long applicationId;
        private long pullConfigurationId;

        private ConfigurationRequestWrapper() {

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
                    Log.e(TAG, "Could not create ConfigurationRequestWrapper because "+label+ " is null.");
                    return true;
                }
                return false;
            }
        }

        public ConfigurationRequestWrapper(String url, String language, Activity activity, long applicationId, long
                pullConfigurationId) {
            this.url = url;
            this.language = language;
            this.startingActivity = startingActivity;
            this.activityToStart = activityToStart;
            this.applicationId = applicationId;
            this.pullConfigurationId = pullConfigurationId;
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
    }
}
