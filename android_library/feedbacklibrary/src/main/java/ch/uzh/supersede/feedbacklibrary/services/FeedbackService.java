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
import ch.uzh.supersede.feedbacklibrary.activities.FeedbackHubActivity;
import ch.uzh.supersede.feedbacklibrary.configurations.ConfigurationItem;
import ch.uzh.supersede.feedbacklibrary.configurations.OrchestratorConfigurationItem;
import ch.uzh.supersede.feedbacklibrary.utils.DialogUtils;
import ch.uzh.supersede.feedbacklibrary.utils.Utils;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener.EventType;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.FeedbackActivityConstants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.SUPERSEEDE_BASE_URL;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ACTIVE;

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

    public void pingOrchestrator(IFeedbackServiceEventListener callback) {
        feedbackAPI.pingOrchestrator().enqueue(new CallbackWrapper<ResponseBody>(callback, EventType.PING_ORCHESTRATOR) {
        });
    }

    public void pingRepository(IFeedbackServiceEventListener callback) {
        feedbackAPI.pingRepository().enqueue(new CallbackWrapper<ResponseBody>(callback, EventType.PING_REPOSITORY) {
        });
    }

    public void createFeedbackVariant(IFeedbackServiceEventListener callback, String language, long applicationId, MultipartBody.Part feedback, List<MultipartBody.Part> files) {
        feedbackAPI.createFeedbackVariant(language, applicationId, feedback, files).enqueue(new CallbackWrapper<JsonObject>(callback, EventType.CREATE_FEEDBACK_VARIANT) {
        });
    }

    /**
     * Opens the FeedbackActivity from the feedback library in case if a PULL feedback is triggered with a specific PULL configuration.
     */
    public void pullConfigurationAndStartActivity(ConfigurationRequestWrapper configurationRequestWrapper) {
        if (configurationRequestWrapper != null) {
            new AsyncOrchestratorConfigurationFetcher(configurationRequestWrapper).execute();
        }
    }

    //<Params, Progress, Result>
    private static class AsyncOrchestratorConfigurationFetcher extends AsyncTask<Void, Integer, Response<OrchestratorConfigurationItem>> {
        private ConfigurationRequestWrapper configurationRequestWrapper;

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
        protected void onPostExecute(Response<OrchestratorConfigurationItem> response) {
            if (response == null || response.body() == null) {
                return;
            }

            OrchestratorConfigurationItem configuration = response.body();
            List<ConfigurationItem> configurationItems = configuration.getConfigurationItems();
            long selectedPullConfigurationIndex = -1;
            ConfigurationItem selectedConfigurationItem = null;
            Log.d("APPLICATION-ID: " + configurationRequestWrapper.getApplicationId(), " APPLICATION-ID " + configurationRequestWrapper.getApplicationId() + " HAT " + configurationItems.size() + " " +
                    "CONFIGURATIONS!");
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

            if (selectedPullConfigurationIndex != -1 && selectedConfigurationItem != null) {
                // If no "showIntermediateDialog" is provided, show it
                boolean showIntermediateDialog = true;
                for (Map<String, Object> parameter : selectedConfigurationItem.getGeneralConfigurationItem().getParameters()) {
                    String key = (String) parameter.get("key");
                    showIntermediateDialog = isShowIntermediateDialog(showIntermediateDialog, parameter, key);
                }

                String jsonConfiguration = new GsonBuilder()
                        .setLenient()
                        .create()
                        .toJson(configuration);
                //Notabene, hier wird die ganze Konfiguration verwendet, nicht Bruchstuecke..

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
                DialogUtils.showInformationDialog(configurationRequestWrapper.getStartingActivity(), new String[]{configurationRequestWrapper
                        .getStartingActivity()
                        .getResources().getString(R.string.supersede_feedbacklibrary_feedback_application_unavailable_text)}, true);
            }
        }

        private boolean isShowIntermediateDialog(boolean showIntermediateDialog, Map<String, Object> parameter, String key) {
            if (key.equals("showIntermediateDialog")) {
                //TODO: [mbo] Refactor this casting-mess
                showIntermediateDialog = (Utils.intToBool(((Double) parameter.get("value")).intValue()));
            }
            return showIntermediateDialog;
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
    public void startFeedbackHubWithScreenshotCapture(@NonNull final String baseURL, @NonNull final Activity activity, final long applicationId, @NonNull final String language) {
        Intent intent = new Intent(activity, FeedbackHubActivity.class);
        if (ACTIVE.check(activity)) {
            Utils.wipeImages(activity.getApplicationContext());
            Utils.storeScreenshotToDatabase(activity);
        } else {
            Utils.storeScreenshotToIntent(activity, intent);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(EXTRA_KEY_APPLICATION_ID, applicationId);
        intent.putExtra(EXTRA_KEY_LANGUAGE, language);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
                    Log.e(TAG, "Could not create ConfigurationRequestWrapper because " + label + " is null.");
                    return true;
                }
                return false;
            }
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
