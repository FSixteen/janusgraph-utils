package com.xyshzh.janusgraph.exportdata;

import com.xyshzh.janusgraph.task.Task;

/**
 * JanusGraph图数据库中导出全量Vertex信息.
 * @author Shengjun Liu
 * @version 2018-07-21
 *
 */
public class ExportVertex implements Task {
  public void execute(java.util.HashMap<String, String> options) {

    // 试图打开文件,文件使用结束后或出现异常后,在finally内关闭文件
    com.xyshzh.janusgraph.datasource.WriterFile writer = new com.xyshzh.janusgraph.datasource.WriterFile(options.get("file"));

    if (!writer.check()) { // 检测数据源
      System.out.println("文件异常,请重试.");
      System.exit(0);
    }

    java.util.concurrent.atomic.AtomicInteger total = new java.util.concurrent.atomic.AtomicInteger(0); // 计数器
    com.xyshzh.janusgraph.core.GraphFactory graphFactory = new com.xyshzh.janusgraph.core.GraphFactory(); // 创建图数据库连接
    org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource g = graphFactory.getG(); // 获取遍历源,判断是否存在使用
    try {
      org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal<org.apache.tinkerpop.gremlin.structure.Vertex, org.apache.tinkerpop.gremlin.structure.Vertex> vs = g
          .V(); // 获取所有节点信息
      try {
        while (vs.hasNext()) { // 如果有下个节点
          org.apache.tinkerpop.gremlin.structure.Vertex v = vs.next(); // 获取下一个节点
          java.util.HashMap<String, Object> prop = new java.util.HashMap<>(); // 创建属性集合
          for (String key : v.keys()) { // 处理所有属性
            prop.put(key, v.value(key));
          }
          prop.put("~id", v.id()); // 添加id
          prop.put("~label", v.label()); // 添加label
          writer.writerLine(prop); // 写入到文件中
          total.addAndGet(1); // 写入后,计数加一
          if (total.get() % 400 == 0) { // 每四百次,提示一次
            System.out.println("Current Position >> " + total.get());
          }
        }
        System.out.println("Current Position >> " + total.get() + ", over!");
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        vs.close(); // 关闭查询
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        g.close();// 关闭遍历源 
        graphFactory.close(); // 关闭图数据库连接
      } catch (Exception e) {
        e.printStackTrace();
      }
      writer.close(); // 关闭文件
    }

    System.out.println("I'm OK!");

  }
}
