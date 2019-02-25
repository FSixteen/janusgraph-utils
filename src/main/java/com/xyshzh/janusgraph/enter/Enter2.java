package com.xyshzh.janusgraph.enter;

import com.xyshzh.janusgraph.arguments.Arguments;
import com.xyshzh.janusgraph.exportdata.ExportEdge;
import com.xyshzh.janusgraph.exportdata.ExportVertex;
import com.xyshzh.janusgraph.importdata.ImportEdge;
import com.xyshzh.janusgraph.importdata.ImportVertex;
import com.xyshzh.janusgraph.schema.BuildSchema;

/**
 * 统一入口
 * @author Shengjun Liu
 * @version 2018-07-26
 *
 */
public class Enter2 {
  public static void main(String[] args) {

    //    args = new String[] { "--task=schema", "--conf=/Users/liushengjun/workspace/janusgraph-utils_/src/main/resources/janusgarph.properties",
    //        "--file=/Users/liushengjun/workspace/schema.json" };

    //    args = new String[] { "--task=import", "--conf=/Users/liushengjun/workspace/janusgraph-utils_/src/main/resources/janusgarph.properties",
    //        "--type=file", "--file=/Users/liushengjun/Desktop/V_1225.txt", "--setvertexid=false", "--isVertex=true", "--isEdge=false" };
    //
    //    args = new String[] { "--task=import", "--conf=/Users/liushengjun/workspace/janusgraph-utils_/src/main/resources/janusgarph.properties",
    //        "--type=file", "--file=/Users/liushengjun/Desktop/E_1225.txt", "--setvertexid=false", "--isVertex=false", "--isEdge=true",
    //        "--fkeys=label1,uid1,name1", "--tkeys=label2,uid2,name2", "--thread=3" };
    //
    //    args = new String[] { "--task=export", "--conf=/Users/liushengjun/workspace/janusgraph-utils_/src/main/resources/janusgarph.properties",
    //        "--type=file", "--file=/Users/liushengjun/Desktop/V_1225.txt", "--isVertex=true", "--isEdge=false" };
    //
    //    args = new String[] { "--task=export", "--conf=/Users/liushengjun/workspace/janusgraph-utils_/src/main/resources/janusgarph.properties",
    //        "--type=file", "--file=/Users/liushengjun/Desktop/E_1225.txt", "--isVertex=false", "--isEdge=true" };

    // 简单的将参数生成Map集合.

    Arguments options = new Arguments(args);
    // java.util.HashMap<String, String> options = com.xyshzh.janusgraph.utils.ArgsUtils.initOptions(args);

    if (options.isSchema()) { // Schema
      new BuildSchema().execute(options.getCommonOptions().getOptionsMap());
    } else if (options.isExport()) { // 导出
      if (Boolean.valueOf(String.class.cast(options.getCommonOptions().getOptionsMap().getOrDefault("isVertex", "false")))) { // 导出点
        new ExportVertex().execute(options.getCommonOptions().getOptionsMap());;
      } else if (Boolean.valueOf(String.class.cast(options.getCommonOptions().getOptionsMap().getOrDefault("isEdge", "false")))) { // 导出边
        new ExportEdge().execute(options.getCommonOptions().getOptionsMap());
      } else {
        System.out.println("ERROR!");
        System.exit(1);
      }
    } else if (options.isImport()) { // 导入
      if (Boolean.valueOf(String.class.cast(options.getCommonOptions().getOptionsMap().getOrDefault("isVertex", "false")))) { // 导入点
        new ImportVertex().execute(options.getCommonOptions().getOptionsMap());
      } else if (Boolean.valueOf(String.class.cast(options.getCommonOptions().getOptionsMap().getOrDefault("isEdge", "false")))) { // 导入边
        new ImportEdge().execute(options.getCommonOptions().getOptionsMap());
      } else {
        System.out.println("ERROR!");
        System.exit(1);
      }
    } else { // 异常
      System.out.println("ERROR!");
      System.exit(1);
    }

  }
}
