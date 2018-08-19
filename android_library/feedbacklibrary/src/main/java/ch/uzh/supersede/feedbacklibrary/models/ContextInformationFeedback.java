package ch.uzh.supersede.feedbacklibrary.models;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.google.gson.annotations.Expose;

import java.util.Calendar;
import java.util.Locale;

import ch.uzh.supersede.feedbacklibrary.utils.*;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.NOT_YET_IMPLEMENTED_EXCEPTION;

public final class ContextInformationFeedback extends AbstractFeedbackPart {
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
                DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                this.resolution = screen.x + " x " + screen.y + ", density: "+metrics.density+ ", densityDpi: "+metrics.densityDpi;
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
            this.metaData = "MANUFACTURER:"+StringUtility.nulLSafe(Build.MANUFACTURER)+
                    ";HARDWARE:"+ StringUtility.nulLSafe(Build.HARDWARE)+
                    ";MODEL:"+StringUtility.nulLSafe(Build.MODEL)+
                    ";DEVICE:"+StringUtility.nulLSafe(Build.DEVICE)+
                    ";BRAND:"+StringUtility.nulLSafe(Build.BRAND)+
                    ";TYPE:"+StringUtility.nulLSafe(Build.TYPE);
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


    public String getAndroidVersion() {
        return androidVersion;
    }

    public String getLocalTime() {
        return localTime;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public String getCountry() {
        return country;
    }

    public String getMetaData() {
        return metaData;
    }
}
