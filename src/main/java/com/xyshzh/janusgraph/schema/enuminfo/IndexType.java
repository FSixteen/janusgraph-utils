package com.xyshzh.janusgraph.schema.enuminfo;

/**
 * schema中Index可支持的枚举类型.
 * @author Shengjun Liu
 * @version 2018-07-25
 *
 */
public enum IndexType {
  Vertex(org.apache.tinkerpop.gremlin.structure.Vertex.class),

  Edge(org.apache.tinkerpop.gremlin.structure.Edge.class);

  private Class<? extends org.apache.tinkerpop.gremlin.structure.Element> clazz;

  private IndexType(Class<? extends org.apache.tinkerpop.gremlin.structure.Element> clazz) {
    this.clazz = clazz;
  }

  public Class<? extends org.apache.tinkerpop.gremlin.structure.Element> getClazz() {
    return clazz;
  }
}