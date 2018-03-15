package ch.uzh.supersede.feedbacklibrary.services;

import android.util.Log;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.API.IFeedbackAPI;
import ch.uzh.supersede.feedbacklibrary.configurations.OrchestratorConfigurationItem;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.SUPERSEEDE_BASE_URL;

public class FeedbackService {
    private static final String TAG = "FeedbackService";
    private static FeedbackService instance;
    private static IFeedbackAPI feedbackAPI;

    private FeedbackService() {
    }

    public static FeedbackService getInstance() {
        if (instance == null) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(SUPERSEEDE_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
            feedbackAPI = retrofit.create(IFeedbackAPI.class);
            instance = new FeedbackService();
        }
        return instance;
    }

    public boolean pingOrchestrator() {
        try {
            Response<ResponseBody> response = feedbackAPI.pingOrchestrator().execute();
            if (response.code() == 200) {
                return true;
            }
            //TODO [jfo]: error handling
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return false;
    }

    public boolean createFeedbackVariant(String language, long applicationId, MultipartBody.Part feedback, List<MultipartBody.Part> files) {
        try {
            Response<JsonObject> response = feedbackAPI.createFeedbackVariant(language, applicationId, feedback, files).execute();
            if (response.code() == 200) {
                return true;
            }
            //TODO [jfo]: error handling
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return false;
    }

    public boolean pingRepository() {
        try {
            Response<ResponseBody> response = feedbackAPI.pingRepository().execute();
            if (response.code() == 200) {
                return true;
            }
            //TODO [jfo]: error handling
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return false;
    }

    public Response<OrchestratorConfigurationItem> getConfiguration(String language, long applicationId) {
        try {
            Response<OrchestratorConfigurationItem> response = feedbackAPI.getConfiguration(language, applicationId).execute();
            if (response.code() == 200) {
                return response;
            }
            //TODO [jfo]: error handling
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }
}
