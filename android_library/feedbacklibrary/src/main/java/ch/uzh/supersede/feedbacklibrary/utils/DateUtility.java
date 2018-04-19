package ch.uzh.supersede.feedbacklibrary.utils;

import java.util.Calendar;

public class DateUtility {

    private DateUtility() {
    }

    public static String getDate() {
        return getDateFromLong(System.currentTimeMillis());
    }

    public static String getPastDateString(int yearsBack) {
        long yearsInMs = (long) ((31540000000L * yearsBack) * Math.random());
        return getDateFromLong(System.currentTimeMillis() - yearsInMs);
    }

    public static Long getPastDateLong(int yearsBack) {
        long yearsInMs = (long) ((31540000000L * yearsBack) * Math.random());
        return System.currentTimeMillis() - yearsInMs;
    }

    public static String getDateFromLong(long ms) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ms);
        String year = formatDateNumber(calendar.get(Calendar.YEAR));
        String month = formatDateNumber(calendar.get(Calendar.MONTH) + 1);
        String day = formatDateNumber(calendar.get(Calendar.DAY_OF_MONTH));
        return day.concat(".").concat(month).concat(".").concat(year);
    }

    private static String formatDateNumber(int number) {
        return number < 10 ? "0" + number : String.valueOf(number);
    }

}
