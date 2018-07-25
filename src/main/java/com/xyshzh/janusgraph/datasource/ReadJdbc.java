package com.xyshzh.janusgraph.datasource;

/**
 * 数据源为JDBC.
 * @author Shengjun Liu
 * @version 2018-07-20
 *
 */
public class ReadJdbc implements Read, java.io.Serializable {

  private static final long serialVersionUID = -3033449241202336863L;
  
  public ReadJdbc() {
  }

  @Override
  public void init() {

  }

  @Override
  public String readLine() {
    return null;
  }

  @Override
  public void close() {

  }

  @Override
  public boolean check() {
    return false;
  }

}
