package com.tianjiqx.sort;

public class Sort {

  public static int[] MergeSort(int[] nums) {
    // 同样申请临时空间
    int[] tempNums = new int[nums.length];
    int len = 1;
    while (len < nums.length) {
      // 一趟归并，归并结果存放在tempNums
      MergePass(nums, tempNums,len);
      len *=2;
      // 一趟归并，归并结果存放在nums
      // 对于len > nums.length 情况，实际只做了一次O(N)复制
      MergePass(tempNums, nums,len);
      len *=2;
    }
    // 每次循环最后存放在nums，所以返回nums
    return nums;
  }
  // nums 待排， mergedNums 作为结果存储，len当前有序子序列大小
  // 一趟排序，令每个相邻的2*len大小的子数组有序
  private static void MergePass(int[] nums, int[] mergedNums, int len) {
    int i = 0;
    int size = nums.length;
    // 使每个2*len的子序列有序
    while (i+ 2*len  < size) {
      // 合并len的两个有序子数组
      merge(nums, mergedNums, i, i + len - 1, i + 2*len -1);
      i+=2*len;
    }
    // 剩余不足2*len的末尾子序列
    if (i + len < size - 1) {
      // 合并2个有序子数组
      merge(nums, mergedNums, i, i + len - 1, size -1);
    } else {
      // 只有一个长度在len内的有序数组，那么直接复制
      while(i < size) {
        mergedNums[i]=nums[i];
        i++;
      }
    }
  }

  // 合并相邻的有序子数组
// nums 待排， mergedNums 作为结果存储
// left 是相邻有序子数组最左的下标，mid是相邻有序子数组左区间的末尾下标，right是右区间的末尾下标
  private static void merge(int[] nums, int[] mergedNums, int left, int mid, int right) {
    // i,j 分别左右区间指针，k记录合并结果指针
    int i = left, j = mid +1, k = left;
    // 比较左右区间，结果按顺序存储在mergedNums
    while( i <= mid && j <= right) {
      if (nums[i] <= nums [j]) {
        mergedNums[k++] = nums[i++];
      } else {
        mergedNums[k++] = nums[j++];
      }
    }
    // 剩余子区间直接追加到末尾
    // 左区间有剩余元素
    while(i<=mid) {
      mergedNums[k++]=nums[i++];
    }
    // 右区间
    while(j<=right) {
      mergedNums[k++]=nums[j++];
    }
  }


}
