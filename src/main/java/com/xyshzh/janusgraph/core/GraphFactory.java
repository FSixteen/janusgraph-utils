package com.xyshzh.janusgraph.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.JanusGraphTransaction;

/**
 * JanusGraphFactory, 在使用JanusGraph前, 必须让其初始化完存储库(自动建表等).
 * 
 * @author Shengjun Liu
 * @version 2018-01-15
 *
 */
public final class GraphFactory implements Serializable {
  private static final long serialVersionUID = 1L;
  private JanusGraphFactory.Builder config = null;
  private JanusGraph graph = null;

  public GraphFactory() {
    builderConfigByFile(null);
  }

  public GraphFactory(String path) {
    builderConfigByFile(path);
  }

  public JanusGraphFactory.Builder getConfig() {
    return config;
  }

  public JanusGraph getGraph() {
    return graph;
  }

  public GraphTraversalSource getG() {
    return this.graph.traversal();
  }

  public JanusGraphTransaction getTx() {
    return this.graph.newTransaction();
  }

  public void close() {
    if (null != graph) {
      try {
        graph.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void builderConfigByFile(String filePath) {
    InputStream file = null;
    try {
      file = (null == filePath) ? this.getClass().getResourceAsStream("/janusgarph.property")
          : new FileInputStream(filePath);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    Map<String, Object> properties = new HashMap<String, Object>();
    try {
      Properties p = new Properties();
      p.load(file);
      p.forEach((key, value) -> {
        properties.put((String) key, value);
        System.out.println(key + ":::" + value);
      });
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    builderConfigByProperties(properties);
  }

  private void builderConfigByProperties(Map<String, Object> properties) {
    config = JanusGraphFactory.build();
    if (null != properties) {
      properties.forEach((key, value) -> config.set(key, value));
    }
    graph = config.open();
  }
}
