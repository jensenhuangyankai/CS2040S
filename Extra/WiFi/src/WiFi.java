import java.util.Arrays;

class WiFi {
    public static double roundToHalf(double d) {
        return Math.round(d * 2) / 2.0;
    }

    /**
     * Implement your solution here
     */
    public static double computeDistance(int[] houses, int numOfAccessPoints) {
        double begin = 0;
        Arrays.sort(houses);
        double end = houses[houses.length-1] - houses[0];

        while (begin < end){
            double mid = (end+begin)/2;
            if (coverable(houses, numOfAccessPoints, mid)){
                end = mid;
            }
            else {
                begin = roundToHalf(mid);
            }
        }
        return end;

    }

    /**
     * Implement your solution here
     */
    public static boolean coverable(int[] houses, int numOfAccessPoints, double distance) {
        // logic is if houses[i+n] - houses[i] is less than 2*r, it will be covered by 1 router
        // else plus one on accesspoints needed
        Arrays.sort(houses);
        int apNeeded = 1;
        double distanceCovered = 2 * distance;

        for (int i = 0; i < houses.length; i++){
            int house0 = houses[i];
            while (i < houses.length-1){
                if (houses[i+1] - house0 <= distanceCovered){
                    i++;
                }
                else{
                    apNeeded++;
                    break;
                }

            }
        }
        if (apNeeded > numOfAccessPoints) {
            return false;
        }
        else {
            return true;
        }

    }
}
