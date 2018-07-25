package com.xyshzh.janusgraph.schema.entity;

import java.io.Serializable;

import com.xyshzh.janusgraph.schema.enuminfo.Mapping;

/**
 * schema中IndexPropertyKey实体信息.
 * @author Shengjun Liu
 * @version 2018-07-25
 *
 */
public class IndexPropertyKey implements Serializable {

  private static final long serialVersionUID = 8944255584746876120L;

  private String name;

  private Mapping mapping;

  public IndexPropertyKey() {}

  public IndexPropertyKey(String name) {
    this.name = name;
  }

  public IndexPropertyKey(String name, Mapping mapping) {
    this.name = name;
    this.mapping = mapping;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Mapping getMapping() {
    return mapping;
  }

  public void setMapping(Mapping mapping) {
    this.mapping = mapping;
  }

}
