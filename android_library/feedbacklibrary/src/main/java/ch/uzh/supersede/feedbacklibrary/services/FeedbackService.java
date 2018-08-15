package ch.uzh.supersede.feedbacklibrary.services;

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
import ch.uzh.supersede.feedbacklibrary.models.*;
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
    private static final String VIEW_PUBLIC = "public";
    private static final String VIEW_PRIVATE = "private";
    private static final String VIEW_ALL = null;

    private static FeedbackApiService apiInstance;
    private static FeedbackMockService mockInstance;

    private static IFeedbackAPI feedbackAPI;
    private String token;
    private static long applicationId;
    private static String language;

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
            applicationId = PreferenceManager.getDefaultSharedPreferences(context).getLong(SHARED_PREFERENCES_HOST_APPLICATION_ID, SHARED_PREFERENCES_HOST_APPLICATION_ID_FALLBACK);
            language = PreferenceManager.getDefaultSharedPreferences(context).getString(SHARED_PREFERENCES_HOST_APPLICATION_LANGUAGE, SHARED_PREFERENCES_HOST_APPLICATION_LANGUAGE_FALLBACK);

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

    public abstract void authenticate(IFeedbackServiceEventListener callback, AuthenticateRequest authenticateRequest);

    public abstract void createFeedback(IFeedbackServiceEventListener callback, Context context, Feedback feedback, byte[] screenshot);

    public abstract void getFeedbackList(IFeedbackServiceEventListener callback, Context context);

    public abstract void getPrivateFeedbackList(IFeedbackServiceEventListener callback);

    public abstract void getReportedFeedbackList(IFeedbackServiceEventListener callback);

    public abstract void createUser(IFeedbackServiceEventListener callback, AndroidUser androidUser);

    public abstract void getUser(IFeedbackServiceEventListener callback, AndroidUser androidUser);

    public abstract void getMineFeedbackVotes(IFeedbackServiceEventListener callback, Context context);

    public abstract void getOthersFeedbackVotes(IFeedbackServiceEventListener callback, Context context);

    public abstract void getFeedbackSubscriptions(IFeedbackServiceEventListener callback, Context context);

    public abstract void pingRepository(IFeedbackServiceEventListener callback);

    public abstract void updateFeedbackStatus(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean, Object item);

    public abstract void deleteFeedback(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean);

    public abstract void reportFeedback(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean, String report);

    public abstract void respondFeedback(IFeedbackServiceEventListener callback, FeedbackBean feedbackDetailsBean, String response);

    public abstract void makeFeedbackPublic(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean);

    public abstract void voteFeedback(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean, boolean upVote);

    public abstract void getFeedbackImage(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean);

    public abstract void getFeedbackTags(IFeedbackServiceEventListener callback);

    public void createSubscription(IFeedbackServiceEventListener callback, FeedbackBean feedbackBean) {
        callback.onEventCompleted(CREATE_FEEDBACK_SUBSCRIPTION, feedbackBean);
    }

    public void getLocalFeedbackSubscriptions(IFeedbackServiceEventListener callback, Context context) {
        callback.onEventCompleted(GET_LOCAL_FEEDBACK_SUBSCRIPTIONS, FeedbackDatabase.getInstance(context).getFeedbackBeans(SUBSCRIBED));
    }

    private static class FeedbackApiService extends FeedbackService {

        @Override
        public void authenticate(IFeedbackServiceEventListener callback, AuthenticateRequest authenticateRequest) {
            feedbackAPI.authenticate(authenticateRequest).enqueue(
                    new RepositoryCallback<AuthenticateResponse>(callback, EventType.AUTHENTICATE) {
                    });
        }

        @Override
        public void createUser(IFeedbackServiceEventListener callback, AndroidUser androidUser) {
            feedbackAPI.createUser(getToken(), language, applicationId, androidUser).enqueue(
                    new RepositoryCallback<AndroidUser>(callback, EventType.CREATE_USER) {
                    });
        }

        @Override
        public void getUser(IFeedbackServiceEventListener callback, AndroidUser androidUser) {
            feedbackAPI.getUser(getToken(), language, applicationId, androidUser.getName()).enqueue(
                    new RepositoryCallback<AndroidUser>(callback, EventType.GET_USER) {
                    });
        }

        @Override
        public void createFeedback(IFeedbackServiceEventListener callback, Context context, Feedback feedback, byte[] screenshot) {
            List<MultipartBody.Part> multipartFiles = new ArrayList<>();
            multipartFiles.add(MultipartBody.Part.createFormData("screenshot", "screenshot", RequestBody.create(MediaType.parse("image/png"), screenshot)));
            multipartFiles.add(MultipartBody.Part.createFormData("audio", "audio", RequestBody.create(MediaType.parse("audio/mp3"), new byte[0]))); //FIXME [jfo] load audio

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            String jsonString = gson.toJson(feedback);
            MultipartBody.Part jsonFeedback = MultipartBody.Part.createFormData("json", "json", RequestBody.create(MediaType.parse("application/json"), jsonString.getBytes()));

            feedbackAPI.createFeedback(getToken(), language, applicationId, jsonFeedback, multipartFiles).enqueue(
                    new RepositoryCallback<Feedback>(callback, EventType.CREATE_FEEDBACK) {
                    });
        }

        private void getFeedbackList(IFeedbackServiceEventListener callback, EventType eventType, String viewMode, String ids){
            feedbackAPI.getFeedbackList(getToken(), language, applicationId, viewMode, ids).enqueue(
                    new RepositoryCallback<List<Feedback>>(callback, eventType) {
                    });
        }


        @Override
        public void getFeedbackList(IFeedbackServiceEventListener callback, Context context) {
            getFeedbackList(callback, EventType.GET_FEEDBACK_LIST, VIEW_PUBLIC, null);
        }

        @Override
        public void getPrivateFeedbackList(IFeedbackServiceEventListener callback) {
            getFeedbackList(callback, EventType.GET_PRIVATE_FEEDBACK_LIST, VIEW_PRIVATE, null);
        }

        @Override
        public void getReportedFeedbackList(IFeedbackServiceEventListener callback) {
            getFeedbackList(callback, EventType.GET_REPORTED_FEEDBACK_LIST, VIEW_PUBLIC, null); //TODO [jfo] view reported
        }

        @Override
        public void getMineFeedbackVotes(IFeedbackServiceEventListener callback, Context context) {
            String ids = FeedbackUtility.getIds(FeedbackDatabase.getInstance(context).getFeedbackBeans(OWN));
            getFeedbackList(callback, EventType.GET_OTHERS_FEEDBACK_VOTES, VIEW_ALL, ids);
        }

        @Override
        public void getOthersFeedbackVotes(IFeedbackServiceEventListener callback, Context context) {
            String ids = FeedbackUtility.getIds(FeedbackDatabase.getInstance(context).getFeedbackBeans(VOTED));
            getFeedbackList(callback, EventType.GET_MINE_FEEDBACK_VOTES, VIEW_PUBLIC, ids);
        }

        @Override
        public void getFeedbackSubscriptions(IFeedbackServiceEventListener callback, Context context) {
            String ids = FeedbackUtility.getIds(FeedbackDatabase.getInstance(context).getFeedbackBeans(SUBSCRIBED));
            getFeedbackList(callback, EventType.GET_FEEDBACK_SUBSCRIPTIONS, VIEW_ALL, ids);
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
            feedbackAPI.getTagList(getToken(), language, applicationId).enqueue(
                    new RepositoryCallback<List<String>>(callback, EventType.GET_FEEDBACK_TAGS) {
                    });
        }

        @Override
        public void getFeedbackImage(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean) {
            feedbackAPI.getFeedbackImage(getToken(), language, applicationId,feedbackDetailsBean.getBitmapName()).enqueue(
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
            callback.onEventCompleted(GET_USER_MOCK, androidUser);
        }

        @Override
        public void createFeedback(IFeedbackServiceEventListener callback, Context context, Feedback feedback, byte[] screenshot) {
            FeedbackDatabase.getInstance(context).writeFeedback(FeedbackUtility.feedbackToFeedbackDetailsBean(context, feedback).getFeedbackBean(), Enums.SAVE_MODE.CREATED);
            callback.onEventCompleted(CREATE_FEEDBACK, feedback);
        }

        @Override
        public void getFeedbackList(IFeedbackServiceEventListener callback, Context context) {
            LocalConfigurationBean configuration = ConfigurationUtility.getConfigurationFromDatabase(context);
            ArrayList<FeedbackListItem> allFeedbackList = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<>();
            for (FeedbackDetailsBean bean : RepositoryStub.getFeedback(context, 50, -30, 50, 0.1f)) {
                if (bean != null) {
                    FeedbackListItem listItem = new FeedbackListItem(context, 8, bean, configuration, 0);
                    listItem.addAllLabels(labels);
                    allFeedbackList.add(listItem);
                }
            }
            float textSize = ScalingUtility.getInstance().getMinTextSizeScaledForWidth(15, 0, 0.4, labels.toArray(new String[labels.size()]));
            for (FeedbackListItem listItem : allFeedbackList){
                listItem.equalizeTextSize(textSize);
            }
            callback.onEventCompleted(GET_FEEDBACK_LIST_MOCK, allFeedbackList);
        }

        @Override
        public void getPrivateFeedbackList(IFeedbackServiceEventListener callback) {

        }

        @Override
        public void getReportedFeedbackList(IFeedbackServiceEventListener callback) {

        }

        @Override
        public void getMineFeedbackVotes(IFeedbackServiceEventListener callback, Context context) {
            callback.onEventCompleted(GET_MINE_FEEDBACK_VOTES_MOCK, FeedbackDatabase.getInstance(context).getFeedbackBeans(VOTED));
        }

        @Override
        public void getOthersFeedbackVotes(IFeedbackServiceEventListener callback, Context context) {
            callback.onEventCompleted(GET_OTHERS_FEEDBACK_VOTES_MOCK, FeedbackDatabase.getInstance(context).getFeedbackBeans(OWN));
        }

        @Override
        public void getFeedbackSubscriptions(IFeedbackServiceEventListener callback, Context context) {
            callback.onEventCompleted(GET_FEEDBACK_SUBSCRIPTIONS_MOCK, FeedbackDatabase.getInstance(context).getFeedbackBeans(SUBSCRIBED));
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
