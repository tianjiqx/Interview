
## 二叉树树

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



### 105. 从前序与中序遍历序列构造二叉树

```java

// 给定两个整数数组 preorder 和 inorder ，其中 preorder 是二叉树的先序遍历， inorder 是同一棵树的中序遍历，请构造二叉树并返回其根节点。

// [ 根节点, [左子树的前序遍历结果], [右子树的前序遍历结果] ]
// [ [左子树的中序遍历结果], 根节点, [右子树的中序遍历结果] ]

// 优化: 哈希表 val:pos 

public TreeNode buildTree(int[] preorder, int[] inorder) {
    int n = preorder.length;
    var indexMap = new HashMap<Integer, Integer>();
    for (int i =0; i<n; i++) {
        indexMap.put(inorder[i], i);
    }
    return build(indexMap, preorder, inorder, 0, n-1, 0, n-1);
}
public TreeNode build(Map<Integer, Integer>indexMap, int[] preorder, int[] inorder, int preorderLeft,
    int preorderRight, int inorderLeft, int inorderRight) {
    if (preorderLeft > preorderRight) {
        return null;
    }
    // root
    int preorderRoot = preorderLeft;
    int inorderRoot = indexMap.get(preorder[preorderRoot]);

    TreeNode root = new TreeNode(preorder[preorderRoot]);

    // left
    int leftTreeSize = inorderRoot - inorderLeft;
    TreeNode leftRoot = build(indexMap, preorder, inorder, 
    preorderLeft+1, preorderLeft+leftTreeSize, 
    inorderLeft, inorderLeft + leftTreeSize);
    root.left = leftRoot;

    // right
    TreeNode right = build(indexMap, preorder, inorder,
    preorderLeft + leftTreeSize + 1, preorderRight, 
    inorderRoot+1, inorderRight);
    root.right = right;
    return root;
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

#### 重建二叉树

```java

// 根据先序和中序遍历结果的数组，重建二叉树

class Solution {
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        // 使用一个哈希表来快速查找中序遍历数组中的节点位置
        HashMap<Integer, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            indexMap.put(inorder[i], i);
        }
        
        return buildTreeRecursive(preorder, 0, preorder.length - 1,
                inorder, 0, inorder.length - 1, indexMap));
    }

    private TreeNode buildTreeRecursive(int[] preorder, int preStart, int preEnd,
                                                       int[] inorder, int inStart, int inEnd,
                                                       HashMap<Integer, Integer> indexMap) {
        // 基本情况：如果前序或中序遍历的范围无效（例如，开始索引大于结束索引），则返回 null 表示空树。
        if (preStart > preEnd || inStart > inEnd) {
            return null;
        }

        // 1. 从先序遍历的第一个节点构造根节点
        int rootValue = preorder[preStart];
        TreeNode root = new TreeNode(rootValue);

        // 2. 在中序遍历数组中找到根节点的位置
        //     通过HashMap快速查找，时间复杂度O(1)
        int rootIndexInorder = indexMap.get(rootValue);

        // 3. 计算左子树的节点数量：根索引在中序序列中的位置 - 中序起始位置
        int leftSubTreeSize = rootIndexInorder - inStart;

        // 4. 递归构建左子树和右子树
        //     左子树的先序范围是：preStart+1 到 preStart+leftSubTreeSize+1
        //     右子树的先序范围是：preStart+leftSubTreeSize+1 到 preEnd
        root.left = buildTreeRecursive(preorder, preStart + 1,
                preStart + leftSubTreeSize, inorder, inStart,
                rootIndexInorder - 1, indexMap));
        
        // 右子树的范围计算
        int rightStartPre = preStart + leftSubTreeSize + 1;
        root.right = buildTreeRecursive(preorder, rightStartPre,
                preEnd, inorder, rootIndexInorder + 1, inEnd,
                indexMap));

        return root;
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


### leetcode 99. 恢复二叉搜索树

```java

/* 
给你二叉搜索树的根节点 root ，该树中的 恰好 两个节点的值被错误地交换。请在不改变其结构的情况下，恢复这棵树 。
*/

class Solution {
    /*

        设原本递增序列中的两个数是 a < b，交换后：
        原来：... a, 中间的数, b ...
        现在：... b, 中间的数, a ...

        不相邻：
        a < 中间的数 < b
        ... b, 第一个中间数, ..., 最后一个中间数, a ...
        └──────────┘        └───────────────┘
        一定下降               一定下降


        其他相邻位置仍保持递增。因此：
        - 第一次下降的左边，一定是错误的大值 b。
        - 最后一次下降的右边，一定是错误的小值 a。

        如果它们相邻，则只有：
        ... b, a ...
    */

    private TreeNode prev;
    private TreeNode first;
    private TreeNode second;

    public void recoverTree(TreeNode root) {
        prev = null;
        first = null;
        second = null;

        inorder(root);

        if (first != null && second != null) {
            int temp = first.val;
            first.val = second.val;
            second.val = temp;
        }
    }



    private void inorder(TreeNode node) {
        if (node == null) {
            return;
        }

        inorder(node.left);

        // 按中序顺序访问当前节点
        if (prev != null && prev.val > node.val) {
            if (first == null) {
                first = prev;
            }
            second = node;
        }
        prev = node;

        inorder(node.right);
    }
}
```

### leetcode 235. 二叉搜索树的最近公共祖先

给定一个二叉搜索树, 找到该树中两个指定节点的最近公共祖先。
百度百科中最近公共祖先的定义为：“对于有根树 T 的两个结点 p、q，最近公共祖先表示为一个结点 x，满足 x 是 p、q 的祖先且 x 的深度尽可能大（一个节点也可以是它自己的祖先）。”

```java
/*
对于当前节点 root：
1. 如果 p.val 和 q.val 都小于 root.val，说明两个节点都在左子树，继续向左查找。
2. 如果都大于 root.val，说明两个节点都在右子树，继续向右查找。
3. 否则，当前节点就是最近公共祖先，因为两条路径在这里分叉，或者当前节点本身就是 p 或 q。
*/

public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    while (root != null) {
        if (p.val < root.val && q.val < root.val) {
            // p 和 q 都在左子树
            root = root.left;
        } else if (p.val > root.val && q.val > root.val) {
            // p 和 q 都在右子树
            root = root.right;
        } else {
            // 当前节点是最近公共祖先
            return root;
        }
    }
    return null;
}
```

### leeeeetcode 236. 二叉树的最近公共祖先


给定一个二叉树, 找到该树中两个指定节点的最近公共祖先。

方法:
- 递归后序遍历：左右子树分别搜索，通过递归返回值找到 LCA。（单次查询）
- DFS + HashMap 记录父节点 + HashSet：先建立父节点索引，再沿祖先链查找交点。 （多次查询）


```
public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    // 递归终止条件
    if (root == null || root == p || root == q) {
        return root;
    }

    // 分别在左右子树中寻找 p 和 q
    TreeNode left = lowestCommonAncestor(root.left, p, q);
    TreeNode right = lowestCommonAncestor(root.right, p, q);

    // 左右子树都找到，当前节点就是最近公共祖先
    if (left != null && right != null) {
        return root;
    }

    // 只有一侧找到，向上返回
    return left != null ? left : right;
}


public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    if (root == null || root == p || root == q) {
        return root;
    }

    // 子节点 -> 父节点
    Map<TreeNode, TreeNode> parent = new HashMap<>();
    parent.put(root, null);

    // 迭代 DFS
    Deque<TreeNode> stack = new ArrayDeque<>();
    stack.push(root);

    // 只需要找到 p 和 q，无须遍历整棵树
    while (!parent.containsKey(p) || !parent.containsKey(q)) {
        TreeNode node = stack.pop();

        if (node.left != null) {
            parent.put(node.left, node);
            stack.push(node.left);
        }

        if (node.right != null) {
            parent.put(node.right, node);
            stack.push(node.right);
        }
    }

    // 记录 p 的所有祖先，包括 p 本身
    Set<TreeNode> visited = new HashSet<>();

    while (p != null) {
        visited.add(p);
        p = parent.get(p);
    }

    // 从 q 开始向上寻找第一个公共祖先
    while (q != null) {
        if (visited.contains(q)) {
            return q;
        }
        q = parent.get(q);
    }

    return null;
}


```


### leetcode 114. 二叉树展开为链表

给你二叉树的根结点 root ，请你将它展开为一个单链表：
展开后的单链表应该同样使用 TreeNode ，其中 right 子指针指向链表中下一个结点，而左子指针始终为 null 。
展开后的单链表应该与二叉树 先序遍历 顺序相同。

```java                             
/*
解法一：递归 + 前驱节点
使用反向先序遍历：右 → 左 → 根。
维护一个 prev，表示已经处理好的链表头节点。

时间复杂度：O(n)
空间复杂度：O(h)，递归调用栈，最坏 O(n)
*/


class Solution {
    private TreeNode prev = null;

    public void flatten(TreeNode root) {
        if (root == null) {
            return;
        }

        // 反向先序遍历
        flatten(root.right);
        flatten(root.left);

        // 当前节点连接已处理好的链表
        root.right = prev;
        root.left = null;

        // 更新链表头
        prev = root;
    }
}

/*
解法三：原地迭代，O(1) 空间
核心思想：把当前节点的右子树接到左子树最右侧的节点后，再将左子树整体移动到右侧。

*/
public void flatten(TreeNode root) {
    TreeNode curr = root;

    while (curr != null) {
        if (curr.left != null) {

            // 找到左子树最右侧节点
            TreeNode predecessor = curr.left;
            while (predecessor.right != null) {
                predecessor = predecessor.right;
            }

            // 原右子树挂到左子树的最右侧
            predecessor.right = curr.right;

            // 左子树移到右侧
            curr.right = curr.left;
            curr.left = null;
        }

        // 继续处理链表下一个节点
        curr = curr.right;
    }
}

```

