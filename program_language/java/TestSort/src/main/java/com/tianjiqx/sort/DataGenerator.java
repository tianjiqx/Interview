package com.tianjiqx.sort;

import com.tianjiqx.utils.PrintRunTime;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generate test data file for sort
 */
public class DataGenerator {

  private static Logger LOG = LoggerFactory.getLogger(DataGenerator.class);


  private final static int  BATCH_SIZE = 1 << 20; // 1M
  private final static int MAX_THREAD_POOL_SIZE = 1024;

  public final static String DEFAULT_FILE_NAME = "default_data.txt";
  final static int DEFAULT_NUM = 1 << 10; // 1K

  private final static int CPU_NUMS = Runtime.getRuntime().availableProcessors();

  ThreadPoolExecutor threadPoolExecutor;


  class GenAndWriter implements Runnable {

    private AtomicInteger completeNum;
    private int totol_task_num;
    private String file_name;
    private int size; // number of write
    private long offset; // write offset

    @Override
    public void run() {
      try {
        generateData(this.file_name, this.size, this.offset);
      } catch (IOException e) {
        e.printStackTrace();
        LOG.error("generateData failed, due to "+ e.getMessage());
      }
    }

    public GenAndWriter(AtomicInteger completeNum,
        int totol_task_num, String file_name, int size,
        long offset) {
      this.completeNum = completeNum;
      this.totol_task_num = totol_task_num;
      this.file_name = file_name;
      this.size = size;
      this.offset = offset;
    }

    @Override
    public String toString() {
      return "GenAndWriter{" +
          "completeNum=" + completeNum +
          ", totol_task_num=" + totol_task_num +
          ", file_name='" + file_name + '\'' +
          ", size=" + size +
          ", offset=" + offset +
          '}';
    }

    public void generateData(String file_name, int num, long offset) throws IOException {
      File file = new File(file_name);
      FileChannel outputChannel = FileChannel.open(
          Paths.get(file.toURI()), StandardOpenOption.WRITE);

      System.out.println("this:"+ this.toString());

      int loop = num / BATCH_SIZE;
      Random random = new Random(System.currentTimeMillis());

      int [] batchData = new int[Math.min(num, BATCH_SIZE)];
      ByteBuffer byteBuffer = ByteBuffer.allocate(batchData.length * 4);
      for (int i =0; i < loop; i++) {
        for (int j = 0; j < num && j < BATCH_SIZE; j++) {
          int v = random.nextInt();
          byteBuffer.put( (byte) (v & 0xFF) );
          byteBuffer.put( (byte) (v >> 8 & 0xFF) );
          byteBuffer.put( (byte) (v >> 8 & 0xFF) );
          byteBuffer.put( (byte) (v >> 8 & 0xFF) );
          //byteBuffer.put( (byte) '\t' );
        }
        byteBuffer.flip();
        int bytes = outputChannel.write(byteBuffer, offset);
        offset += bytes;
        byteBuffer.clear();
      }
      outputChannel.close();

      completeNum.incrementAndGet();
      LOG.info("generateData complete "+ completeNum.get()*100/totol_task_num + " %.");
      System.out.println("generateData complete "+ completeNum.get()*100/totol_task_num + " %.");

    }

  }

  private void init() {
    threadPoolExecutor = new ThreadPoolExecutor(CPU_NUMS,
        2 * CPU_NUMS,
        0l,
        TimeUnit.MICROSECONDS,
        new ArrayBlockingQueue<>(MAX_THREAD_POOL_SIZE),
        Executors.defaultThreadFactory(),
        // run task at main thread
        new ThreadPoolExecutor.CallerRunsPolicy());
  }

  private void clear() {
    threadPoolExecutor.shutdown();
  }


  private GenAndWriter[] genDefaultData() {
    return genData(DEFAULT_FILE_NAME, DEFAULT_NUM);
  }

  private GenAndWriter[] genDefaultData2() {
    return genData(DEFAULT_FILE_NAME, 2 * BATCH_SIZE);
  }

  private GenAndWriter[] genDefaultData3() {
    return genData(DEFAULT_FILE_NAME, 100 * BATCH_SIZE);
  }

  private GenAndWriter[] genData(String path, int num) {
    File file = new File(path);
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    AtomicInteger completeNum = new AtomicInteger();
    if (num < BATCH_SIZE) {
      return new GenAndWriter[]{
          new GenAndWriter(completeNum,1,path,num,0)
      };
    } else {
      int threadNum = Math.min((int) Math.ceil( num * 1.0/BATCH_SIZE), CPU_NUMS);
      GenAndWriter[] genAndWriters = new GenAndWriter[threadNum];
      int bathSize = num / threadNum;
      for (int i = 0; i <  threadNum -1; i++) {
        genAndWriters[i] = new GenAndWriter(completeNum,threadNum,path,bathSize,i*bathSize*4);
      }
      genAndWriters[threadNum -1] = new GenAndWriter(completeNum, threadNum, path, num - (threadNum -1) * bathSize,(threadNum -1)*bathSize*4);
      return genAndWriters;
    }
  }


  public static void main(String [] args) throws Exception {

    //System.out.println(BATCH_SIZE);
    DataGenerator generator = new DataGenerator();
    generator.init();

    new PrintRunTime() {
      @Override
      public void run() throws Exception {
        GenAndWriter[] writers = generator.genDefaultData3();
        for (int i = 0;i < writers.length; i++) {
          generator.threadPoolExecutor.execute(writers[i]);
        }

        while(generator.threadPoolExecutor.getActiveCount() >0) {
          Thread.sleep(10); //sleep 10 ms
        }
      }
    }.execute("Generate dafualt data");

    /*
    2CPU, 400MB, 5.24s, 76.34MB/s
    this:GenAndWriter{completeNum=0, totol_task_num=2, file_name='default_data.txt', size=52428800, offset=0}
    this:GenAndWriter{completeNum=0, totol_task_num=2, file_name='default_data.txt', size=52428800, offset=209715200}
    Generate dafualt data spend time total 5248 ms
    */

    generator.clear();
  }

}
