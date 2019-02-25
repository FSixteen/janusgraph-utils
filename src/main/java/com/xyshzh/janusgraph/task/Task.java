package com.xyshzh.janusgraph.task;

/**
 * 任务执行方法.
 * @author Shengjun Liu
 * @version 2018-07-27
 *
 */
public interface Task {
  public void execute(java.util.Map<String, Object> options);
}
