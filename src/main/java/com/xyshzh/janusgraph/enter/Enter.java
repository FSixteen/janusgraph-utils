package com.xyshzh.janusgraph.enter;

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
public class Enter {
  public static void main(String[] args) {

    args = new String[] { "--task=schema", "--conf=/Users/liushengjun/workspace/janusgraph-utils_/src/main/resources/janusgarph.property",
        "--file=/Users/liushengjun/workspace/janusgraph-utils_/schema.json" };

    // 简单的将参数生成Map集合.
    java.util.HashMap<String, String> options = com.xyshzh.janusgraph.utils.ArgsUtils.initOptions(args);
    if ("schema".equals(options.get("task"))) { // Schema
      new BuildSchema().execute(options);
    } else if ("export".equals(options.get("task"))) { // 导出
      if (Boolean.valueOf(options.get("isVertex"))) { // 导出点
        new ExportVertex().execute(options);
        ;
      } else if (Boolean.valueOf(options.get("isEdge"))) { // 导出边
        new ExportEdge().execute(options);
      } else {
        System.out.println("ERROR!");
        System.exit(1);
      }
    } else if ("import".equals(options.get("task"))) { // 导入
      if (Boolean.valueOf(options.get("isVertex"))) { // 导入点
        new ImportVertex().execute(options);
        ;
      } else if (Boolean.valueOf(options.get("isEdge"))) { // 导入边
        new ImportEdge().execute(options);
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
