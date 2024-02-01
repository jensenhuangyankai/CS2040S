public class Guessing {

    // Your local variables here
    private int low = 0;
    private int high = 1000;

    private int mid = (low + high) / 2;
    /**
     * Implement how your algorithm should make a guess here
     */
    public int guess() {

        if (low < high){

            return mid;
        }
        else{

            return high;
        }

    }

    /**
     * Implement how your algorithm should update its guess here
     */
    public void update(int answer) {

        //System.out.println(mid);
        if (answer == -1){
            low = mid+1;
            mid = (low + high) / 2;
        }
        else {
            high = mid;
            mid = (low + high) / 2;
        }
    }
}
