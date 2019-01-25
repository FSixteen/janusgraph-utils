package com.xyshzh.janusgraph.test;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraphTransaction;
import org.janusgraph.core.JanusGraphVertex;

public class Test {

  @org.junit.Test
  public void checkTest() {
    int thread = 4;
    java.util.concurrent.atomic.AtomicInteger total = new java.util.concurrent.atomic.AtomicInteger(0); // 计数器
    java.util.concurrent.CountDownLatch countDownLatch = new java.util.concurrent.CountDownLatch(thread); // 闭锁
    // 试图打开文件,文件使用结束后或出现异常后,在finally内关闭文件
    long s = System.currentTimeMillis();
    com.xyshzh.janusgraph.datasource.Read reader = new com.xyshzh.janusgraph.datasource.ReadFile("/Users/liushengjun/Desktop/V.txt");
    reader.init();
    for (int i = 0; i < thread; i++) {
      new Thread(new Runnable() {
        @Override
        public void run() {
          String tempString = null; // 接收文件中每行数据,使用一变量,不需要重新生成新变量
          while ((tempString = reader.readLine()) != null) {
            total.addAndGet(1);
          }
          countDownLatch.countDown();
        }
      }).start();
    }
    try {
      countDownLatch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println(total);
    System.out.println(System.currentTimeMillis() - s);
    reader.close();
  }
  
  @org.junit.Test
  public void dropE() {
    com.xyshzh.janusgraph.core.GraphFactory graphFactory = new com.xyshzh.janusgraph.core.GraphFactory(); // 创建图数据库连接
    org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource g = graphFactory.getG(); // 获取遍历源,判断是否存在使用
    
    for (int i = 0; i < 30; i++) {
      System.out.println(i);
      g.E().has("type", "Individual").has("sub_money", 0.0).has("rea_money", 0.0).has("present", 0.0).limit(500).drop();
      g.tx().commit();
      g.tx().open();
    }
    
    graphFactory.close();
  }

  @org.junit.Test
  public void updateV() {
    com.xyshzh.janusgraph.core.GraphFactory graphFactory = new com.xyshzh.janusgraph.core.GraphFactory(); // 创建图数据库连接
    org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource g = graphFactory.getG(); // 获取遍历源,判断是否存在使用

    GraphTraversal<Vertex, Vertex> starts = g.V(1 << 8);

    Vertex start = null;
    if (starts.hasNext()) start = starts.next();

    if (null != start) {
      start.property("uid", "100000");
      System.out.println("update");
    }

    g.tx().commit();
    g.tx().open();
    graphFactory.close();
  }

  @org.junit.Test
  public void insertE() {
    com.xyshzh.janusgraph.core.GraphFactory graphFactory = new com.xyshzh.janusgraph.core.GraphFactory(); // 创建图数据库连接
    org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource g = graphFactory.getG(); // 获取遍历源,判断是否存在使用
    // 先查找起点和终点
    GraphTraversal<Vertex, Vertex> starts = g.V(1 << 8);
    GraphTraversal<Vertex, Vertex> ends = g.V(2 << 8);
    // 判断起点和终点是否存在
    Vertex start = null;
    Vertex end = null;
    if (starts.hasNext()) start = starts.next();
    if (ends.hasNext()) end = ends.next();
    // 如果存在, 添加关系, 关系方向是 end -> start
    if (null != start && null != end) {
      Edge p = end.addEdge("OWN", start);
      // 给关系添加属性
      p.property("uid", "uid-" + Long.valueOf(1 << 8).toString());
      p.property("name", "name-" + Long.valueOf(1 << 8).toString());
    }
    // 提交事务
    g.tx().commit();
    // 如果还有操作, 需要重新开启事务
    g.tx().open();
    // 关闭会话
    graphFactory.close();
  }

  @org.junit.Test
  public void insertV() {
    com.xyshzh.janusgraph.core.GraphFactory graphFactory = new com.xyshzh.janusgraph.core.GraphFactory(); // 创建图数据库连接
    JanusGraphTransaction tx = graphFactory.getTx(); // 创建事物

    JanusGraphVertex v1 = tx.addVertex(org.apache.tinkerpop.gremlin.structure.T.label, "Person",
        org.apache.tinkerpop.gremlin.structure.T.id, 1 << 8);
    v1.property("uid", "uid-" + Long.valueOf(1 << 8).toString()).property("name", "name-" + Long.valueOf(1 << 8).toString());

    JanusGraphVertex v2 = tx.addVertex(org.apache.tinkerpop.gremlin.structure.T.label, "Person",
        org.apache.tinkerpop.gremlin.structure.T.id, 2 << 8);
    v2.property("uid", "uid-" + Long.valueOf(2 << 8).toString()).property("name", "name-" + Long.valueOf(2 << 8).toString());

    tx.commit();
    graphFactory.close();
  }

}
