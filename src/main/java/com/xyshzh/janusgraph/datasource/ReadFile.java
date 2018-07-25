package com.xyshzh.janusgraph.datasource;

/**
 * 数据源为本地文件.
 * @author Shengjun Liu
 * @version 2018-07-20
 *
 */
public class ReadFile implements Read, java.io.Serializable {
  
  private static final long serialVersionUID = 294234546449731300L;
  
  private java.io.BufferedReader reader = null;

  public ReadFile(String filepath) {
    try {
      reader = (null == filepath)
          ? new java.io.BufferedReader(new java.io.InputStreamReader(this.getClass().getResourceAsStream("/default.txt")))
          : new java.io.BufferedReader(new java.io.FileReader(filepath));
    } catch (java.io.FileNotFoundException e) { // 拦截文件异常
      System.err.println("文件找不到.");
    }
  }

  @Override
  public void init() {}

  @Override
  public synchronized String readLine() {
    try {
      return reader.readLine();
    } catch (java.io.IOException e) { // 拦截文件异常
      System.err.println("文件IO读取异常.");
      return null;
    }
  }

  @Override
  public void close() {
    try {
      reader.close();
    } catch (java.io.IOException e) {
      System.err.println("文件IO关闭异常.");
    }
  }

  @Override
  public boolean check() {
    return null != reader;
  }

}
