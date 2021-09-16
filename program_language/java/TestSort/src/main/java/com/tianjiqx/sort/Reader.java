package com.tianjiqx.sort;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Reader {

  public static int[] read(String file_name, long offset, int num) throws IOException {
    File file = new File(file_name);
    FileChannel inputChannel = FileChannel.open(
        Paths.get(file.toURI()), StandardOpenOption.READ);

    ByteBuffer byteBuffer = ByteBuffer.allocate(num * 4);
    inputChannel.position(offset);
    int len = inputChannel.read(byteBuffer);
    inputChannel.close();
    int[] values = new int[len/4];
    byteBuffer.flip();
    for (int i =0; i < values.length; i++) {
      values[i] = byteBuffer.getInt();
    }
    return values;
  }

}
