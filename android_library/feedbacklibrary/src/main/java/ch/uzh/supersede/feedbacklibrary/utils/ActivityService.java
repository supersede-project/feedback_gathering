package ch.uzh.supersede.feedbacklibrary.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ch.uzh.supersede.feedbacklibrary.FeedbackActivity;
import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.configurations.ConfigurationItem;
import ch.uzh.supersede.feedbacklibrary.configurations.OrchestratorConfigurationItem;
import retrofit2.Response;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.FeedbackActivityConstants.DEFAULT_IMAGE_PATH;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.FeedbackActivityConstants.EXTRA_KEY_APPLICATION_ID;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.FeedbackActivityConstants.EXTRA_KEY_BASE_URL;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.FeedbackActivityConstants.EXTRA_KEY_LANGUAGE;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.FeedbackActivityConstants.IS_PUSH_STRING;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.FeedbackActivityConstants.JSON_CONFIGURATION_STRING;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.FeedbackActivityConstants.SELECTED_PULL_CONFIGURATION_INDEX_STRING;

public class ActivityService {
    private static ActivityService instance;

    private ActivityService() {
    }

    public static ActivityService getInstance() {
        if (instance == null) {
            instance = new ActivityService();
        }
        return instance;
    }

    /**
     * Opens the FeedbackActivity from the feedback library in case if a PULL feedback is triggered with a random PULL configuration.
     */
    public void triggerRandomPullFeedback(@NonNull final String baseURL, @NonNull final Activity activity, final long applicationId, final @NonNull String language) {
        FeedbackService svc = FeedbackService.getInstance();
        svc.pingOrchestrator();
        Response<OrchestratorConfigurationItem> response = svc.getConfiguration(language, applicationId);

        OrchestratorConfigurationItem configuration = response.body();
        if (configuration != null) {
            List<ConfigurationItem> configurationItems = configuration.getConfigurationItems();
            List<Long> shuffleIds = new ArrayList<>();
            Map<Long, List<Map<String, Object>>> idParameters = new HashMap<>();
            for (ConfigurationItem configurationItem : configurationItems) {
                if (configurationItem.getType().equals("PULL")) {
                    shuffleIds.add(configurationItem.getId());
                    idParameters.put(configurationItem.getId(), configurationItem.getGeneralConfigurationItem().getParameters());
                }
            }

            Random rnd = new Random(System.nanoTime());
            Collections.shuffle(shuffleIds, rnd);
            for (int i = 0; i < shuffleIds.size(); ++i) {
                double likelihood = -1;
                // If no "showIntermediateDialog" is provided, show it
                boolean showIntermediateDialog = true;
                for (Map<String, Object> parameter : idParameters.get(shuffleIds.get(i))) {
                    String key = (String) parameter.get("key");
                    // Likelihood
                    if (key.equals("likelihood")) {
                        likelihood = Double.parseDouble((String) parameter.get("value"));
                    }
                    // Intermediate dialog
                    if (key.equals("showIntermediateDialog")) {
                        showIntermediateDialog = (Utils.intToBool(((Double) parameter.get("value")).intValue()));
                    }
                }

                if (rnd.nextDouble() <= likelihood) {
                    String jsonConfiguration = new GsonBuilder().setLenient().create().toJson(configuration);
                    Intent intent = createPullFeedbackIntent(baseURL, activity, jsonConfiguration, language, shuffleIds.get(i));
                    if (!showIntermediateDialog) {
                        // Start the feedback activity without asking the user
                        activity.startActivity(intent);
                    } else {
                        // Ask the user if he would like to give feedback or not
                        DialogUtils.PullFeedbackIntermediateDialog d = DialogUtils.PullFeedbackIntermediateDialog.newInstance(activity.getResources().getString(ch.uzh.supersede.feedbacklibrary.R.string.supersede_feedbacklibrary_pull_feedback_question_string), jsonConfiguration, shuffleIds.get(i), baseURL, language);
                        d.show(activity.getFragmentManager(), "feedbackPopupDialog");
                    }
                    break;
                }
            }
        }
    }

    /**
     * Opens the FeedbackActivity from the feedback library in case if a PULL feedback is triggered with a specific PULL configuration.
     */
    public void triggerSpecificPullFeedback(@NonNull final String baseURL, @NonNull final Activity activity, final long applicationId, final @NonNull String language,
                                            final long pullConfigurationId, final @NonNull String intermediateDialogText) {
        FeedbackService svc = FeedbackService.getInstance();
        svc.pingOrchestrator();
        Response<OrchestratorConfigurationItem> response = svc.getConfiguration(language, applicationId);

        OrchestratorConfigurationItem configuration = response.body();
        if (configuration != null) {
            List<ConfigurationItem> configurationItems = configuration.getConfigurationItems();
            long selectedPullConfigurationIndex = -1L;
            ConfigurationItem selectedConfigurationItem = null;
            for (ConfigurationItem configurationItem : configurationItems) {
                if (configurationItem.getType().equals("PULL") && configurationItem.getId() == pullConfigurationId) {
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
                    // Intermediate dialog
                    if (key.equals("showIntermediateDialog")) {
                        showIntermediateDialog = (Utils.intToBool(((Double) parameter.get("value")).intValue()));
                    }
                }

                String jsonConfiguration = new GsonBuilder().setLenient().create().toJson(configuration);
                Intent intent = createPullFeedbackIntent(baseURL, activity, jsonConfiguration, language, selectedPullConfigurationIndex);
                if (!showIntermediateDialog) {
                    // Start the feedback activity without asking the user
                    activity.startActivity(intent);
                } else {
                    // Ask the user if he would like to give feedback or not
                    DialogUtils.PullFeedbackIntermediateDialog d = DialogUtils.PullFeedbackIntermediateDialog.newInstance(intermediateDialogText, jsonConfiguration, selectedPullConfigurationIndex, baseURL, language);
                    d.show(activity.getFragmentManager(), "feedbackPopupDialog");
                }
            } else {
                DialogUtils.showInformationDialog(activity, new String[]{activity.getResources().getString(R.string.supersede_feedbacklibrary_feedback_application_unavailable_text)}, true);
            }
        }
    }

    /**
     * Takes a screenshot of the current screen automatically and opens the FeedbackActivity from the feedback library in case if a PUSH feedback is triggered.
     */
    public void startActivityWithScreenshotCapture(@NonNull final String baseURL, @NonNull final Activity activity, final long applicationId, @NonNull final String language) {
        FeedbackService.getInstance().pingOrchestrator();

        Intent intent = new Intent(activity, FeedbackActivity.class);
        String defaultImagePath = Utils.captureScreenshot(activity);
        intent.putExtra(DEFAULT_IMAGE_PATH, defaultImagePath);
        intent.putExtra(EXTRA_KEY_APPLICATION_ID, applicationId);
        intent.putExtra(EXTRA_KEY_BASE_URL, baseURL);
        intent.putExtra(EXTRA_KEY_LANGUAGE, language);
        activity.startActivity(intent);
    }

    private Intent createPullFeedbackIntent(String baseURL, Activity activity, String jsonConfiguration, String language, long selectedPullConfigurationIndex) {
        Intent intent = new Intent(activity, activity.getClass());
        intent.putExtra(IS_PUSH_STRING, false);
        intent.putExtra(JSON_CONFIGURATION_STRING, jsonConfiguration);
        intent.putExtra(SELECTED_PULL_CONFIGURATION_INDEX_STRING, selectedPullConfigurationIndex);
        intent.putExtra(EXTRA_KEY_BASE_URL, baseURL);
        intent.putExtra(EXTRA_KEY_LANGUAGE, language);
        return intent;
    }
}
