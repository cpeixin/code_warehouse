package leetcode;

import java.util.Arrays;

public class MergeSort {
    public int[] merge_sort(int[] nums){
        if (nums.length <= 1) return nums;
        int mid = (nums.length - 1) >> 1;
        int[] left = merge_sort(Arrays.copyOfRange(nums, 0, mid));
        int[] right = merge_sort(Arrays.copyOfRange(nums, mid, nums.length - 1));
        return merge(left, right);
    }

    public int[] merge(int[] left, int[] right){
        int[] nums = new int[left.length + right.length];
        int left_index = 0, right_index = 0, k=0;
        while (left_index < left.length && right_index < right.length){
            if (left[left_index] < right[right_index]){
                nums[k] = left[left_index];
                k++;
                left_index++;
            }else{
                nums[k] = right[right_index];
                k++;
                right_index++;
            }
        }

        if (left_index == left.length){
            for (int i: ri)
        }else{

        }

    }
}
