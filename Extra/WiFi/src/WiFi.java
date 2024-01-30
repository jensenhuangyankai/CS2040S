import java.util.Arrays;

class WiFi {

    /**
     * Implement your solution here
     */
    public static double computeDistance(int[] houses, int numOfAccessPoints) {
        return 0.0;
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
            if (i < houses.length-1){
                while (houses[i+1] - house0 < distanceCovered){
                    i++;
                }
                apNeeded++;
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
