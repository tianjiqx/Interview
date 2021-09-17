package com.tianjiqx.sort;

import java.util.Arrays;

public class Sort {


  public static int[] MergeSort(int[] nums) {
    // 申请临时空间
    int[] tempNums = new int[nums.length];
    int len = 1;
    return Sort.MergeSort(nums, tempNums, len, new SortRange(0, nums.length));
  }

  public static int[] MergeSort(int[] nums, int[] tempNums, int len, SortRange sortRange) {
    while (len < sortRange.length()) {
      // 一趟归并，归并结果存放在tempNums
      MergePass(nums, tempNums, len, sortRange);
      len *= 2;
      // 一趟归并，归并结果存放在nums
      // 对于len > nums.length 情况，实际只做了一次O(N)复制
      MergePass(tempNums, nums, len, sortRange);
      len *= 2;
    }
    // 每次循环最后存放在nums，所以返回nums
    return nums;
  }

  // nums 待排， mergedNums 作为结果存储，len当前有序子序列大小
  // 一趟排序，令每个相邻的2*len大小的子数组有序
  private static void MergePass(int[] nums, int[] mergedNums, int len, SortRange sortRange) {
    //System.out.println("before:"+Arrays.toString(nums));
    int start = sortRange.start;
    int end = sortRange.end;
    // 使每个2*len的子序列有序
    while (start + 2 * len <= end) {
      // 合并len的两个有序子数组
      merge(nums, mergedNums, start, start + len - 1, start + 2 * len - 1);
      start += 2 * len;
    }
    // 剩余不足2*len的末尾子序列
    //System.out.println("i = "+i+ " len = "+ len + " size = "+ size);
    if (start + len < end) {
      // 合并2个有序子数组
      merge(nums, mergedNums, start, start + len - 1, end - 1);
    } else {
      // 只有一个长度在len内的有序数组，那么直接复制
      while (start < end) {
        mergedNums[start] = nums[start];
        start++;
      }
    }
    //System.out.println("after:"+Arrays.toString(mergedNums));
  }

  // 合并相邻的有序子数组
// nums 待排， mergedNums 作为结果存储
// left 是相邻有序子数组最左的下标，mid是相邻有序子数组左区间的末尾下标，right是右区间的末尾下标
  public static void merge(int[] nums, int[] mergedNums, int left, int mid, int right) {
    // i,j 分别左右区间指针，k记录合并结果指针
    int i = left, j = mid + 1, k = left;
    // 比较左右区间，结果按顺序存储在mergedNums
    while (i <= mid && j <= right) {
      if (nums[i] <= nums[j]) {
        mergedNums[k++] = nums[i++];
      } else {
        mergedNums[k++] = nums[j++];
      }
    }
    // 剩余子区间直接追加到末尾
    // 左区间有剩余元素
    while (i <= mid) {
      mergedNums[k++] = nums[i++];
    }
    // 右区间
    while (j <= right) {
      mergedNums[k++] = nums[j++];
    }
  }


}
