package ch.uzh.supersede.feedbacklibrary.services;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import ch.uzh.supersede.feedbacklibrary.api.IFeedbackAPI;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackBean;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackDetailsBean;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.feedback.Feedback;
import ch.uzh.supersede.feedbacklibrary.models.AndroidUser;
import ch.uzh.supersede.feedbacklibrary.models.AuthenticateRequest;
import ch.uzh.supersede.feedbacklibrary.models.AuthenticateResponse;
import ch.uzh.supersede.feedbacklibrary.stubs.RepositoryStub;
import ch.uzh.supersede.feedbacklibrary.utils.Enums;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener.EventType;
import static ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener.EventType.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.SUPERSEDE_BASE_URL;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.FETCH_MODE.*;

/**
 * Singleton class that returns the original {@link FeedbackApiService} with its functions, defined in {@link IFeedbackAPI} iff {@code BuildConfig.DEBUG} is enabled, otherwise
 * {@link FeedbackMockService} will be used instead. Classes that use this Service will have to implement {@link IFeedbackServiceEventListener} and its functions in order to handle the
 * asynchronous events accordingly.
 */
public abstract class FeedbackService {
    private static FeedbackService instance;
    private static IFeedbackAPI feedbackAPI;
    private String token;
    private long applicationId;
    private String language;

    private FeedbackService() {
    }

    public static FeedbackService getInstance() {
        //        if (BuildConfig.DEBUG) {
        //            if (instance == null) {
        //                instance = new FeedbackMockService();
        //            }
        //            return instance;
        //        }
        if (instance == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SUPERSEDE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            feedbackAPI = retrofit.create(IFeedbackAPI.class);
            instance = new FeedbackApiService();
        }
        return instance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public abstract void authenticate(IFeedbackServiceEventListener callback, AuthenticateRequest authenticateRequest);

    public abstract void createFeedback(IFeedbackServiceEventListener callback, Activity activity, FeedbackDetailsBean feedbackDetailsBean,
            List<MultipartBody.Part> files);

    public abstract void createUser(IFeedbackServiceEventListener callback, AndroidUser androidUser);

    public abstract void getUser(IFeedbackServiceEventListener callback, AndroidUser androidUser);

    public abstract void getMineFeedbackVotes(IFeedbackServiceEventListener callback, Activity activity);

    public abstract void getOthersFeedbackVotes(IFeedbackServiceEventListener callback, Activity activity);

    public abstract void getFeedbackSettings(IFeedbackServiceEventListener callback, Activity activity);

    public abstract void createSubscription(IFeedbackServiceEventListener callback, Context context, FeedbackBean feedbackBean, boolean isChecked);

    private static class FeedbackApiService extends FeedbackService {

        @Override
        public void authenticate(IFeedbackServiceEventListener callback, AuthenticateRequest authenticateRequest) {
            feedbackAPI.authenticate(authenticateRequest).enqueue(
                    new RepositoryCallback<AuthenticateResponse>(callback, EventType.AUTHENTICATE) {
                    });
        }

        @Override
        public void createUser(IFeedbackServiceEventListener callback, AndroidUser androidUser) {
            feedbackAPI.createUser(getToken(), getLanguage(), getApplicationId(), androidUser).enqueue(
                    new RepositoryCallback<AndroidUser>(callback, EventType.CREATE_USER) {
                    });
        }

        @Override
        public void getUser(IFeedbackServiceEventListener callback, AndroidUser androidUser) {
            feedbackAPI.getUser(getToken(), getLanguage(), getApplicationId(), androidUser.getName()).enqueue(
                    new RepositoryCallback<AndroidUser>(callback, EventType.GET_USER) {
                    });
        }

        @Override
        public void createFeedback(IFeedbackServiceEventListener callback, Activity activity, FeedbackDetailsBean feedbackDetailsBean, List<MultipartBody.Part> attachments) {
            GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithoutExposeAnnotation();
            builder.serializeNulls();
            Gson gson = builder.create();

            String jsonString = gson.toJson(feedbackDetailsBean);
            MultipartBody.Part feedback = MultipartBody.Part.createFormData("json", "json", RequestBody.create(MediaType.parse("application/json"), jsonString.getBytes()));

            feedbackAPI.createFeedback(getToken(), getLanguage(), getApplicationId(), feedback, attachments).enqueue(
                    new RepositoryCallback<Feedback>(callback, EventType.CREATE_FEEDBACK) {
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
            RepositoryStub.sendSubscriptionChange(context, feedbackBean, isChecked);
            callback.onEventCompleted(CREATE_SUBSCRIPTION, FeedbackDatabase.getInstance(context).getFeedbackState(feedbackBean));
        }
    }

    private static class FeedbackMockService extends FeedbackService {
        @Override
        public void authenticate(IFeedbackServiceEventListener callback, AuthenticateRequest authenticateRequest) {
            callback.onEventCompleted(AUTHENTICATE, RepositoryStub.generateAuthenticateResponse());
        }

        @Override
        public void createUser(IFeedbackServiceEventListener callback, AndroidUser androidUser) {
            callback.onEventCompleted(CREATE_USER, androidUser);
        }

        @Override
        public void getUser(IFeedbackServiceEventListener callback, AndroidUser androidUser) {
            callback.onEventCompleted(GET_USER, androidUser);
        }

        @Override
        public void createFeedback(IFeedbackServiceEventListener callback, Activity activity, FeedbackDetailsBean feedbackDetailsBean, List<MultipartBody
                .Part> files) {
            FeedbackDatabase.getInstance(activity).writeFeedback(feedbackDetailsBean.getFeedbackBean(), Enums.SAVE_MODE.CREATED);
            callback.onEventCompleted(CREATE_FEEDBACK, null);
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
