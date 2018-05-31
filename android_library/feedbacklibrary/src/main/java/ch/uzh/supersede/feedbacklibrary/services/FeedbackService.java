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
import ch.uzh.supersede.feedbacklibrary.api.IFeedbackAPI;
import ch.uzh.supersede.feedbacklibrary.beans.ConfigurationRequestBean;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackBean;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackDetailsBean;
import ch.uzh.supersede.feedbacklibrary.beans.LocalFeedbackBean;
import ch.uzh.supersede.feedbacklibrary.components.buttons.AbstractSettingsListItem;
import ch.uzh.supersede.feedbacklibrary.components.buttons.SubscriptionListItem;
import ch.uzh.supersede.feedbacklibrary.components.buttons.VoteListItem;
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

    public abstract void createFeedbackVariant(IFeedbackServiceEventListener callback, Activity activity, String language, long applicationId, FeedbackDetailsBean feedbackDetailsBean, List<MultipartBody.Part> files);

    public abstract void getMineFeedbackVotes(IFeedbackServiceEventListener callback, Activity activity);

    public abstract void getOthersFeedbackVotes(IFeedbackServiceEventListener callback, Activity activity);

    public abstract void getFeedbackSettings(IFeedbackServiceEventListener callback, Activity activity);

    public abstract void createSubscription(IFeedbackServiceEventListener callback, Context context, FeedbackBean feedbackBean, boolean isChecked);

    private static class FeedbackApiService extends FeedbackService {

        @Override
        public void createFeedbackVariant(IFeedbackServiceEventListener callback, Activity activity, String language, long applicationId, FeedbackDetailsBean feedbackDetailsBean, List<MultipartBody.Part> files) {
            GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithoutExposeAnnotation();
            builder.serializeNulls();
            Gson gson = builder.create();

            String jsonString = gson.toJson(feedbackDetailsBean);
            MultipartBody.Part jsonPart = MultipartBody.Part.createFormData("json", "json", RequestBody.create(MediaType.parse("application/json"), jsonString.getBytes()));

            feedbackAPI.createFeedbackVariant(language, applicationId, jsonPart, files).enqueue(
                    new FeedbackCallback<JsonObject>(callback, EventType.CREATE_FEEDBACK_VARIANT) {
                    });
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
    }

    private static class FeedbackMockService extends FeedbackService {

        @Override
        public void createFeedbackVariant(IFeedbackServiceEventListener callback, Activity activity, String language, long applicationId, FeedbackDetailsBean feedbackDetailsBean, List<MultipartBody.Part> files) {
            FeedbackDatabase.getInstance(activity).writeFeedback(feedbackDetailsBean.getFeedbackBean(), Enums.SAVE_MODE.CREATED);
            callback.onEventCompleted(CREATE_FEEDBACK_VARIANT, null);
        }

        @Override
        public void getMineFeedbackVotes(IFeedbackServiceEventListener callback, Activity activity) {
            callback.onEventCompleted(GET_MINE_FEEDBACK_VOTES, FeedbackDatabase.getInstance(activity).getFeedbackBeans(VOTED));
        }

        @Override
        public void getOthersFeedbackVotes(IFeedbackServiceEventListener callback, Activity activity) {
            callback.onEventCompleted(GET_OTHERS_FEEDBACK_VOTES, FeedbackDatabase.getInstance(activity).getFeedbackBeans(OWN));
        }

        @Override
        public void getFeedbackSettings(IFeedbackServiceEventListener callback, Activity activity) {
            callback.onEventCompleted(GET_FEEDBACK_SETTINGS, FeedbackDatabase.getInstance(activity).getFeedbackBeans(SUBSCRIBED));
        }

        @Override
        public void createSubscription(IFeedbackServiceEventListener callback, Context context, FeedbackBean feedbackBean, boolean isChecked) {
            RepositoryStub.sendSubscriptionChange(context, feedbackBean, isChecked);
            callback.onEventCompleted(CREATE_SUBSCRIPTION, FeedbackDatabase.getInstance(context).getFeedbackState(feedbackBean));
        }
    }
}
