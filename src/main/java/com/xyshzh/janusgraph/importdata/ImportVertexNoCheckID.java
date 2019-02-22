package com.xyshzh.janusgraph.importdata;

import com.xyshzh.janusgraph.datasource.read.Read;
import com.xyshzh.janusgraph.task.Task;

/**
 * 每类用于通过文本文档向JanusGraph导入数据.
 * 文档内容大体如下:
 * {"~id":1,"~label":"person","key1":"value1","key2":"value2","key3":"value3"}
 * 其中label为必须项,当setvertexid为true时,id也为必须项,反之为可选项,其他内容可自由添加(Schema中存在的)
 * 
 * @author Shengjun Liu
 * @version 2018-01-16
 *
 */
public class ImportVertexNoCheckID implements Task {

  public void execute(java.util.Map<String, String> options) {

    // 试图打开文件,文件使用结束后或出现异常后,在finally内关闭文件
    Read reader = new com.xyshzh.janusgraph.datasource.read.ReadFile(options.get("file").toString()).init();

    if (!reader.check()) { // 检测数据源
      System.out.println("文件异常,请重试.");
      System.exit(0);
    }

    java.util.concurrent.atomic.AtomicInteger total = new java.util.concurrent.atomic.AtomicInteger(0); // 计数器

    boolean checkvertex = Boolean.valueOf(options.getOrDefault("checkvertex", "false")); // 检查节点信息是否存在,初次导入可以忽略

    boolean setvertexid = Boolean.valueOf(options.getOrDefault("setvertexid", "false")); // 自定义id

    String[] keys = options.getOrDefault("keys", "").split(","); // 如果不自定义id,则通过这些字段判断节点信息是否存在

    try {
      com.xyshzh.janusgraph.core.GraphFactory graphFactory = new com.xyshzh.janusgraph.core.GraphFactory(
          options.containsKey("conf") ? options.get("conf") : null); // 创建图数据库连接
      System.out.println("graphFactory 初始化完成......");
      org.janusgraph.core.JanusGraphTransaction tx = graphFactory.getTx(); // 获取新事务,添加节点信息使用
      System.out.println("tx           初始化完成......");
      org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource g = graphFactory.getG(); // 获取遍历源,判断是否存在使用
      System.out.println("g            初始化完成......");

      String tempString = null; // 接收文件中每行数据,使用一变量,不需要重新生成新变量
      while ((tempString = reader.readLine()) != null) {
        total.addAndGet(1); // 每读一行,计数器加一
        if (tempString.length() > 20) {
          net.sf.json.JSONObject content = null;

          try { // 避免Json文件转换异常, 抛弃异常记录
            content = net.sf.json.JSONObject.fromObject(tempString);
          } catch (net.sf.json.JSONException e) {
            System.out.println("Current Position >> " + total.get() + " >> tempString :: " + tempString + " >> " + e.getMessage());
            continue;
          } finally {
            if (null == content) {
              System.out.println("Current Position >> " + total.get() + " >> tempString :: " + tempString + " >> Json文件转换异常");
              continue;
            }
          }
          Long id = null;
          if (setvertexid) { // 如果id内容为空或小于1,则忽略本条记录
            id = (content.containsKey("~id") ? content.getLong("~id") : (content.containsKey("id") ? content.getLong("id") : null)); // 获取id信息
            if ((null == id || 1 > id)) {
              System.out.println("Current Position >> " + total.get() + " >> id :: " + id + " >> ignore.");
              continue;
            }
          }

          String label = (content.containsKey("~label") ? content.getString("~label")
              : (content.containsKey("label") ? content.getString("label") : null)); // 获取label信息
          if (null == label || "".equals(label.trim())) { // 如果label内容为空,则忽略本条记录
            System.out.println("Current Position >> " + total.get() + " >> label :: " + label + " >> ignore.");
            continue;
          }

          /**
           * 当checvertex==true时,判断节点信息.
           * 判断节点信息的方式,取决于setvertexid的取值方式.
           * 当setvertexid==true时,可通过id进行判断,反之只能通过判断字段进行判断.
           * 逻辑如下:
           * checvertex--------false------>跳出判断.
           *    |
           *   true
           *    |
           * setvertexid------true----->通过id检查节点信息是否存在
           *    |
           *   false
           *    |
           * 通过判定条件检查
           */
          if (checkvertex && setvertexid && g.V(id).hasNext()) { // 检查节点信息是否存在,通过id检查
            System.out.println("Current Position >> " + total.get() + " >> id :: " + id + " >> existed.");
            continue;
          } else if (checkvertex && !setvertexid) { // 检查节点信息是否存在,通过判定条件检查
            java.util.HashMap<String, Object> kvs = new java.util.HashMap<String, Object>(); // 临时判定条件
            for (String key : keys) { // 判断字段集合
              if (content.containsKey(key)) { // 判断字段是否存在,存在则参与计算
                kvs.put(key.toString(), content.get(key));
              }
            }
            if (has(g, kvs)) { // 节点信息存在
              System.out.println("Current Position >> " + total.get() + " >> keys :: " + kvs.toString() + " >> existed.");
              continue;
            }
          } else {
            ;
          }
          org.janusgraph.core.JanusGraphVertex v = null;

          try {
            if (setvertexid) { // 生成新Vertex,并指定label,id
              v = tx.addVertex(org.apache.tinkerpop.gremlin.structure.T.label, label, org.apache.tinkerpop.gremlin.structure.T.id, id);
            } else { // 生成新Vertex,并指定label
              v = tx.addVertex(org.apache.tinkerpop.gremlin.structure.T.label, label);
            }
          } catch (java.lang.IllegalArgumentException e) {
            System.out.println("Current Position >> " + total.get() + " >> tempString :: " + tempString + " >> " + e.getMessage());
            System.out.println("请检查JanusGraph配置项: graph.set-vertex-id 是否正常配置. 此值在初始化backend即以成定局, 后期修改均无效.");
            System.out.println("请选择合适的校验方式.");
            System.exit(1);
          }

          if (null == v) { // 生成新Vertex异常
            System.out.println("Current Position >> " + total.get() + " >> tempString :: " + tempString + " >> 生成新Vertex异常");
            continue;
          }
          // 将数据中的其他字段添加到Vertex
          for (Object key : content.keySet()) {
            if (key.equals("~id") || key.equals("~label") || key.equals("id") || key.equals("label")) { // 忽略~id & ~label & id & label
              continue;
            }
            v.property(key.toString(), content.get(key));
          }
          // 分批次提交,每次提交事务都会关闭,提交后需要重新创建事务
          if (total.get() % 1000 == 0) {
            try {
              tx.commit();
            } catch (java.lang.Exception e) {
              e.printStackTrace();
            } finally {
              tx.close();
            }
            tx = graphFactory.getTx();
            System.out.println("Current Position >> " + total.get());
          }
        }
      }
      try {
        tx.commit();
      } catch (java.lang.Exception e) {
        e.printStackTrace();
      } finally {
        tx.close();
      }
      graphFactory.close();
      System.out.println("Current Position >> " + total.get() + ", over!");
    } finally {
      reader.close(); // 执行最后,是否异常,皆关闭文件
    }
    System.out.println("I'm Main Thread, I'm Over!");
  }

  /**
   * 判断节点是否存在.
   * 
   * @param g
   *          A graph traversal source.
   * @param kvs
   *          keyValues key-value pairs of properties to check the vertex.
   * @return <tt>true</tt> if this GraphTraversalSource contains the specified
   *         vertex.
   */
  public static boolean has(org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource g,
      java.util.HashMap<String, Object> kvs) {
    org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal<org.apache.tinkerpop.gremlin.structure.Vertex, org.apache.tinkerpop.gremlin.structure.Vertex> r = null;
    r = g.V();
    for (java.util.Map.Entry<String, Object> kv : kvs.entrySet()) {
      r = r.has(kv.getKey(), kv.getValue());
    }
    return (null == r) ? false : r.hasNext();
  }

}