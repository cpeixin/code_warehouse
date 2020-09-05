package leetcode;

import java.util.ArrayList;

public class QuickSort {
    public static void  quickSort(int[] nums, int begin, int end){
        if (begin > end) return;
        int pivot = partition(nums, begin, end);
        quickSort(nums, begin, pivot-1);
        quickSort(nums, pivot+1, end);
    }


    private static int partition(int[] nums, Integer begin, Integer end){
        int pivot = nums[begin];
        int mark = begin;
        for (int i = begin+1; i<end+1; i++){
            if (nums[i] < pivot){
                mark += 1;
                int tmp = nums[mark]; nums[mark] = nums[i]; nums[i] = tmp;
            }
        }
        int tmp = nums[mark]; nums[mark] = nums[begin]; nums[begin] = tmp;
        return mark;
    }

    public static void main(String[] args){
        int[] nums = {3,2,1,4,5,7,6};
        quickSort(nums, 0, nums.length-1);
        for(int i: nums){
            System.out.println(i);
        }
    }
}
