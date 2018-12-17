package com.xyshzh.janusgraph.schema.entity;

import java.io.Serializable;

import org.janusgraph.core.Multiplicity;

/**
 * schema中EdgeLabelKey实体信息.
 * @author Shengjun Liu
 * @version 2018-07-25
 *
 */
public class EdgeLabelKey implements Serializable {

  private static final long serialVersionUID = -4145200612421215610L;

  private String name;
  private Multiplicity multiplicity;
  private String signature;
  private String description;

  public EdgeLabelKey() {}

  public EdgeLabelKey(String name, Multiplicity multiplicity, String signature) {
    this.name = name;
    this.multiplicity = multiplicity;
    this.signature = signature;
  }

  public EdgeLabelKey(String name, Multiplicity multiplicity, String signature, String description) {
    this.name = name;
    this.multiplicity = multiplicity;
    this.signature = signature;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Multiplicity getMultiplicity() {
    return multiplicity;
  }

  public void setMultiplicity(Multiplicity multiplicity) {
    this.multiplicity = multiplicity;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
