package com.xyshzh.janusgraph.test;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraphTransaction;
import org.janusgraph.core.JanusGraphVertex;

public class Test {
  
  @org.junit.Test
  public void updateV() {
    com.xyshzh.janusgraph.core.GraphFactory graphFactory = new com.xyshzh.janusgraph.core.GraphFactory(); // 创建图数据库连接
    org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource g = graphFactory.getG(); // 获取遍历源,判断是否存在使用
    
    GraphTraversal<Vertex, Vertex> starts = g.V(1 << 8);
    
    Vertex start = null;
    if(starts.hasNext()) start = starts.next();
    
    if(null != start) {
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
    
    GraphTraversal<Vertex, Vertex> starts = g.V(1 << 8);
    GraphTraversal<Vertex, Vertex> ends = g.V(2 << 8);
    
    Vertex start = null;
    Vertex end = null;
    if(starts.hasNext()) start = starts.next();
    if(ends.hasNext()) end = ends.next();
    
    if(null != start && null != end) {
      Edge p = end.addEdge("OWN", start);
      p.property("uid", "uid-" + Long.valueOf(1 << 8).toString());
      p.property("name", "name-" + Long.valueOf(1 << 8).toString());
    }
    
    g.tx().commit();
    g.tx().open();
    graphFactory.close();
  }

  @org.junit.Test
  public void insertV() {
    com.xyshzh.janusgraph.core.GraphFactory graphFactory = new com.xyshzh.janusgraph.core.GraphFactory(); // 创建图数据库连接
    JanusGraphTransaction tx = graphFactory.getTx(); // 创建事物

    JanusGraphVertex v1 = tx.addVertex(org.apache.tinkerpop.gremlin.structure.T.label, "Person", org.apache.tinkerpop.gremlin.structure.T.id, 1 << 8);
    v1.property("uid", "uid-" + Long.valueOf(1 << 8).toString()).property("name", "name-" + Long.valueOf(1 << 8).toString());
    
    JanusGraphVertex v2 = tx.addVertex(org.apache.tinkerpop.gremlin.structure.T.label, "Person", org.apache.tinkerpop.gremlin.structure.T.id, 2 << 8);
    v2.property("uid", "uid-" + Long.valueOf(2 << 8).toString()).property("name", "name-" + Long.valueOf(2 << 8).toString());
    
    tx.commit();
    graphFactory.close();
  }

}
