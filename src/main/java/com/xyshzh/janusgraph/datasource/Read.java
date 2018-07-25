package com.xyshzh.janusgraph.datasource;

/**
 * 数据源通用接口,子类可包含${ReadFile},${ReadJdbc}等等.
 * @author Shengjun Liu
 * @version 2018-07-20
 *
 */
public interface Read {

  /** 初始化 */
  void init();

  /** 按行读,为null结束 */
  String readLine();

  /** 关闭数据源 */
  void close();
  
  /** 检测数据源 */
  boolean check();

}
