package com.xyshzh.janusgraph.utils;

import java.util.Arrays;

/**
 * 简单的将参数生成Map集合.
 * @author Shengjun Liu
 * @version 2018-07-21
 *
 */
public class ArgsUtils {

  private final static String[] task = { "schema", "export", "import" };

  public static void main(String[] args) {
    showHelp();
  }

  private static void show(java.util.HashMap<String, String> options) {
    options.forEach((k, v) -> System.out.println(k + " = " + v));
  }

  private static boolean showHelp() {
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
    return false;
  }

  private static boolean check(java.util.HashMap<String, String> options) {
    if (null == options || 0 == options.size() || options.containsKey("help") || options.containsKey("h")) { // 显示帮助
      return showHelp();
    }
    if (!options.containsKey("conf")) { // 未包含JanusGraph配置文件
      System.out.println("  >>  --conf  >>  请指定JanusGraph配置文件路径,properties文件");
      return showHelp();
    }
    if (!options.containsKey("task") || !Arrays.asList(task).contains(options.get("task"))) { // 未包含任务内容
      System.out.println("  >>  --task  >>  请指定正确的任务内容");
      return showHelp();
    }
    // 创建Schema
    if (options.get("task").equals("schema")) {
      if (!options.containsKey("file")) {
        System.out.println("  >>  --file  >>  请指定正确的schema模板文件路径,json文件");
        return showHelp();
      }
    }
    // 导出数据
    if (options.get("task").equals("export")) {
      // 判断导出类型
      if ((!options.containsKey("isVertex") && !options.containsKey("isEdge")) // 不包含两个值
          || (Boolean.valueOf(options.containsKey("isVertex")) && Boolean.valueOf(options.containsKey("isEdge"))) // 包含两个true值
          || (!Boolean.valueOf(options.containsKey("isVertex")) && !Boolean.valueOf(options.containsKey("isEdge"))) // 包含两个false值
      ) {
        System.out.println("  >>  --isVertex | --isEdge  >>  请选择单一导出方式");
        return showHelp();
      }
      // 判断导出方式
      if (!options.containsKey("type") || !Arrays.asList("type", "jdbc").contains(options.get("type"))) { // 未指定导出方式 或 导出方式指定异常
        System.out.println("  >>  --type  >>  请选择导出方式");
        return showHelp();
      }
      if ("file".equals(options.get("type"))) { // 导出到file
        if (!options.containsKey("file")) { // 未指定导出位置
          System.out.println("  >>  --file  >>  请选择导出位置");
          return showHelp();
        }
      } else { // 导出到jdbc
        ;
      }
    }
    // 导入数据
    if (options.get("task").equals("import")) {
      // 判断导入类型
      if ((!options.containsKey("isVertex") && !options.containsKey("isEdge")) // 不包含两个值
          || (Boolean.valueOf(options.containsKey("isVertex")) && Boolean.valueOf(options.containsKey("isEdge"))) // 包含两个true值
          || (!Boolean.valueOf(options.containsKey("isVertex")) && !Boolean.valueOf(options.containsKey("isEdge"))) // 包含两个false值
      ) {
        System.out.println("  >>  --isVertex | --isEdge  >>  请选择单一导入方式");
        return showHelp();
      }

    }
    // 判断导入方式
    if (!options.containsKey("type") || !Arrays.asList("type", "jdbc").contains(options.get("type"))) { // 未指定导入方式 或 导入方式指定异常
      System.out.println("  >>  --type  >>  请选择导出方式");
      return showHelp();
    }
    if ("file".equals(options.get("type"))) { // 通过file导入
      if (!options.containsKey("file")) { // 未指定导入文件
        System.out.println("  >>  --file  >>  请选择导入文件");
        return showHelp();
      }
    } else { // 通过jdbc方式导入
      ;
    }

    // 验证方法完善中....

    if (options.containsKey("checkvertex") && !options.containsKey("setvertexid") && !options.containsKey("keys")) {
      System.out.println("检查节点信息,但没有自定义id,也没有指定字段判断节点或关系信息");
      return showHelp();
    } else if (options.containsKey("checkedge") && !options.containsKey("setvertexid")
        && !options.containsKey("keys")) {
      System.out.println("检查关系信息,但没有自定义id,也没有指定字段判断节点或关系信息");
      return showHelp();
    } else if (!options.containsKey("type")) {
      System.out.println("没有指定数据来源");
      return showHelp();
    } else if (options.get("type").equals("file") && !options.containsKey("file")) {
      System.out.println("没有指定数据来源文件");
      return showHelp();
    } else if (options.get("type").equals("jdbc") && (!options.containsKey("driver") || !options.containsKey("url"))) {
      System.out.println("没有指定数据来源数据库");
      return showHelp();
    } else {
      return true;
    }
  }

  public static java.util.HashMap<String, String> initOptions(String[] args) {
    java.util.HashMap<String, String> options = new java.util.HashMap<>();

    // 参数重组
    boolean check = true;
    for (int i = 0; null != args && i < args.length; i++) {
      if (args[i].startsWith("--")) {
        String[] kv = args[i].substring(2).split("=");
        if (1 == kv.length) {
          options.put(kv[0], "true");
        } else if (2 == kv.length) {
          options.put(kv[0], kv[1]);
        } else {
          check = false;
          System.out.println("参数 < " + args[i] + " > 无效.");
        }
      } else {
        check = false;
        System.out.println("参数 < " + args[i] + " > 无效.");
      }
    }

    // 参数重组通过且参数完整性检查通过
    if (check && check(options)) {
      show(options); // 显示重组的内容
      return options; // 返回所有参数
    } else {
      showHelp(); // 检查不通过,显示帮助内容后退出
      System.exit(0); // 退出
      return null;
    }
  }

}
