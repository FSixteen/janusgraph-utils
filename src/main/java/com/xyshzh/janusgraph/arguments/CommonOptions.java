package com.xyshzh.janusgraph.arguments;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CommonOptions {
  /** 全局可用执行任务 */
  private final Set<String> tasks = new HashSet<String>(Arrays.asList("schema", "export", "import"));

  // 全局
  /** (必)JanusGraph配置文件路径,properties文件 */
  private String conf = null;
  /** (必)任务内容,schema->创建Schema,export->导出数据,import->导入数据 */
  private String task = null;

  private Arguments2Map options = null;

  /** 错误信息 */
  private final Set<String> error = new HashSet<>();

  public CommonOptions() {}

  public CommonOptions(String[] args) {
    this.reset(args);
  }

  /**
   * 重新初始化参数.
   * @param args
   */
  public void reset(String[] args) {
    this.options = new Arguments2Map(args);
    // 获取参数内容
    if (this.options.check()) {
      Object _task = this.options.getOptions().getOrDefault("task", null);
      Object _conf = this.options.getOptions().getOrDefault("conf", null);
      if (_conf instanceof String) {
        this.conf = String.class.cast(_conf);
      }
      if (_task instanceof String && tasks.contains(_task)) {
        this.task = String.class.cast(_task);
      }
    }
    // 判断参数是否正常
    if (null == this.task) {
      this.error.add("--task 内容未能正常指定.");
    }
    if (null == this.conf) {
      this.error.add("--conf 内容未能正常指定.");
    }
  }

  public boolean isSchema() {
    if ("schema".equals(this.options.getOptions())) { return true; }
    return false;
  }

  public boolean isImport() {
    if ("import".equals(this.options.getOptions())) { return true; }
    return false;
  }

  public boolean isExport() {
    if ("export".equals(this.options.getOptions())) { return true; }
    return false;
  }
  
  /**
   * 获取处理状态.
   * @return
   */
  public boolean check() {
    return this.options.check() && 0 == this.error.size();
  }

  /**
   * 获取参数化Map.
   * @return 
   * @return
   */
  public Arguments2Map getOptions() {
    return this.options;
  }

  /**
   * 获取错误信息.
   * @return
   */
  public Set<String> getError() {
    Set<String> errors = new HashSet<>();
    errors.addAll(this.options.getError());
    errors.addAll(this.error);
    return errors;
  }

  /**
   * 打印错误信息.
   */
  public void printError() {
    this.options.printError();
    this.error.forEach(e -> System.out.println(e));
  }

  /**
   * 打印规则.
   */
  public void printRole() {
    this.options.printRole();
    System.out.println("--conf=?              config file path");
    System.out.println("                      (必)JanusGraph配置文件路径,properties文件\n");
    System.out.println("--task=?              schema | export | import");
    System.out.println("                      (必)任务内容,schema->创建Schema,export->导出数据,import->导入数据\n");
    System.out.println("--------------------------------------------------\n");
  }

}
