package com.xyshzh.janusgraph.datasource;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

/** 数据源为本地文件.
 * @author Shengjun Liu
 * @version 2018-07-20 */
public class ReadFile implements Read, Serializable {

  private static final long serialVersionUID = 294234546449731300L;

  private BufferedReader reader = null;

  public ReadFile(String filepath) {
    try {
      reader = new BufferedReader(new FileReader(filepath));
    } catch (FileNotFoundException e) {
      System.err.println(filepath + " >> 文件找不到.");
      System.exit(0);
    }
  }

  @Override
  public synchronized void init() {}

  @Override
  public synchronized String readLine() {
    try {
      String data = null;
      if (null != reader) data = reader.readLine();
      return null == data ? EXIT : data;
    } catch (IOException e) {
      System.err.println("文件IO读取异常.");
      return EXIT;
    }
  }

  @Override
  public synchronized void commitOffset() {}

  @Override
  public synchronized void close() {
    try {
      if (null != reader) reader.close();
    } catch (IOException e) {
      System.err.println("文件IO关闭异常.");
    }
  }

  @Override
  public synchronized boolean check() {
    return null != reader;
  }

}
