package com.xyshzh.janusgraph.parameter.entity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 程序入口参数.
 * @author Shengjun Liu
 * @version 2018-12-21
 *
 */
public class Arguments {

  private final static String prefix = "--";
  private final static String kv_split = "=";
  private final static String mapkv_split = ":";
  private final static String map_split = ",";

  /** 全局可用执行任务 */
  private final static String[] tasks = { "schema", "export", "import" };
  private final static String[] types = { "file", "jdbc", "kafka" };

  // 全局
  /** (必)任务内容,schema->创建Schema,export->导出数据,import->导入数据 */
  private String task = null;
  /** (必)JanusGraph配置文件路径,properties文件 */
  private String conf = null;
  /** (选)schema模板文件路径,json文件 | (必)导入(出)文件路径,txt文件 | ... */
  private String file = null;
  /** (选)导入(出)并行度,默认为1 */
  private int thread = 1;
  /** (选)导入(出)顶点类型 */
  private boolean isVertex = false;
  /** (选)导入(出)关系类型 */
  private boolean isEdge = false;
  /** (选)导入数据源或导出目标类型,file->文件,jdbc->数据库,kafka->消息队列 */
  private String type = null;
  /** (选)自定义顶点id */
  private boolean setvertexid = false;
  /** (选)检查顶点节点是否存在,初次导入可以忽略 */
  private boolean checkvertex = false;
  /** (选)检查关系节点是否存在,初次导入可以忽略 */
  private boolean checkedge = false;
  /** (选)当校验节点存在,新数据是否覆盖,false->忽略,true->覆盖 */
  private boolean overwrite = false;
  /** (选)不自定义顶点id,则通过这些字段判断顶点节点是否存在 | 通过这些字段判断关系节点是否存在 */
  private Map<String, String> keys = null;
  /** (选)不自定义顶点id,则通过这些字段判断关系起始节点是否存在,uid1->uid,name1->name,label1->type */
  private Map<String, String> fkeys = null;
  /** (选)不自定义顶点id,则通过这些字段判断关系结束节点是否存在,uid1->uid,name1->name,label1->type */
  private Map<String, String> tkeys = null;
  /**  */
  private String driver = null;
  private String url = null;
  private String jdbctype = null;
  private String ip = null;
  private String port = null;
  private String user = null;
  private String password = null;
  private String table = null;
  private String query = null;
  /**  */
  private String zk = null;
  private String topic = null;

  public Arguments(String[] args) {
    HashMap<String, String> options = new HashMap<>();
    boolean check = true;
    for (int i = 0; null != args && i < args.length; i++) {
      if (args[i].trim().startsWith(prefix)) {
        String[] kv = args[i].trim().substring(2).split(kv_split);
        if (1 == kv.length) {
          options.put(kv[0].trim(), "true");
        } else if (2 == kv.length) {
          options.put(kv[0].trim(), kv[1].trim());
        } else {
          check = false;
          System.out.println("参数 < " + args[i] + " > 无效.");
        }
      } else {
        check = false;
        System.out.println("参数 < " + args[i] + " > 无效.");
      }
    }
    if (check) init(options); // 正常, 初始化参数
    else showHelp(); // 异常, 打印帮助信息
  }

  /** 打印帮助信息, 并退出 */
  private void showHelp() {
    System.out.println("\n");
    System.out.println("--conf                config file path");
    System.out.println("                      (必)JanusGraph配置文件路径,properties文件\n");
    System.out.println("--task=?              schema | export | import");
    System.out.println("                      (必)任务内容,schema->创建Schema,export->导出数据,import->导入数据\n");

    System.out.println("---------------------------------------------------------------------------\n");

    System.out.println("--task=schema:");
    System.out.println("--file=path           schema file path");
    System.out.println("                      (必)schema模板文件路径,json文件\n");

    System.out.println("---------------------------------------------------------------------------\n");

    System.out.println("--task=export:");
    System.out.println("--isVertex=false      false | true");
    System.out.println("                      (选)导出顶点类型\n");
    System.out.println("--isEdge=false        false | true");
    System.out.println("                      (选)导出关系类型\n");
    System.out.println("--type=file           file | jdbc");
    System.out.println("                      (必)数据目标(目前只支持file)\n");
    System.out.println("--file=path           file path (限--type=file)");
    System.out.println("                      (必)导出文件路径,txt文件\n");

    System.out.println("---------------------------------------------------------------------------\n");

    System.out.println("--task=import:");
    System.out.println("--thread=1            [1+]");
    System.out.println("                      (选)导入并行度,默认为1\n");
    System.out.println("--isVertex=?          false | true");
    System.out.println("                      (选)导入顶点类型\n");
    System.out.println("--isEdge=?            false | true");
    System.out.println("                      (选)导入关系类型\n");
    System.out.println("--type=file           file | jdbc");
    System.out.println("                      (必)数据目标(目前只支持file)\n");
    System.out.println("--file=path           file path (限--type=file)");
    System.out.println("                      (必)导出文件路径,txt文件\n");
    System.out.println("--checkvertex=?       false | true");
    System.out.println("                      (选)检查节点信息是否存在,初次导入可以忽略\n");
    System.out.println("--checkedge=?         false | true");
    System.out.println("                      (选)检查关系信息是否存在,初次导入可以忽略\n");
    System.out.println("--setvertexid=?       false | true");
    System.out.println("                      (选)自定义id\n");
    System.out.println("--keys=uid,name       uid,name,time      不自定义id,则通过这些字段判断节点或关系信息是否存在\n");
    System.out.println("--fkeys=uid1,name1    uid1,name1         不自定义id,则通过这些字段判断关系起始节点是否存在\n");
    System.out.println("--tkeys=uid2,name2    uid2,name2         不自定义id,则通过这些字段判断关系结束节点是否存在\n");

    System.out.println("---------------------------------------------------------------------------\n");

    System.out.println("当数据来源是jdbc时, 完善中:");
    System.out.println("--driver=class        数据库驱动");
    System.out.println("--url=jdbc://...      数据库连接方式");
    System.out.println("--table=user          数据库来源表, user | (select a,b,c from user) as t");
    System.out.println("\n");
    System.exit(0);
  }

  /** 初始化参数信息 */
  private void init(HashMap<String, String> options) {
    for (Entry<String, String> entry : options.entrySet()) {
      switch (entry.getKey()) {
      case "task":
        this.task = entry.getValue();
        break;
      case "conf":
        this.conf = entry.getValue();
        break;
      case "file":
        this.file = entry.getValue();
        break;
      case "thread":
        this.thread = Integer.parseInt(entry.getValue());
        break;
      case "isVertex":
        this.isVertex = Boolean.parseBoolean(entry.getValue());
        break;
      case "isEdge":
        this.isEdge = Boolean.parseBoolean(entry.getValue());
        break;
      case "type":
        this.type = entry.getValue();
        break;
      case "setvertexid":
        this.setvertexid = Boolean.parseBoolean(entry.getValue());
        break;
      case "checkvertex":
        this.checkvertex = Boolean.parseBoolean(entry.getValue());
        break;
      case "checkedge":
        this.checkedge = Boolean.parseBoolean(entry.getValue());
        break;
      case "overwrite":
        this.overwrite = Boolean.parseBoolean(entry.getValue());
        break;
      case "keys":
        Map<String, String> _keys = new HashMap<>();
        for (String mapkv : entry.getValue().split(map_split)) {
          String[] kv = mapkv.split(mapkv_split);
          if (1 == kv.length) {
            _keys.put(kv[0].trim(), kv[0].trim());
          } else if (2 == kv.length) {
            _keys.put(kv[0].trim(), kv[1].trim());
          } else {
            showHelp();
          }
        }
        this.keys = _keys;
        break;
      case "fkeys":
        Map<String, String> _fkeys = new HashMap<>();
        for (String mapkv : entry.getValue().split(map_split)) {
          String[] kv = mapkv.split(mapkv_split);
          if (1 == kv.length) {
            _fkeys.put(kv[0].trim(), kv[0].trim());
          } else if (2 == kv.length) {
            _fkeys.put(kv[0].trim(), kv[1].trim());
          } else {
            showHelp();
          }
        }
        this.fkeys = _fkeys;
        break;
      case "tkeys":
        Map<String, String> _tkeys = new HashMap<>();
        for (String mapkv : entry.getValue().split(map_split)) {
          String[] kv = mapkv.split(mapkv_split);
          if (1 == kv.length) {
            _tkeys.put(kv[0].trim(), kv[0].trim());
          } else if (2 == kv.length) {
            _tkeys.put(kv[0].trim(), kv[1].trim());
          } else {
            showHelp();
          }
        }
        this.tkeys = _tkeys;
        break;
      case "driver":
        this.driver = entry.getValue();
        break;
      case "url":
        this.url = entry.getValue();
        break;
      case "jdbctype":
        this.jdbctype = entry.getValue();
        break;
      case "ip":
        this.ip = entry.getValue();
        break;
      case "port":
        this.port = entry.getValue();
        break;
      case "user":
        this.user = entry.getValue();
        break;
      case "password":
        this.password = entry.getValue();
        break;
      case "table":
        this.table = entry.getValue();
        break;
      case "query":
        this.query = entry.getValue();
        break;
      case "zk":
        this.zk = entry.getValue();
        break;
      case "topic":
        this.topic = entry.getValue();
        break;
      default:
        showHelp();
        break;
      }
    }
  }

  /**
   * 校验参数内容
   * @return true:正常,false:失败
   */
  private boolean check() {
    // 校验任务类型是否正常
    if (null == this.task || !Arrays.asList(this.tasks).contains(this.task)) showHelp();
    // 校验JanusGraph配置文件路径,properties文件
    if (null == this.conf) showHelp();
    if (this.tasks[0].equals(this.task)) { // "schema", "export", "import"
      // 判断schema模板文件路径,json文件
      if (null == this.file) showHelp();
    } else if (this.tasks[1].equals(this.task)) { // "export"

    } else { // "import"

    }
    return true;
  }

}
