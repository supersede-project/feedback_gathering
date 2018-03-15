package ch.uzh.supersede.feedbacklibrary.services;

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

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.activities.FeedbackActivity;
import ch.uzh.supersede.feedbacklibrary.configurations.ConfigurationItem;
import ch.uzh.supersede.feedbacklibrary.configurations.OrchestratorConfigurationItem;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService.ConfigurationRequestWrapper;
import ch.uzh.supersede.feedbacklibrary.utils.DialogUtils;
import ch.uzh.supersede.feedbacklibrary.utils.Utils;
import retrofit2.Response;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.FeedbackActivityConstants.*;

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


}
