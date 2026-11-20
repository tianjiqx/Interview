# 双指针算法：核心思想、技巧与题型总结

双指针（Two Pointers）是 LeetCode 高频算法技巧之一。它不是一种固定算法，而是利用两个指针之间的位置、速度或状态关系，减少不必要的遍历，从而降低时间或空间复杂度。

通常可以将双指针分成 5 种主要类型。

## 一、双指针的五大类型

| 类型     | 指针移动方式      | 核心目的     | 典型题目           |
| ------ | ----------- | -------- | -------------- |
| 左右指针   | 从两端向中间移动    | 缩小搜索范围   | 两数之和、盛最多水的容器   |
| 快慢指针   | 同方向、不同速度    | 原地处理、检测环 | 删除重复项、寻找重复数    |
| 滑动窗口   | 左右边界同向移动    | 维护连续区间   | 最长无重复子串、最小覆盖子串 |
| 同向双指针  | 两指针同向且按条件推进 | 合并、分组、匹配 | 合并有序数组、移动零     |
| 分离链表指针 | 不同位置或不同步长   | 寻找特殊节点   | 倒数第 K 个节点、相交链表 |

其中，滑动窗口也可以视为同向双指针的一种特殊形式，快慢指针与同向指针之间也存在重叠。

## 二、左右指针（对撞指针）

核心： 两个指针从数组两端出发，根据条件决定移动哪一个指针。

示例：有序数组中寻找两数之和

例如 LeetCode 167「两数之和 II」。

给你一个下标从 1 开始的整数数组 numbers ，该数组已按 非递减顺序排列。

请你从数组中找出满足相加之和等于目标数 target 的 两个 数。令这两个数分别是 numbers[index1] 和 numbers[index2] ，其中 1 <= index1 < index2 <= numbers.length 。

以长度为 2 的整数数组 [index1, index2] 的形式返回这两个整数的下标 index1 和 index2。

你可以假设每个输入 只对应唯一的答案 ，而且你 不可以 重复使用相同的元素。

你所设计的解决方案必须只使用常数级的额外空间。

```
public int[] twoSum(int[] numbers, int target) {
    // l + r = sum, 判断 sum 与 starget，迭代，两边向中间走
    int l = 0, r = numbers.length - 1;

    while (l < r) {
        int sum = numbers[l] + numbers[r];

        if (sum == target) {
            return new int[]{l + 1, r + 1};
        } else if (sum < target) {
            // 增大 sum
            l++;
        } else {
            // 减小 sum
            r--;
        }
    }

    return new int[]{-1, -1};
}
```

关键技巧：

- `sum < target`：左指针右移，使和变大。
- `sum > target`：右指针左移，使和变小。
- 每次移动都要能够证明：被排除的候选不可能成为答案。

适用题型：有序数组搜索、回文判断、三数之和、容器面积等。

## 三、快慢指针

这是 LeetCode 287「寻找重复数」使用的方法。


给定一个包含 n + 1 个整数的数组 nums ，其数字都在 [1, n] 范围内（包括 1 和 n），可知至少存在一个重复的整数。

假设 nums 只有 一个重复的整数 ，返回 这个重复的数 。

你设计的解决方案必须 不修改 数组 nums 且只用常量级 O(1) 的额外空间。
(简单好理解的算法是，迭代二分1-n，检查范围内是否有重复元素)

快慢指针实际上有两种不同用途。

### 1. 快慢指针：原地读写

一个指针负责读取数据，另一个负责维护有效数据区域。

例如 LeetCode 26「删除有序数组中的重复项」。

给你一个 非严格递增排列 的数组 nums ，请你 原地 删除重复出现的元素，使每个元素 只出现一次 ，返回删除后数组的新长度。元素的 相对顺序 应该保持 一致 。然后返回 nums 中唯一元素的个数。

考虑 nums 的唯一元素的数量为 k。去重后，返回唯一元素的数量 k。

nums 的前 k 个元素应包含 排序后 的唯一数字。下标 k - 1 之后的剩余元素可以忽略。

```
public int removeDuplicates(int[] nums) {
    int slow = 0;

    for (int fast = 1; fast < nums.length; fast++) {
        if (nums[fast] != nums[slow]) {
            slow++;
            nums[slow] = nums[fast];
        }
    }

    return slow + 1;
}
```

这里：

- `fast`：扫描数组，寻找符合条件的元素。
- `slow`：指向已经处理完成的有效区域的最后一个元素。

技巧：维护一个清晰的区间不变量。

例如上面的 `[0, slow]` 始终是去重后的有效区域。

### 2. 快慢指针：不同速度（Floyd 判圈算法）

环形链表中的快慢指针

```
ListNode slow = head;
ListNode fast = head;

while (fast != null && fast.next != null) {
    slow = slow.next;
    fast = fast.next.next;

    if (slow == fast) {
        // 存在环
        return true;
    }
}

return false;
```

为什么能判断环？

如果存在环，快指针每轮比慢指针多走一步，两者之间的环上距离会不断改变，最终必然相遇。

如果不存在环，快指针先到达链表末尾。

技巧：两个指针的速度差可以揭示链表中的结构特征。

典型题目包括：

- LeetCode 141：环形链表
- LeetCode 142：环形链表 II（寻找环入口）
- LeetCode 287：寻找重复数
- LeetCode 876：链表的中间结点

## 四、滑动窗口

滑动窗口的两个指针通常是 `left` 和 `right`，它们表示一个连续区间。

核心是：

右指针扩张窗口，左指针收缩窗口。

例如 LeetCode 209「长度最小的子数组」，数组元素均为正整数。

```
public int minSubArrayLen(int target, int[] nums) {
    int left = 0, sum = 0;
    int ans = Integer.MAX_VALUE;

    for (int right = 0; right < nums.length; right++) {
        sum += nums[right];

        while (sum >= target) {
            ans = Math.min(ans, right - left + 1);
            sum -= nums[left];
            left++;
        }
    }

    return ans == Integer.MAX_VALUE ? 0 : ans;
}
```

这里的区间 `[left, right]` 就是当前窗口。

要掌握三个问题：

1. 什么时候扩大窗口？
2. 什么时候缩小窗口？
3. 什么时候更新答案？

最重要的是根据题目的约束，判断窗口是否具有单调性。例如该题要求正整数，才能保证左边界移动后窗口和不会增大。

## 五、同向双指针

两个指针沿同一个方向遍历，但职责不同。

典型题目：LeetCode 283「移动零」。

```
public void moveZeroes(int[] nums) {
    int slow = 0;

    for (int fast = 0; fast < nums.length; fast++) {
        if (nums[fast] != 0) {
            int temp = nums[slow];
            nums[slow] = nums[fast];
            nums[fast] = temp;
            slow++;
        }
    }
}
```

此时：

- `[0, slow)`：已处理好的非零元素。
- `[slow, fast)`：已扫描、等待处理的零元素区域。
- `[fast, n)`：尚未处理的区域（在本轮处理前）。

其本质是将数组划分成具有不同含义的逻辑区域。

## 六、分离链表指针

例如 LeetCode 19「删除链表的倒数第 N 个结点」。

核心技巧是让两个指针保持固定的距离。

```
ListNode dummy = new ListNode(0, head);
ListNode fast = dummy, slow = dummy;

// fast 先走 n + 1 步
for (int i = 0; i <= n; i++) {
    fast = fast.next;
}

// fast 和 slow 保持固定距离
while (fast != null) {
    fast = fast.next;
    slow = slow.next;
}

// slow 指向待删除节点的前一个节点
slow.next = slow.next.next;

return dummy.next;
```

这里 `fast` 与 `slow` 的相对距离始终保持不变，所以当 `fast` 到达末尾时，`slow` 就能定位到倒数第 N 个节点的前驱。

## 七、LeetCode 高频双指针题目

按学习难度推荐以下练习顺序：

| 题目                       | 类型   | 难度 | 核心技巧   |
| ------------------------ | ---- | -- | ------ |
| LeetCode 125. 验证回文串      | 左右指针 | 简单 | 对撞     |
| LeetCode 283. 移动零        | 同向指针 | 简单 | 原地交换   |
| LeetCode 26. 删除有序数组中的重复项 | 快慢指针 | 简单 | 原地去重   |
| LeetCode 167. 两数之和 II    | 左右指针 | 中等 | 利用排序   |
| LeetCode 11. 盛最多水的容器     | 左右指针 | 中等 | 贪心缩小   |
| LeetCode 15. 三数之和        | 左右指针 | 中等 | 排序、去重  |
| LeetCode 209. 长度最小的子数组   | 滑动窗口 | 中等 | 动态区间   |
| LeetCode 3. 无重复字符的最长子串   | 滑动窗口 | 中等 | 维护窗口状态 |
| LeetCode 141. 环形链表       | 快慢指针 | 简单 | 环检测    |
| LeetCode 142. 环形链表 II    | 快慢指针 | 中等 | 环入口    |
| LeetCode 287. 寻找重复数      | 快慢指针 | 中等 | 隐式链表   |
| LeetCode 42. 接雨水         | 左右指针 | 困难 | 两侧最大值  |

## 八、如何判断题目应该使用哪种双指针？

双指针题型判断路线

题目是否涉及数组、字符串或链表的两个位置？

有序数组、两端比较

左右指针

原地删除、移动、去重

读写双指针

连续子数组、子串

滑动窗口

链表环、重复数

快慢指针

倒数节点、固定间隔

距离双指针

## 九、最重要的三个编程技巧

技巧 1：先定义每个指针的含义。

例如 `fast` 指向待检查元素，`slow` 指向有效区域的下一个写入位置。不要在写循环后才考虑其含义。

技巧 2：定义循环不变量。

每次循环前后，必须保证一个稳定的性质。例如：

- 左右指针：答案仍在候选区间内。
- 原地读写：`[0, slow)` 始终保存有效结果。
- 滑动窗口：`[left, right]` 始终对应当前维护的窗口。

技巧 3：证明为什么移动指针不会错过答案。

这是双指针算法最关键的部分。不能仅凭直觉决定移动左指针还是右指针，必须从有序性、单调性、距离关系或其他题目条件来证明。

最后，双指针和二分查找有一个共同的核心：

二分查找通过排除一半候选来提高效率；双指针通过维护两个位置之间的关系，避免重复搜索。

在学习阶段，建议优先熟练掌握「左右指针、原地读写双指针、滑动窗口」三类，再深入 Floyd 判圈算法。