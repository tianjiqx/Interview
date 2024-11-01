
## Type

```
Integer  
Long
Double


数组 .length 
集合 .size()

```

## Stack & Queue

```java
// 用list 代替栈
Stack<Long> stack = new LinkedList<Long>();
Stack<Long> stack = new Stack<Long>();
Deque<String> stack = new ArrayDeque<>();

// stack
- push()
- peek()
- pop()
- isEmpty()
- size()

// queue
 Queue<String> queue = new LinkedList<>();
// 基于优先级堆的优先级队列
Queue<String> queue = new PriorityQueue<>();

- add, offer()

- element() 返回队列头部的元素, 不存在抛出异常
- peek() 返回队列头部的元素


- remove() 移除并返回队列头部的元素。如果队列为空，则抛出
- poll() 移除并返回队列头部的元素。如果队列为空，则返回 null


// deque

Deque<Integer> levelList = new LinkedList<Integer>();

- poll()
- offerLast()
- offerFirst()


```


## List
```java

List<Integer> list = new ArrayList<>();

- addAll()
- add(), add(int index, E element)

// 访问
- get(int index)
- indexOf(Object o)

- subList(int fromIndex, int toIndex) [fromIndex, toIndex)

// 查询
- contains(Object o) 
- containsAll(Collection<?> c)

```


## Tree
```
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */


// 中序遍历
// 可以用来获取二叉搜索树中 Kth 大小
// 或者记录节点孩子节点数量
public List<Integer> inorderTraversal(TreeNode root) {
    List<Integer> result = new LinkedList();

    if (root == null) {
        return result;
    }
    if (root.left != null) {
        result.addAll(inorderTraversal(root.left)); 
    }
    result.add(root.val);
    if (root.right != null) {
        result.addAll(inorderTraversal(root.right));
    }
    return result;
}

// 非递归中序遍历
public void visitAllLeft(TreeNode node, Stack<TreeNode> stack) {
    while(node != null) {
        stack.add(node);
        node = node.left;
    }
}

public List<Integer> inorderTraversal(TreeNode root) {
    List<Integer> result = new LinkedList<>();
    if (root == null) return result;
    Stack<TreeNode> stack = new LinkedList<>();
    while(true) {
        visitAll(root, stack);
        if (stack.isEmpty()) {
            return result;
        }
        TreeNode mid = stack.peek();
        result.add(mid.val);
        root = mid.right;
        stack.pop();
    }
}

// 层序遍历：queue 存储 当前访问层队列，2层循环，记录当前层队列大小，控制外循环出队次数

// 二叉树的最近公共祖先
// 用哈希表存储所有节点的父节点，Set集合存储p访问过节点，q如果碰到已经访问过的节点，那么这个节点就是我们要找的最近公共祖先

Map<Integer, TreeNode> parent = new HashMap<Integer, TreeNode>();
Set<Integer> visited = new HashSet<Integer>();


public void dfs(TreeNode root) {
    if (root.left != null) {
        parent.put(root.left.val, root);
        dfs(root.left);
    }
    if (root.right != null) {
        parent.put(root.right.val, root);
        dfs(root.right);
    }
}

public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    dfs(root);
    while (p != null) {
        visited.add(p.val);
        p = parent.get(p.val);
    }
    while (q != null) {
        if (visited.contains(q.val)) {
            return q;
        }
        q = parent.get(q.val);
    }
    return null;
}

// trie 

class Trie {
    private Trie[] children;
    private boolean isEnd;

    public Trie() {
        children = new Trie[26];
        isEnd = false;
    }
    
    public void insert(String word) {
        Trie node = this;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            int index = ch - 'a';
            if (node.children[index] == null) {
                node.children[index] = new Trie();
            }
            node = node.children[index];
        }
        node.isEnd = true;
    }
    
    public boolean search(String word) {
        Trie node = searchPrefix(word);
        return node != null && node.isEnd;
    }
    
    public boolean startsWith(String prefix) {
        return searchPrefix(prefix) != null;
    }

    private Trie searchPrefix(String prefix) {
        Trie node = this;
        for (int i = 0; i < prefix.length(); i++) {
            char ch = prefix.charAt(i);
            int index = ch - 'a';
            if (node.children[index] == null) {
                return null;
            }
            node = node.children[index];
        }
        return node;
    }
}


```

## Search & sort

```java

public int search(int key, int[] array) {
    while(l <= h>) { // <=
        int mid = l + (h-l)/2;
        if (key == array[mid]) return mid;
        if (key < array[mid]) h = mid - 1;
        else l = mid + 1;
    }
    return -1;
}

// find Kth value
// 快速排序 选择k， 堆排序 
public int findKthLargest(int[] nums, int k) {
        Arrays.sort(nums);
        return nums[nums.length - k];
}

private int quickSelect(List<Integer> nums, int k) {
    // 随机选择基准数
    Random rand = new Random();
    int pivot = nums.get(rand.nextInt(nums.size()));
    // 将大于、小于、等于 pivot 的元素划分至 big, small, equal 中
    List<Integer> big = new ArrayList<>();
    List<Integer> equal = new ArrayList<>();
    List<Integer> small = new ArrayList<>();
    for (int num : nums) {
        if (num > pivot)
            big.add(num);
        else if (num < pivot)
            small.add(num);
        else
            equal.add(num);
    }
    // 第 k 大元素在 big 中，递归划分
    if (k <= big.size())
        return quickSelect(big, k);
    // 第 k 大元素在 small 中，递归划分
    if (nums.size() - small.size() < k)
        return quickSelect(small, k - nums.size() + small.size());
    // 第 k 大元素在 equal 中，直接返回 pivot
    return pivot;
}

public int findKthLargest(int[] nums, int k) {
    List<Integer> numList = new ArrayList<>();
    for (int num : nums) {
        numList.add(num);
    }
    return quickSelect(numList, k);
}


```





