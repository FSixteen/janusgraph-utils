package com.xyshzh.janusgraph.arguments;

import java.util.HashSet;
import java.util.Set;

public class ImportOptions {

  /** (必)schema模板文件路径,json文件. */
  private String file = null;

  private Arguments2Map options = null;

  /** 错误信息 */
  private final Set<String> error = new HashSet<>();

  public ImportOptions() {}

  public ImportOptions(Arguments2Map options) {
    this.options = options;
    this.reset();
  }

  /**
   * 重新初始化参数.
   * @param args
   */
  public void reset() {
    // 获取参数内容
    if (this.options.check()) {
      Object _file = this.options.getOptions().getOrDefault("file", null);
      if (_file instanceof String) {
        this.file = String.class.cast(_file);
      }
    }
    // 判断参数是否正常
    if (null == this.file) {
      this.error.add("--file 内容未能正常指定.");
    }
  }

  /**
   * 获取处理状态.
   * @return
   */
  public boolean check() {
    return 0 == this.error.size();
  }

  /**
   * 获取错误信息.
   * @return
   */
  public Set<String> getError() {
    return this.error;
  }

  /**
   * 打印错误信息.
   */
  public void printError() {
    this.error.forEach(e -> System.out.println(e));
  }

  /**
   * 打印规则.
   */
  public void printRole() {
    System.out.println("--task=schema:");
    System.out.println("--file=path           schema file path");
    System.out.println("                      (必)schema模板文件路径,json文件\n");
    System.out.println("--------------------------------------------------\n");
  }

}
