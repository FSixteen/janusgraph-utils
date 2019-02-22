package com.xyshzh.janusgraph.datasource.read;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

/** 数据源通用读取接口,数据源为本地文件.
 * @author Shengjun Liu
 * @version 2018-07-20 */
public class ReadFile implements Read {

  private static final long serialVersionUID = 294234546449731300L;

  /**
   * BufferedReader 实例.
   */
  private BufferedReader reader = null;

  /**
   * 已读偏移量.
   */
  private AtomicLong offset = new AtomicLong(0);

  public ReadFile(String filepath) {
    try {
      reader = new BufferedReader(new FileReader(filepath));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.exit(0);
    }
  }

  @Override
  public synchronized Read init() {
    return this;
  }

  @Override
  public synchronized String readLine() {
    try {
      String data = null;
      if (null != reader) {
        data = reader.readLine();
        offset.addAndGet(1);
      }
      return null == data ? EXIT : data;
    } catch (IOException e) {
      e.printStackTrace();
      return EXIT;
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public synchronized Long commitOffset() {
    return new Long(offset.get());
  }

  @Override
  public synchronized void close() {
    try {
      if (null != reader) reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public synchronized boolean check() {
    return null != reader;
  }

}
