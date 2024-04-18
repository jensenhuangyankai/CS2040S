import java.util.Arrays;

class InversionCounter {

    public static long mergeSort(int[] arr, int[] spaceArr, int begin, int end) {
        if (begin >= end) {
            return 0;
        }
        long countSwap = 0;
        int mid = begin + (end - begin) / 2;

        countSwap += mergeSort(arr, spaceArr, begin, mid);
        countSwap += mergeSort(arr, spaceArr,mid + 1, end);
        countSwap += helper(arr, spaceArr, begin, mid, mid+1, end);

        return countSwap;
    }
    public static long countSwaps(int[] arr) {
        int[] temp = new int[arr.length];
        return mergeSort(arr, temp, 0,  arr.length - 1);
    }

    public static long helper(int[] arr, int[] spaceArr, int left1, int right1, int left2, int right2) {
        int start = left1;
        int currentIndex = start;
        long countSwap = 0;

        while (left1 <= right1 && left2 <= right2) {
            if (arr[left1] <= arr[left2]) {
                spaceArr[currentIndex] = arr[left1];
                left1++;
            } else {
                spaceArr[currentIndex] = arr[left2];
                countSwap += (right1 - (left1 - 1));
                left2++;
            }

            currentIndex++;
        }

        while (left2 <= right2) {
            spaceArr[currentIndex] = arr[left2];
            left2++;
            currentIndex++;
        }

        while (left1 <= right1) {
            spaceArr[currentIndex] = arr[left1];
            left1++;
            currentIndex++;
        }
        
        //copy arr
        for (int i = start; i <= right2; i++) {
            arr[i] = spaceArr[i];
        }

        return countSwap;
    }

    public static long mergeAndCount(int[] arr, int left1, int right1, int left2, int right2) {
        if (arr.length == 0 || arr.length == 1) return 0;
        int[] temp = new int[arr.length];
        return helper(arr, temp, left1, right1, left2, right2);
    }
}
