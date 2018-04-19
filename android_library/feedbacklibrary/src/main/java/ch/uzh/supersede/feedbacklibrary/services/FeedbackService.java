package ch.uzh.supersede.feedbacklibrary.services;

import android.content.Intent;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.api.IFeedbackAPI;
import ch.uzh.supersede.feedbacklibrary.configurations.ConfigurationItem;
import ch.uzh.supersede.feedbacklibrary.configurations.OrchestratorConfigurationItem;
import ch.uzh.supersede.feedbacklibrary.utils.DialogUtils;
import ch.uzh.supersede.feedbacklibrary.utils.Utils;
import ch.uzh.supersede.feedbacklibrary.beans.ConfigurationRequestBean;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener.EventType;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.ServicesConstants.FEEDBACK_SERVICE_TAG;

public class FeedbackService {
    private static FeedbackService instance;
    private static IFeedbackAPI feedbackAPI;

    private FeedbackService() {
    }

    public static FeedbackService getInstance() {
        if (instance == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(EXTRA_KEY_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            feedbackAPI = retrofit.create(IFeedbackAPI.class);
            instance = new FeedbackService();
        }
        return instance;
    }

    public void pingOrchestrator(IFeedbackServiceEventListener callback) {
        feedbackAPI.pingOrchestrator().enqueue(
                new FeedbackCallback<ResponseBody>(callback, EventType.PING_ORCHESTRATOR) {
                });
    }

    public void pingRepository(IFeedbackServiceEventListener callback) {
        feedbackAPI.pingRepository().enqueue(
                new FeedbackCallback<ResponseBody>(callback, EventType.PING_REPOSITORY) {
                });
    }

    public void createFeedbackVariant(IFeedbackServiceEventListener callback, String language, long applicationId, MultipartBody.Part feedback, List<MultipartBody.Part> files) {
        feedbackAPI.createFeedbackVariant(language, applicationId, feedback, files).enqueue(
                new FeedbackCallback<JsonObject>(callback, EventType.CREATE_FEEDBACK_VARIANT) {
                });
    }

    public void getConfiguration(IFeedbackServiceEventListener callback, ConfigurationRequestBean configurationRequestBean) {
        feedbackAPI.getConfiguration(configurationRequestBean.getLanguage(), configurationRequestBean.getApplicationId()).enqueue(
                new FeedbackCallback<OrchestratorConfigurationItem>(callback, EventType.GET_CONFIGURATION) {
                });
    }

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
