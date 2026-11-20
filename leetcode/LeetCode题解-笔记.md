# LeetCode-笔记

[TOC]

解题办法：

抓住题目特征，匹配解题思想，根据解题思想进行建模，考虑细节，实现，用例测试，边界测试。



通用算法思想：

- 分治法
  - 大问题可以被分解为两个、多个相同，类似的子问题，最后合并子问题的解
  - 应用
    - 二分法
    - 快速，归并排序
    - 二叉树递归，迭代
    - 分布式计划两阶段聚合，MapReduce
- 动态规划
  - 原问题分解为简单的子问题（降维，添加限制），具有重叠子问题，最优子结构性质
  - 一维，二维，区间
  - 应用
    - 斐波那契数列
    - 背包问题
    - 字符串问题，最长公共子串
- 贪心算法
  - 每一步，都采取当前最好，最优的选择，问题具有局部最优，也是全局最优性质
  - 应用
    - 最短路径，最小生成树，哈夫曼编码


## 5.树



REF

- [二叉树后序遍历的两种易写的非递归写法](https://zhuanlan.zhihu.com/p/80578741)

## 数据结构



### 位运算

#### 二进制1的个数、位运算的加法

```c++
/*
原码：符号位和二进制数的绝对值
反码：正数的反码等于其原码，负数的反码是其原码除符号位外，按位取反。
补码：正数的补码等于其原码，负数的补码是其反码加1。
计算机中的二进制是用补码表示。
补码直接计算使用溢出原理，保证正确。
最小值：1000000... =-2^(N-1), 11111... = -1 
*/


/* 剑指10：二进制1的个数
实现函数，输入整数，输出该数二进制表示中1的个数
陷阱：右移位，检查最右位是否为1，负数，导致循环。
解法1：使用无符号，32为整形1，逐一试探没一位，O(n)
*/
int NumberOf1(int n) {
    int count =0;
    unsinned int flag =1;
    // 循环32次
    while(flag) {
        if (n&flag){
            count++;
        }
        flag = flag <<1;
    }
    return count;
}
/*
解法2：
技巧：把一个整数减去1，再和原整数做与运算，会把原整数最右边一个1变成0。
边界测试：正数（1,0x7FFFF），负数(0x80000,0xFFFFFF)，0
*/
int NumberOf1(int n) {
	int count = 0;
    while(n){
        count++;
        n=(n-1)&n;
    }
    return count;
}

/*
利用解法2的技巧，可以判断相似问题
- 判断一个整数是不是2的整数次方（二进制中有且只有一位是1）
- 输入两个整数m和n，计算该变m的二进制多少位，才能等到n。分成2步，第一步m异或n，第二步计算结果中1的个数。
*/

/*
剑指47：位运算的加法
原理：位相加，不考虑进位，等于异或。
进位是1+1,情况，等于与操作，然后左移1位。
最后，将进位与异或结果相加，相加过程重复前两部，知道无进位。
*/
int BitAdd(int num1, int num2){
    int sum,carry;
    while(num2!=0) {
        sum = num1^num2;
        carrry = (num1 & num2) <<1;
        
        num1=sum;
        num2=carry;
    }
    return num1;
}

// 交换2个变量的值,不使用新变量
// 基于加减      基于异或(a^a=0,a^0=a)
// a=a+b;       a=a^b;
// b=a-b;		b=a^b;(a^b^b=a)
// a=a-b;		a=a^b;(a^b^a=b)


```

### 并查集



### 跳表SkipList



### B树



### Trie (前缀树)

递归的定义

- 26个子孩子（孩子也是Trie树）
- 是否是一个单词末尾节点（精确查找，避免长单词路径覆盖，导致误存在）

基本方法

- 初始化
- 插入
- 搜索
- 前缀搜索

 

### 图

- 图的存储结构
  - 临接矩阵
  - 临接链表

- DFS
  - 辅助数组visited[]，从0 开始，到n结束，标记节点是否访问过
  - 访问过程，从顶点V0开始访问器一个未被访问过的临接顶点，然后再继续从该顶点出发访问
    - 没有未被访问过的顶点，回退到，仍有临接顶点的顶点，重复过程。
- BFS
  - 辅助数组visited[]，从0 开始，到n结束，标记节点是否访问过
  - 辅助队列，记录已经访问的节点，直到队列为空
  - 访问过程，从顶点V0开始访问并入队，出队时将所有临接的顶点入队



最小生成树

- 带权边的代价最小的生成树
- prim算法
  - 初始顶点集合，然后从其顶点出发选择代价最小的边，将其另一个节点加入集合
  - 再从顶点集合中挑选代价最小的边，加入新的顶点
  - 重复直至所有的顶点加入
- kruskal算法
  - 初始n个顶点，无边
  - 在边集合中挑选代价最小的边，并且该边可以连接2个连通分量，然后边集合中删除
  - 重复，直到所有的顶点都在一个连通分量



### 数组

#### 去重、旋转数组、合并有序数组

```java
// leetcode: 删除排序数组中的重复项
/*
给你一个有序数组 nums ，请你 原地 删除重复出现的元素，使每个元素 只出现一次 ，返回删除后数组的新长度。
不要使用额外的数组空间，你必须在 原地 修改输入数组 并在使用 O(1) 额外空间的条件下完成。

提示：
0 <= nums.length <= 3 * 104
-104 <= nums[i] <= 104
nums 已按升序排列
链接：https://leetcode-cn.com/leetbook/read/top-interview-questions-easy/x2gy9m/
*/

/*
分析:
条件有序数组，去除重复，返回结果是数组长度。
输入即形如：1,2,2,3,4,4,4,5
限制原地删除，那么返回的结果使用原数组，期望是1,2,3,4,5
需要将重复值位置占位，依次将不重复值移动到前面，因此考虑双指针记录扫描位置和不重复值位置。
*/
public int removeDuplicates(int[] nums) {
    int len = nums.length;
    // 边界值入考虑
    if (len <=1) {
        return len;
    }
    // i 是不重复值，j是以扫描值位置
    int i=0,j=1;
    while (j<len) {
        /*  分类讨论： 有序数组（默认从小到大），前后值小于和等于2种情况
        小于，则当前j位置是我们需要输出的值，于是将其搬到i后，并且i，j都移
        这里可能i+1==j情况，可以不用搬，为了代码简洁，统一规则直接简写。
        等于，则说明是重复值，j后移。
        因为，无论如何，每次判断j的值后，需要后移，所以提取出来。
        */
        if (nums[i] < nums[j]) {
            nums[i+1]=nums[j];
            i++;
        }
        j++;
    }
    // 完全不重复情况，i+1也不会超出len，因为从j=1 开始，i 最多到len-1
    return i+1;
}

// 其他更简洁的写法
//双指针解决
public int removeDuplicates(int[] A) {
    //边界条件判断
    /*觉得null值，应该不会作为case，一般不需要考虑*/
    if (A == null || A.length == 0)
        return 0;
    int left = 0;
    /* for 循环代替while;  != 代替 < 更通用; */
    for (int right = 1; right < A.length; right++)
        //如果左指针和右指针指向的值一样，说明有重复的，
        //这个时候，左指针不动，右指针继续往右移。如果他俩
        //指向的值不一样就把右指针指向的值往前挪
        if (A[left] != A[right])
            A[++left] = A[right];
    return ++left;
}

// leetcode: 旋转数组
/*
给定一个数组，将数组中的元素向右移动 k 个位置，其中 k 是非负数。
进阶：
尽可能想出更多的解决方案，至少有三种不同的方法可以解决这个问题。
你可以使用空间复杂度为 O(1) 的 原地 算法解决这个问题吗？
*/
/*
方案：
1. 使用O(K) 空间，暂存末尾K个数，然后O(N) 后移，与前K个填写
2. 多次反转，第一次全部反转，第二次，反转前k个，第三次反转后面的。 时间开销O(2n)
3. 环形旋转，https://leetcode-cn.com/leetbook/read/top-interview-questions-easy/x2skh7/
还需要注意， 数组长度为k的倍数，这个会原地打转
*/
// 方案2
/*
反转法，可以完成，数组的左右交换。
（TODO）
*/
public void rotate(int[] nums, int k) {
    int length = nums.length;
    k %= length;
    reverse(nums, 0, length - 1);//先反转全部的元素
    reverse(nums, 0, k - 1);//在反转前k个元素
    reverse(nums, k, length - 1);//接着反转剩余的
}

//把数组中从[start，end]之间的元素两两交换,也就是反转
public void reverse(int[] nums, int start, int end) {
    while (start < end) {
        int temp = nums[start];
        nums[start++] = nums[end];
        nums[end--] = temp;
    }
}


/* leetcode: 合并有序数组
给你两个按 非递减顺序 排列的整数数组 nums1 和 nums2，另有两个整数 m 和 n ，分别表示 nums1 和 nums2 中的元素数目。

请你 合并 nums2 到 nums1 中，使合并后的数组同样按 非递减顺序 排列。

注意：最终，合并后数组不应由函数返回，而是存储在数组 nums1 中。为了应对这种情况，nums1 的初始长度为 m + n，其中前 m 个元素表示应合并的元素，后 n 个元素为 0 ，应忽略。nums2 的长度为 n 。

提示：
nums1.length == m + n
nums2.length == n
0 <= m, n <= 200
1 <= m + n <= 200
-109 <= nums1[i], nums2[j] <= 109

思路：
按一般的思路，从头开始，会导致nums1多次后移调整位置，
所以，可以考虑从尾部开始，因为尾部是空闲的，可以保证直接插入到最终的正确的位置。
*/
public void merge(int[] nums1, int m, int[] nums2, int n) {
    int k = nums1.length -1;
    while (m > 0 && n > 0) {
        if (nums1[m-1] >= nums2[n-1]){
            nums1[k--] =  nums1[m-1];
            m--;
        } else {
            nums1[k--] =  nums2[n-1];
            n--;
        }
    }
    while(n > 0) {
        nums1[k--] =  nums2[n-1];
        n--;
    }
}
```

#### shuffle

```java
/*
leetcode: 打乱数组
给你一个整数数组 nums ，设计算法来打乱一个没有重复元素的数组。

实现 Solution class:

Solution(int[] nums) 使用整数数组 nums 初始化对象
int[] reset() 重设数组到它的初始状态并返回
int[] shuffle() 返回数组随机打乱后的结果

Solution solution = new Solution([1, 2, 3]);
solution.shuffle();    // 打乱数组 [1,2,3] 并返回结果。任何 [1,2,3]的排列返回的概率应该相同。例如，返回 [3, 1, 2]
solution.reset();      // 重设数组到它的初始状态 [1, 2, 3] 。返回 [1, 2, 3]
solution.shuffle();    // 随机返回数组 [1, 2, 3] 打乱后的结果。例如，返回 [1, 3, 2]

提示：

1 <= nums.length <= 200
-106 <= nums[i] <= 106
nums 中的所有元素都是 唯一的
最多可以调用 5 * 104 次 reset 和 shuffle

链接：https://leetcode-cn.com/leetbook/read/top-interview-questions-easy/xn6gq1/

题解：
该题的关键是，保证等概率出现在任何位置，需要每次调整过、交换过位置后，不能再次参与调整位置。
那么，一种方法是，不放回抽样，将抽出的结果，顺序放到以经抽的位置（交换值）。然后再从剩余的位置抽取。
由于需要抽取/交换n-1次，时间复杂度是O(n)
*/
class Solution {

    int[] origin;
    Random random = new Random();

    public Solution(int[] nums) {
        this.origin = nums;
    }

    /** Resets the array to its original configuration and return it. */
    public int[] reset() {
        return origin;
    }

    /** Returns a random shuffling of the array. */
    public int[] shuffle() {
        if (origin == null) {
            return null;
        }
        int[] newNums = origin.clone();
        int len = newNums.length;
        for (int i = len ; i> 0; i--) {
            int j = random.nextInt(i);
            swap(newNums, i-1, j);
        }
        return newNums;
    }

    private void swap(int[] nums, int i, int j) {
        if (i != j) {
            // 范围限制不会有溢出
            nums[i] = nums[i] + nums[j];
            nums[j] = nums[i] - nums[j];
            nums[i] = nums[i] - nums[j]; 
        }
    }
}
```

#### 排序、最大数

```java
import java.util.Arrays;
// sort int[] arr, int from_Index, int to_Index
Arrays.sort(); 


/**
给定一组非负整数 nums，重新排列每个数的顺序（每个数不可拆分）使之组成一个最大的整数。
注意：输出结果可能非常大，所以你需要返回一个字符串而不是整数。
*/
public String largestNumber(int[] nums) {
    int len=nums.length;
    String[] strings=new String[len];
    for (int i = 0; i <len ; i++) {
        strings[i]=String.valueOf(nums[i]);
    }
    // 递减顺序
    Arrays.sort(strings, (s1, s2) -> s2.concat(s1).compareTo(s1.concat(s2)));
    // all is zero
    if (strings[0].equals("0"))
        return String.valueOf(0);
    StringBuilder sb=new StringBuilder();
    for (String s:strings) {
        sb.append(s);
    }
    return sb.toString();
}

// java stream sorted
public String largestNumber(int[] nums) {
    String res = Arrays.stream(nums)
        .mapToObj(String::valueOf)
        .sorted((s1, s2) -> (s2 + s1).compareTo(s1 + s2))
        .reduce("", (a , b) -> (a + b));
    return res.charAt(0) == '0' ? "0" : res;
}
```

```c++
/*
c++ stl sort
*/
#include<vector>
#include<algorithm>

int arr[] = {1, 2, 3, 4};
vector<int> v5(arr, arr + 4);
sort(v5.begin(), v5.end());
```



#### 第k大的数

```C++
/*
leetcode 215 无序数组，第k大的元素
给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。
示例：
输入: [3,2,1,5,6,4] 和 k = 2
输出: 5
提示：
1 <= k <= nums.length <= 104
-104 <= nums[i] <= 104

坑：逆序，递减的第k个值

方法：
1. 先排序，在求第k位； 复杂度O(NlogN)
2. 改进，使用快速排序，或者选择排序，只保证第k个位置的顺序已经排好即可。
（k很小，或者k很大可以选择排序可以接受；k比较中间时，快排通过随机选pivot可以在O(n) 期望内把k位置排好）
*/
class Solution {
public:
    int findKthLargest(vector<int>& nums, int k) {
    	return quickSelect(nums, 0, nums.size() - 1, k - 1);
    }
    // 逆序排，从大到小
    int quickSelect(vector<int>& nums, int l, int r, int k) {
    	int index = randomPartition(nums, l, r);
    	//cout << "randomPartition " << index <<endl;
    	if (index == k) {
    		return nums[k];
    	}else {
    		return index < k ? quickSelect(nums, index + 1, r, k):quickSelect(nums, l, index-1, k);
    	}
    }

    int randomPartition(vector<int>& nums, int l, int r) {
    	int pivot = r; // 基准值位置
    	int i =  rand() % (r - l + 1) + l;
    	//cout << "randomPartition l=" << l <<" r=" << r <<endl;
    	swap(nums, i, pivot);
    	// index表示[left+1,right]区间第一个比pivot大的位置，在它左边都是大于的pivot，右边是比pivot小的元素
    	int index = l - 1;
    	// pivot 位置暂不处理
    	for (int i = l; i< r; i++) {
    		// 将比基准值大的值与index位置交换，将大值放到前面，同时后移index位置，保证index仍然是比pivot大的元素第一个位置
    		if (nums[i] > nums[pivot]) {
    			swap(nums, i, ++index);
    		}
    	}
    	// index 最后一个比pivot大的位置，那么index + 1， 即等于pivot位置
    	// 原来的index+1 也比pivot的值小，可以安全交换到原来pivot位置
    	swap(nums, pivot, index+1);

    	return index + 1;
    }

    void swap(vector<int>& nums, int i, int j) {
    	int temp = nums[i];
    	nums[i] = nums[j];
    	nums[j] = temp;
    }
};
```

#### 两个有序数组的中位数

```c++
/**
leetcode 4:
给定两个大小分别为 m 和 n 的正序（从小到大）数组 nums1 和 nums2。请你找出并返回这两个正序数组的中位数 。

输入：nums1 = [1,3], nums2 = [2]
输出：2.00000
解释：合并数组 = [1,2,3] ，中位数 2
输入：nums1 = [1,2], nums2 = [3,4]
输出：2.50000
解释：合并数组 = [1,2,3,4] ，中位数 (2 + 3) / 2 = 2.5

https://leetcode-cn.com/problems/median-of-two-sorted-arrays/

注意：奇数，偶数总长度

分析：
原始的方法：两个指针，顺序比较，扫描加起来，过半。即求得中位数位置。O(m+n)
优化：因为数组本身是有序的，可以二分查找，迭代的淘汰小值。
分类讨论：
m+n是奇数，中位数是两个有序数组中的第 (m+n)/2个元素
m+n是偶数时，中位数是两个有序数组中的第 (m+n)/2个元素和第 (m+n)/2+1个元素的平均值
也即查找两个有序数组中第k小的元素，k为(m+n)/2， (m+n)/2+1。

A[k/2-1] 和 B[k/2-1] 前分别有k/2 - 1个元素，那么比Min(A[k/2-1],B[k/2-1])小的元素，最多有 k-2个。
因此，小于Min(A[k/2-1],B[k/2-1])的部分都可以淘汰（从vector中remove）。

情况：
1) A[k/2-1] <= B[k/2-1], 淘汰A[0] 到 A[k/2-1]
2) A[k/2-1] > B[k/2-1],淘汰B[0] 到 B[k/2-1]
淘汰之后，更新k = k/2

边界情况处理：
1)A[k/2-1], B[k/2-1] 数组越界，去末尾值作为比较值，然后k的更新根据实际排除的个数进行更新。
2)A/B 集合为空（都被淘汰），则B/A[k]为所求
3)k=1, 取min(A[0],B[0])

https://leetcode-cn.com/problems/median-of-two-sorted-arrays/solution/xun-zhao-liang-ge-you-xu-shu-zu-de-zhong-wei-s-114/
*/
class Solution {
public:
    int getKthElement(const vector<int>& nums1, const vector<int>& nums2, int k) {
        /* 主要思路：要找到第 k (k>1) 小的元素，那么就取 pivot1 = nums1[k/2-1] 和 pivot2 = nums2[k/2-1] 进行比较
         * 这里的 "/" 表示整除
         * nums1 中小于等于 pivot1 的元素有 nums1[0 .. k/2-2] 共计 k/2-1 个
         * nums2 中小于等于 pivot2 的元素有 nums2[0 .. k/2-2] 共计 k/2-1 个
         * 取 pivot = min(pivot1, pivot2)，两个数组中小于等于 pivot 的元素共计不会超过 (k/2-1) + (k/2-1) <= k-2 个
         * 这样 pivot 本身最大也只能是第 k-1 小的元素
         * 如果 pivot = pivot1，那么 nums1[0 .. k/2-1] 都不可能是第 k 小的元素。把这些元素全部 "删除"，剩下的作为新的 nums1 数组
         * 如果 pivot = pivot2，那么 nums2[0 .. k/2-1] 都不可能是第 k 小的元素。把这些元素全部 "删除"，剩下的作为新的 nums2 数组
         * 由于我们 "删除" 了一些元素（这些元素都比第 k 小的元素要小），因此需要修改 k 的值，减去删除的数的个数
         */

        int m = nums1.size();
        int n = nums2.size();
        int index1 = 0, index2 = 0;

        while (true) {
            // 边界情况
            if (index1 == m) { // nums1是空集了
                return nums2[index2 + k - 1];
            }
            if (index2 == n) {// nums2是空集了
                return nums1[index1 + k - 1];
            }
            if (k == 1) {
                return min(nums1[index1], nums2[index2]);
            }

            // 正常情况
            int newIndex1 = min(index1 + k / 2 - 1, m - 1);
            int newIndex2 = min(index2 + k / 2 - 1, n - 1);
            int pivot1 = nums1[newIndex1];
            int pivot2 = nums2[newIndex2];
            if (pivot1 <= pivot2) { // 淘汰nums1
                k -= newIndex1 - index1 + 1;
                index1 = newIndex1 + 1;
            }
            else { // 淘汰nums2
                k -= newIndex2 - index2 + 1;
                index2 = newIndex2 + 1;
            }
        }
    }

    double findMedianSortedArrays(vector<int>& nums1, vector<int>& nums2) {
        int totalLength = nums1.size() + nums2.size();
        if (totalLength % 2 == 1) {
            return getKthElement(nums1, nums2, (totalLength + 1) / 2);
        }
        else {
            return (getKthElement(nums1, nums2, totalLength / 2) + getKthElement(nums1, nums2, totalLength / 2 + 1)) / 2.0;
        }
    }
};


/*

我们要找的是第(k)小的元素，其中k = (m+n+1)/2
在nums1和nums2中分别进行二分搜索，找到合适的分割点
通过比较两个数组在分割点处的元素来确定最终答案

步骤1：预处理
确保nums1是较短的数组，这样可以减少搜索空间复杂度
计算需要找的第k小元素的位置：k = (m + n + 1) / 2
步骤2：二分搜索分割点
在nums1中进行二分搜索：

i 表示nums1中的分割点（左边有i个元素）
对应地，j = k - i 表示nums2中的分割点
步骤3：判断条件
我们需要找到合适的分割点使得：

nums1[i-1] <= nums2[j]
nums2[j-1] <= nums1[i]
如果nums1[i-1] > nums2[j]，说明nums1的左半部分太大了，应该向左调整。

步骤4：计算结果
找到正确的分割点后：

左半部分的最大值为 max(nums1Left, nums2Left)
右半部分的最小值为 min(nums1Right, nums2Right)
如果总长度是奇数，中位数就是左半部分的最大值；
如果是偶数，中位数就是左右两部分最大值和最小值的平均值。

复杂度分析
时间复杂度：O(log(min(m,n)))，因为我们只在较短数组上进行二分搜索
空间复杂度：O(1)，只使用了常数额外空间
*/

public class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;
        
        // 确保nums1是较短的数组，这样可以减少搜索空间
        if (m > n) {
            return findMedianSortedArrays(nums2, nums1);
        }
        
        // 在较短数组上进行二分搜索
        int left = 0;
        int right = m;
        int k = (m + n + 1) / 2; // 需要找的第k小元素
        
        while (left < right) {
            // nums1的分割点
            int i = left + (right - left) / 2;
            // 根据k和i计算nums2的分割点
            int j = k - i;
            
            // 如果nums1[i-1] > nums2[j]，说明nums1的左半部分太大了
            if (nums1[i - 1] > nums2[j]) {
                right = i - 1;
            } else {
                // 否则调整搜索范围
                left = i;
            }
        }
        
        int i = left;
        int j = k - i;
        
        // 计算左半部分的最大值和右半部分的最小值
        int nums1Left = (i == 0) ? Integer.MIN_VALUE : nums1[i - 1];
        int nums1Right = (i == m) ? Integer.MAX_VALUE : nums1[i];
        int nums2Left = (j == 0) ? Integer.MIN_VALUE : nums2[j - 1];
        int nums2Right = (j == n) ? Integer.MAX_VALUE : nums2[j];
        
        // 根据总长度的奇偶性返回结果
        if ((m + n) % 2 == 1) {
            return Math.max(nums1Left, nums2Left);
        } else {
            int leftMax = Math.max(nums1Left, nums2Left);
            int rightMin = Math.min(nums1Right, nums2Right);
            return (leftMax + rightMin) / 2.0;
        }
    }
}


```



### 哈希表Map

映射的集合。

```java
import java.util.*;
Map<String, Integer> map = new HashMap<String, Integer>();
map.put("bill", 98);
map.put("ryan", 99);
boolean exist = map.containsKey("ryan"); // check key exists in map
int point = map.get("bill"); // get value by key
int point = map.remove("bill") // remove by key, return value
Set<String> set = map.keySet();
// iterate Map
for (Map.Entry<String, Integer> entry : map.entrySet()) {
	String key = entry.getKey();
	int value = entry.getValue();
}
```



### 栈Stack

LIFO，先进后出。

```java
import java.util.*;
// 双端队列代替stack，性能更好
Deque<Integer> stack = new ArrayDeque<Integer>();
s.size(); // size of stack
```

重要方法：

- boolean isEmpty() - 判断栈是否为空， 若使用 Stack 类构造则为 empty()  
- E peek() - 取栈顶元素， 不移除 
- E pop() - 移除栈顶元素并返回该元素 
- E push(E item) - 向栈顶添加元素

#### 有效的括号、最小栈

```java
/*
leetcode20:有效的括号
给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串 s ，判断字符串是否有效。
有效字符串需满足：
左括号必须用相同类型的右括号闭合。
左括号必须以正确的顺序闭合。
*/
class Solution {
    public boolean isValid(String s) {
        int n = s.length();
        if (n % 2 == 1) {
            return false;
        }

        Map<Character, Character> pairs = new HashMap<Character, Character>() {{
            put(')', '(');
            put(']', '[');
            put('}', '{');
        }};
        Deque<Character> stack = new ArrayDeque<Character>();
        for (int i = 0; i < n; i++) {
            char ch = s.charAt(i);
            if (pairs.containsKey(ch)) {
                if (stack.isEmpty() || stack.peek() != pairs.get(ch)) {
                    return false;
                }
                stack.pop();
            } else {
                stack.push(ch);
            }
        }
        return stack.isEmpty();
    }
}

/*
leetcode:
设计一个支持 push ，pop ，top 操作，并能在常数时间内检索到最小元素的栈。

push(x) —— 将元素 x 推入栈中。
pop() —— 删除栈顶的元素。
top() —— 获取栈顶元素。
getMin() —— 检索栈中的最小元素。

解题方法；
1. 辅助类，定义ListNode，带有min属性，push操作头插入，并且新的头节点记录当前的min，pop，删除头节点，min值还是是当前头节点的min。top，头节点。min，头节点的min。
2. 双栈，栈1存放需要压栈的值，栈2存放最小值。push，先栈1，入栈。栈2为空，或者当前最小值大于或等于入栈值，入栈新最小值到栈2；pop，先出栈1，出栈结果等于当前最小值，那么栈2也出栈；top，栈1的栈顶，min，栈2的栈顶。

https://leetcode-cn.com/leetbook/read/top-interview-questions-easy/xnkq37/
*/

class MinStack {
    //链表头，相当于栈顶
    private ListNode head;

    //压栈，需要判断栈是否为空
    public void push(int x) {
        if (empty())
            head = new ListNode(x, x, null);
        else
            head = new ListNode(x, Math.min(x, head.min), head);
    }

    //出栈，相当于把链表头删除
    public void pop() {
        if (empty())
            throw new IllegalStateException("栈为空……");
        head = head.next;
    }

    //栈顶的值也就是链表头的值
    public int top() {
        if (empty())
            throw new IllegalStateException("栈为空……");
        return head.val;
    }

    //链表中头结点保存的是整个链表最小的值，所以返回head.min也就是
    //相当于返回栈中最小的值
    public int getMin() {
        if (empty())
            throw new IllegalStateException("栈为空……");
        return head.min;
    }

    //判断栈是否为空
    private boolean empty() {
        return head == null;
    }
}

class ListNode {
    public int val;
    public int min;//最小值
    public ListNode next;

    public ListNode(int val, int min, ListNode next) {
        this.val = val;
        this.min = min;
        this.next = next;
    }
}

class MinStack {
    //栈1存放的是需要压栈的值
    Stack<Integer> stack1 = new Stack<>();
    //栈2存放的是最小值
    Stack<Integer> stack2 = new Stack<>();

    public void push(int x) {
        stack1.push(x);
        if (stack2.empty() || x <= getMin())
            stack2.push(x);
    }

    public void pop() {
        //如果出栈的值等于最小值，说明栈中的最小值
        //已经出栈了，因为stack2中的栈顶元素存放的
        //就是最小值，所以stack2栈顶元素也要出栈
        if (stack1.pop() == getMin())
            stack2.pop();
    }

    public int top() {
        return stack1.peek();
    }

    public int getMin() {
        return stack2.peek();
    }
}
```



### 队列Queue

FIFO，先进先出。

```java
import java.util.*;
Queue<Integer> q = new LinkedList<Integer>();
int qLen = q.size(); // get queue length
```

重要方法：

- boolean add(E e) - 加入队尾
- E remove() - 移除队首，队列空抛出异常，poll返回null
- E peek() - 返回队首元素，不移除，队列空返回null，element()抛出异常



### 集合Set

不重复元素集合。

```java
import java.util.*;
Set<String> hash = new HashSet<String>();
hash.add("billryan");
hash.contains("billryan");
hash.size();
```

- HashSet：散列函数，无序，查询最快
- TreeSet：红黑树，有序

重要方法：

- boolean isEmpty() - 是否空集
- boolean add(E e) - 添加元素
- boolean remove(Object o) - 删除元素



### 字符串

```java
/**
perm 枚举所有可能
递归，分治
perm(n)= n!
*/

public static List<String> subpermm(List<String> list ) {
    int len = list.size();
    if (len ==1) return list;
    List<String> retList = new ArrayList<>();
    //System.out.println(Arrays.toString(list.toArray()));

    for (int i=0; i < len;i++) {
        String s = ""+list.get(i);
        List<String> l = new ArrayList<>();
        l.addAll(list);
        l.remove(i);
        List<String> ret =  subpermm(l);
        for (int j=0; j< ret.size(); j++ ){
            retList.add(s + ret.get(j));
        }
    }
    return retList;
}

public static void perm(int n) {
    List<String> retList = new ArrayList<>();
    for (int i =0; i < n; i++){
        retList.add(""+ (i+1));
    }

    System.out.println(Arrays.toString(retList.toArray()));
    List<String> list = subpermm(retList);

    for(int i =0; i < list.size(); i++) {
        System.out.println(list.get(i));
    }
}
```

### 双指针技巧


/*
11. 盛最多水的容器 https://leetcode.cn/problems/container-with-most-water/description/

给定一个长度为 n 的整数数组 height 。有 n 条垂线，第 i 条线的两个端点是 (i, 0) 和 (i, height[i]) 。
找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
返回容器可以储存的最大水量。
说明：你不能倾斜容器。

目标最大化 area=min(height[left],height[right])×(right−left)

关键思想（双指针法）
我们可以使用双指针法来高效地遍历所有可能的两个垂直线，并找到最大容量：

初始化两个指针 left = 0 和 right = len(height) - 1，分别指向数组的起点和终点。
在每一步中：
    计算当前两根垂直线所形成的容器的面积。
    更新最大值。
    如果 height[left] < height[right]，则移动左指针（因为更矮的一边限制了容量），否则移动右指针。
    这一步是关键：每次只移动较小的那一个。这样做的原因是，在当前的两个位置中，较短的那个是瓶颈，无法再容纳更多的水。如果将它往内移，则有可能找到更高的线，从而提升面积。

时间复杂度：O(n)
空间复杂度：O(1)
*/

```python
def maxArea(height):
    left = 0
    right = len(height) - 1
    max_area = 0
    
    while left < right:
        current_area = min(height[left], height[right]) * (right - left)
        if current_area > max_area:
            max_area = current_area
        
        # 每次移动较小的指针
        if height[left] < height[right]:
            left += 1
        else:
            right -= 1
    
    return max_area

```


/**
接雨水（Trapping Rain Water）

https://leetcode.cn/problems/trapping-rain-water/

给定一个非负整数数组，表示每个柱子的高度。请计算在这些柱子之间可以接多少水。

每个位置的“储水量”由其左右两边的最低高度决定。
常用方法包括双指针法、单调栈或动态规划。

思路：
双指针法：从两端向中间遍历，维护当前最左和最右的最大高度，每一步计算当前柱子的储水量。
单调栈法：通过维护一个递减栈来找到左右边界，从而确定每个位置可以接多少水。

双指针法：时间 O(n)，空间 O(1)
单调栈法：时间 O(n)，空间 O(n)
*/


/*
直方图中最大的矩形（Largest Rectangle in Histogram）

给定一个非负整数数组，表示直方图的柱子高度。找出其中能形成的最大矩形面积。

🔍 解题核心：
每个柱子可以看作是矩形的高，宽度由左右第一个比它矮的柱子确定。
常用方法为 单调栈。
🧠 思路分析：
通过维护一个递增栈来找到每个柱子右边界（即右边第一个较小的元素）。
每个高度可以扩展到尽可能远，从而形成最大面积。
📘 复杂度：
时间 O(n)，空间 O(n)
*/


/*
最大乘积问题

给定一个整数数组，找出两个元素的乘积最大值。

🔍 解题核心：
可能是正数、负数或零。需考虑极端值。
常用方法为 排序后比较首尾元素 或 一次遍历维护最大值和最小值。
🧠 思路分析：
若数组有正负，最大乘积可能出现在两个最大的正数或两个最小的负数之间。
使用两次扫描分别记录最大与次大、最小与次小的数，然后比较所有组合。
📘 复杂度：
时间 O(n)，空间 O(1)
*/