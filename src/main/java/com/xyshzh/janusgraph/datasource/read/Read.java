package com.xyshzh.janusgraph.datasource.read;

/** 数据源通用读取接口,子类可包含${ReadFile},${ReadJdbc}等等.
 * @author Shengjun Liu
 * @version 2018-07-20 */
public interface Read extends java.io.Serializable {

  /** 退出 */
  public static final String EXIT = "EXIT";

  /** 初始化 */
  Read init();

  /** 按行读,为EXIT结束 */
  String readLine();

  /** 提交偏移量 */
  <T> T commitOffset();

  /** 关闭数据源 */
  void close();

  /** 检测数据源 */
  boolean check();

}
