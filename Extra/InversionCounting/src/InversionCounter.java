class InversionCounter {

    public static long countSwaps(int[] arr) {
        long inversions = 0;
        for (int i = 0; i < arr.length; i++){
            for (int j = i; j < arr.length; j++){
                if (arr[i] > arr[j]) inversions++;
            }
        }
        return inversions;
    }

    /**
     * Given an input array so that arr[left1] to arr[right1] is sorted and arr[left2] to arr[right2] is sorted
     * (also left2 = right1 + 1), merges the two so that arr[left1] to arr[right2] is sorted, and returns the
     * minimum amount of adjacent swaps needed to do so.
     */
    public static long mergeAndCount(int[] arr, int left1, int right1, int left2, int right2) {
        int[] result = new int[arr.length];
        long swaps = 0;

        int counterLeft = 0;
        int counterRight = left2;

        int resultCounter = 0;
        while (counterLeft <= right1 && counterRight <= right2){
            if (arr[counterRight] < arr[counterLeft]) {
                result[resultCounter] = arr[counterRight];
                swaps += counterRight-resultCounter;
                counterRight++;
            }
            else{
                result[resultCounter] = arr[counterLeft];
                counterLeft++;
            }
            resultCounter++;
        }
        while (counterLeft <= right1){
            result[resultCounter++] = arr[counterLeft++];
        }
        while (counterRight <= right2){
            result[resultCounter++] = arr[counterRight++];
        }
        arr = result;
        return swaps;

    }
}
