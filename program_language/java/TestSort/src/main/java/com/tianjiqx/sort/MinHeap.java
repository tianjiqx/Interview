package com.tianjiqx.sort;

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


  public static void main(String[] args) {

    MinHeap minHeap = new MinHeap(100);
    int[] nums = {10, 1, 2, 3, 5, 6, 3, 7, 8, 10, 19, 4, 1};
    minHeap.HeapSort(nums);
  }


}
