
## 二分查找

```java
public int search (int key, int[] array) {
    int l = 0, h = array.length - 1;
    while( l <= h) {
        int mid = l + (h-l) / 2;
        if (key  == array[mid]) return mid;
        if (key < array[mid]) h = mid -1;
        else l= mid + 1;
    }
    return -1;
}
```

该二分查找的写法，关键：

- 循环条件 l <= h，而不是l < h
- 中值更新mid = l + (h-l)/2
  - 不直接(l+h)/2，避免溢出
- 边界值更新l=mid+1, h = mid -1;（循环l和h可以等，则边界更新时使用开区间）
  - h的更新是由循环条件决定，l <= h时,若h = mid，会导致循环无法退出，如l=1, h=1时，此时mid=1。
  - l<h，若 h =mid -1, 会跳过查找的数，如[1,2,3]，查找1时，l,h.mid，将会是{0,2,1}，{0,0,-} 由于l == h 直接退出循环
  - l<h作为二分查找，是在题目需要检查循环体外l==h结果是否符合预期，例如序列[1,2,3],查询3时，[l,h]为[2,2]跳出循环体，需要检查跳过pos=2的结果是否为查找值。而对于某些题目，就是希望得出区间为1的结果，不是查找mid值，才使用该循环条件。

二分查找原理(循环条件l<=h)：

给定区间[l,h]，查看中值，比较，迭代选择左，右区间。迭代到最后，l,h有以下几种情形：

- 区间length=4，[l,h]为[0,3]，mid等于1，产生区间长度为1和2的[l,h]子区间
- 区间length=3，[l,h]为[0,2],mid等于1，产生2个区间长度为1的[l,h]子区间
- 区间length=2, [l,h]为[0,1],mid等于0，要么找到退出，要么产生1个区间长度为1的[l,h]子区间
- 区间length=1, [l,h]为[0,0],mid等于0，要么找到，要不未找到，都退出。

二分查找原理2(循环条件l<h)：

给定区间[l,h]，查看中值，比较，迭代选择左，右区间。迭代到最后，l,h有以下几种情形：

- 区间length=4，[l,h]为[0,3]，mid等于1，产生区间2个长度为2的[l,h]子区间[0,1]和[2,3]
- 区间length=3，[l,h]为[0,2],mid等于1，产生区间长度为2和1的[l,h]子区间[0,1]和[2,2]（退出循环，检查是否找到）
- 区间length=2, [l,h]为[0,1],mid等于0，要么找[0,0]到退出，要么找不到也退出循环产生区间[1,1]（注意若循环结束结果，最后需要判断l是否等于查找值）
- 区间length=1, [l,h]为[0,0],mid等于0，该情况是需要在循环体外判断的，循环条件l<h会导致这个结果不会被检查。



二分搜索的变种写法：

- 查找第一个与目标相等的元素
- 查找最后一个与目标相等的元素
- 查找第一个大于等于目标的元素
- 查找最后一个小于等于目标的元素

```java
//1.查找第一个与目标相等的元素
public int searchFirstEqualElement(int[] nums, target int) {
    int l =0, h = nums.length -1;
    while (l <= h){
        int mid = l + (h-l) / 2;
        if (nums[mid] > target) {
            h = mid -1;
        } else if (nums[mid] < target) {
            l= mid +1;
        } else {
            // 找到第一个与target相等的元素
            if (mid == 0 || nums[mid-1] != target){
                return mid;
            }
            h = mid -1;
        }
    }
}
// 2.查找最后一个与目标相等的元素
public int searchLastEqualElement(int[] nums, target int) {
    int l =0, h = nums.length -1;
    while (l <= h){
        int mid = l + (h-l) / 2;
        if (nums[mid] > target) {
            h = mid -1;
        } else if (nums[mid] < target) {
            l= mid +1;
        } else {
            // 找到最后一个与target相等的元素
            if (mid == nums.length -1|| nums[mid+1] != target){
                return mid;
            }
            l = mid + 1;
        }
    }
}
// 3.查找第一个大于等于目标的元素
public int searchFirstGreaterOrEqualElement(int[] nums, target int) {
    int l =0, h = nums.length -1;
    while (l <= h){
        int mid = l + (h-l) / 2;
        if (nums[mid] > target) {
             // 找到第一个大于等于target的元素
            if (mid == 0 || nums[mid-1] < target){
                return mid;
            }
            h = mid -1;
        } else {
            l= mid +1;
        }
    }
}
// 4.查找最后一个小于等于目标的元素
public int searchLastLessOrEqualElement(int[] nums, target int) {
    int l =0, h = nums.length -1;
    while (l <= h){
        int mid = l + (h-l) / 2;
        if (nums[mid] < target) {
             // 找到最后一个小于等于target的元素
            if (mid == nums.length -1|| nums[mid+1] > target){
                return mid;
            }
            l = mid + 1;
        } else {
            h = mid -1;
        }
    }
}
```

适用题型：

- 迭代，数学X^2相关表达式，如求开方sqrt(n)，在0-n之间二分查找mid，mid^2 =n
  - X^2相关表达式变种，如n=(x+1)*x/2,求x，也可以二分查找。判断n=mid * （mid+1）/2。
- 双数组问题
  - LeetCode4 2数组的中间值
  - LeetCode540 有序数组，只有一个数不出现2次，找到这个数
  - LeetCode162 随机数组，返回任意峰值



### l540: 有序数组，只有一个数不出现2次，找到这个数

解题思想，单数的对象一定在奇数个的有序区间，迭代查询奇数个的区间

判断，单值在哪个区间，若mid为偶数位置，左区间是偶数个，右区间也是偶数个。若m与m+1相等，那么m需要加到右边（迭代时，可以直接去掉m和m+1的值），则说明单值在右区间，否则将mid加到左区间，检查左区间。

迭代到最后，区间长度为1时退出，即为所需要的单值。

```java
public int singleNonDuplicate(int[] nums) {
    int l =0, h = nums.lengt -1;
    while(l < h) {
        int m = l + (h-l) / 2;
        // 保证l,h,m都是偶数，使查找区间一直是奇数
        if (m % 2 == 1) m--;
        if (nums[m] == nums[m+1]) l= m+2;
        else h = m;
    }
    return nums[l];
}
```


### l162: 随机数组，返回任意峰值

解题思想，将数组视为多段交替连续的有序数组，将mid与右表值比较，大于，说明在下降，峰值在左边，小于说明上升，峰值在右表，迭代缩减区间，只剩1个时，即为峰值
```java
public int findPeakElement(int nums) {
    int l =0, h = nums.lengt -1;
    while(l < h) {
        int m = l + (h-l) / 2;
        if (nums[m] > nums[m+1]) h = m;
        else l = mid +1;
    }
    return nums[l];
}
```

### l4 寻找两个正序数组的中位数
给定两个大小分别为 m 和 n 的正序（从小到大）数组 nums1 和 nums2。请你找出并返回这两个正序数组的 中位数 。

算法的时间复杂度应该为 O(log (m+n)) 。


示例 1：

输入：nums1 = [1,3], nums2 = [2]
输出：2.00000
解释：合并数组 = [1,2,3] ，中位数 2
示例 2：

输入：nums1 = [1,2], nums2 = [3,4]
输出：2.50000
解释：合并数组 = [1,2,3,4] ，中位数 (2 + 3) / 2 = 2.5


#### 一、 问题思考方式

##### 1. 如何保证正确性？
*   **定义中位数：** 如果总长度为 $L$，中位数就是第 $\lfloor (L-1)/2 \rfloor$ 和 $\lfloor L/2 \rfloor$ 个元素（从0开始计数）的平均值。
*   **划分思想：** 我们的目标是将两个数组分别切分为左右两部分，使得：
    1.  **左半部分的长度等于右半部分的长度**（或比右半部分多一个）。
    2.  **左半部分的所有元素都小于或等于右半部分的所有元素。**
*   **边界条件处理：** 必须考虑数组为空的情况，以及切分点在数组最左端或最右端的情况（使用 $-\infty$ 和 $+\infty$ 进行填充）。

##### 2. 如何保证最佳效率？
*   **避免合并：** 如果直接合并数组再排序，复杂度是 $O((m+n) \log(m+n))$。如果用双指针合并，复杂度是 $O(m+n)$。题目要求 $O(\log(m+n))$，这意味着我们必须使用**二分查找**。
*   **减少搜索空间：** 我们不需要对两个数组都进行完整的二分。我们可以只在较短的数组上进行二分。假设 $m \le n$，我们在 `nums1` 中找切分点 $i$，那么对应的 `nums2` 切分点 $j$ 可以通过公式 $j = (m+n+1)/2 - i$ 直接计算出来。
*   **收敛条件：** 每次二分，我们根据 `nums1[i-1]` 和 `nums2[j-1]` 与 `nums1[i]` 和 `nums2[j]` 的大小关系，决定是向左还是向右移动切分点。

---

#### 二、 解题思路：二分查找（划分数组）

假设 $m \le n$。我们在 `nums1` 中寻找一个位置 $i$，使得：
*   `nums1[i-1] <= nums2[j]` 且 `nums2[j-1] <= nums1[i]`
*   其中 $j = (m+n+1)/2 - i$。

如果 `nums1[i-1] > nums2[j]`，说明 `nums1` 的左侧太大了，需要减小 $i$；反之，增大 $i$。

---

#### 三、 Java 代码实现

```java
public class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        // 确保 nums1 是较短的数组，以优化时间复杂度并简化逻辑
        if (nums1.length > nums2.length) {
            return findMedianSortedArrays(nums2, nums1);
        }

        int m = nums1.length;
        int n = nums2.length;
        int low = 0;
        int high = m;

        while (low <= high) {
            // 在较短的数组中寻找切分点 i
            int i = low + (high - low) / 2;
            // 根据总长度计算 nums2 的切分点 j
            int j =  (int)(( (long)m + n + 1 ) / 2 - i);

            // 处理边界情况：如果切分点在最左侧，取负无穷；在最右侧，取正无穷
            int maxLeft1 = (i == 0) ? Integer.MIN_VALUE : nums1[i - 1];
            int minRight1 = (i == m) ? Integer.MAX_VALUE : nums1[i];

            int maxLeft2 = (j == 0) ? Integer.MIN_VALUE : nums2[j - 1];
            int minRight2 = (j == n) ? Integer.MAX_VALUE : nums2[j];

            // 判断是否找到正确的切分点
            if (maxLeft1 <= minRight2 && maxLeft2 <= minRight1) {
                // 如果总长度是奇数，中位数是左半部分的最大值
                if ((m + n) % 2 != 0) {
                    return Math.max(maxLeft1, maxLeft2);
                } else {
                    // 如果总长度是偶数，中位数是（左最大和右最小的平均值）
                    return (Math.max(maxLeft1, maxLeft2) + Math.min(minRight1, minRight2)) / 2.0;
                }
            } else if (maxLeft1 > minRight2) {
                // nums1 的左侧太大，向左移动切分点
                high = i - 1;
            } else {
                // nums1 的右侧太小，向右移动切分点
                low = i + 1;
            }
        }

        return 0.0; // 理论上不会执行到这里
    }

    public static void main(String[] args) {
        Solution sol = new Solution();
        System.out.println(sol.findMedianSortedArrays(new int[]{1, 3}, new int[]{2}));    // 输出: 2.0
        System.out.println(sol.findMedianSortedArrays(new int[]{1, 2}, new int[]{3, 4}));  // 输出: 2.5
    }
}
```


#### 四、 复杂度分析

1.  **时间复杂度：$O(\log(\min(m, n)))$**
    *   我们始终在较短的数组上进行二分查找。由于每次循环都会将搜索范围缩小一半，因此符合 $O(\log(m+n))$ 的要求（实际上比要求的更优）。

2.  **空间复杂度：$O(1)$**
    *   只使用了常数级别的额外变量，没有开辟新的数组空间。

#### 五、 总结思考点
*   **为什么选短的数组做二分？** 因为 $j = (m+n+1)/2 - i$，如果 $m$ 是短的，$i$ 的范围小，且能保证 $j$ 不会越界（在 $0 \le j \le n$ 范围内）。
*   **为什么用 `(m + n + 1) / 2`？** 这是为了处理奇数情况。当总长度为奇数时，`(m+n+1)/2` 会让左半部分多出一个元素，这样中位数就直接是 `max(left)`。如果是偶数，这个公式依然有效，只是我们需要取左右两边的平均值。

**奇偶数下的中位数逻辑**

假设 $m+n=5$（奇数）：
```text
左半部分 (3个元素) | 右半部分 (2个元素)
-------------------|--------------------
nums1: [ ... , maxLeft1 ] | [ minRight1, ... ]
nums2: [ ... , maxLeft2 ] | [ minRight2, ... ]
                        |
                中位数就在这里！(左边最大的那个)
```

假设 $m+n=4$（偶数）：
```text
左半部分 (2个元素) | 右半部分 (2个元素)
-------------------|--------------------
nums1: [ ... , maxLeft1 ] | [ minRight1, ... ]
nums2: [ ... , maxLeft2 ] | [ minRight2, ... ]
                        |
                中位数是这两者的平均值！
```

总结思考逻辑：
1.  **为什么用 `(m+n+1)/2`？** 为了让左边在奇数时多出一个元素。
2.  **为什么奇数返回 `maxLeft`？** 因为那个“多出来”的元素就在左边，且它是左边最大的（因为数组是有序的）。
3.  **为什么偶数要平均？** 因为左右两边数量相等，中位数处于两个部分的交界处。


```java
/*
leetecode 35 搜索插入位置

给定一个排序数组和一个目标值，在数组中找到目标值，并返回其索引。如果目标值不存在于数组中，返回它将会被按顺序插入的位置。
*/

// 二分查找
public int searchInsert(int[] nums, int target) {
    int l = 0, h = nums.length -1;
    int mid = 0;
    while(l <=h) {
        mid = l + (h-l)/2;
        if (nums[mid] == target) {
            return mid;
        } else if (nums[mid] < target) {
            l = mid +1;
        } else {
            h = mid -1;
        }
    }

    // not found:
    return l;

    }


/*
leetcode 69 x的平方根
给你一个非负整数 x ，计算并返回 x 的 算术平方根 。

由于返回类型是整数，结果只保留 整数部分 ，小数部分将被 舍去 。

注意：不允许使用任何内置指数函数和算符，例如 pow(x, 0.5) 或者 x ** 0.5 。

解法：
二分查找的下界为 0，上界可以粗略地设定为 x。在二分查找的每一步中，我们只需要比较中间元素 mid 的平方与 x 的大小关系，并通过比较的结果调整上下界的范围。

*/

public int mySqrt(int x) {
    // 防止int溢出
    long l=0, h=x;
    long mid=0,cur=0;
    while(l <= h) {
        mid = l + (h-l)/2;
        cur = mid * mid;
        if (cur == x) {
            return (int) mid;
        } else if (cur < x) {
            l = mid + 1;
        } else {
            h = mid -1;
        }
    }
    // 一定有 left > right，且 right * right <= x < left * left
    return (int)h;
}



/*
leetcode 74. 搜索二维矩阵

给你一个满足下述两条属性的 m x n 整数矩阵：

每行中的整数从左到右按非严格递增顺序排列。
每行的第一个整数大于前一行的最后一个整数。
给你一个整数 target ，如果 target 在矩阵中，返回 true ；否则，返回 false 。
*/


public boolean searchMatrix(int[][] matrix, int target) {
    // 2 次 2分查找 
    // 第一次 查找所在行， 第一个大于 target 的行，在上一行中查找

    int l =0, h = matrix.length -1;
    int mid = 0;
    while (l <= h) {

        mid = l + (h -l)/2;
        
        if ( target > matrix[mid][0] ) {
            // 在 right 区域
            l = mid + 1;
        } else if (target == matrix[mid][0]) {
            // 直接存在
            return true;
        } else {
            h = mid -1;
        }
    }

    // 特殊case，直接不存在
    if ( h < 0) {
        return false;
    }

    int n = matrix[h].length -1;
    // 可选剪枝：target 大于该行最大值，则不存在
    if (target > matrix[mid][n]) {
        return false;
    }

    // l > h, 2mid = l + h ,  mid > h 
    // l = 第一个行首大于 target 的位置
    // 所以 h 是 需要的行，第一个小于 target的行
    int x0 = h; 
    // 第二次 查找所在列，等于target的列
    l =0;
    h = n;
    while(l <= h) {
        mid = l + (h-l)/2;
        if (target == matrix[x0][mid]) {
            return true;
        } else if (target > matrix[x0][mid]) {
            l = mid +1;
        } else {
            h = mid -1;
        }
    }
    return false;
}



/*
leetcode 162. 寻找峰值

峰值元素是指其值严格大于左右相邻值的元素。

给你一个整数数组 nums，找到峰值元素并返回其索引。数组可能包含多个峰值，在这种情况下，返回 任何一个峰值 所在位置即可。

你可以假设 nums[-1] = nums[n] = -∞ 。

你必须实现时间复杂度为 O(log n) 的算法来解决此问题。

思路：
沿着上坡方向寻找，一定能够到达某个峰值。

*/

public int findPeakElement(int[] nums) {
    int l = 0, h = nums.length - 1;

    // 注意 l < h， 迭代结果是 l==h，可以检查l==h是否满足特定条件
    while (l < h) {
        int mid = l + (h - l) / 2;

        if (nums[mid] > nums[mid + 1]) {
            // 下降趋势：左侧（包含 mid）一定存在峰值
            h = mid;
        } else {
            // 上升趋势：右侧一定存在峰值
            l = mid + 1;
        }
    }

    // 最后 l == h
    return l;
}



/*
leetcode 287. 寻找重复数
给定一个包含 n + 1 个整数的数组 nums ，其数字都在 [1, n] 范围内（包括 1 和 n），可知至少存在一个重复的整数。

假设 nums 只有 一个重复的整数 ，返回 这个重复的数 。

你设计的解决方案必须 不修改 数组 nums 且只用常量级 O(1) 的额外空间。

解法一：二分查找（按数值范围）
时间 O(n log n)，空间 O(1)。

思路：
二分的是数字范围 [1, n]
如果有超过 mid 个元素落在这个范围，必定存在重复。


解法二：快慢指针（最优）
时间 O(n)，空间 O(1)。
数组看成一种隐式链表 next(i) = nums[i];
因为每个数组值都在 [1, n]，从索引 0 出发不断跳转，最终一定进入一个环。
重复数字就是这个环的入口。

*/

public int findDuplicate(int[] nums) {
    int l = 1, h = nums.length - 1;

    while (l < h) {
        int mid = l + (h - l) / 2;

        int count = 0;
        for (int num : nums) {
            if (num <= mid) {
                count++;
            }
        }

        if (count > mid) {
            // [1, mid] 中一定存在重复数, 包含 mid
            h = mid;
        } else {
            // 重复数在 [mid + 1, h]
            l = mid + 1;
        }
    }

    // l == h, 找到重复数, l 是重复数
    return l;
}

/*
第一阶段，慢指针每次走一步，快指针每次走两步，最终在环内相遇。
第二阶段，将一个指针放回索引 0，两个指针都每次走一步，最终在环入口相遇。
*/
public int findDuplicate(int[] nums) {
    int slow = 0, fast = 0;

    // 第一阶段：找到环内相遇点
    do {
        slow = nums[slow];
        fast = nums[nums[fast]];
    } while (slow != fast);

    // 第二阶段：找到环的入口
    slow = 0;

    while (slow != fast) {
        slow = nums[slow];
        fast = nums[fast];
    }

    return slow;
}

```
