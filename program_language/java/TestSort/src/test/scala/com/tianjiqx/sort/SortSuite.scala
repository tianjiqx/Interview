package com.tianjiqx.sort

import org.junit.Assert.{assertArrayEquals, assertEquals}
import org.scalatest.funsuite.AnyFunSuite

import java.util
import java.util.Arrays

class SortSuite extends AnyFunSuite {


  test("baisc test1") {
    val nums: Array[Int] = Array(10, 1, 2, 3, 5, 6, 3, 7, 8, 10, 19, 4)
    Sort.MergeSort(nums)

    val std = Array(1, 2, 3, 3, 4, 5, 6, 7, 8, 10, 10, 19);
    System.out.println("sorted: " + util.Arrays.toString(nums))
    assertArrayEquals(std, nums);
  }

  test("baisc test2") {
    val nums: Array[Int] = Array(10, 1, 2, 3, 5, 6, 3, 7, 8, 10, 19, 4, 1)
    Sort.MergeSort(nums)

    val std = Array(1, 1, 2, 3, 3, 4, 5, 6, 7, 8, 10, 10, 19);
    System.out.println("sorted: " + util.Arrays.toString(nums))
    assertArrayEquals(std, nums);
  }

  test("baisc test3") {
    val nums: Array[Int] = Array(1)
    Sort.MergeSort(nums)

    val std = Array(1);
    System.out.println("sorted: " + util.Arrays.toString(nums))
    assertArrayEquals(std, nums);
  }
  test("baisc test4") {
    val nums: Array[Int] = Array(2,1)
    Sort.MergeSort(nums)

    val std = Array(1,2);
    System.out.println("sorted: " + util.Arrays.toString(nums))
    assertArrayEquals(std, nums);
  }

}
