


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

// 优先队列
PriorityQueue<Integer> minHeap = new PriorityQueue<>();

minHeap.offer(5);
minHeap.offer(2);
minHeap.offer(8);
minHeap.offer(1);

System.out.println(minHeap.peek()); // 1
System.out.println(minHeap.poll()); // 1
System.out.println(minHeap.peek()); // 2


PriorityQueue<Integer> maxHeap =
    new PriorityQueue<>(Comparator.reverseOrder());

maxHeap.offer(5);
maxHeap.offer(2);
maxHeap.offer(8);
maxHeap.offer(1);

System.out.println(maxHeap.peek()); // 8
System.out.println(maxHeap.poll()); // 8
System.out.println(maxHeap.peek()); // 5


```


PriorityQueue 常用方法
方法	作用	时间复杂度
offer(x)	插入元素	\(O(\log n)\)
poll()	删除并返回堆顶	\(O(\log n)\)
peek()	查看堆顶	\(O(1)\)
size()	返回元素数量	\(O(1)\)
isEmpty()	是否为空	\(O(1)\)
remove(x)	删除指定元素	\(O(n)\)


LeetCode 高频堆题
题号	题目	推荐结构
215	数组中的第 K 个最大元素	大小为 K 的小顶堆
347	前 K 个高频元素	按频率排序的小顶堆
23	合并 K 个升序链表	小顶堆
295	数据流的中位数	大顶堆 + 小顶堆
703	数据流中的第 K 大元素	大小为 K 的小顶堆
973	最接近原点的 K 个点	大小为 K 的大顶堆


```java

// leetecode 215 数组中的第 K 个最大元素
// 时间复杂度：\(O(n\log k)\)，空间复杂度：\(O(k)\)。
public int findKthLargest(int[] nums, int k) {
    // 维持一个只有 K 个元素的小顶堆，堆顶就是第 K 大的元素。
    PriorityQueue<Integer> heap = new PriorityQueue<>();

    for (int num : nums) {
        heap.offer(num);

        if (heap.size() > k) {
            heap.poll();
        }
    }

    return heap.peek();
}

// leetecode 347 前 K 个高频元素
// 给定一个无差别的整数数组 nums 和一个整数 k，返回频率前 k 高的元素及其对应的频率，结果按频率降序排列。
import java.util.*;

public List<int[]> getTopKFrequent(int[] nums, int k) {
    // 1. 使用哈希表统计每个元素的出现频率（与之前思路一致，保证频率准确）
    Map<Integer, Integer> frequencyMap = new HashMap<>();
    for (int num : nums) {
        frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
    }
    
    // 2. 定义最大堆的比较规则：
    //    优先保证频率更高（频率降序），频率相同时元素值更小（元素值升序）
    //    堆顶是「前K个中最弱的元素」（频率最低，频率相同时元素值最大）
    PriorityQueue<int[]> maxHeap = new PriorityQueue<>(
        (a, b) -> {
            // 先比较频率：b[1]（堆顶频率） - a[1]（当前元素频率），频率高的优先级更高（最大堆）
            int freqCompare = Integer.compare(b[1], a[1]);
            if (freqCompare != 0) {
                return freqCompare;
            }
            // 频率相同时，元素值更大的优先级更高（因为堆顶是「最弱的」，频率相同时元素值大的更弱）
            return Integer.compare(b[0], a[0]);
        }
    );
    
    // 3. 将频率统计结果转为int[]数组（[元素值, 频率]），逐个加入堆
    for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
        maxHeap.offer(new int[]{entry.getKey(), entry.getValue()});
        //4. 堆的初始大小超过k时，不断弹出堆顶（移除最弱的元素），直到堆大小为k
        if (maxHeap.size() > k) {
            maxHeap.poll();
        }
    }
    
    // 5. 堆中剩余元素即为前K个高频元素，直接返回
    return new ArrayList<>(maxHeap);
}


```



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


