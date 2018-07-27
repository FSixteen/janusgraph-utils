package com.xyshzh.janusgraph.schema.entity;

import java.util.Arrays;
import java.util.List;

import org.janusgraph.core.Multiplicity;
import org.janusgraph.core.schema.ConsistencyModifier;

import com.google.bigtable.repackaged.com.google.gson.Gson;
import com.xyshzh.janusgraph.schema.enuminfo.DataType;
import com.xyshzh.janusgraph.schema.enuminfo.IndexType;
import com.xyshzh.janusgraph.schema.enuminfo.Mapping;

/**
 * schema实体信息.
 * @author Shengjun Liu
 * @version 2018-07-25
 *
 */
public class Schema {
  /**
   * 所有的属性信息.
   */
  private List<PropertyKey> props;
  /**
   * 所有顶点类型.
   */
  private List<VertexLabelKey> vertices;
  /**
   * 所有关系类型.
   */
  private List<EdgeLabelKey> edges;
  /**
   * 所有索引信息.
   */
  private List<IndexKey> indexes;

  public Schema() {}

  public Schema(List<PropertyKey> props, List<VertexLabelKey> vertices, List<EdgeLabelKey> edges,
      List<IndexKey> indexes) {
    this.props = props;
    this.vertices = vertices;
    this.edges = edges;
    this.indexes = indexes;
  }

  public List<PropertyKey> getProps() {
    return props;
  }

  public void setProps(List<PropertyKey> props) {
    this.props = props;
  }

  public List<VertexLabelKey> getVertices() {
    return vertices;
  }

  public void setVertices(List<VertexLabelKey> vertices) {
    this.vertices = vertices;
  }

  public List<EdgeLabelKey> getEdges() {
    return edges;
  }

  public void setEdges(List<EdgeLabelKey> edges) {
    this.edges = edges;
  }

  public List<IndexKey> getIndexes() {
    return indexes;
  }

  public void setIndexes(List<IndexKey> indexes) {
    this.indexes = indexes;
  }

  public synchronized static Schema apply(String content) {
    return new Gson().fromJson(content, Schema.class);
  }

  /**
   * 获取测试类
   * @return
   */
  public static Schema getTest() {
    Schema schema = new Schema();

    List<PropertyKey> p = Arrays
        .asList(new PropertyKey[] { new PropertyKey("uid", DataType.String), new PropertyKey("name", DataType.String),
            new PropertyKey("birthday", DataType.String), new PropertyKey("reg_person", DataType.String),
            new PropertyKey("pageRank", DataType.Double), new PropertyKey("address", DataType.String),
            new PropertyKey("sex", DataType.String), new PropertyKey("type", DataType.String),
            new PropertyKey("ctype", DataType.String), new PropertyKey("tag", DataType.String),
            new PropertyKey("time", DataType.String), new PropertyKey("state", DataType.String),
            new PropertyKey("updatetime", DataType.Long), new PropertyKey("timestamp", DataType.Long) });

    schema.setProps(p);

    List<VertexLabelKey> v = Arrays.asList(new VertexLabelKey[] { new VertexLabelKey("Person"),
        new VertexLabelKey("Company"), new VertexLabelKey("Department") });

    schema.setVertices(v);

    List<EdgeLabelKey> e = Arrays.asList(new EdgeLabelKey[] { new EdgeLabelKey("INVEST_H", Multiplicity.MULTI, null),
        new EdgeLabelKey("INVEST_O", Multiplicity.MULTI, null), new EdgeLabelKey("KINSHIP", Multiplicity.MULTI, null),
        new EdgeLabelKey("OWN", Multiplicity.MULTI, null), new EdgeLabelKey("PAYMENT", Multiplicity.MULTI, null),
        new EdgeLabelKey("PAYMENT", Multiplicity.MULTI, null), new EdgeLabelKey("SERVE", Multiplicity.MULTI, null) });

    schema.setEdges(e);

    List<IndexKey> i = Arrays.asList(new IndexKey[] {
        new IndexKey("all_vertex1", IndexType.Vertex, ConsistencyModifier.DEFAULT,
            Arrays.asList(new IndexPropertyKey[] { new IndexPropertyKey("uid", Mapping.TEXTSTRING),
                new IndexPropertyKey("uid", Mapping.TEXTSTRING) }),
            true, true, true, "search"),
        new IndexKey("all_edge1", IndexType.Edge,
            ConsistencyModifier.DEFAULT, Arrays.asList(new IndexPropertyKey[] {
                new IndexPropertyKey("uid", Mapping.TEXTSTRING), new IndexPropertyKey("uid", Mapping.TEXTSTRING) }),
            true, true, true, "search") });

    schema.setIndexes(i);

    return schema;
  }

}
