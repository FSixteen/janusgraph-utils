package com.xyshzh.janusgraph.datasource.write;

/**
 * 数据目标为本地文件.
 * @author Shengjun Liu
 * @version 2018-07-20
 *
 */
public class WriterFile implements Writer, java.io.Serializable {

  private static final long serialVersionUID = 2690398973874468201L;
  
  com.google.gson.Gson gson = new com.google.gson.GsonBuilder().disableHtmlEscaping().create(); // Json转换

  private java.io.FileWriter writer = null;

  public WriterFile(String filepath) {
    try {
      writer = (null == filepath) ? new java.io.FileWriter("./default.txt") : new java.io.FileWriter(filepath);
    } catch (java.io.IOException e) { // 拦截文件异常
      System.err.println("文件找不到或打开异常.");
      e.printStackTrace();
    }
  }

  @Override
  public void init() {}

  @Override
  public synchronized void writerLine(java.util.HashMap<String, Object> content) {
    try {
      writer.write(gson.toJson(content));
      writer.write("\r\n");
    } catch (java.io.IOException e) { // 拦截文件异常
      System.err.println("文件IO读写异常.");
    }
  }

  @Override
  public void close() {
    try {
      writer.flush();
      writer.close();
    } catch (java.io.IOException e) {
      System.err.println("文件IO关闭异常.");
    }
  }

  @Override
  public boolean check() {
    return null != writer;
  }

}
