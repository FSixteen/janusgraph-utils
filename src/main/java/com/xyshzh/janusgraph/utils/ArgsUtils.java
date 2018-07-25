package com.xyshzh.janusgraph.utils;

/**
 * 简单的将参数生成Map集合.
 * @author Shengjun Liu
 * @version 2018-07-21
 *
 */
public class ArgsUtils {
  
  public static void main(String[] args) {
    initOptions(new String[]{"--h"});
  }

  private static void show(java.util.HashMap<String, Object> options) {
    options.forEach((k, v) -> System.out.println(k + " = " + v));
  }

  private static boolean showHelp() {
    System.out.println("\n\n");
    System.out.println("--checkvertex         检查节点信息是否存在,初次导入可以忽略");
    System.out.println("--checkedge           检查关系信息是否存在,初次导入可以忽略");
    System.out.println("--setvertexid         自定义id");
    System.out.println("--thread=1            integer value  并行度");
    System.out.println("--keys=uid,name       uid,name,time  不自定义id,则通过这些字段判断节点或关系信息是否存在");
    System.out.println("--fkeys=uid1,name1    uid1,name1     不自定义id,则通过这些字段判断关系起始节点是否存在");
    System.out.println("--tkeys=uid2,name2    uid2,name2     不自定义id,则通过这些字段判断关系结束节点是否存在");
    
    System.out.println("--type=file           file | jdbc    数据来源");
    
    System.out.println("");
    System.out.println("当数据来源是file时:");
    System.out.println("--file=path           ");
    
    System.out.println("");
    System.out.println("当数据来源是jdbc时, 完善中:");
    System.out.println("--driver=class        数据库驱动");
    System.out.println("--url=jdbc://...      数据库连接方式");
    System.out.println("--table=user          数据库来源表, user | (select a,b,c from user) as t");
    System.out.println("\n\n");
    return false;
  }

  private static boolean check(java.util.HashMap<String, Object> options) {
    if (null == options || options.containsKey("help") || options.containsKey("h")) {
      // 显示帮助
      return showHelp();
    } else if (options.containsKey("checkvertex") && !options.containsKey("setvertexid") && !options.containsKey("keys")) {
      System.out.println("检查节点信息,但没有自定义id,也没有指定字段判断节点或关系信息");
      return showHelp();
    } else if (options.containsKey("checkedge") && !options.containsKey("setvertexid") && !options.containsKey("keys")) {
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

  public static java.util.HashMap<String, Object> initOptions(String[] args) {
    java.util.HashMap<String, Object> options = new java.util.HashMap<>();

    for (int i = 0; null != args && i < args.length; i++) {
      if (args[i].startsWith("--")) {
        String[] kv = args[i].substring(2).split("=");
        if (1 == kv.length) {
          options.put(kv[0], true);
        } else if (2 == kv.length) {
          options.put(kv[0], kv[1]);
        } else {
          System.out.println("参数 < " + args[i] + " > 无效.");
        }
      } else {
        System.out.println("参数 < " + args[i] + " > 无效.");
      }
    }

    if (check(options)) {
      show(options);
      return options;
    } else {
      System.exit(0);
      return null;
    }
  }

}
