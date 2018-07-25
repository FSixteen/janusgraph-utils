package com.xyshzh.janusgraph.schema.entity;

import java.io.Serializable;

import com.xyshzh.janusgraph.schema.enuminfo.DataType;

/**
 * schema中PropertyKey实体信息.
 * @author Shengjun Liu
 * @version 2018-07-25
 *
 */
public class PropertyKey implements Serializable {

  private static final long serialVersionUID = 8944255584746876120L;

  private String name;

  private DataType type;

  public PropertyKey() {}

  public PropertyKey(String name, DataType type) {
    this.name = name;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DataType getType() {
    return type;
  }

  public void setType(DataType type) {
    this.type = type;
  }

}
