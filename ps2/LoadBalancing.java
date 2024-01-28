/**
 * Contains static routines for solving the problem of balancing m jobs on p processors
 * with the constraint that each processor can only perform consecutive jobs.
 */
public class LoadBalancing {

    /**
     * Checks if it is possible to assign the specified jobs to the specified number of processors such that no
     * processor's load is higher than the specified query load.
     *
     * @param jobSizes the sizes of the jobs to be performed
     * @param queryLoad the maximum load allowed for any processor
     * @param p the number of processors
     * @return true iff it is possible to assign the jobs to p processors so that no processor has more than queryLoad load.
     */
    public static boolean isFeasibleLoad(int[] jobSizes, int queryLoad, int p) {
        // runs in O(n) time
        if (jobSizes == null || jobSizes.length == 0) {
            return false;
        }
        int processorsNeeded = 1;

        int totalLoad = 0;
        for (int i = 0; i < jobSizes.length; i++){
            totalLoad += jobSizes[i];
            if (jobSizes[i] > queryLoad){
                return false;
            }
            if (totalLoad > queryLoad){
                processorsNeeded++;
                totalLoad = 0;
                i--;
            }
        }
        return processorsNeeded <= p;

    }



    private static boolean validJobs(int[] jobSizes, int p){
        if (jobSizes.length == 0){
            return false;
        }
        if (p <= 0){
            return false;
        }
        else{
            return true;
        }
    }



    //returns true if given load is achievable with the p limit, and each subArray can hit max
    private static boolean possible(int[] jobSizes, int mid, int p){
        int count = 1; //compare this value with p
        int subTotal = 0; //total value of each subArray
        //System.out.println("new");
        //System.out.println(mid);
        for (int i = 0; i < jobSizes.length; i++){
            if (jobSizes[i] > mid){
                return false;
            }
            else if (subTotal+jobSizes[i] <= mid){
                subTotal += jobSizes[i];
            }
            else {
                subTotal += jobSizes[i];
                //System.out.println(subTotal);
                if (i > 0){
                    i--;
                }
                count++;
                subTotal = 0;
                //System.out.println("count =" + count);

            }

        }


        if (count > p){
            return false;
        }
        else{
            return true;
        }



    }

    /**
     * Returns the minimum achievable load given the specified jobs and number of processors.
     *
     * @param jobSizes the sizes of the jobs to be performed
     * @param p the number of processors
     * @return the maximum load for a job assignment that minimizes the maximum load
     */
    public static int findLoad(int[] jobSizes, int p) {
        // TODO: runs in nLogn time
        if (!validJobs(jobSizes,p)){
            return -1;
        }
        int begin = 0;
        int end = 0;
        for (int jobSize : jobSizes) {
            end += jobSize; // end is the sum of everything in the array
        }
        while (begin < end){
            int mid = (begin + end) / 2;
            if (possible(jobSizes, mid, p)){
                end = mid;
            }
            else{
                begin = mid+1;
            }

        }

        return end;
        
    }

    // These are some arbitrary testcases.
    public static int[][] testCases = {
            {1, 3, 5, 7, 9, 11, 10, 8, 6, 4},
            {67, 65, 43, 42, 23, 17, 9, 100},
            {4, 100, 80, 15, 20, 25, 30},
            {2, 3, 4, 5, 6, 7, 8, 100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83},
            {7}
    };

    /**
     * Some simple tests for the findLoad routine.
     */
    public static void main(String[] args) {
        for (int p = 1; p < 30; p++) {
            System.out.println("Processors: " + p);
            for (int[] testCase : testCases) {
                System.out.println(findLoad(testCase, p));
            }
        }
    }
}
