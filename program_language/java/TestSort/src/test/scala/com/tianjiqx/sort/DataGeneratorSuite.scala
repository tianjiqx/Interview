package com.tianjiqx.sort

import org.scalatest.funsuite.AnyFunSuite

// use AnyFunSuite replace FunSuite after 3.2.0 version
class DataGeneratorSuite extends AnyFunSuite {

  test("test 1") {
    System.out.println("ddd");
  }

  test("Reader test") {
    val DEFAULT_FILE_NAME = "default_data.txt"

    val values = Reader.read(DEFAULT_FILE_NAME, 0, 10);

    System.out.println(values.mkString(","));

  }


}
