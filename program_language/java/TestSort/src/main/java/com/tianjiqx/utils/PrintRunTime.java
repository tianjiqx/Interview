package com.tianjiqx.utils;

public abstract class PrintRunTime {

  public abstract void run() throws Exception;

  public void execute(String name) throws Exception{
    long startTime = System.currentTimeMillis();
    run();
    long endTime = System.currentTimeMillis();
    long spendTime = endTime - startTime;
    System.out.println(name + " spend time total " + spendTime + " ms");
  }

}
