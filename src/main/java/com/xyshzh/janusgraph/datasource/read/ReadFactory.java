package com.xyshzh.janusgraph.datasource.read;

import java.util.Map;

public class ReadFactory {

  public static final Read getRead(Map<String, Object> options) {
    String type = String.class.cast(options.getOrDefault("type", "NULL"));
    switch (type) {
    case "file":
      return new ReadFile(options);
    case "kafka":
      return new ReadKafka(options);
    case "jdbc":
      return new ReadJdbc(options);
    default:
      return null;
    }
  }

}
