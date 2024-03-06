import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.util.Arrays;
import java.util.Random;

public class SortingTester {
    /*
    private static void bubbleSort(KeyValuePair[] arr, int size){
        boolean swapped = false;

        for (int i = 0; i < size - 1; i++){
            for (int j = 0; j < size - i - 1; j++){
                if (arr[j].compareTo(arr[j+1]) == 1) { // if j larger than j+1
                    KeyValuePair temp = arr[j+1];
                    arr[j+1] = arr[j];
                    arr[j] = temp;
                    swapped = true;
                }

            }
            if (!swapped) break;
        }

    }
    
     */

    public static boolean checkSort(ISort sorter, int size) {
        KeyValuePair[] testArray;
        testArray = new KeyValuePair[10];
        testArray[0] = new KeyValuePair(10, 20);
        testArray[1] = new KeyValuePair(5, 20);
        testArray[2] = new KeyValuePair(8, 20);
        testArray[3] = new KeyValuePair(8, 20);
        testArray[4] = new KeyValuePair(12, 20);
        testArray[5] = new KeyValuePair(14, 20);
        testArray[6] = new KeyValuePair(1, 20);
        testArray[7] = new KeyValuePair(412, 20);
        testArray[8] = new KeyValuePair(32, 20);
        testArray[9] = new KeyValuePair(75, 20);


        ///for (KeyValuePair val: testArray) System.out.println(val.toString());
        //KeyValuePair[] copy = new KeyValuePair[size];
        //System.arraycopy(testArray, 0, copy, 0, size);
        sorter.sort(testArray);

        for (int i = 0; i < size-1; i++){
            if (testArray[i].compareTo(testArray[i+1]) == 1){
                return false;
            }
        }
        //for (KeyValuePair val: testArray) System.out.println(val.toString());
        return true;
        //bubbleSort(copy,size);


        //System.out.println("--------SORTED-------");

        //System.out.println("array2 sorted");
        //for (KeyValuePair val: copy) System.out.println(val.toString());
        //System.out.println(Arrays.equals(testArray, copy));
        //return Arrays.equals(testArray, copy);
    }

    /*
    public static boolean isStable(ISort sorter, int size) {
        // TODO: implement this
        return false;
    }

    public static void main(String[] args) {


        System.out.println(checkSort(new SorterA(), 10));

        //System.out.println(checkSort(new SorterB(), 3));
    }
    */
}
