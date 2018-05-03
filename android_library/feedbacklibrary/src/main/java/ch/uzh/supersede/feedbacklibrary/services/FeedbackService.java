package ch.uzh.supersede.feedbacklibrary.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.uzh.supersede.feedbacklibrary.BuildConfig;
import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.activities.AbstractBaseActivity;
import ch.uzh.supersede.feedbacklibrary.api.IFeedbackAPI;
import ch.uzh.supersede.feedbacklibrary.beans.ConfigurationRequestBean;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackBean;
import ch.uzh.supersede.feedbacklibrary.beans.LocalFeedbackBean;
import ch.uzh.supersede.feedbacklibrary.components.buttons.AbstractSettingsListItem;
import ch.uzh.supersede.feedbacklibrary.components.buttons.FeedbackListItem;
import ch.uzh.supersede.feedbacklibrary.components.buttons.SubscriptionsListItem;
import ch.uzh.supersede.feedbacklibrary.components.buttons.VotesListItem;
import ch.uzh.supersede.feedbacklibrary.configurations.ConfigurationItem;
import ch.uzh.supersede.feedbacklibrary.configurations.OrchestratorConfigurationItem;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.stubs.RepositoryStub;
import ch.uzh.supersede.feedbacklibrary.utils.DialogUtils;
import ch.uzh.supersede.feedbacklibrary.utils.Enums;
import ch.uzh.supersede.feedbacklibrary.utils.Utils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener.EventType;
import static ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener.EventType.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.ServicesConstants.FEEDBACK_SERVICE_TAG;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.FETCH_MODE.*;

/**
 * Singleton class that returns the original {@link FeedbackApiService} with its functions, defined in {@link IFeedbackAPI} iff {@code BuildConfig.DEBUG} is enabled, otherwise
 * {@link FeedbackMockService} will be used instead. Classes that use this Service will have to implement {@link IFeedbackServiceEventListener} and its functions in order to handle the
 * asynchronous events accordingly.
 */
public abstract class FeedbackService {
    private static FeedbackService instance;
    private static IFeedbackAPI feedbackAPI;

    private FeedbackService() {
    }

    public static FeedbackService getInstance() {
        if (BuildConfig.DEBUG) {
            if (instance == null) {
                instance = new FeedbackMockService();
            }
            return instance;
        }
        if (instance == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(EXTRA_KEY_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            feedbackAPI = retrofit.create(IFeedbackAPI.class);
            instance = new FeedbackApiService();
        }
        return instance;
    }

    public abstract void pingOrchestrator(IFeedbackServiceEventListener callback);

    public abstract void pingRepository(IFeedbackServiceEventListener callback);

    public abstract void createFeedbackVariant(IFeedbackServiceEventListener callback, Activity activity, String language, long applicationId, FeedbackBean feedbackBean, List<MultipartBody.Part> files);

    public abstract void getConfiguration(IFeedbackServiceEventListener callback, ConfigurationRequestBean configurationRequestBean);

    public abstract void execImportConfigurationAndStartActivity(ConfigurationRequestBean configurationRequestBean, Response<OrchestratorConfigurationItem> response);

    public abstract void getMineFeedbackVotes(IFeedbackServiceEventListener callback, Activity activity);

    public abstract void getOthersFeedbackVotes(IFeedbackServiceEventListener callback, Activity activity);

    public abstract void getFeedbackSettings(IFeedbackServiceEventListener callback, Activity activity);

    public abstract void createSubscription(IFeedbackServiceEventListener callback, Context context, FeedbackBean feedbackBean, boolean isChecked);

    private static class FeedbackApiService extends FeedbackService {
        @Override
        public void pingOrchestrator(IFeedbackServiceEventListener callback) {
            feedbackAPI.pingOrchestrator().enqueue(
                    new FeedbackCallback<ResponseBody>(callback, EventType.PING_ORCHESTRATOR) {
                    });
        }

        @Override
        public void pingRepository(IFeedbackServiceEventListener callback) {
            feedbackAPI.pingRepository().enqueue(
                    new FeedbackCallback<ResponseBody>(callback, EventType.PING_REPOSITORY) {
                    });
        }

        @Override
        public void createFeedbackVariant(IFeedbackServiceEventListener callback, Activity activity, String language, long applicationId, FeedbackBean feedbackBean, List<MultipartBody.Part> files) {
            // The JSON string of the feedback
            GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithoutExposeAnnotation();
            builder.serializeNulls();
            Gson gson = builder.create();

            String jsonString = gson.toJson(feedbackBean);
            MultipartBody.Part jsonPart = MultipartBody.Part.createFormData("json", "json", RequestBody.create(MediaType.parse("application/json"), jsonString.getBytes()));

            feedbackAPI.createFeedbackVariant(language, applicationId, jsonPart, files).enqueue(
                    new FeedbackCallback<JsonObject>(callback, EventType.CREATE_FEEDBACK_VARIANT) {
                    });
        }

        @Override
        public void getConfiguration(IFeedbackServiceEventListener callback, ConfigurationRequestBean configurationRequestBean) {
            feedbackAPI.getConfiguration(configurationRequestBean.getLanguage(), configurationRequestBean.getApplicationId()).enqueue(
                    new FeedbackCallback<OrchestratorConfigurationItem>(callback, EventType.GET_CONFIGURATION) {
                    });
        }

        @Override
        public void execImportConfigurationAndStartActivity(ConfigurationRequestBean configurationRequestBean, Response<OrchestratorConfigurationItem> response) {
            if (response == null || response.body() == null) {
                return;
            }

            OrchestratorConfigurationItem configuration = response.body();
            List<ConfigurationItem> configurationItems = configuration.getConfigurationItems();
            long selectedPullConfigurationIndex = -1;
            ConfigurationItem selectedConfigurationItem = null;
            Log.d(FEEDBACK_SERVICE_TAG, " Application with ID [" + configurationRequestBean.getApplicationId() + "] has " + configurationItems.size() + "  configurations!");
            for (ConfigurationItem configurationItem : configurationItems) {
                if (configurationItem
                        .getType()
                        .equals("PULL") && configurationItem.getId() == configurationRequestBean
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

                Intent intent = createFeedbackIntentFromPull(configurationRequestBean, jsonConfiguration, selectedPullConfigurationIndex);

                if (!showIntermediateDialog) {
                    // Start the feedback activity without asking the user
                    configurationRequestBean
                            .getStartingActivity()
                            .startActivity(intent);
                } else {
                    // Ask the user if he would like to give feedback or not
                    DialogUtils.PullFeedbackIntermediateDialog d = DialogUtils.PullFeedbackIntermediateDialog.newInstance(configurationRequestBean.getIntermediateDialogText(), jsonConfiguration,
                            selectedPullConfigurationIndex, configurationRequestBean.getUrl(), configurationRequestBean.getLanguage());
                    d.show(configurationRequestBean.getStartingActivity().getFragmentManager(), "feedbackPopupDialog");
                }
            } else {
                DialogUtils.showInformationDialog(configurationRequestBean.getStartingActivity(), new String[]{configurationRequestBean
                        .getStartingActivity()
                        .getResources().getString(R.string.info_application_unavailable)}, true);
            }
        }

        @Override
        public void getMineFeedbackVotes(IFeedbackServiceEventListener callback, Activity activity) {
            //TODO [jfo] implement
        }

        @Override
        public void getOthersFeedbackVotes(IFeedbackServiceEventListener callback, Activity activity) {
            //TODO [jfo] implement
        }

        @Override
        public void getFeedbackSettings(IFeedbackServiceEventListener callback, Activity activity) {
            //TODO [jfo] implement
        }

        @Override
        public void createSubscription(IFeedbackServiceEventListener callback, Context context, FeedbackBean feedbackBean, boolean isChecked) {
            //TODO [jfo] implement
        }

        private boolean isShowIntermediateDialog(boolean showIntermediateDialog, Map<String, Object> parameter, String key) {
            if (key.equals("showIntermediateDialog")) {
                showIntermediateDialog = Utils.intToBool(Integer.valueOf(parameter.get("value").toString()));
            }
            return showIntermediateDialog;
        }

        /**
         * Starts an activity with the orchestrator-configuration
         */
        private static Intent createFeedbackIntentFromPull(ConfigurationRequestBean configWrapper, String jsonConfiguration, long selectedPullConfigurationIndex) {
            Intent intent = new Intent(configWrapper.getStartingActivity(), configWrapper.getActivityToStart());
            intent.putExtra(IS_PUSH_STRING, false);
            intent.putExtra(JSON_CONFIGURATION_STRING, jsonConfiguration);
            intent.putExtra(SELECTED_PULL_CONFIGURATION_INDEX, selectedPullConfigurationIndex);
            intent.putExtra(EXTRA_KEY_BASE_URL, configWrapper.getUrl());
            intent.putExtra(EXTRA_KEY_LANGUAGE, configWrapper.getLanguage());
            return intent;
        }
    }

    private static class FeedbackMockService extends FeedbackService {
        @Override
        public void pingOrchestrator(IFeedbackServiceEventListener callback) {
            callback.onEventCompleted(PING_ORCHESTRATOR, null);
        }

        @Override
        public void pingRepository(IFeedbackServiceEventListener callback) {
            callback.onEventCompleted(PING_REPOSITORY, null);
        }

        @Override
        public void createFeedbackVariant(IFeedbackServiceEventListener callback, Activity activity, String language, long applicationId, FeedbackBean feedback, List<MultipartBody.Part> files) {
            FeedbackDatabase.getInstance(activity).writeFeedback(feedback, Enums.SAVE_MODE.CREATED);
            callback.onEventCompleted(CREATE_FEEDBACK_VARIANT, null);
        }

        @Override
        public void getConfiguration(IFeedbackServiceEventListener callback, ConfigurationRequestBean configurationRequestBean) {
            callback.onEventCompleted(GET_CONFIGURATION, null);
        }

        @Override
        public void execImportConfigurationAndStartActivity(ConfigurationRequestBean configurationRequestBean, Response<OrchestratorConfigurationItem> response) {
            //TODO [jfo] probably not needed
        }

        @Override
        public void getMineFeedbackVotes(IFeedbackServiceEventListener callback, Activity activity) {
            ArrayList<AbstractSettingsListItem> feedbackList = new ArrayList<>();
            for (LocalFeedbackBean feedback : FeedbackDatabase.getInstance(activity).getFeedbackBeans(VOTED)) {
                feedbackList.add(new VotesListItem(activity, 8, feedback));
            }
            callback.onEventCompleted(GET_MINE_FEEDBACK_VOTES, feedbackList);
        }

        @Override
        public void getOthersFeedbackVotes(IFeedbackServiceEventListener callback, Activity activity) {
            ArrayList<FeedbackListItem> feedbackList = new ArrayList<>();

            for (LocalFeedbackBean localFeedbackBean : FeedbackDatabase.getInstance(activity).getFeedbackBeans(OWN)) {
                feedbackList.add(new FeedbackListItem(activity, 8, RepositoryStub.getFeedback(activity, localFeedbackBean), ((AbstractBaseActivity)activity).getConfiguration()));
            }

            callback.onEventCompleted(GET_OTHERS_FEEDBACK_VOTES, feedbackList);
        }

        @Override
        public void getFeedbackSettings(IFeedbackServiceEventListener callback, Activity activity) {
            ArrayList<AbstractSettingsListItem> feedbackList = new ArrayList<>();
            for (LocalFeedbackBean feedback : FeedbackDatabase.getInstance(activity).getFeedbackBeans(SUBSCRIBED)) {
                feedbackList.add(new SubscriptionsListItem(activity, 8, feedback));
            }
            callback.onEventCompleted(GET_FEEDBACK_SETTINGS, feedbackList);
        }

        @Override
        public void createSubscription(IFeedbackServiceEventListener callback, Context context, FeedbackBean feedbackBean, boolean isChecked) {
            RepositoryStub.sendSubscriptionChange(context, feedbackBean, isChecked);
            callback.onEventCompleted(CREATE_SUBSCRIPTION, FeedbackDatabase.getInstance(context).getFeedbackState(feedbackBean));
        }
    }
}
