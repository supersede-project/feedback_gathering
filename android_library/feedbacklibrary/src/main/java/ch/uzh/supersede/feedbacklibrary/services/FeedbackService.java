package ch.uzh.supersede.feedbacklibrary.services;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.api.IFeedbackAPI;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackBean;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackDetailsBean;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.models.AbstractFeedbackPart;
import ch.uzh.supersede.feedbacklibrary.models.AndroidUser;
import ch.uzh.supersede.feedbacklibrary.models.AuthenticateRequest;
import ch.uzh.supersede.feedbacklibrary.models.AuthenticateResponse;
import ch.uzh.supersede.feedbacklibrary.models.Feedback;
import ch.uzh.supersede.feedbacklibrary.stubs.RepositoryStub;
import ch.uzh.supersede.feedbacklibrary.utils.Enums;
import ch.uzh.supersede.feedbacklibrary.utils.FeedbackUtility;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener.EventType;
import static ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener.EventType.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.FETCH_MODE.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.RUNNING_MODE_TYPE.MOCKUP;

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
        if (RUNNING_MODE == MOCKUP) {
            if (instance == null) {
                instance = new FeedbackMockService();
            }
            return instance;
        }
        if (instance == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SUPERSEDE_BASE_URL)
                    .client(client)
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

    public abstract void createFeedback(IFeedbackServiceEventListener callback, Activity activity, Feedback feedback, byte[] screenshot);

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
        public void createFeedback(IFeedbackServiceEventListener callback, Activity activity, Feedback feedback, byte[] screenshot) {
            List<MultipartBody.Part> multipartFiles = new ArrayList<>();
            multipartFiles.add(MultipartBody.Part.createFormData("screenshot", "screenshot", RequestBody.create(MediaType.parse("image/png"), screenshot)));
            multipartFiles.add(MultipartBody.Part.createFormData("audio", "audio", RequestBody.create(MediaType.parse("audio/mp3"), new byte[0]))); //FIXME [jfo] load audio

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            String jsonString = gson.toJson(feedback);
            MultipartBody.Part jsonFeedback = MultipartBody.Part.createFormData("json", "json", RequestBody.create(MediaType.parse("application/json"), jsonString.getBytes()));

            feedbackAPI.createFeedback(getToken(), getLanguage(), getApplicationId(), jsonFeedback, multipartFiles).enqueue(
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
        public void createFeedback(IFeedbackServiceEventListener callback, Activity activity, Feedback feedback, byte[] screenshot) {
            FeedbackDatabase.getInstance(activity).writeFeedback(FeedbackUtility.feedbackToFeedbackDetailsBean(activity, feedback).getFeedbackBean(), Enums.SAVE_MODE.CREATED);
            callback.onEventCompleted(CREATE_FEEDBACK, feedback);
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
