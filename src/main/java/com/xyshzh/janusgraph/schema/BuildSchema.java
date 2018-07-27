package com.xyshzh.janusgraph.schema;

import java.io.FileReader;

import org.janusgraph.core.EdgeLabel;
import org.janusgraph.core.Multiplicity;
import org.janusgraph.core.PropertyKey;
import org.janusgraph.core.VertexLabel;
import org.janusgraph.core.schema.ConsistencyModifier;
import org.janusgraph.core.schema.EdgeLabelMaker;
import org.janusgraph.core.schema.JanusGraphIndex;
import org.janusgraph.core.schema.JanusGraphManagement;

import com.google.gson.GsonBuilder;
import com.xyshzh.janusgraph.schema.entity.IndexPropertyKey;
import com.xyshzh.janusgraph.schema.entity.Schema;
import com.xyshzh.janusgraph.schema.enuminfo.Mapping;
import com.xyshzh.janusgraph.task.Task;

/**
 * 创建Schema. // 初版,后续完善.
 * An external index cannot be unique.
 * @author Shengjun Liu
 * @version 2018-07-25
 *
 */
public class BuildSchema implements Task {
  public void execute(java.util.HashMap<String, String> options) {
    com.xyshzh.janusgraph.core.GraphFactory graphFactory = new com.xyshzh.janusgraph.core.GraphFactory(); // 创建图数据库连接
    JanusGraphManagement mgmt = graphFactory.getGraph().openManagement(); // 获取管理入口
    try {

      Schema schema = new GsonBuilder().disableHtmlEscaping().create().fromJson(new FileReader(options.get("file")),
          Schema.class);

      // 初始化属性
      System.out.println("开始初始化属性信息...");
      schema.getProps().forEach((p) -> {
        // 判断属性是否存在
        if (mgmt.containsPropertyKey(p.getName())) {
          PropertyKey hisPropertyKey = mgmt.getPropertyKey(p.getName());
          System.out.println("已存在  >> " + hisPropertyKey.name() + " :: " + hisPropertyKey.dataType());
          System.out.println("       >> " + p.getName() + " :: " + p.getType().getClazz());
        } else {
          mgmt.makePropertyKey(p.getName()).dataType(p.getType().getClazz()).make();
          System.out.println("已添加  >> " + p.getName() + " :: " + p.getType().getClazz());
        }
      });
      System.out.println("结束初始化属性信息...");

      System.out.println();

      // 初始化顶点类型
      System.out.println("开始初始化顶点类型信息...");
      schema.getVertices().forEach((v) -> {
        // 判断顶点类型是否存在
        if (mgmt.containsVertexLabel(v.getName())) {
          VertexLabel hisVertexLabel = mgmt.getVertexLabel(v.getName());
          System.out.println("已存在  >> " + hisVertexLabel.name());
          System.out.println("       >> " + v.getName());
        } else {
          mgmt.makeVertexLabel(v.getName()).make();
          System.out.println("已添加  >> " + v.getName());
        }
      });
      System.out.println("结束初始化顶点类型信息...");

      System.out.println();

      // 初始化关系类型
      System.out.println("开始初始化关系类型信息...");
      schema.getEdges().forEach((e) -> {
        // 判断关系类型是否存在
        if (mgmt.containsEdgeLabel(e.getName())) {
          EdgeLabel hisEdgeLabel = mgmt.getEdgeLabel(e.getName());
          System.out.println("已存在  >> " + hisEdgeLabel.name() + " :: " + hisEdgeLabel.multiplicity());
          System.out.println("       >> " + e.getName() + " :: " + e.getMultiplicity());
        } else {
          EdgeLabelMaker edgeLabelMaker = mgmt.makeEdgeLabel(e.getName());
          edgeLabelMaker.multiplicity((null == e.getMultiplicity()) ? Multiplicity.MULTI : e.getMultiplicity());
          PropertyKey propertyKey = ((null == e.getSignature() || "".equals(e.getSignature().trim())) ? null
              : mgmt.getPropertyKey(e.getSignature()));
          if (null != propertyKey)
            edgeLabelMaker.signature(propertyKey);
          edgeLabelMaker.make();
          System.out.println("已添加  >> " + e.getName());
        }
      });
      System.out.println("结束初始化关系类型信息...");

      System.out.println();

      // 初始化索引类型
      System.out.println("开始初始化索引类型信息...");
      schema.getIndexes().forEach((i) -> {
        // 判断关系类型是否存在
        if (mgmt.containsGraphIndex(i.getName())) {
          JanusGraphIndex hisGraphIndex = mgmt.getGraphIndex(i.getName());
          System.out.println(
              "已存在  >> " + hisGraphIndex.name() + " :: isUnique=" + hisGraphIndex.isUnique() + " :: isCompositeIndex="
                  + hisGraphIndex.isCompositeIndex() + " :: isMixedIndex=" + hisGraphIndex.isMixedIndex());
          System.out.println("       >> " + i.getName() + " :: isUnique=" + i.isUniqueIndex() + " :: isCompositeIndex="
              + i.isCompositeIndex() + " :: isMixedIndex=" + i.isMixedIndex());
        } else {
          JanusGraphManagement.IndexBuilder index = null;
          if (null == i.getType()) {
            System.out.println("未指定索引类型  >> " + i.getName() + " >> " + " 跳过本次索引内容创建. ");
            return;
          } else {
            index = mgmt.buildIndex(i.getName(), i.getType().getClazz());
          }
          // 处理索引字段
          for (IndexPropertyKey p : i.getProps()) {
            if (mgmt.containsPropertyKey(p.getName())) {
              PropertyKey key = mgmt.getPropertyKey(p.getName());
              if (null == p.getMapping() || Mapping.NULL == p.getMapping()) {
                index.addKey(key);
              } else {
                index.addKey(key, p.getMapping().getMapping().asParameter());
              }
            } else {
              System.out.println("未发现属性类型  >> " + p.getName() + " >> 索引" + i.getName() + "忽略此属性.");
            }
          }
          // 创建唯一性索引
          if (i.isUniqueIndex()) {
            index.unique();
          }
          // 创建复合索引
          if (i.isCompositeIndex()) {
            mgmt.setConsistency(index.buildCompositeIndex(),
                null == i.getConsistencyModifier() ? ConsistencyModifier.LOCK : i.getConsistencyModifier());
          }
          // 创建混合索引
          if (i.isMixedIndex()) {
            if (null == i.getMixedIndexName() || "".equals(i.getMixedIndexName().trim())) {
              System.out.println("未发现后端索引方式  >> " + i.getName() + " :: isMixedIndex=" + i.isMixedIndex()
                  + " :: MixedIndexName=" + i.getMixedIndexName() + " >> 跳过本次索引内容创建. ");
            } else {
              index.buildMixedIndex(i.getMixedIndexName());
            }
          } else {
            System.out
                .println("未发现索引方式  >> " + i.getName() + " :: isUnique=" + i.isUniqueIndex() + " :: isCompositeIndex="
                    + i.isCompositeIndex() + " :: isMixedIndex=" + i.isMixedIndex() + " >> 跳过本次索引内容创建. ");
          }
          System.out.println("已添加  >> " + i.getName());
        }
      });
      System.out.println("结束初始化索引类型信息...");

      mgmt.commit();

      System.out.println("\n\n\\^o^/字段创建完成\\^o^/\n\n");

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      graphFactory.close();
    }

  }
}
