package com.xyshzh.janusgraph.datasource.read;

import java.util.Map;

/** 数据源通用读取接口,数据源为JDBC.
 * @author Shengjun Liu
 * @version 2018-07-20 */
public class ReadJdbc implements Read {

  private static final long serialVersionUID = -3033449241202336863L;

  public ReadJdbc(Map<String, Object> options) {}

  public ReadJdbc() {}

  @Override
  public synchronized Read init() {
    return this;
  }

  @Override
  public String readLine() {
    return null;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object commitOffset() {
    return null;
  }

  @Override
  public void close() {}

  @Override
  public boolean check() {
    return false;
  }

}
