package com.tianjiqx.sort;

import com.tianjiqx.utils.PrintRunTime;
import com.tianjiqx.utils.ThreadPoolTool;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
multi-thread merge sort
*/
public class ParalleMergeSort {

  private static Logger LOG = LoggerFactory.getLogger(ParalleMergeSort.class);

  public final ConcurrentHashMap<Integer, SortRange> CompleteSortRangeStartMap = new ConcurrentHashMap<>();
  public final LinkedBlockingQueue<MergeRange> mergeTaskQueue = new LinkedBlockingQueue<>();

  class MergeRange {
    public SortRange left;
    public SortRange right;

    public MergeRange(SortRange left, SortRange right) {
      this.left = left;
      this.right = right;
    }
  }


  class SortWorker implements Runnable {

    private AtomicInteger completeNum;
    private int totol_task_num;

    private int[] nums;
    private int[] tempNums;

    private SortRange sortRange;
    private ConcurrentHashMap<Integer, SortRange> completeSortRangeStartMap;
    private LinkedBlockingQueue<MergeRange> mergeTaskQueue;

    @Override
    public void run() {
      Sort.MergeSort(nums, tempNums, 1, sortRange);
      synchronized (completeSortRangeStartMap){
        SortRange range = completeSortRangeStartMap.remove(sortRange.end);
        if (range != null) {
          mergeTaskQueue.add(new MergeRange(sortRange, range));
        } else {
         Object[] preRanges =
             completeSortRangeStartMap.entrySet()
             .stream().filter(e -> e.getValue().end == sortRange.start)
             .map(e -> e.getValue()).toArray();
          if (preRanges.length > 0) {
            SortRange preRange = (SortRange) preRanges[0];
            completeSortRangeStartMap.remove(preRange);
            mergeTaskQueue.add(new MergeRange(preRange, sortRange));
          } else {
            completeSortRangeStartMap.put(sortRange.start, sortRange);
          }
        }
      }

      //completeSortRangeStartMap.put(Integer.valueOf(sortRange.start), sortRange);
      completeNum.incrementAndGet();
      LOG.info("Sort sub array complete " + completeNum.get() * 100 / totol_task_num + " %.");
      System.out.println("sort sub array [" + sortRange.start + "," + sortRange.end + "].");
      System.out
          .println("Sort sub array complete " + completeNum.get() * 100 / totol_task_num + " %.");
    }

    public SortWorker(AtomicInteger completeNum, int totol_task_num, int[] nums, int[] tempNums,
        SortRange sortRange, ConcurrentHashMap<Integer, SortRange> CompleteSortRangeStartMap,
        LinkedBlockingQueue<MergeRange> mergeTaskQueue) {
      this.completeNum = completeNum;
      this.totol_task_num = totol_task_num;
      this.nums = nums;
      this.tempNums = tempNums;
      this.sortRange = sortRange;
      this.completeSortRangeStartMap = CompleteSortRangeStartMap;
      this.mergeTaskQueue = mergeTaskQueue;
    }
  }

  /*
  merge adjacent sorted array
   */
  class MergeWorker implements Runnable {

    private AtomicInteger completeNum;
    private int totol_task_num;

    private int[] nums;
    private int[] tempNums;

    private int start;
    private int mid;
    private int end;

    private ConcurrentHashMap<Integer, SortRange> completeSortRangeStartMap;
    private LinkedBlockingQueue<MergeRange> mergeTaskQueue;

    @Override
    public void run() {
      Sort.merge(nums, tempNums, start, mid-1, end-1);
      int i = start;
      // ugly, todo refine
      while (i < end) {
        nums[i] = tempNums[i];
        i++;
      }
      synchronized (completeSortRangeStartMap) {
        SortRange range = completeSortRangeStartMap.remove(end);
        if (range != null) {
          mergeTaskQueue.add(new MergeRange(new SortRange(start, end), range));
        } else {
          Object[] preRanges =
              completeSortRangeStartMap.entrySet()
                  .stream().filter(e -> e.getValue().end == start)
                  .map(e -> e.getValue()).toArray();
          if (preRanges.length > 0) {
            SortRange preRange = (SortRange) preRanges[0];
            completeSortRangeStartMap.remove(preRange);
            mergeTaskQueue.add(new MergeRange(preRange, new SortRange(start, end)));
          } else {
            completeSortRangeStartMap.put(start, new SortRange(start, end));
          }
        }
      }
      //completeSortRangeStartMap.put(start,new SortRange(start, end));
      completeNum.incrementAndGet();
      LOG.info("Merge sub array complete " + completeNum.get() * 100 / totol_task_num + " %.");
      System.out.println("Merge sub array [" + start + "," + end + "].");
      System.out
          .println("Merge sub array complete " + completeNum.get() * 100 / totol_task_num + " %.");
    }

    public MergeWorker(AtomicInteger completeNum, int totol_task_num, int[] nums, int[] tempNums,
        int start, int mid, int end, ConcurrentHashMap<Integer, SortRange> CompleteSortRangeStartMap,
        LinkedBlockingQueue<MergeRange> mergeTaskQueue) {
      this.completeNum = completeNum;
      this.totol_task_num = totol_task_num;
      this.nums = nums;
      this.tempNums = tempNums;
      this.start = start;
      this.mid = mid;
      this.end = end;
      this.completeSortRangeStartMap = CompleteSortRangeStartMap;
      this.mergeTaskQueue = mergeTaskQueue;
    }
  }

  public SortWorker[] genDefaultParalleSortWorker1(int[] nums, int[] tempNums) {
    int batchsize = ThreadPoolTool.BATCH_SIZE;
    int taskNums = Math
        .min((int) Math.ceil(nums.length * 1.0 / batchsize), ThreadPoolTool.CPU_NUMS);
    SortWorker[] workers = new SortWorker[taskNums];

    AtomicInteger completeNum = new AtomicInteger();

    batchsize = (int) Math.ceil(nums.length * 1.0 / taskNums);
    int start = 0;
    for (int i = 0; i < taskNums - 1; i++) {
      workers[i] = new SortWorker(completeNum, taskNums, nums, tempNums,
          new SortRange(start, start + batchsize), this.CompleteSortRangeStartMap, this.mergeTaskQueue);
      start += batchsize;
    }
    workers[taskNums - 1] = new SortWorker(completeNum, taskNums, nums, tempNums,
        new SortRange(start, nums.length), this.CompleteSortRangeStartMap, this.mergeTaskQueue);
    return workers;
  }

  public SortWorker[] genDefaultParalleSortWorker2(int[] nums, int[] tempNums) {
    int batchsize = ThreadPoolTool.BATCH_SIZE;
    int taskNums = 10;
    SortWorker[] workers = new SortWorker[taskNums];

    AtomicInteger completeNum = new AtomicInteger();

    batchsize = (int) Math.ceil(nums.length * 1.0 / taskNums);

    int start = 0;
    for (int i = 0; i < taskNums - 1; i++) {
      workers[i] = new SortWorker(completeNum, taskNums, nums, tempNums,
          new SortRange(start, start + batchsize),
          this.CompleteSortRangeStartMap, this.mergeTaskQueue);
      start += batchsize;
    }
    workers[taskNums - 1] = new SortWorker(completeNum, taskNums, nums, tempNums,
        new SortRange(start, nums.length),
        this.CompleteSortRangeStartMap, this.mergeTaskQueue);
    return workers;
  }


  public MergeWorker genMergeWorker(AtomicInteger completeNum, int totol_task_num, int[] nums, int[] tempNums,
      int start, int mid, int end) {
    System.out.println("genMergeWorker:["+ start+","+end+"]");
    return new MergeWorker(completeNum, totol_task_num,
        nums, tempNums, start, mid, end, this.CompleteSortRangeStartMap, this.mergeTaskQueue);
  }


  public static void main(String[] args) throws Exception {

    int[] data;
    data = Reader.read(ThreadPoolTool.DEFAULT_FILE_NAME, 0, 100 * ThreadPoolTool.BATCH_SIZE);

    /*
    new PrintRunTime() {
      @Override
      public void run() throws Exception {
        Sort.MergeSort(data);
      }
    }.execute("Sort data single thread");
    */
    /*
    1thread, 400MB(100M), 16.4s,  6M/s
    Sort data single thread spend time total 16404 ms
    1 thread, 200MB(50M), 7.8s, 6.4M/s
    Sort data single thread spend time total 7867 ms
    Sort data single thread spend time total 8148 ms
     */



    ParalleMergeSort paralleMergeSort = new ParalleMergeSort();

    ThreadPoolTool threadPoolTool = new ThreadPoolTool();

    new PrintRunTime() {
      @Override
      public void run() throws Exception {
        int[] tempNums = new int[data.length];

        SortWorker[] workers = paralleMergeSort.genDefaultParalleSortWorker1(data, tempNums);
        //SortWorker[] workers = paralleMergeSort.genDefaultParalleSortWorker2(data, tempNums);

        for (int i = 0;i < workers.length; i++) {
          threadPoolTool.threadPoolExecutor.execute(workers[i]);
        }

        int totalMergeCount = workers.length - 1;
        AtomicInteger completeNum = new AtomicInteger();
        int i = 0;
        //while(i < totalMergeCount) {  //completeNum.get() < totalMergeCount
        while(completeNum.get() < totalMergeCount) {

          // 1. task queue
          i++;
          //MergeRange mergeRange = paralleMergeSort.mergeTaskQueue.take(); //poll(100, TimeUnit.MICROSECONDS);
          // still need use poll, take will block main thread
          MergeRange mergeRange = paralleMergeSort.mergeTaskQueue.poll(0, TimeUnit.MICROSECONDS);
          if (mergeRange != null) {
            MergeWorker mergeWorker = paralleMergeSort.genMergeWorker(completeNum, totalMergeCount,
                data, tempNums, mergeRange.left.start,mergeRange.left.end, mergeRange.right.end);
            threadPoolTool.threadPoolExecutor.execute(mergeWorker);
          }
          // free thread
          Thread.sleep(10); //sleep 10 ms
          /*
          // 2. polling way
          synchronized (paralleMergeSort.CompleteSortRangeStartMap) {
            //System.out.println("size " + paralleMergeSort.CompleteSortRangeStartMap.size()) ;
            if (paralleMergeSort.CompleteSortRangeStartMap.size() >= 2) {
              int[] starts =  new int[paralleMergeSort.CompleteSortRangeStartMap.size()];
              List<Integer> todoMergeList = new LinkedList<Integer>();
              int i = 0;
              for(Map.Entry<Integer, SortRange> entry:  paralleMergeSort.CompleteSortRangeStartMap.entrySet()) {
                starts[i++] = entry.getKey();
              }
              Arrays.sort(starts);
              for (int j=0; j < starts.length -1; j++) {
                SortRange sortRange = paralleMergeSort.CompleteSortRangeStartMap.get(starts[j]);
                if (sortRange.end == starts[j+1]) {
                  SortRange nextRange = paralleMergeSort.CompleteSortRangeStartMap.get(starts[j+1]);
                  paralleMergeSort.CompleteSortRangeStartMap.remove(starts[j]);
                  paralleMergeSort.CompleteSortRangeStartMap.remove(starts[j+1]);
                  MergeWorker mergeWorker = paralleMergeSort.genMergeWorker(completeNum, totalMergeCount,
                      data, tempNums, starts[j],sortRange.end, nextRange.end);
                  threadPoolTool.threadPoolExecutor.execute(mergeWorker);
                  j++;
                }
              }

            }
          }

          Thread.sleep(10); //sleep 10 ms
           */
        }

        // wait lastest merge task end
        while (threadPoolTool.threadPoolExecutor.getActiveCount() > 0) {
          //System.out.println("sleep end...");
          Thread.sleep(10); //sleep 10 ms
        }

      }
    }.execute("Sort data multi thread");

    threadPoolTool.threadPoolExecutor.shutdown();


    /*
    200MB, 50M, 5.5s  9M/s
    sort sub array [0,26214400].
    Sort sub array complete 50 %.
    sort sub array [26214400,52428800].
    Sort sub array complete 100 %.
    genMergeWorker:[0,52428799]
    Merge sub array [0,52428799].
    Merge sub array complete 100 %.
    Sort data multi thread spend time total 5539 ms

    Process finished with exit code 0

      400MB, 100M, 12.26s, 7.9M/s
      sort sub array [0,52428800].
      Sort sub array complete 50 %.
      sort sub array [52428800,104857600].
      Sort sub array complete 100 %.
      genMergeWorker:[0,104857599]
      Merge sub array [0,104857599].
      Merge sub array complete 100 %.
      Sort data multi thread spend time total 12260 ms

      Sort data multi thread spend time total 11491 ms
      Sort data multi thread spend time total 10616 ms

      2CPU,10thread,400MB, 100M, 23.477s
      Sort data multi thread spend time total 23477 ms

      // task queue version:



      2CPU,10thread,400MB, 100M, 15.67s, 7.9M/s
      Sort data multi thread spend time total 15673 ms
     */
  }

}
