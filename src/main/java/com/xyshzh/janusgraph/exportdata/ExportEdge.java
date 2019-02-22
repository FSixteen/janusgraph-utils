package com.xyshzh.janusgraph.exportdata;

import org.apache.tinkerpop.gremlin.structure.Vertex;

import com.xyshzh.janusgraph.task.Task;

/**
 * JanusGraph图数据库中导出全量Edge信息.
 * @author Shengjun Liu
 * @version 2018-07-21
 *
 */
public class ExportEdge implements Task {
  public void execute(java.util.Map<String, String> options) {

    // 试图打开文件,文件使用结束后或出现异常后,在finally内关闭文件
    com.xyshzh.janusgraph.datasource.write.WriterFile writer = new com.xyshzh.janusgraph.datasource.write.WriterFile(options.get("file"));

    if (!writer.check()) { // 检测数据源
      System.out.println("文件异常,请重试.");
      System.exit(0);
    }

    java.util.concurrent.atomic.AtomicInteger total = new java.util.concurrent.atomic.AtomicInteger(0); // 计数器
    com.xyshzh.janusgraph.core.GraphFactory graphFactory = new com.xyshzh.janusgraph.core.GraphFactory(); // 创建图数据库连接
    org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource g = graphFactory.getG(); // 获取遍历源,判断是否存在使用
    try {
      org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal<org.apache.tinkerpop.gremlin.structure.Edge, org.apache.tinkerpop.gremlin.structure.Edge> vs = g
          .E()/*.hasLabel( "Kinship", "Individual", "Enterprise", "Serve", "Project", "Own", "Payment")*/; // 获取所有关系信息
      try {
        while (vs.hasNext()) { // 如果有下个关系
          org.apache.tinkerpop.gremlin.structure.Edge v = vs.next(); // 获取下一个关系
          java.util.HashMap<String, Object> prop = new java.util.HashMap<>(); // 创建属性集合
          for (String key : v.keys()) { // 处理所有属性
            prop.put(key, v.value(key));
          }
          org.janusgraph.graphdb.relations.RelationIdentifier id = (org.janusgraph.graphdb.relations.RelationIdentifier) v
              .id();
          prop.put("~relationId", id.getRelationId()); // 添加关系id
          prop.put("~inVertexId", id.getInVertexId()); // 添加终点id
          prop.put("~outVertexId", id.getOutVertexId()); // 添加起点id
          prop.put("~typeId", id.getTypeId()); // 添加类型id
          prop.put("~label", v.label()); // 添加label
          Vertex fvid = g.V(id.getOutVertexId()).next();
          try { prop.put("uid1", fvid.value("uid")); } catch (Exception e) {}
          try { prop.put("name1", fvid.value("name")); } catch (Exception e) {}
          try { prop.put("label1", fvid.label()); } catch (Exception e) {}
          Vertex tvid = g.V(id.getInVertexId()).next();
          try { prop.put("uid2", tvid.value("uid")); } catch (Exception e) {}
          try { prop.put("name2", tvid.value("name")); } catch (Exception e) {}
          try { prop.put("label2", tvid.label()); } catch (Exception e) {}
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
