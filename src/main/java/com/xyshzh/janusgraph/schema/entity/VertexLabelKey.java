package com.xyshzh.janusgraph.schema.entity;

import java.io.Serializable;

/**
 * schema中VertexLabelKey实体信息.
 * @author Shengjun Liu
 * @version 2018-07-25
 *
 */
public class VertexLabelKey implements Serializable {

  private static final long serialVersionUID = 3755222377192664944L;

  private String name;

  public VertexLabelKey() {}

  public VertexLabelKey(String name) {
    super();
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
