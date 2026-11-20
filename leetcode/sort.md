

## 排序算法

口诀：

最快的排序：“快希**归**堆”

不稳定排序：“快希**选**堆”

稳定排序：“插冒归计基”

### 4.1 快速排序

```java
// 递归
public class QuickSort implements IArraySort {
    @Override
    public int[] sort(int[] sourceArray) throws Exception {
        // 对 arr 进行拷贝，不改变参数内容
        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);

        return quickSort(arr, 0, arr.length - 1);
    }
    private int[] quickSort(int[] arr, int left, int right) {
        // 区间范围大于1，继续递归
        if (left < right) {
            // 一轮排序，分区位置是最终位置
            int partitionIndex = partition(arr, left, right);
            // 左右区间，递归排序
            quickSort(arr, left, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, right);
        }
        return arr;
    }
    private int partition(int[] arr, int left, int right) {
        // 设定基准值（pivot）
        int pivot = left;
        /*
        int i = rand() % (right - left + 1) + left; // 随机选一个作为我们的主元
        swap(arr,i, pivot);
        */
        // index表示[left+1,right]区间第一个比pivot大的位置，在它左边都是小于的pivot，右边是比pivot大的元素
        int index = pivot + 1;
        // pivot的位置暂时不处理
        //从left+1开始，遍历，发现比pivot小的元素，交换与index的位置，并index后移，
        //确保index位置左边都是小于的pivot，右边是比pivot大的元素。
        for (int i = index; i <= right; i++) {
            if (arr[i] < arr[pivot]) {
                swap(arr, i, index);
                index++;
            }
        }
        // 更新基准值的位置，index是第一个比pivot大的元素，index - 1位置才是真正pivot的位置，
        // 而index - 1位置的值，一定是小于或等于pivot的值，可以放心交换。
        swap(arr, pivot, index - 1);
        return index - 1;
    }
    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
// 传统，非递归需要双指针来回交换的值
```

算法步骤：

- 从数列中挑出一个元素，称为 "基准"（pivot）;

- 重新排序数列，所有元素比基准值小的摆放在基准前面，所有元素比基准值大的摆在基准的后面（相同的数可以到任一边）。在这个分区退出之后，该基准就处于数列的中间位置。这个称为分区（partition）操作；
- 递归地（recursive）把小于基准值元素的子数列和大于基准值元素的子数列排序；



### 4.2 归并排序/mergesort

原理：

利用分治的思想来对序列进行排序。

对一个长为n的待排序的序列，我们将其分解成两个长度为n/2的子序列。

每次先递归调用函数使两个子序列有序，然后我们再线性合并两个有序的子序列使整个序列有序。

分：

- 规模缩小，多次二分，最后大小为1
  - 二分的原因，是为了在合并的时候，2个子序列大小接近，比较时能够

治：

- 问题降维，有序多维数组合并，多次合并，最终合并所需要全局有序



特性：

- 稳定排序
- 总时间复杂度O(nlogn)，空间复杂度O(n)
  - logN的合并次数，每次合并的时间复杂度是n

```java
class Solution {
    // 临时空间, 申请了额外O(N)的空间
    int[] tmp;

    public int[] sortArray(int[] nums) {
        tmp = new int[nums.length];
        mergeSort(nums, 0, nums.length - 1);
        return nums;
    }
	// 递归
    public void mergeSort(int[] nums, int l, int r) {
        // 区间为1，停止递归
        if (l >= r) {
            return;
        }
        int mid = l + ( r -l ) >> 1;
        // 递归，排序左右
        mergeSort(nums, l, mid);
        mergeSort(nums, mid + 1, r);
        // 合并有序子序列
        int i = l, j = mid + 1;
        int cnt = 0;
        while (i <= mid && j <= r) {
            if (nums[i] <= nums[j]) {
                tmp[cnt++] = nums[i++];
            } else {
                tmp[cnt++] = nums[j++];
            }
        }
        // 处理剩余部分
        while (i <= mid) {
            tmp[cnt++] = nums[i++];
        }
        while (j <= r) {
            tmp[cnt++] = nums[j++];
        }
        // 将已拍好序的子序列，复制回原数组
        for (int k = 0; k < r - l + 1; ++k) {
            nums[k + l] = tmp[k];
        }
    }
}

/** 
非递归与递归版本相反，
从1开始，逐渐增大已排序子数组的长度大小，直到长度超过原始数组长度。
*/
public int[] MergeSort(int[] nums) {
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
public void MergePass(int[] nums, int[] mergedNums, int len) {
    int i = 0;
    int size = nums.length;
    // 使每个2*len的子序列有序
    while (i+ 2*len  <= size) {
        // 合并len的两个有序子数组
        merge(nums, mergedNums, i, i + len - 1, i + 2*len -1);
        i+=2*len;
    }
    // 剩余不足2*len的末尾子序列
   	if (i + len < size) {
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
void merge(int[] nums, int[] mergedNums, int left, int mid, int right) {
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
```



优化：

- 对小规模子数组使用插入排序
  - 插入或者选择小规模数组（<15）上比递归版本的归并要快  （10%~15%）
    - 元素数据较小时，数据近乎有序的几率比较大
- 测试子数组是否有序
  - `a[mid] <= a[mid+1]` 时证明直接合并即可
- 减少复制，交替交换辅助数组和输入数组角色
  - 非递归版本已做

**大文件归并排序/ 外部排序**

- 一个大文件，在磁盘上，但是内存存不下
- 一个大文件，在单个服务器上存放不下，将这个大文件的内容进行排序

原理：

- 分割，然后排序单个小文件，使其内部有序
- 多路归并，将每个小文件的数读取一个（批），在内存中进行归并，即为最终的大文件的一个（批）有序结果，循环该过程，完成整个文件的排序



(TODO: pingcap/tanlent  tidb mergesort  go语言版本，性能优化，并发多协程版本：

按cpu核数划分几个子区间进行merge sort，最后单协程做最后的merge？

- [java版本 并行mergesort实现 doc](https://github.com/tianjiqx/Interview/blob/main/program_language/java/%E5%B9%B6%E8%A1%8CMergeSort.md)   [code](https://github.com/tianjiqx/Interview/tree/main/program_language/java/TestSort))



#### REF

- [图解排序算法(四)之归并排序](https://www.cnblogs.com/chengxiao/p/6194356.html)
- [归并排序笔记](https://wiesen.github.io/post/%E5%BD%92%E5%B9%B6%E6%8E%92%E5%BA%8F%E7%AC%94%E8%AE%B0/)


