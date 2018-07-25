package com.xyshzh.janusgraph.test;

import java.util.Arrays;
import java.util.List;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.janusgraph.core.Multiplicity;
import org.janusgraph.core.schema.ConsistencyModifier;

import com.google.gson.Gson;
import com.xyshzh.janusgraph.core.GraphFactory;
import com.xyshzh.janusgraph.schema.entity.EdgeLabelKey;
import com.xyshzh.janusgraph.schema.entity.IndexKey;
import com.xyshzh.janusgraph.schema.entity.IndexPropertyKey;
import com.xyshzh.janusgraph.schema.entity.PropertyKey;
import com.xyshzh.janusgraph.schema.entity.Schema;
import com.xyshzh.janusgraph.schema.entity.VertexLabelKey;
import com.xyshzh.janusgraph.schema.enuminfo.DataType;
import com.xyshzh.janusgraph.schema.enuminfo.IndexType;
import com.xyshzh.janusgraph.schema.enuminfo.Mapping;

public class Test {

  @org.junit.Test
  public void indexTest() {
    Schema schema = new Schema();

    List<PropertyKey> p = Arrays.asList(new PropertyKey[] { new PropertyKey("uid", DataType.String),
        new PropertyKey("name", DataType.String), new PropertyKey("other", DataType.String) });

    schema.setProps(p);

    List<VertexLabelKey> v = Arrays.asList(new VertexLabelKey[] { new VertexLabelKey("PERSON"),
        new VertexLabelKey("COMPANY"), new VertexLabelKey("DEPARTMENT") });

    schema.setVertices(v);

    List<EdgeLabelKey> e = Arrays.asList(new EdgeLabelKey[] { new EdgeLabelKey("Kinship", Multiplicity.MULTI, null),
        new EdgeLabelKey("Own", Multiplicity.MULTI, null), new EdgeLabelKey("Serve", Multiplicity.MULTI, null) });

    schema.setEdges(e);

    List<IndexKey> i = Arrays
        .asList(new IndexKey[] { new IndexKey("all_vertex", IndexType.Vertex, ConsistencyModifier.DEFAULT,
            Arrays.asList(new IndexPropertyKey[] { new IndexPropertyKey("uid", Mapping.STRING) }), false, false, false,
            "search") });

    schema.setIndexes(i);

    System.out.println(new Gson().toJson(schema));
  }

  @org.junit.Test
  public void PropertyTestIv() {
    String data = "{\"name\":\"name\",\"type\":\"Double\"}";
    PropertyKey p = new Gson().fromJson(data, PropertyKey.class);
    System.out.println(p.getName() + "  ::  " + p.getType());
  }

  @org.junit.Test
  public void PropertyTest() {
    PropertyKey p = new PropertyKey("name", DataType.Double);
    System.out.println(new Gson().toJson(p));
  }

  public static void main(String[] args) throws Exception {
    GraphFactory graph = new GraphFactory();
    GraphTraversalSource g = graph.getG();
    System.out.println(g.V().count().next().longValue());
    g.close();
    graph.close();
  }
}
