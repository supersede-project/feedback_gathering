package ch.uzh.supersede.feedbacklibrary.utils;

import java.util.Calendar;

public class DateUtility {
    public static String getDate(boolean retardFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        String year = formatDateNumber(calendar.get(Calendar.YEAR));
        String month = formatDateNumber(calendar.get(Calendar.MONTH) + 1);
        String day = formatDateNumber(calendar.get(Calendar.DAY_OF_MONTH));
        return retardFormat ? month.concat(".").concat(day).concat(".").concat(year) : day.concat(".").concat(month).concat(".").concat(year);
    }

    private static String formatDateNumber(int number) {
        return number < 10 ? "0" + number : String.valueOf(number);
    }

}
