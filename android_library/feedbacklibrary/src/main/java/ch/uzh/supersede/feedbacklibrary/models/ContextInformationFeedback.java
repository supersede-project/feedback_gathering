package ch.uzh.supersede.feedbacklibrary.models;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.text.format.Time;
import android.view.WindowManager;

import com.google.gson.annotations.Expose;

import java.util.Calendar;
import java.util.Locale;

import ch.uzh.supersede.feedbacklibrary.utils.CompareUtility;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.NOT_YET_IMPLEMENTED_EXCEPTION;

public class ContextInformationFeedback extends AbstractFeedbackPart {
    @Expose
    private String resolution;
    @Expose
    private String androidVersion;
    @Expose
    private String localTime;
    @Expose
    private String timeZone;
    @Expose
    private String country;
    @Expose
    private String metaData;
    private String userAgent;
    private String devicePixelRatio;
    private String region;
    private String url;

    public ContextInformationFeedback() {
        // NOP
    }

    public static class Builder {
        private Context context;
        private String resolution;
        private String androidVersion;
        private String localTime;
        private String timeZone;
        private String country;
        private String metaData;
        private String userAgent;
        private String devicePixelRatio;
        private String region;
        private String url;

        public Builder() {
        }

        public Builder(Context context) {
            this.context = context;
        }

        public Builder withResolution() {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                Point screen = new Point();
                windowManager.getDefaultDisplay().getRealSize(screen);
                this.resolution = screen.x + "x" + screen.y;
            }
            return this;
        }

        public Builder withUserAgent() {
            throw new UnsupportedOperationException(NOT_YET_IMPLEMENTED_EXCEPTION);
        }

        public Builder withAndroidVersion() {
            this.androidVersion = Build.VERSION.RELEASE;
            return this;
        }

        public Builder withLocalTime() {
            this.localTime = Calendar.getInstance().getTime().toString();
            return this;
        }

        public Builder withTimeZone() {
            this.timeZone = Time.getCurrentTimezone();
            return this;
        }

        public Builder withDevicePixelRation() {
            throw new UnsupportedOperationException(NOT_YET_IMPLEMENTED_EXCEPTION);
        }

        public Builder withCountry() {
            this.country = Locale.getDefault().getCountry();
            return this;
        }

        public Builder withRegion() {
            throw new UnsupportedOperationException(NOT_YET_IMPLEMENTED_EXCEPTION);
        }

        public Builder withUrl() {
            throw new UnsupportedOperationException(NOT_YET_IMPLEMENTED_EXCEPTION);
        }

        public Builder withMetaData() {
            this.metaData = Build.MODEL;
            return this;
        }

        public ContextInformationFeedback build() {
            if (CompareUtility.notNull(resolution, androidVersion, localTime, timeZone, country, metaData)) {
                ContextInformationFeedback contextInformationFeedback = new ContextInformationFeedback();
                contextInformationFeedback.resolution = this.resolution;
                contextInformationFeedback.androidVersion = this.androidVersion;
                contextInformationFeedback.localTime = this.localTime;
                contextInformationFeedback.timeZone = this.timeZone;
                contextInformationFeedback.country = this.country;
                contextInformationFeedback.metaData = this.metaData;
                return contextInformationFeedback;
            }
            return null;
        }
    }

    public String getResolution() {
        return resolution;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public String getLocalTime() {
        return localTime;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public String getDevicePixelRatio() {
        return devicePixelRatio;
    }

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public String getUrl() {
        return url;
    }

    public String getMetaData() {
        return metaData;
    }
}
