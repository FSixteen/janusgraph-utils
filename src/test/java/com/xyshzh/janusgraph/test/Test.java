package com.xyshzh.janusgraph.test;

import java.io.FileNotFoundException;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;

import com.google.gson.Gson;
import com.xyshzh.janusgraph.core.GraphFactory;
import com.xyshzh.janusgraph.datasource.ReadFile;
import com.xyshzh.janusgraph.schema.entity.PropertyKey;
import com.xyshzh.janusgraph.schema.entity.Schema;
import com.xyshzh.janusgraph.schema.enuminfo.DataType;

public class Test {

  @org.junit.Test
  public void testBoolean() throws FileNotFoundException {
    System.out.println(Boolean.valueOf("true"));
    System.out.println(Boolean.valueOf("false"));
    System.out.println(Boolean.valueOf("true") == true);
    System.out.println(Boolean.valueOf("false") == false);
  }

  @org.junit.Test
  public void h() {
    // [birthday, reg_person, pageRank, address, sex, type, uid, ~label, ctype, name, ~id, tag, time, state, updatetime, timestamp]
    ReadFile reader = new ReadFile("./V.txt");
    String tempString = null; // 接收文件中每行数据,使用一变量,不需要重新生成新变量
    java.util.HashSet set = new java.util.HashSet();
    while ((tempString = reader.readLine()) != null) {
      net.sf.json.JSONObject content = null;
      content = net.sf.json.JSONObject.fromObject(tempString);
      content.keySet().forEach(k -> set.add(k.toString()));
    }
    System.out.println(set);
  }

  @org.junit.Test
  public void indexTest() {
    System.out.println(new Gson().toJson(Schema.getTest()));
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
