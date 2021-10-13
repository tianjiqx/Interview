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



## 1.  二分查找

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
  - l<h作为二分查找，是在题目需要检查循环体外l==h结果是否符合预期，例如序列[1,2,3],查询3时，[l,h]为[2,2]跳出循环体，需要检查跳过pos=2的结果是否为查找值。而对于某些题目，就是希望得出区间为1的结果，不是查找mid值，通常使用该循环条件。

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

```java
// l540:解题思想，单数的对象一定在奇数个的有序区间，迭代查询奇数个的区间
// 判断，单值在哪个区间，若mid为偶数位置，左区间是偶数个，右区间也是偶数个。若m与m+1相等，那么m需要加到右边（迭代时，可以直接去掉m和m+1的值），则说明单值在右区间，否则将mid加到左区间，检查左区间。
// 迭代到最后，区间长度为1时退出，即为所需要的单值。
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
// l162:解题思想，将数组视为多段交替连续的有序数组，将mid与右表值比较，大于，说明在下降，峰值在左边，小于说明上升，峰值在右表，迭代缩减区间，只剩1个时，即为峰值
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



## 2. 动态规划

多阶段决策过程，每步处理一个子问题，求解**组合优化问题**

解决递归问题的步骤：

- 定义子问题（问题降阶、增加限制、组合子问题）
- 写递推表达式
  - 递推的表达式未必一定要是最终结果，也可以是限制条件下的初步结果，然后通过max，min求取最终结果
- 识别和处理最基础子问题问题

3特性：

- 最优子结构：最优解所包含的的子问题的解也是最优的，或者说，子问题的最优解，可以推出整个问题的最优解
- 子问题重叠：使用递归，自顶向下对问题求解时，每次的子问题不是新问题，子问题会重复计算多次，将子问题结果保存，获得高效率。
- 无后效性：每个状态都是过去历史的完整总结，以前的各个阶段的状态不影响。





### 1维动态规划

问题：给定n，找出由1，3,4的的组合数量。例如：

n=5, 组合数量时6,

1+1+1+1+1+1

1+1+3

1+3+1

3+1+1

1+4

4+1

定义子问题：Dn表示由1,3,4组成n的组合数量。

递推表达式：Dn=D(n-1)+D(n-3)+D(n-4)

基础情况：D0 = 1;Dn =0 （n<0;

D0=D1=D2=1,D3=2

```java
// 代码
// 改问题与斐波那契问题的变种，都一样，只需要有一次循环，递推到所求结果
D[0]=D[1]=D[2]=1;
D[3]=2;
for (i=4;i<=n;i++)
    D[i]=D[i-1]+D[i-3]+D[i-4];


/* 
POJ2663 Tri Tiling 完美覆盖
现在给出一个3*n（0<=n<=30）的矩阵，要求用1*2的骨牌来填充，问有多少种填充方式？

3*n 需要被2整除，那么n一定非奇数，所以，子问题 可能是f(n-2),f(n-4),f(n-6),f(n-8)... 这类
将n作为列数，n=2时，可以有3个1*2方块组成三种图形。


n=4列，无法被分拆成2列构成的图形（单独2列会截断骨牌），只有2种，n=6，8...列也是一种。
具体图形是分别是（倒）凹型，中间横放骨牌
因此递推
f(0)=1,
f(2)=3,
f(4)=3*f(2)+2*(f(0))
...
f(n-2)=3*(f(n-4))+2(f(n-6)+....f(0))
f(n)=3*f(n-2)+2*(f(n-4)+f(n-6)+....+f(2)+f(0))
f(n)=f(n-2)可得：
f(n)=4f(n-2)-f(n-4)
*/

f[0]=1,f[2]=3;
for(i=4;i<=30;i+=2)
    f[i]=4f[i-2]+f[4];

/*
剑指offer42/leetcode53:
输入一个整型数组，数组中的一个或连续多个整数组成一个子数组。求所有子数组的和的最大值。
要求时间复杂度为O(n)。
分析：
根据连续数组条件，dp[i]设为以i结尾的最大子序列，保证递推式符合连续子序列限制。
递推式：
dp[i]=max(dp[i-1],nums[i])
结果，非一般地，dp[n-1]是结果，而是需要求max(dp[i])
代码见 斐波那契专题，使用了两个max。
*/

/*
leetcode121:股票买卖的最佳时间
给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。
你只能选择 某一天 买入这只股票，并选择在 未来的某一个不同的日子 卖出该股票。设计一个算法来计算你所能获取的最大利润。
返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0 。
分析：
最大利润，需要低买高卖。
设dp[i]为以第i天卖出的最大利润(当天价格减去i-1天前最低点价格)。
取max(dp[i])即为所求。
*/
public int maxProfit(int[] prices) {
    int len = prices.length;
    // i天前，买入最低点
    int minPrice = prices[0];
    int maxProfit = 0;
    for (int i=1;i<len;i++){
        maxProfit = Math.max(maxProfit, prices[i]- minPrice);
        // 更新最小值
        minPrice=Math.min(minPrice,prices[i]);
    }
    return maxProfit;
}

/**
leetcode 初级算法，打家劫舍
你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。

给定一个代表每个房屋存放金额的非负整数数组，计算你 不触动警报装置的情况下 ，一夜之内能够偷窃到的最高金额。
1 <= nums.length <= 100
0 <= nums[i] <= 400
分析：
定义子问题，将时间固定，令DP[i]是前i个房间偷取最大金额，最终问题即DP[n-1]
递推公式：DP[i]=max(nums[i]+ DP[i-2], DP[i-1])
因为只和前2相关，空间优化，只用3个值即可。
*/
public int rob(int[] nums) {
    int len = nums.length;
    if (len < 2) {
        return nums[0];
    }
    int dpi_2 = nums[0];
    int dpi_1 = Math.max( nums[1], nums[0]);
    int dp = dpi_1;
    for (int i = 2 ; i < len; i ++) {
        dp =  Math.max( nums[i] + dpi_2, dpi_1);
        dpi_2 = dpi_1;
        dpi_1 = dp;
    }
    return dp;
}
```

### 2维动态规划（LCS问题）

```java
/*
leetcode1143:
LCS：
给定2个字符串x，y，找到共同最长公共子序列，并打印长度
例子：
x:ABCBDAB
y:BDCABC
"BCAB"是最长子序列，子序列可以不连续。
那么一个字符串可能的子序列就有2^n个,直接暴力枚举，匹配的开销是2^(m+n)。

分析：
1)定义问题：
因为有2个字符串，所以问题有两个变量，这就是被叫做2维动态规划问题原因。
正如一维动态规划削减问题规模大小，可以分别对2个维度加以限制，降阶问题。
因此，定义问题D[i][j]是以字符串是i,j结束的最长公共子序列。
2)然后用子问题，递推后续的结果的递推公式是：
情形1，Xi=yi,则D[i][j]=D[i-1][j-1]+1
情形2，Xi!=yi,则D[i][j]=max(D[i][j-1],D[i-1][j])
3)最后，初始情形
D[i][0]=D[0][j]=0
*/
// 注意是<=,而不能是<
// 另外，这两个循环也可以放到DP中，但是需要额外判断，提取出来，减少判断，更加清晰，对应初始化
for(i=0;i<=n;i++) D[i][0]=0;
for(j=0;j<=m;j++) D[0][j]=0;

for(i=1;i<=n;i++){
    for(j=1;j<=m;j++){
        // 注意起始从1开始，所以下标需要是-1
        if (x[i-1]==y[j-1]) {
            //D[i-1][j-1] 在上次i-1循环已经求出
            D[i][j]=D[i-1][j-1]+1;
        }else {
            //D[i-1][j] 在上次i-1循环已经求出，D[i][j-1]本内层循环的前一次求出
            D[i][j]=Math.max(D[i-1][j],D[i][j-1])
        }
    }
}
return D[n][m];
// 空间
/*
递推公式，D[i][j]只与前一个D[i][j-1]和上一行的D[i-1][j-1]、D[i-1][j]有关。
所以可以复用数组,最容易理解的是（一维滚动数组）O(m)。
使用2*(m+1)的滚动数组：
*/
int cur,pre;
int D[2][m+1];
for(i=1;i<=n;i++){
    cur = i%2;//当前行
    pre = 1 - now;//上一行
    for(j=1;j<=m;j++){
        if (x[i-1]==y[j-1]) {
            //D[i-1][j-1] 在上次i-1循环已经求出
            D[cur][j]=D[pre][j-1]+1;
        }else {
            //D[i-1][j] 在上次i-1循环已经求出，D[i][j-1]本内层循环的前一次求出
            D[cur][j]=Math.max(D[pre][j],D[cur][j-1])
        }
    }
}
/*
实际，还可以更进一步的减少空间，使用完全一维的数组，更抽象一点。相对于原始的滚动数组，只保留当前行。
D[j-1]表示D[i][j-1]，当前行计算的前一个值
D[j]表示二维数组的D[i][j](更新后)、D[i-1][j](更新前，是上一轮计算的结果)，迭代更新数组
变量last表示D[i-1][j-1]
*/
int[] D = new int[m+1];
int last;
for(i=1;i<=n;i++){
    //last记录原始D[i-1][j-1],由于D[i-1][0]=0,所以last开始为0
    last=0;
    for(j=1;j<=m;j++){
        //此时的D[j]是D[i-1][j]，保存作为下次计算D[i-1][j-1]
        int temp = D[j];
        if (x[i-1]==y[j-1]) {
            //D[i-1][j-1] 在上次i-1循环已经求出
            D[j]=last+1;
        }else {
            //D[i-1][j] 在上次i-1循环已经求出，D[i][j-1]本内层循环的前一次求出
            //D[j-1]表示原来的D[i][j-1]
            D[j]=Math.max(D[j],D[j-1]);  
        }
        //更新last的值,由于j增加，所以，此时的D[i-1][j]，是下一次计算时D[i-1][j-1]
        last=temp;
    }
}
// 如何输出元素
/*
对于打印公共子序列，可以增加一个二维数组，记录没一个D[i][j]计算时的路径字符
每次计算D[i][j]时，有3种情况：
a. D[i][j]=D[i-1][j-1]+1;此时打印xi
b. D[i][j]=D[i-1][j];递归找
c. D[i][j]=D[i][j-1]；递归找
时间O(n)
*/
```

### 区间动态规划Interval DP

区间DP，枚举区间，把区间分成左右两个部分，然后求出左右区间再合并。

#### 典型：回文问题

```java
/*
1.leetcode516
给定一个字符串 s ，找到其中最长的回文子序列，并返回该序列的长度。可以假设 s 的最大长度为 1000。
分析:
回文特点是，首位2个字母相同，在首尾不同时，对于[i][j]的字符串可以产生2个区间[i][j-1]和[i+1][j]。
因为实际范围是多个区间，而非维度单向的递减，所以，被分做区间动态规划。
1）定义子问题（状态）
D[i][j]是字符串[i:j]范围的最长回文子序列
2）递推公式
若s[i]==s[j],那么D[i][j]=D[i+1][j-1]+2 （如果i>j,则D[i][j]=0）
若s[i]!=s[j],那么D[i][j] =max(D[i+1][j], D[i][j-1])
3）初始情况
单个字符D[i][i]=1,所求D[0][n-1]。
关于递推顺序，可以看到应该先求i大的，然后i减小，而j先求小的，然后在求大的。
所以，两层循环，i循环，从n-1开始，j从i+1开始(j小于i为0，j==i为1)
起始i =n-1, j =n 忽略
起始i =n-2,j =n-1, 若s[i]==s[j], f[n-2][n-1] = f[n-1][n-2]+2，其中f[n-1][n-2]=0
不等，单字符回文数1，max(f[n-1][n-1],f[n-2][n-2])==1

时间，空间开销O(n^2)
*/
public int longestPalindromeSubseq(String s) {
    int n = s.length();
    // 默认值为0
    int[][] f = new int[n][n];
    for (int i = n - 1; i >= 0; i--) {
        f[i][i] = 1;
        for (int j = i + 1; j < n; j++) {
            if (s.charAt(i) == s.charAt(j)) {
                f[i][j] = f[i + 1][j - 1] + 2;
            } else {
                f[i][j] = Math.max(f[i + 1][j], f[i][j - 1]);
            }
        }
    }
    return f[0][n - 1];
}

/*
2. leetcode647
给定一个字符串，你的任务是计算这个字符串中有多少个回文子串。
大问题：一个子串是否是回文串，统计回文串个数
子问题：一个子串是回文串，那么，除去首尾，仍然是回文串，所以 


*/



/*
3.给定字符串，找到使其成为回文字符串的最少字符个数
Ab3bd
答案：dAb3bAd or Adb3dbA
插入2个字符(d,A)

1) 定义子问题

D[i][j]是使字符串[i,j]成为回文的最小字符个数
2)

*/

```



 

### 参考

- [【DP专辑】ACM动态规划总结](https://blog.csdn.net/cc_again/article/details/25866971)



### 2.1 斐波那契

入门级：

自顶向下：递归思想+memo想法（记忆化搜索），然后转化为自底向上，即为动态规划。

例子，斐波那契 f(n) = f(n-1) + f(n-2)， 记录每个f(n)避免重复计算（重叠子问题），自底向上，从n=2开始，递推计算。



```java
// leetcode 53
// 最大子序和：给定一个整数数组 nums ，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
// 思路：动态规划
// 递推，减少问题集大小，化简难度
// f(i)表示以第i个数结尾的连续子数组的最大和，那么max(f(i),0<i<n-1)即所求答案，
// 问题简化了一阶，从两头可变，增加限制以i结尾。
// f(i)的求法，f(i)= max(nms[i]+f(i-1), nums[i])
// 时间复杂度 O(n),空间复杂度O(n)
// 由于f(i) 只与f(i-1)相关，可以用一个变量pre维护f(i-1),用n次比较替换当前max(f(i))，代替n次空间开销，变成空间O(1)。

public int maxSubArray(int[] nums) {
    int pre = 0, maxAns = nums[0];
    for (int x : nums) {
        pre = Math.max(pre + x, x);
        maxAns = Math.max(maxAns, pre);
    }
    return maxAns;
}
// todo:扩展



```

### 2.2 三角形最小路径和

```java
// leetcode 120
// 三角形最小路径和:
// 给定一个三角形 triangle ，找出自顶向下的最小路径和。
// 每一步只能移动到下一行中相邻的结点上。相邻的结点 在这里指的是 下标 与 上一层结点下标 相同或者等于 上一层结点下标 + 1 的两个结点。也就是说，如果正位于当前行的下标 i ，那么下一步可以移动到下一行的下标 i 或 i + 1 。
/*
输入：triangle = [[2],[3,4],[6,5,7],[4,1,8,3]]
输出：11
解释：如下面简图所示：
   2
  3 4
 6 5 7
4 1 8 3
自顶向下的最小路径和为 11（即，2 + 3 + 5 + 1 = 11）。
*/
```

分析

#### 三角形矩阵的表示

三角形矩阵，是非规范矩阵，用vector<vector<integer>> 类型或list<list<integer>>表示，根据常规矩阵\[i\]\[j\]使用习惯，可以将三角形放在第四象限的直角坐标系中，纵坐标为i，方向向下，横坐标为j，方向向右，并将等腰三角左对齐转化为等腰直角三角。示例：

```
-------> j
|1
|2 3
|4 5 6
i
这样，根据三角矩阵的移动特性，f(0,0)转移的两个路径是f(1,0)和f(1,1),并且下一步的搜索空间还是一个小一阶n-1的直角三角型。
```

在能够正常表示三角矩阵，并搜索子空间后，原问题的状态转移方程便可求出：`f(i,j)=min(f(i+1,j),f(i+1,j+1))+A(i,j), 0<=i<n,0<=j<n`问题所求f(0,0)是多少。

```java
// leetcode 120 题解
// 为了计算f(0,0),需要先计算f(1,0)和f(1,1)，递推，i应该从n-2开始，0<=j<i+1,作为最底层，向三角顶点计算f(i,j)，对于f(i,j)只与f(i+1,)有关，与f(i+2,) ，所以，存储空间可以使用A(i,j)本身,无需另外存储。

public int minimumTotal(List<List<Integer>> triangle) {
            int len =  triangle.size();
            for (int i=len -2; i>=0; i-- ) {
                for (int j=0; j < i+1;j++ ) {
                    Integer sum = triangle.get(i).get(j)+ Math.min(triangle.get(i+1).get(j),triangle.get(i+1).get(j+1));
                    triangle.get(i).set(j,sum); 
                }
            }
            return triangle.get(0).get(0);
    }
```





### 2.3 01背包问题

有n个重量和价值分别为wi，vi的武平，从这些物品中提先出总重量不超过W的物品，求价值总和最大值。

1<=n<=100

1<=wi,vi <<100

1<=W<=10000





### 2.4 括号匹配

（pingcap19 面试题目）

还是不太理解，贪心思想，sadness。。。

```java
/**
leetcode678:
给定一个只包含三种字符的字符串：（ ，） 和 *，写一个函数来检验这个字符串是否为有效字符串。有效字符串具有如下规则：
任何左括号 ( 必须有相应的右括号 )。
任何右括号 ) 必须有相应的左括号 ( 。
左括号 ( 必须在对应的右括号之前 )。
* 可以被视为单个右括号 ) ，或单个左括号 ( ，或一个空字符串。
一个空字符串也被视为有效字符串。
case:
()  true
(*) true
(*)) true
贪心思想：
在处理字符串中的当前字符时，让 lo、hi 分别为可能的最小和最大左括号数
遇到左括号：lo++, hi++
遇到星号：lo--, hi++（因为星号有三种情况）
遇到右括号：lo--, hi--
lo要保持不小于0，
如果hi < 0，说明把星号全变成左括号也不够了，False
最後，如果lo > 0，说明末尾有多余的左括号，False
时间复杂度O(n)
*/
class Solution {
    public boolean checkValidString(String s) {
       int lo = 0, hi = 0;
       for (char c: s.toCharArray()) {
           lo += c == '(' ? 1 : -1;
           hi += c != ')' ? 1 : -1;
           if (hi < 0) break;
           lo = Math.max(lo, 0);
       }
       return lo == 0;
    }
}


/*
栈思路@Booooo_：
栈(先进后出)
定义两个栈，leftStack 存 ' ( ' 所在位置的下标，starStack 存 '*' 所在位置的下标。
1.当遇到 ' ( ' 时，' ( ' 所在位置的下标入栈；当遇到 ' * ' 时，' * ' 所在位置的下标入栈。
2.当遇到 ' ) ' 时，要令 leftStack 中的栈顶元素出栈，若此时 leftStack 为空，则继续看 starStack 是否为空，若不为空则 starStack 栈顶元素出栈，若为空返回则 false (遇到了 ' ) '，但在这之前一个 ' ( ' 和 ' * ' 都没遇到，则一定不会匹配)。
3.当字符串全部遍历完时，若 starStack 的长度比 leftStack 的长度要小，则返回 false (有剩余的 ' ( '，但 ' * ' 的数量不够了，则一定不会匹配)；否则，比较两个栈的栈顶元素值的大小，要保证 ' ( ' 在 ' * ' 的左边(starStack.peek() > leftStack.peek())才能匹配成功，当遇到满足条件的栈顶元素时，栈顶元素出栈，继续比较下一个。只要有一次该条件不满足，则直接返回 false；否则，返回 true。
时间、空间O(n)
*/ 
class Solution {

    public boolean checkValidString(String s) {
        int n = s.length();
        Stack<Integer> leftStack = new Stack<>();
        Stack<Integer> starStack = new Stack<>();

        for (int i=0;i<n;i++) {
            if (s.charAt(i) == '(') {
                leftStack.push(i);//存的是下标
            } else if (s.charAt(i) == '*') {
                starStack.push(i);
            } else {
                if (!leftStack.isEmpty()) {
                    leftStack.pop();
                } else {
                    if (!starStack.isEmpty()) {
                        starStack.pop();
                    } else {
                        return false;
                    }
                }
            }
        }
        if (starStack.size() < leftStack.size()) return false;
        else {
            while (!starStack.isEmpty()) {
                if (starStack.peek() < leftStack.peek()) {
                    return false;
                }
                starStack.pop();
                leftStack.pop();
            }
            return true;
        }
    }
}
```



## 3.贪心算法

思想：局部最优，也是全局最优。

无后效性，某个状态以前的过程不影响以后的状态，只与当前的状态有关。

数学归纳法、反证法可以证明贪心算法。

**题型特点：问题中带有最大/小，最多/少，是否满足可以转化为前者**

```java
/*
leetcode455:饼干分配
假设你是一位很棒的家长，想要给你的孩子们一些小饼干。但是，每个孩子最多只能给一块饼干。
对每个孩子 i，都有一个胃口值 g[i]，这是能让孩子们满足胃口的饼干的最小尺寸；并且每块饼干 j，都有一个尺寸 s[j] 。如果 s[j] >= g[i]，我们可以将这个饼干 j 分配给孩子 i ，这个孩子会得到满足。你的目标是尽可能满足越多数量的孩子，并输出这个最大数值。

为什么是贪心算法：
要求满足数量最多的孩子，也是需要将饼干最大化利用。那么从最小大小的饼干开始，给胃口最小的孩子，可以保证饼干最小的被浪费。
证明：假设贪心策略给第i个孩子第m个饼干，并且是满足第i个孩子的最小的饼干。假设选择给第i个孩子，最优解法是饼干n，n大于m，那么使用m代替n，不影响最优策略，后续能使用m给第j个孩子的地方一定可以使用n。所以贪心算法一定可以达到理论保证满足最多的孩子（可能存在多个方案）。
*/
import java.util.Arrays;
public int findContentChilderen(int [] g, int [] s){
    // 从小到大，排序
    Arrays.sort(g);
	Arrays.sort(s);
    int len1 = g.length;
    int len2 = s.length;
    int i=0,j=0;
    while(i<len1 && j< len2){
        if (g[i] <= s[j]){
            i++;
        }
        j++;
    }
    return i;// i即满足孩子的数量
}

/*
tips:Arrays 静态方法
1.toSting
int[] arrInt = {55,44,33,22,11};
Arrays.toString(arrInt);//将数组按照默认格式输出为字符串,[55, 44, 33, 22, 11]
2.sort(Array[] array, int fromIndex, int toIndex)
Arrays.sort(arrInt); //默认升序排序
3.binarySearch(Array[] array, int fromIndex, int toIndex, data key)
int[] arrInt = {55,44,33,22,11};
//使用binarySearch方法前对数组进行排序
Arrays.sort(arrInt);
int  pos = Arrays.binarySearch(arrInt, 44);// return 3
*/


/*
leetcode452:用最少数量的箭引爆气球
在二维空间中有许多球形的气球。对于每个气球，提供的输入是水平方向上，气球直径的开始和结束坐标。由于它是水平的，所以纵坐标并不重要，因此只要知道开始和结束的横坐标就足够了。开始坐标总是小于结束坐标。

一支弓箭可以沿着 x 轴从不同点完全垂直地射出(向y轴无限前进)。在坐标 x 处射出一支箭，若有一个气球的直径的开始和结束坐标为 xstart，xend， 且满足  xstart ≤ x ≤ xend，则该气球会被引爆。可以射出的弓箭的数量没有限制。 弓箭一旦被射出之后，可以无限地前进。我们想找到使得所有气球全部被引爆，所需的弓箭的最小数量。
给你一个数组 points ，其中 points [i] = [xstart,xend] ，返回引爆所有气球所必须射出的最小弓箭数。

输入：points = [[10,16],[2,8],[1,6],[7,12]]
输出：2
解释：对于该样例，x = 6 可以射爆 [2,8],[1,6] 两个气球，以及 x = 11 射爆另外两个气球

分析：
射出的弓箭最小，即每次尽可能引爆最多的气球。
可以模拟，看出将从最小的xend开始，以气球尾部xend[i]为界，可以尽可能的将其他xstart[j]<xend[i]的气球射破。

*/
public int findMinArrowShots(int [][] points){
    if (points.length==0) return 0;
    // 以xend排序，从小到大
    // 整型溢出问题
    //Arrays.sort(points,(a,b)->(a[1]-b[1]));
    // 或者由于等号不影响题目，Arrays.sort(points,(a,b)->(a[1]<b[1]?-1:1));
    Arrays.sort(points,(a,b)->{
        if (a[1] < b[1]){
           return -1;
        } else if (a[1]==b[1]){
           return 0;
        } else return 1;
    });
    
    int curPos = points[0][1];
    int ret = 1;
    for (int i=1;i<points.length;i++){
        if (points[i][0]<=curPos){
            continue;
        }
        curPos = points[i][1];
        ret++;
    }
    return ret;
}

/*
leetcode605:种花问题
假设有一个很长的花坛，一部分地块种植了花，另一部分却没有。可是，花不能种植在相邻的地块上，它们会争夺水源，两者都会死去。

给你一个整数数组  flowerbed 表示花坛，由若干 0 和 1 组成，其中 0 表示没种植花，1 表示种植了花。另有一个数 n ，能否在不打破种植规则的情况下种入 n 朵花？能则返回 true ，不能则返回 false。

分析：
能否种n朵花，使用贪心的思想是，尽可能的种花，若可以种下n朵花，表示符合。
尽可能种花，最大化情况下是，只间隔一朵。
*/
public boolean canPlaceFlowers(int[] flowerbed,int n){
    int len = flowerbed.length;
    // -1位置，预先为0
    int pre = 0;
    for (int i=0;i<len && n > 0;i++){
        if (pre==0&&flowerbed[i]==0
            && (i==len-1 || flowerbed[i+1]==0)){
            n--;
            // 当前位置种花
            flowerbed[i]=1;
        }
        pre = flowerbed[i];
    }
    return n==0;
}

/*
leetcode122.买卖股票的最佳时机 II
给定一个数组 prices ，其中 prices[i] 是一支给定股票第 i 天的价格。
设计一个算法来计算你所能获取的最大利润。你可以尽可能地完成更多的交易（多次买卖一支股票）。
注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
分析：
利润最大，即每次低买高买，吃掉波动中的所有上升阶段，忽略掉下降阶段。
*/
public int maxProfit(int[] prices) {
    int profit =0;
    for(int i=1;i< prices.length;i++){
        profit+=Math.max(0,prices[i]-prices[i-1]);
    }
    return profit;
}

/*
leetcode860:柠檬水找零
在柠檬水摊上，每一杯柠檬水的售价为 5 美元。
顾客排队购买你的产品，（按账单 bills 支付的顺序）一次购买一杯。
每位顾客只买一杯柠檬水，然后向你付 5 美元、10 美元或 20 美元。你必须给每个顾客正确找零，也就是说净交易是每位顾客向你支付 5 美元。
注意，一开始你手头没有任何零钱。
如果你能给每位顾客正确找零，返回 true ，否则返回 false 。
分析：
分类讨论
支付5元，无需找零;
支付10元，需要找零5;
支付20元，需要找零10(5+5)+5元;同时20不可用于找零。
要能尽可能能找零，应在给20找零时先用10元，再用5元。
*/
public boolean lemonadeChange(int[] bills) {
    int cnt5=0;
    int cnt10=0;
    // for (int bill : bills) {
    for (int i=0;i < bills.length;i++){
        if (bills[i] == 5){
            cnt5++;
        }else if (bills[i]==10){
            if (cnt5>0){
                cnt10++;
                cnt5--;
            }else {
                return false;
            }
        } else {
            if (cnt10>0 && cnt5 > 0){
                cnt10--;
                cnt5--;
            }else if (cnt5 >= 3){
                cnt5-=3;
            }else {
                return false;
            }
        }
    }
    return true;
}

/*
LeetCode55：
给定一个非负整数数组 nums ，你最初位于数组的 第一个下标 。
数组中的每个元素代表你在该位置可以跳跃的最大长度。
判断你是否能够到达最后一个下标。
输入：nums = [2,3,1,1,4]
输出：true
解释：可以先跳 1 步，从下标 0 到达下标 1, 然后再从下标 1 跳 3 步到达最后一个下标。
反例：[3,2,1,0,4]
分析：
记录每个位置能到达的最远位置，然后迭代，这个范围内能到达的最远位置。
*/
public class Solution {
    public boolean canJump(int[] nums) {
        int n = nums.length;
        int rightmost = 0;
        for (int i = 0; i < n; ++i) {
            if (i <= rightmost) {
                rightmost = Math.max(rightmost, i + nums[i]);
                if (rightmost >= n - 1) {
                    return true;
                }
            }
        }
        return false;
    }
}
```



## 4.排序算法

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

### 链表

常用技巧：

双指针，用于一次遍历，或者step长不一样，检查是否相遇，判断是否有环。

#### 删除链表的倒数第N个节点

```java
/*
leetcode:删除链表的倒数第N个节点，并且返回链表的头结点。
解题方法：双指针。额外需要注意，删除头节点情况。
*/
public ListNode removeNthFromEnd(ListNode head, int n) {
    ListNode fast = head;
    ListNode slow = head;
    //fast移n步，这里忽略了 n > length处理
    for (int i = 0; i < n; i++) {
        fast = fast.next;
    }
    //如果fast为空，表示删除的是头结点
    if (fast == null)
        return head.next;

    while (fast.next != null) {
        fast = fast.next;
        slow = slow.next;
    }
    //这里最终slow不是倒数第n个节点，他是倒数第n+1个节点，
    //他的下一个结点是倒数第n个节点,所以删除的是他的下一个结点
    slow.next = slow.next.next;
    return head;
}
```

借助栈，对单链表，回文，逆序访问。

#### 回文链表

```java
/*
leetcode:回文链表
给你一个单链表的头节点 head ，请你判断该链表是否为回文链表。如果是，返回 true ；否则，返回 false 。
*/

// 直接反转链表
public boolean isPalindrome(ListNode head) {
    ListNode fast = head, slow = head;
    //通过快慢指针找到中点
    while (fast != null && fast.next != null) {
        fast = fast.next.next;
        slow = slow.next;
    }
    //如果fast不为空，说明链表的长度是奇数个
    if (fast != null) {
        slow = slow.next;
    }
    //反转后半部分链表
    slow = reverse(slow);

    fast = head;
    while (slow != null) {
        //然后比较，判断节点值是否相等
        if (fast.val != slow.val)
            return false;
        fast = fast.next;
        slow = slow.next;
    }
    return true;
}

//反转链表
public ListNode reverse(ListNode head) {
    ListNode prev = null;
    while (head != null) {
        ListNode next = head.next;
        head.next = prev;
        prev = head;
        head = next;
    }
    return prev;
}

//链接：https://leetcode-cn.com/leetbook/read/top-interview-questions-easy/xnv1oc/?discussion=HUFvuK

// 性能差一点
public boolean isPalindrome(ListNode head) {
    if (head == null)
        return true;
    ListNode temp = head;
    Stack<Integer> stack = new Stack();
    //链表的长度
    int len = 0;
    //把链表节点的值存放到栈中
    while (temp != null) {
        stack.push(temp.val);
        temp = temp.next;
        len++;
    }
    //len长度除以2
    len >>= 1;
    //然后再出栈
    while (len-- >= 0) {
        if (head.val != stack.pop())
            return false;
        head = head.next;
    }
    return true;
}
```



### 并查集



### 跳表SkipList



### 二叉树树

#### 二叉树最大深度、对称二叉树

```java
/**
给定一个二叉树，找出其最大深度。

二叉树的深度为根节点到最远叶子节点的最长路径上的节点数。

说明: 叶子节点是指没有子节点的节点。

示例：
给定二叉树 [3,9,20,null,null,15,7], 返回的最大深度是3.
    3
   / \
  9  20
    /  \
   15   7
https://leetcode-cn.com/leetbook/read/top-interview-questions-easy/xnd69e/
*/

// 1.递归
int maxdepth(TreeNode root, int depth) {
    if (root == null) {
        return depth;
    }
    return Math.max(maxdepth(root.left, depth + 1), maxdepth(root.right, depth + 1));
}

public int maxDepth(TreeNode root) {
    return maxdepth(root, 0);
}
// 精简写法
public int maxDepth(TreeNode root) {
    return root==null? 0 : Math.max(maxDepth(root.left), maxDepth(root.right))+1;
}

// 2. BFS
// 根据运行时间来看，效率不高，可能原因在于申请队列，入队出队的时间消耗。
// 遍历一遍，理论还是O(n)
// 而基于递归的方法。在无栈溢出情况下，也是O(N)，但是操作精简，性能更好。
public int maxDepth(TreeNode root) {
    if (root == null)
        return 0;
    //创建一个队列
    Deque<TreeNode> deque = new LinkedList<>();
    // Queue<Integer> q = new LinkedList<Integer>();
    deque.push(root);
    int count = 0;
    while (!deque.isEmpty()) {
        //每一层的个数
        int size = deque.size();
        while (size-- > 0) {
            TreeNode cur = deque.pop();
            if (cur.left != null)
                deque.addLast(cur.left);
            if (cur.right != null)
                deque.addLast(cur.right);
        }
        count++;
    }
    return count;
}

// 3.DFS
public int maxDepth(TreeNode root) {
    if (root == null)
        return 0;
    //stack记录的是节点，而level中的元素和stack中的元素
    //是同时入栈同时出栈，并且level记录的是节点在第几层
    Stack<TreeNode> stack = new Stack<>();
    Stack<Integer> level = new Stack<>();
    stack.push(root);
    level.push(1);
    int max = 0;
    while (!stack.isEmpty()) {
        //stack中的元素和level中的元素同时出栈
        TreeNode node = stack.pop();
        int temp = level.pop();
        max = Math.max(temp, max);
        if (node.left != null) {
            //同时入栈
            stack.push(node.left);
            level.push(temp + 1);
        }
        if (node.right != null) {
            //同时入栈
            stack.push(node.right);
            level.push(temp + 1);
        }
    }
    return max;
}


/**
leetcode:对称二叉树
给定一个二叉树，检查它是否是镜像对称的。
    1
   / \
  2   2
 / \ / \
3  4 4  3
*/
public boolean isSymmetric(TreeNode root) {
    if (root == null) {
        return true;
    }
    return isSymmetricHelper(root.left, root.right);
}

public boolean isSymmetricHelper(TreeNode left, TreeNode right) {
    //如果左右子节点都为空，说明当前节点是叶子节点，返回true
    if (left == null && right == null)
        return true;
    //如果当前节点只有一个子节点或者有两个子节点，但两个子节点的值不相同，直接返回false
    if (left == null || right == null || left.val != right.val)
        return false;
    //然后左子节点的左子节点和右子节点的右子节点比较，左子节点的右子节点和右子节点的左子节点比较
    return isSymmetricHelper(left.left, right.right) && isSymmetricHelper(left.right, right.left);
}
```

#### 二叉树初始化，遍历

```java
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode() {}
    TreeNode(int val) { this.val = val; }
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}
// 初始化
public TreeNode  init( int [] nums) {
    if (nums.length ==0) return null;
    TreeNode [] nodes = new TreeNode[nums.length];
    int i = 1;
    TreeNode root = new TreeNode(nums[0]);
    nodes[0] = root;
    while (i <  nums.length) {
        if (nums[i] != 0) {
            nodes[i] = new TreeNode(nums[i]);
            if (i %2 == 1) {
                nodes[(i-1)/2].left = nodes[i];
            } else {
                nodes[(i-1)/2].right = nodes[i];
            }
        }
        i++;
    }
    return root;
}
// 层次遍历打印非空节点
public void printBinaryTree(TreeNode root) {
    if (root == null) {
        return;
    }
    Queue<TreeNode> queue = new LinkedList<>();
    queue.add(root);
    while (!queue.isEmpty()) {
        TreeNode node = queue.poll();
        System.out.println(node.val);
        if (node.left != null) {
            queue.add(node.left);
        }
        if (node.right != null) {
            queue.add(node.right);
        }
    }
}
```

#### 平衡二叉树

```c++
/*
判断给定二叉树是否平衡
思路：
根据定义，计算左右孩子的深度，不超过1时满足定义，并且左右孩子都是平衡二叉树
*/
bool isBalanced(TreeNode * root){
    return maxDepth(root) != -1;
}
int maxDepth(TreeNode * root){
    if (root == NULL){
        return 0;
    }
    int leftDepth = maxDepth(root->left);
    int rightDepth = maxDepth(root->right);
    // 检查左右孩子是否平衡，以及当前节点是否平衡
    if (leftDepth ==-1 || rightDepth ==-1 || abs(leftDepth - rightDepth) >1) {
        return -1;
    }
    return max(leftDepth,rightDepth) + 1;
}
```

#### 二叉搜索树

特性：

- 左节点 <= 根 <= 右节点
- 中遍历即有序数组
- 复杂度，各个操作，都是O（logN）

```java
/*
求二叉搜索树间，任意两个节点间最小值。

技巧：
利用二叉搜索树的特点，中序遍历即为有序结果，则最小的节点差值，在中序结果，相邻结果的差值中取最小值。
*/
int min = Integer.MAX_VALUE;
TreeNode prev =null;
int getMinDifference(TreeNode root) {
    if (root == null) return min;
    // 遍历左子树，左子树的最小差值
    getMinDifference(root.left);
    if (prev !=null) {
        min = Math.min(min,root.val - prev.val);
    }
    prev = root;
    // 遍历右子树，右子树的最小差值
    getMinDifference(root.right);
    return min;
}
// 迭代版
// 提供stack 模拟中序遍历
Deque<TreeNode> stack = new ArrayDeque<TreeNode>();
while(root != null || (!stack.isEmpty())) {
    if (root != null) {
        stack.push(root);
        root = root.left;// 一直迭代到最左端
    } else {
        root = stack.pop();
        if (prev != null) {
            min = Math.min(min, root.val - prev.val);
        }
        prev = root;
        // 重复过程检查当前右子树
        root = root.right;
    }
}
```





### 最小/大堆

- 定义：**完全二叉树**，任何非叶子节点的关键字，都不大于/小于其左右孩子节点的关键字
- 存储结构
  - 数组，完全二叉树，没有空间浪费
- 堆的构造：
  - 半成堆，左右孩子均满足堆的定义
  - 自顶向下的交换（选择左右孩子中较小者），使其满足堆定义
- 任意完全二叉树，自下而上的逐步调整为堆，从最后一个非叶子节点开始
- 堆中插入元素
  - 插在数组末尾，然后向上调整
- 堆中删除堆顶元素
  - 将末尾元素替换堆顶元素，然后向下调整

```java
public class MinHeap {

    private int [] heapArr;
    private int maxHeapSize;
    private int currHeapSize;

    public boolean isFull() {
        return currHeapSize == maxHeapSize;
    }

    public boolean isEmptry() {
        return currHeapSize == 0;
    }

    public MinHeap(int maxHeapSize) {
        this.maxHeapSize = maxHeapSize;
        this.heapArr = new int[maxHeapSize];
        this.currHeapSize = 0;
    }

    public MinHeap(int [] nums) {
        this.heapArr = nums;
        this.maxHeapSize = nums.length;
        this.currHeapSize = nums.length;

        int i = (currHeapSize -2)/2; //last has child node
        while( i>=0) {
            FilterDown(i); //make i is min Heap
            i--;
        }
    }

    public void FilterUp(int i) {
        int j = i;
        int temp = heapArr[i];
        int p = (j-1)/2; // parent index
        while (j > 0) { // j is not root node
            if (heapArr[i] <= temp) { // find parent <= insert value
                break;
            } else {
                heapArr[j] = heapArr[i]; // adjust, pushdown
                j = i;
                i = (j-1)/2;
            }
        }
        heapArr[j] = temp;
    }

    public void FilterDown(int i) {
        int j = 2*i +1; // left child
        int temp = heapArr[i];
        while (j < this.currHeapSize){
            // find min child
            if (j+1 < this.currHeapSize && heapArr[j+1] < heapArr[j]) {
                j++;
            }
            if (temp <= heapArr[j]) {
                break;
            } else {
                heapArr[i] = heapArr[j];
                i = j;
                j = 2*i +1;
            }
        }
        heapArr[i] = temp;
    }

    public void Insert(int val) {
        if (isFull()) {
            System.out.println("Heap is full!");
        }
        heapArr[currHeapSize] = val;
        FilterUp(currHeapSize);
        currHeapSize++;
    }

    public int DeleteTop() {
        if (isEmptry()) {
            System.out.println("Heap is emptry!");
        }
        int temp = heapArr[0];
        heapArr[0] = heapArr[currHeapSize-1];
        currHeapSize--;
        FilterDown(0);
        return temp;
    }

    public void HeapSort(int [] nums) {
        this.heapArr = nums;
        this.maxHeapSize = nums.length;
        this.currHeapSize = nums.length;

        int i = (currHeapSize -2)/2; //last has child node
        while( i>=0) {
            FilterDown(i); //make i is min Heap
            i--;
        }
        for (i =0; i< nums.length; i++) {
            int min = DeleteTop();
            System.out.print(min+" ");
        }
    }
}
```



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





