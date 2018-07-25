package com.xyshzh.janusgraph.schema.entity;

import java.io.Serializable;
import java.util.List;

import org.janusgraph.core.schema.ConsistencyModifier;

import com.xyshzh.janusgraph.schema.enuminfo.IndexType;

/**
 * schema中IndexKey实体信息.
 * @author Shengjun Liu
 * @version 2018-07-25
 *
 */
public class IndexKey implements Serializable {

  private static final long serialVersionUID = -6611572892836792876L;

  private String name;

  private IndexType type;

  // 组合索引创建事务类型.
  private ConsistencyModifier consistencyModifier = org.janusgraph.core.schema.ConsistencyModifier.DEFAULT;;

  private List<IndexPropertyKey> props;

  private Boolean uniqueIndex = false;

  private Boolean compositeIndex = false;

  private Boolean mixedIndex = false;

  private String mixedIndexName;

  public IndexKey() {}

  public IndexKey(String name, IndexType type, ConsistencyModifier consistencyModifier, List<IndexPropertyKey> props,
      Boolean uniqueIndex, Boolean compositeIndex, Boolean mixedIndex, String mixedIndexName) {
    super();
    this.name = name;
    this.type = type;
    this.consistencyModifier = consistencyModifier;
    this.props = props;
    this.uniqueIndex = uniqueIndex;
    this.compositeIndex = compositeIndex;
    this.mixedIndex = mixedIndex;
    this.mixedIndexName = mixedIndexName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public IndexType getType() {
    return type;
  }

  public void setType(IndexType type) {
    this.type = type;
  }

  public ConsistencyModifier getConsistencyModifier() {
    return consistencyModifier;
  }

  public void setConsistencyModifier(ConsistencyModifier consistencyModifier) {
    this.consistencyModifier = consistencyModifier;
  }

  public List<IndexPropertyKey> getProps() {
    return props;
  }

  public void setProps(List<IndexPropertyKey> props) {
    this.props = props;
  }

  public Boolean isUniqueIndex() {
    return uniqueIndex;
  }

  public void setUniqueIndex(Boolean uniqueIndex) {
    this.uniqueIndex = uniqueIndex;
  }

  public Boolean isCompositeIndex() {
    return compositeIndex;
  }

  public void setCompositeIndex(Boolean compositeIndex) {
    this.compositeIndex = compositeIndex;
  }

  public Boolean isMixedIndex() {
    return mixedIndex;
  }

  public void setMixedIndex(Boolean mixedIndex) {
    this.mixedIndex = mixedIndex;
  }

  public String getMixedIndexName() {
    return mixedIndexName;
  }

  public void setMixedIndexName(String mixedIndexName) {
    this.mixedIndexName = mixedIndexName;
  }

}
