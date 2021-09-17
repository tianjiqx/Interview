package com.tianjiqx.sort;

public class SortRange {
  public int start;
  public int end;

  public int length() {
    return  end - start;
  }

  public SortRange() {
  }

  public SortRange(int start, int end) {
    this.start = start;
    this.end = end;
  }
}
