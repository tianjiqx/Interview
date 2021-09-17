package com.tianjiqx.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTool {


  public final static int  BATCH_SIZE = 1 << 20; // 1M
  public final static int MAX_THREAD_POOL_SIZE = 1024;

  public final static String DEFAULT_FILE_NAME = "default_data.txt";
  final static int DEFAULT_NUM = 1 << 10; // 1K

  public final static int CPU_NUMS = Runtime.getRuntime().availableProcessors();

  public ThreadPoolExecutor threadPoolExecutor;

  public ThreadPoolTool() {
    this.threadPoolExecutor = new ThreadPoolExecutor(CPU_NUMS,
        2 * CPU_NUMS,
        0l,
        TimeUnit.MICROSECONDS,
        new ArrayBlockingQueue<>(MAX_THREAD_POOL_SIZE),
        Executors.defaultThreadFactory(),
        // run task at main thread
        new ThreadPoolExecutor.CallerRunsPolicy());
  }
}
