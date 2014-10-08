package com.blunka.mk8assistant.shared;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by clocksmith on 9/8/14.
 */
public class SystemUtils {

  public static long getTotalMemory() {

    String str1 = "/proc/meminfo";
    String str2;
    String[] arrayOfString;
    long initial_memory = 0;
    try {
      FileReader localFileReader = new FileReader(str1);
      BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
      str2 = localBufferedReader.readLine();//meminfo
      arrayOfString = str2.split("\\s+");
      // Total Memory.
      initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
      localBufferedReader.close();
      return initial_memory;
    }
    catch (IOException e)
    {
      return -1;
    }
  }
}
