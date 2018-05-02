package ch.uzh.supersede.feedbacklibrary.utils;

public class NumberUtility {

    private NumberUtility() {
    }

    public static int divide(int a, int b) {
        return (int) ((float) a / (float) b);
    }

    public static int divide(int a, float b) {
        return (int) ((float) a / b);
    }

    public static int multiply(int a, float b) {
        return (int) (a * b);
    }

    public static int multiply(int a, double b) {
        return (int) (a * b);
    }


    public static int randomPosition(Object[] array) {
        if (array == null) {
            return 0;
        }
        return randomInt(0, array.length - 1);
    }

    public static int randomInt(int min, int max) {
        if (max < min) {
            return 0;
        }
        int diff = (max - min) + 1;
        int random = multiply(diff, Math.random());
        if (diff == random) {
            return randomInt(min, max);
        }
        return min + random;
    }


    /**
     * Creates an ID from the Application id (package+application-name)
     * The generation of this ID is based on the documented logic below and is persistent.
     *
     * @param applicationString
     * @return applicationId
     * Max Long:
     * 9 223 372 036 854 775 807
     * Format:
     * [Head, 1 digit] [Length 3 digits] [Application 3 digits] [Package 4 x 3 digits]
     * Example:
     * ch.uzh.supersede.host.hostapplication
     * --> [reversed] hostapplication.host.supersede.uzh.ch
     *  [1 {1d}] [length {3d}] [hostapplication {3d}] [host {3d}] [supersede {3d}] [uzh {3d}] [ch {3d}]
     * Specific:
     * 1020618446618446618  --origin--> "host.hostapplication"
     * 1030618976446618976  --origin--> "supersede.host.hostapplication"
     * 1034618343976446618  --origin--> "uzh.supersede.host.hostapplication"
     * 1037618203343976446  --origin--> "ch.uzh.supersede.host.hostapplication"
     * 1047618203343976446  --origin--> "ch.uzh.supersede.host.something.hostapplication"
     *
     */
    public static long createApplicationIdFromString(String applicationString){
        if (applicationString == null || applicationString.contains("\\.")){
            throw new IllegalArgumentException("Something went wrong, did you register a Application-Name and Package-Location? Current Application-Id: "+applicationString);
        }
        String[] pts = applicationString.split("\\.");
        int type = pts.length-1>3?3:pts.length-1;
        int[][] pointer = {{0,0,0,0},{0,1,0,1},{0,1,2,0},{0,1,2,3}};
        String head = "1";
        String length = fillWithZeros(String.valueOf(applicationString.length()),3);
        String application = stringToLongString(pts[pts.length-1],3);
        String package1 = stringToLongString(pts[pointer[type][0]],3);
        String package2 = stringToLongString(pts[pointer[type][1]],3);
        String package3 = stringToLongString(pts[pointer[type][2]],3);
        String package4 = stringToLongString(pts[pointer[type][3]],3);
        return -Long.valueOf(head+length+application+package1+package2+package3+package4);
    }

    private static String stringToLongString(String s, int maxDigits){
        long stringLong = stringToLong(s,maxDigits);
        return fillWithZeros(String.valueOf(stringLong),maxDigits);
    }
    private static String fillWithZeros(String number, int digits){
        for (int i = number.length();i < digits; i++){
            number = "0".concat(number);
        }
        return number;
    }

    private static long stringToLong(String s, int maxDigits){
        long l = 0;
        for (char c : s.toCharArray()){
            l += c;
        }
        long valWithDigits = maxValWithDigits(maxDigits);
        return valWithDigits <= l ?l%valWithDigits:l;
    }
    private static long maxValWithDigits(int maxDigits){
        return (long)Math.pow(10d,(double)maxDigits);
    }

}
