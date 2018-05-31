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

    public static Long getPastDateAfter(long afterDate) {
        long yearsInMs = (long)((System.currentTimeMillis()-afterDate) * Math.random());
        return System.currentTimeMillis() - yearsInMs;
    }

    public static String getDateFromLong(long ms) {
        String year = formatDateNumber(getYear(ms));
        String month = formatDateNumber(getMonth(ms));
        String day = formatDateNumber(getDay(ms));
        return day.concat(".").concat(month).concat(".").concat(year);
    }
    public static int getYear(){
        return getYear(System.currentTimeMillis());
    }
    public static int getMonth(){
        return getMonth(System.currentTimeMillis());
    }
    public static int getDay(){
        return getDay(System.currentTimeMillis());
    }

    private static int getYear(long ms){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ms);
        return calendar.get(Calendar.YEAR);
    }
    private static int getMonth(long ms){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ms);
        return calendar.get(Calendar.MONTH)+1;
    }
    private static int getDay(long ms){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ms);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    private static String formatDateNumber(int number) {
        return number < 10 ? "0" + number : String.valueOf(number);
    }

}
