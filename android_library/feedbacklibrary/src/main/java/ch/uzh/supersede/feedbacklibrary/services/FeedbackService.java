package ch.uzh.supersede.feedbacklibrary.services;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.api.IFeedbackAPI;
import ch.uzh.supersede.feedbacklibrary.beans.*;
import ch.uzh.supersede.feedbacklibrary.components.buttons.FeedbackListItem;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.models.AndroidUser;
import ch.uzh.supersede.feedbacklibrary.models.AuthenticateRequest;
import ch.uzh.supersede.feedbacklibrary.models.AuthenticateResponse;
import ch.uzh.supersede.feedbacklibrary.models.Feedback;
import ch.uzh.supersede.feedbacklibrary.stubs.RepositoryStub;
import ch.uzh.supersede.feedbacklibrary.utils.*;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener.EventType;
import static ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener.EventType.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.FETCH_MODE.*;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ACTIVE;

/**
 * Singleton class that returns the original {@link FeedbackApiService} with its functions, defined in {@link IFeedbackAPI} iff {@code BuildConfig.DEBUG} is enabled, otherwise
 * {@link FeedbackMockService} will be used instead. Classes that use this Service will have to implement {@link IFeedbackServiceEventListener} and its functions in order to handle the
 * asynchronous events accordingly.
 */
public abstract class FeedbackService {
    private static FeedbackApiService apiInstance;
    private static FeedbackMockService mockInstance;

    private static IFeedbackAPI feedbackAPI;
    private String token;
    private long applicationId;
    private String language;

    private FeedbackService() {
    }

    public static FeedbackService getInstance(Context context, boolean... useStubInput) {
        boolean useStubs = false;
        if (useStubInput.length >0){
            useStubs = useStubInput[0];
        }
        if (ACTIVE.check(context)){
            useStubs = FeedbackDatabase.getInstance(context).readBoolean(USE_STUBS, false);
        }

        if (useStubs) {
            if (mockInstance == null) {
                mockInstance = new FeedbackMockService();
            }
            return mockInstance;
        }

        if (apiInstance == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            String endpointUrl = PreferenceManager.getDefaultSharedPreferences(context).getString(SHARED_PREFERENCES_ENDPOINT_URL, SHARED_PREFERENCES_ENDPOINT_URL_FALLBACK);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(endpointUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            feedbackAPI = retrofit.create(IFeedbackAPI.class);
            apiInstance = new FeedbackApiService();
        }
        return apiInstance;
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

    public abstract void getFeedbackList(IFeedbackServiceEventListener callback, Activity activity, LocalConfigurationBean configuration, int backgroundColor);

    public abstract void createUser(IFeedbackServiceEventListener callback, AndroidUser androidUser);

    public abstract void getUser(IFeedbackServiceEventListener callback, AndroidUser androidUser);

    public abstract void getMineFeedbackVotes(IFeedbackServiceEventListener callback, Activity activity);

    public abstract void getOthersFeedbackVotes(IFeedbackServiceEventListener callback, Activity activity);

    public abstract void getFeedbackSubscriptions(IFeedbackServiceEventListener callback, Context activity);

    public abstract void createSubscription(IFeedbackServiceEventListener callback, FeedbackBean feedbackBean, boolean isSubscribed);

    public abstract void pingRepository(IFeedbackServiceEventListener callback);

    public abstract void updateFeedbackStatus(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean, Object item);

    public abstract void deleteFeedback(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean);

    public abstract void reportFeedback(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean, String report);

    public abstract void respondFeedback(IFeedbackServiceEventListener callback, FeedbackBean feedbackDetailsBean, String response);

    public abstract void makeFeedbackPublic(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean);

    public abstract void voteFeedback(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean, boolean upVote);

    public abstract void getFeedbackImage(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean);

    public abstract void getFeedbackTags(IFeedbackServiceEventListener callback);

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
        public void getFeedbackList(IFeedbackServiceEventListener callback, Activity activity, LocalConfigurationBean configuration, int backgroundColor) {
            feedbackAPI.getFeedbackList(getToken(), getLanguage(), getApplicationId(), null, null).enqueue(
                    new RepositoryCallback<List<Feedback>>(callback, EventType.GET_FEEDBACK_LIST) {
                    });
        }

        @Override
        public void getMineFeedbackVotes(IFeedbackServiceEventListener callback, Activity activity) {
            //TODO [jfo] add list of VOTED feedbackIds as query
            feedbackAPI.getFeedbackList(getToken(), getLanguage(), getApplicationId(), null, null).enqueue(
                    new RepositoryCallback<List<Feedback>>(callback, EventType.GET_OTHERS_FEEDBACK_VOTES) {
                    });
        }

        @Override
        public void getOthersFeedbackVotes(IFeedbackServiceEventListener callback, Activity activity) {
            //TODO [jfo] add list of OWN feedbackIds as query
            feedbackAPI.getFeedbackList(getToken(), getLanguage(), getApplicationId(), null, null).enqueue(
                    new RepositoryCallback<List<Feedback>>(callback, EventType.GET_MINE_FEEDBACK_VOTES) {
                    });
        }

        @Override
        public void getFeedbackSubscriptions(IFeedbackServiceEventListener callback, Context activity) {
            //TODO [jfo] add list of SUBSCRIBED feedbackIds as query
            feedbackAPI.getFeedbackList(getToken(), getLanguage(), getApplicationId(), null, null).enqueue(
                    new RepositoryCallback<List<Feedback>>(callback, EventType.GET_FEEDBACK_SUBSCRIPTIONS) {
                    });
        }

        @Override
        public void createSubscription(IFeedbackServiceEventListener callback, FeedbackBean feedbackBean, boolean isSubscribed) {
            callback.onEventCompleted(CREATE_FEEDBACK_SUBSCRIPTION_MOCK, feedbackBean);
        }

        @Override
        public void pingRepository(IFeedbackServiceEventListener callback) {
            feedbackAPI.pingRepository().enqueue(
                    new RepositoryCallback<ResponseBody>(callback, EventType.PING_REPOSITORY) {
                    });
        }

        @Override
        public void updateFeedbackStatus(IFeedbackServiceEventListener callback,FeedbackDetailsBean feedbackDetailsBean, Object item) {
            if (item instanceof String){
                callback.onEventCompleted(CREATE_FEEDBACK_STATUS_UPDATE_MOCK, false);
                //TODO: mbo implement when backend allows this call;
            }
        }

        @Override
        public void deleteFeedback(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean) {
            callback.onEventCompleted(CREATE_FEEDBACK_DELETION_MOCK, false);
            //TODO: mbo implement when backend allows this call;
        }

        @Override
        public void reportFeedback(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean, String report) {
            callback.onEventCompleted(CREATE_FEEDBACK_REPORT_MOCK, report);
            //TODO: mbo implement when backend allows this call;
        }

        @Override
        public void respondFeedback(IFeedbackServiceEventListener callback, FeedbackBean feedbackDetailsBean, String response) {
            callback.onEventCompleted(CREATE_FEEDBACK_RESPONSE_MOCK, response);
            //TODO: mbo implement when backend allows this call;
        }

        @Override
        public void makeFeedbackPublic(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean) {
            callback.onEventCompleted(CREATE_FEEDBACK_PUBLICATION_MOCK, false);
            //TODO: mbo implement when backend allows this call;
        }

        @Override
        public void voteFeedback(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean, boolean upVote) {
            //callback.onEventCompleted(CREATE_FEEDBACK_VOTE_MOCK, false);
            //TODO: mbo implement when backend allows this call;
        }

        @Override
        public void getFeedbackTags(IFeedbackServiceEventListener callback) {
            callback.onEventCompleted(GET_FEEDBACK_IMAGE_MOCK, false);
            //TODO: mbo implement when backend allows this call;
        }

        @Override
        public void getFeedbackImage(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean) {
            feedbackAPI.getFeedbackImage(getToken(), getLanguage(), getApplicationId(),feedbackDetailsBean.getBitmapName()).enqueue(
                    new RepositoryCallback<ResponseBody>(callback, EventType.GET_FEEDBACK_IMAGE) {
                    });
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
        public void getFeedbackList(IFeedbackServiceEventListener callback, Activity activity, LocalConfigurationBean configuration, int backgroundColor) {
            ArrayList<FeedbackListItem> allFeedbackList = new ArrayList<>();
            for (FeedbackDetailsBean bean : RepositoryStub.getFeedback(activity, 50, -30, 50, 0.1f)) {
                if (bean != null) {
                    FeedbackListItem listItem = new FeedbackListItem(activity, 8, bean, configuration, backgroundColor);
                    allFeedbackList.add(listItem);
                }
            }
            callback.onEventCompleted(GET_FEEDBACK_LIST_MOCK, allFeedbackList);
        }

        @Override
        public void getMineFeedbackVotes(IFeedbackServiceEventListener callback, Activity activity) {
            callback.onEventCompleted(GET_MINE_FEEDBACK_VOTES_MOCK, FeedbackDatabase.getInstance(activity).getFeedbackBeans(VOTED));
        }

        @Override
        public void getOthersFeedbackVotes(IFeedbackServiceEventListener callback, Activity activity) {
            callback.onEventCompleted(GET_OTHERS_FEEDBACK_VOTES_MOCK, FeedbackDatabase.getInstance(activity).getFeedbackBeans(OWN));
        }

        @Override
        public void getFeedbackSubscriptions(IFeedbackServiceEventListener callback, Context activity) {
            callback.onEventCompleted(GET_FEEDBACK_SUBSCRIPTIONS_MOCK, FeedbackDatabase.getInstance(activity).getFeedbackBeans(SUBSCRIBED));
        }

        @Override
        public void createSubscription(IFeedbackServiceEventListener callback, FeedbackBean feedbackBean, boolean isSubscribed) {
            callback.onEventCompleted(CREATE_FEEDBACK_SUBSCRIPTION, feedbackBean);
        }

        @Override
        public void pingRepository(IFeedbackServiceEventListener callback) {
            callback.onEventCompleted(PING_REPOSITORY, false);
        }

        @Override
        public void updateFeedbackStatus(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean, Object item) {
            RepositoryStub.updateFeedbackStatus(feedbackDetailsBean,item);
            callback.onEventCompleted(CREATE_FEEDBACK_STATUS_UPDATE, false);
        }

        @Override
        public void deleteFeedback(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean) {
            RepositoryStub.deleteFeedback(feedbackDetailsBean);
            callback.onEventCompleted(CREATE_FEEDBACK_DELETION_MOCK, false);
        }

        @Override
        public void reportFeedback(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean, String report) {
            callback.onEventCompleted(CREATE_FEEDBACK_REPORT_MOCK, false);
        }

        @Override
        public void respondFeedback(IFeedbackServiceEventListener callback, FeedbackBean feedbackDetailsBean, String response) {
            callback.onEventCompleted(CREATE_FEEDBACK_RESPONSE_MOCK, false);
        }

        @Override
        public void makeFeedbackPublic(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean) {
            callback.onEventCompleted(CREATE_FEEDBACK_PUBLICATION_MOCK, false);
        }

        @Override
        public void voteFeedback(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean, boolean upVote) {
            //callback.onEventCompleted(CREATE_FEEDBACK_PUBLICATION_MOCK, false);
        }

        @Override
        public void getFeedbackTags(IFeedbackServiceEventListener callback) {
            callback.onEventCompleted(GET_FEEDBACK_TAGS_MOCK, false);
        }

        @Override
        public void getFeedbackImage(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean) {
            callback.onEventCompleted(GET_FEEDBACK_IMAGE_MOCK, false);
        }

    }
}
