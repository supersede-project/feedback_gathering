package ch.uzh.supersede.feedbacklibrary.utils;

import okhttp3.ResponseBody;

public final class RestUtility {

    private RestUtility() {

    }

    public static boolean responseEquals(Object response, Object equalsObject) {
        if (response == null || equalsObject == null) {
            return false;
        }
        if (response instanceof ResponseBody) {
            try {
                String responseString = ((ResponseBody) response).string();
                Object castedResponse = equalsObject.getClass().cast(responseString);
                return equalsObject.equals(castedResponse);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}
