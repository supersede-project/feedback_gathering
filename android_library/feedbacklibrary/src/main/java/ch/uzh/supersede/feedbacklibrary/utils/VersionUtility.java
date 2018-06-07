package ch.uzh.supersede.feedbacklibrary.utils;

public class VersionUtility {
    private static final int BASE_VERSION = 1;

    private VersionUtility() {
    }

    public static int getDateVersion() {
        //        1	21-23	21.05 - 08.06	45
        //        2	24-26	09.06 - 29.06	22
        //        3	27-29	30.06 - 20.07	45
        //        4	30-32	21.07 - 10.08	22
        //        5	33-35	11.08 - 31.08	12
        if (DateUtility.getMonth() >= 8 && DateUtility.getDay() >= 31){
            return 5;
        }else if (DateUtility.getMonth() >= 8 && DateUtility.getDay() >= 10){
            return 4;
        }else if (DateUtility.getMonth() >= 7 && DateUtility.getDay() >= 20){
            return 3;
        }else if (DateUtility.getMonth() >= 6 && DateUtility.getDay() >= 29){
            return 2;
        }
        return BASE_VERSION;
    }
}
