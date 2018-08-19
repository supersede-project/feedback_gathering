package ch.uzh.supersede.feedbacklibrary.services;

import android.content.Context;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.activities.FeedbackListActivity;
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
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.FeedbackServiceConstants.*;
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
    private static long applicationId;
    private static String language;

    private FeedbackService() {
    }

    public static FeedbackService getInstance(Context context, boolean... useStubInput) {
        boolean useStubs = false;
        if (useStubInput.length > 0) {
            useStubs = useStubInput[0];
        }
        if (ACTIVE.check(context)) {
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

    String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    boolean isTokenSet() {
        return token != null;
    }

    public abstract void pingRepository(IFeedbackServiceEventListener callback);

    public abstract void authenticate(IFeedbackServiceEventListener callback, AuthenticateRequest authenticateRequest);

    public abstract void createFeedback(IFeedbackServiceEventListener callback, Context context, Feedback feedback, byte[] screenshot, File audio);

    public abstract void getFeedbackList(IFeedbackServiceEventListener callback, Context context, String relevantForUser, int backgroundColor);

    public abstract void getFeedbackListPrivate(IFeedbackServiceEventListener callback);

    public abstract void createUser(IFeedbackServiceEventListener callback, AndroidUser androidUser);

    public abstract void getUser(IFeedbackServiceEventListener callback, AndroidUser androidUser);

    public abstract void getFeedbackListOwn(IFeedbackServiceEventListener callback, Context context);

    public abstract void getFeedbackListVoted(IFeedbackServiceEventListener callback, Context context);

    public abstract void getFeedbackListSubscribed(IFeedbackServiceEventListener callback, Context context);

    public abstract void getFeedbackReportList(IFeedbackServiceEventListener callback);

    public abstract void editFeedbackStatus(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean, String status);

    public abstract void editFeedbackPublication(IFeedbackServiceEventListener callback, FeedbackBean feedbackDetailsBean, boolean isPublic);

    public abstract void deleteFeedback(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean);

    public abstract void createFeedbackReport(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean, FeedbackReportRequestBody report);

    public abstract void createFeedbackResponse(IFeedbackServiceEventListener callback, FeedbackBean feedbackDetailsBean, String userName, String response);

    public abstract void deleteFeedbackResponse(IFeedbackServiceEventListener callback, FeedbackBean feedbackDetailsBean, FeedbackResponseBean response);

    public abstract void createVote(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean, int vote, String userName);

    public abstract void getFeedbackImage(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean);

    public abstract void getFeedbackAudio(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean);

    public abstract void getTagList(IFeedbackServiceEventListener callback);

    public void createSubscription(IFeedbackServiceEventListener callback, FeedbackBean feedbackBean) {
        callback.onEventCompleted(CREATE_FEEDBACK_SUBSCRIPTION, feedbackBean);
    }

    private static class FeedbackApiService extends FeedbackService {

        @Override
        public void pingRepository(IFeedbackServiceEventListener callback) {
            feedbackAPI.pingRepository().enqueue(
                    new RepositoryCallback<ResponseBody>(callback, EventType.PING_REPOSITORY) {
                    });
        }

        @Override
        public void authenticate(IFeedbackServiceEventListener callback, AuthenticateRequest authenticateRequest) {
            if (!isTokenSet()) {
                feedbackAPI.authenticate(authenticateRequest).enqueue(
                        new RepositoryCallback<AuthenticateResponse>(callback, EventType.AUTHENTICATE) {
                        });
            }
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
        public void createFeedback(IFeedbackServiceEventListener callback, Context context, Feedback feedback, byte[] screenshot, File audio) {
            List<MultipartBody.Part> multipartFiles = new ArrayList<>();
            multipartFiles.add(MultipartBody.Part.createFormData("screenshot", "screenshot", RequestBody.create(MediaType.parse("image/png"), screenshot)));
            if (audio != null){
                multipartFiles.add(MultipartBody.Part.createFormData("audio", "audio", RequestBody.create(MediaType.parse("audio/m4a"), audio)));
            }

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            String jsonString = gson.toJson(feedback);
            MultipartBody.Part jsonFeedback = MultipartBody.Part.createFormData("json", "json", RequestBody.create(MediaType.parse("application/json"), jsonString.getBytes()));

            feedbackAPI.createFeedback(getToken(), language, applicationId, jsonFeedback, multipartFiles).enqueue(
                    new RepositoryCallback<Feedback>(callback, EventType.CREATE_FEEDBACK) {
                    });
        }

        private void getFeedbackList(IFeedbackServiceEventListener callback, EventType eventType, String viewMode, String ids, String relevantForUser, boolean onlyReported) {
            feedbackAPI.getFeedbackList(getToken(), language, applicationId, viewMode, ids, relevantForUser, onlyReported).enqueue(
                    new RepositoryCallback<List<Feedback>>(callback, eventType) {
                    });
        }

        private void editFeedback(IFeedbackServiceEventListener callback, long feedbackId, EventType eventType, Feedback feedback) {
            feedbackAPI.editFeedback(getToken(), language, applicationId, feedbackId, feedback).enqueue(
                    new RepositoryCallback<Feedback>(callback, eventType) {
                    });
        }

        @Override
        public void getFeedbackList(IFeedbackServiceEventListener callback, Context context, String relevantForUser, int backgroundColor) {
            getFeedbackList(callback, EventType.GET_FEEDBACK_LIST, VIEW_ALL, null, relevantForUser, false);
        }

        @Override
        public void getFeedbackListPrivate(IFeedbackServiceEventListener callback) {
            getFeedbackList(callback, EventType.GET_FEEDBACK_LIST_PRIVATE, VIEW_PRIVATE, null, null, false);
        }

        @Override
        public void getFeedbackListOwn(IFeedbackServiceEventListener callback, Context context) {
            String ids = FeedbackUtility.getIds(FeedbackDatabase.getInstance(context).getFeedbackBeans(OWN));
            if (ids != null && !ids.isEmpty()) {
                getFeedbackList(callback, EventType.GET_FEEDBACK_LIST_OWN, VIEW_ALL, ids, null, false);
            }
        }

        @Override
        public void getFeedbackListVoted(IFeedbackServiceEventListener callback, Context context) {
            String ids = FeedbackUtility.getIds(FeedbackDatabase.getInstance(context).getFeedbackBeans(VOTED));
            if (ids != null && !ids.isEmpty()) {
                getFeedbackList(callback, EventType.GET_FEEDBACK_LIST_VOTED, VIEW_PUBLIC, ids, null, false);
            }
        }

        @Override
        public void getFeedbackListSubscribed(IFeedbackServiceEventListener callback, Context context) {
            String ids = FeedbackUtility.getIds(FeedbackDatabase.getInstance(context).getFeedbackBeans(SUBSCRIBED));
            if (ids != null && !ids.isEmpty()) {
                getFeedbackList(callback, EventType.GET_FEEDBACK_LIST_SUBSCRIBED, VIEW_ALL, ids, null, false);
            }
        }

        @Override
        public void getFeedbackReportList(IFeedbackServiceEventListener callback) {
            getFeedbackList(callback, EventType.GET_FEEDBACK_REPORT_LIST, VIEW_ALL, null, null, true);

        }

        @Override
        public void editFeedbackStatus(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean, String status) {
            Feedback feedback = new Feedback.Builder().withFeedbackStatus(Enums.FEEDBACK_STATUS.valueOf(status.toUpperCase().replace(" ","_"))).build();
            editFeedback(callback, feedbackDetailsBean.getFeedbackId(), EDIT_FEEDBACK_STATUS, feedback);
        }

        @Override
        public void editFeedbackPublication(IFeedbackServiceEventListener callback, FeedbackBean feedbackDetailsBean, boolean isPublic) {
            Feedback feedback = new Feedback.Builder().withIsPublic(isPublic).build();
            editFeedback(callback, feedbackDetailsBean.getFeedbackId(), EDIT_FEEDBACK_PUBLICATION, feedback);
        }

        @Override
        public void deleteFeedback(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean) {
            feedbackAPI.deleteFeedback(getToken(), language, applicationId, feedbackDetailsBean.getFeedbackId()).enqueue(
                    new RepositoryCallback<ResponseBody>(callback, DELETE_FEEDBACK) {
                    });
        }

        @Override
        public void createFeedbackReport(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean, FeedbackReportRequestBody report) {
            feedbackAPI.createFeedbackReport(getToken(), language, applicationId, report).enqueue(
                    new RepositoryCallback<FeedbackReport>(callback, CREATE_FEEDBACK_REPORT) {
                    });
        }

        @Override
        public void createFeedbackResponse(IFeedbackServiceEventListener callback, FeedbackBean feedbackDetailsBean, String userName, String response) {
            FeedbackResponseRequestBody responseBean = new FeedbackResponseRequestBody(userName, response);
            feedbackAPI.createFeedbackResponse(getToken(), language, applicationId, feedbackDetailsBean.getFeedbackId(), responseBean).enqueue(
                    new RepositoryCallback<FeedbackResponse>(callback, CREATE_FEEDBACK_RESPONSE) {
                    });
        }

        @Override
        public void deleteFeedbackResponse(IFeedbackServiceEventListener callback, FeedbackBean feedbackDetailsBean, FeedbackResponseBean response) {
            feedbackAPI.deleteFeedbackResponse(getToken(), language, applicationId, feedbackDetailsBean.getFeedbackId(), response.getResponseId()).enqueue(
                    new RepositoryCallback<ResponseBody>(callback, DELETE_FEEDBACK_RESPONSE) {
                    });
        }

        @Override
        public void createVote(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean, int vote, String userName) {
            FeedbackVoteRequestBody feedbackVote = new FeedbackVoteRequestBody(vote, userName);
            feedbackAPI.createVote(getToken(), language, applicationId, feedbackDetailsBean.getFeedbackId(), feedbackVote).enqueue(
                    new RepositoryCallback<FeedbackVote>(callback, CREATE_VOTE) {
                    });
        }

        @Override
        public void getTagList(IFeedbackServiceEventListener callback) {
            feedbackAPI.getTagList(getToken(), language, applicationId).enqueue(
                    new RepositoryCallback<List<String>>(callback, GET_TAG_LIST) {
                    });
        }

        @Override
        public void getFeedbackImage(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean) {
            feedbackAPI.getFeedbackImage(getToken(), language, applicationId, feedbackDetailsBean.getBitmapName()).enqueue(
                    new RepositoryCallback<ResponseBody>(callback, GET_FEEDBACK_IMAGE) {
                    });
        }

        @Override
        public void getFeedbackAudio(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean) {
            feedbackAPI.getFeedbackAudio(getToken(), language, applicationId, feedbackDetailsBean.getAudioFileName()).enqueue(
                    new RepositoryCallback<ResponseBody>(callback, GET_FEEDBACK_AUDIO) {
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
        public void createFeedback(IFeedbackServiceEventListener callback, Context context, Feedback feedback, byte[] screenshot, File audio) {
            FeedbackDatabase.getInstance(context).writeFeedback(FeedbackUtility.feedbackToFeedbackDetailsBean(feedback).getFeedbackBean(), Enums.SAVE_MODE.CREATED);
            callback.onEventCompleted(CREATE_FEEDBACK, feedback);
        }

        @Override
        public void getFeedbackList(IFeedbackServiceEventListener callback, Context context, String relevantForUser, int backgroundColor) {
            LocalConfigurationBean configuration = ConfigurationUtility.getConfigurationFromDatabase(context);
            ArrayList<FeedbackListItem> allFeedbackList = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<>();
            for (FeedbackDetailsBean bean : RepositoryStub.getFeedback(context, 50, -30, 50, 0.1f)) {
                if (bean != null) {
                    FeedbackListItem listItem = new FeedbackListItem(context, 8, bean, configuration, backgroundColor, FeedbackListActivity.class);
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
        public void getFeedbackListPrivate(IFeedbackServiceEventListener callback) {

        }

        @Override
        public void getFeedbackReportList(IFeedbackServiceEventListener callback) {

        }

        @Override
        public void getFeedbackListOwn(IFeedbackServiceEventListener callback, Context context) {
            callback.onEventCompleted(GET_FEEDBACK_LIST_OWN_MOCK, FeedbackDatabase.getInstance(context).getFeedbackBeans(VOTED));
        }

        @Override
        public void getFeedbackListVoted(IFeedbackServiceEventListener callback, Context context) {
            callback.onEventCompleted(GET_FEEDBACK_LIST_VOTED_MOCK, FeedbackDatabase.getInstance(context).getFeedbackBeans(OWN));
        }

        @Override
        public void getFeedbackListSubscribed(IFeedbackServiceEventListener callback, Context context) {
            callback.onEventCompleted(GET_FEEDBACK_LIST_SUBSCRIBED_MOCK, FeedbackDatabase.getInstance(context).getFeedbackBeans(SUBSCRIBED));
        }

        @Override
        public void pingRepository(IFeedbackServiceEventListener callback) {
            callback.onEventCompleted(PING_REPOSITORY, false);
        }

        @Override
        public void editFeedbackStatus(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean, String status) {
            RepositoryStub.updateFeedbackStatus(feedbackDetailsBean, status);
            callback.onEventCompleted(EDIT_FEEDBACK_STATUS, false);
        }

        @Override
        public void deleteFeedback(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean) {
            RepositoryStub.deleteFeedback(feedbackDetailsBean);
            callback.onEventCompleted(DELETE_FEEDBACK_MOCK, false);
        }

        @Override
        public void createFeedbackReport(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean, FeedbackReportRequestBody report) {
            callback.onEventCompleted(CREATE_FEEDBACK_REPORT_MOCK, false);
        }

        @Override
        public void createFeedbackResponse(IFeedbackServiceEventListener callback, FeedbackBean feedbackDetailsBean, String userName, String response) {
            callback.onEventCompleted(CREATE_FEEDBACK_RESPONSE_MOCK, false);
        }

        @Override
        public void deleteFeedbackResponse(IFeedbackServiceEventListener callback, FeedbackBean feedbackDetailsBean, FeedbackResponseBean response) {
            callback.onEventCompleted(DELETE_FEEDBACK_RESPONSE_MOCK, false);
        }

        @Override
        public void editFeedbackPublication(IFeedbackServiceEventListener callback, FeedbackBean feedbackDetailsBean, boolean isPublic) {
            callback.onEventCompleted(EDIT_FEEDBACK_PUBLICATION_MOCK, false);
        }

        @Override
        public void createVote(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean, int vote, String userName) {
            //callback.onEventCompleted(EDIT_FEEDBACK_PUBLICATION_MOCK, false);
        }

        @Override
        public void getTagList(IFeedbackServiceEventListener callback) {
            callback.onEventCompleted(GET_TAG_LIST_MOCK, false);
        }

        @Override
        public void getFeedbackImage(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean) {
            callback.onEventCompleted(GET_FEEDBACK_IMAGE_MOCK, false);
        }

        @Override
        public void getFeedbackAudio(IFeedbackServiceEventListener callback, FeedbackDetailsBean feedbackDetailsBean) {
            callback.onEventCompleted(GET_FEEDBACK_AUDIO_MOCK, false);
        }

    }
}
