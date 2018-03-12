package ch.uzh.supersede.feedbacklibrary.utils;

import android.util.Log;

import java.io.IOException;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.API.IFeedbackAPI;
import ch.uzh.supersede.feedbacklibrary.configurations.OrchestratorConfigurationItem;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FeedbackService {
    private static final String TAG = "FeedbackService";
    private static FeedbackService instance;
    private static IFeedbackAPI feedbackAPI;

    private FeedbackService() {
    }

    public static FeedbackService getInstance() {
        if (instance == null) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.SUPERSEEDE_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
            feedbackAPI = retrofit.create(IFeedbackAPI.class);
            instance = new FeedbackService();
        }
        return instance;
    }

    public void pingOrchestrator() {
        try {
            feedbackAPI.pingOrchestrator().execute();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public void createFeedbackVariant(String language, long applicationId, MultipartBody.Part feedback, List<MultipartBody.Part> files) {
        try {
            feedbackAPI.createFeedbackVariant(language, applicationId, feedback, files).execute();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public void pingRepository() {
        try {
            feedbackAPI.pingRepository().execute();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public Response<OrchestratorConfigurationItem> getConfiguration(String language, long applicationId) {
        try {
            return feedbackAPI.getConfiguration(language, applicationId).execute();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }
}
