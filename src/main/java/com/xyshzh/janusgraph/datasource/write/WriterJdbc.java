package com.xyshzh.janusgraph.datasource.write;

/**
 * 数据目标为本地文件.
 * @author Shengjun Liu
 * @version 2018-07-20
 *
 */
public class WriterJdbc implements Writer, java.io.Serializable {

  private static final long serialVersionUID = -2888951624720555997L;

  public WriterJdbc(String filepath) {}

  @Override
  public void init() {}

  @Override
  public synchronized void writerLine(java.util.HashMap<String, Object> content) {}

  @Override
  public void close() {}

  @Override
  public boolean check() {
    return false;
  }

}
