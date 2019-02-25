package com.xyshzh.janusgraph.arguments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 传入参数初始化为Map.
 * @author Shengjun Liu
 * @version 2019-02-22
 */
public class Arguments2Map {

  /** 参数前缀符 */
  private final static String prefix = "--";
  /** 参数Key-Value分隔符 */
  private final static String kv_split = "=";
  /** 参数Value多值分隔符 */
  private final static String value_split = ",";
  /** 参数Value多值Key-Value分隔符 */
  private final static String value_kv_split = ":";

  /** 参数信息 */
  private final Map<String, Object> options = new HashMap<>();

  /** 错误信息 */
  private final Set<String> error = new HashSet<>();

  public Arguments2Map() {}

  public Arguments2Map(String[] args) {
    this.reset(args);
    this.print();
  }

  /**
   * 重新初始化参数.
   * @param args
   */
  public void reset(String[] args) {
    options.clear();
    // 迭代每一个参数
    for (int index = 0; null != args && index < args.length; index++) {
      // 判断是否以prefix开始
      if (args[index].trim().startsWith(prefix)) {
        // 分割Key-Value
        String[] kv = args[index].trim().substring(2).split(kv_split);
        if (1 == kv.length) {
          options.put(kv[0].trim(), "true");
        } else if (2 == kv.length) {
          String[] vs = kv[1].trim().split(value_split);
          // 当v为单值且不可分割时
          if (1 == vs.length && 1 == vs[0].split(value_kv_split).length) {
            options.put(kv[0].trim(), kv[1].trim());
          } else {
            Collection<String> list = new ArrayList<>();
            Map<String, String> map = new HashMap<>();
            for (int j = 0; j < vs.length; j++) {
              String[] vkv = vs[j].trim().split(value_kv_split);
              if (1 == vkv.length) list.add(vkv[0].trim());
              else if (2 == vkv.length) map.put(vkv[0].trim(), vkv[1].trim());
              else error.add("参数 < " + args[index] + " > Value_Key-Value分隔无效.");
            }
            if ((0 == list.size() && 0 == map.size()) || (0 != list.size() && 0 != map.size()))
              error.add("参数 < " + args[index] + " > Value_Key-Value分隔无效.");
            else if (0 != list.size()) options.put(kv[0].trim(), list);
            else options.put(kv[0].trim(), map);
          }
        } else {
          error.add("参数 < " + args[index] + " > Key-Value分隔无效.");
        }
      } else {
        error.add("参数 < " + args[index] + " > 前缀无效.");
      }
    }
  }

  /**
   * 获取处理结果.
   * @return
   */
  public Map<String, Object> getOptions() {
    return this.options;
  }

  /**
   * 获取处理状态.
   * @return
   */
  public boolean check() {
    return 0 == this.error.size();
  }

  /**
   * 获取错误信息.
   * @return
   */
  public Set<String> getError() {
    return this.error;
  }

  /**
   * 打印错误信息.
   */
  public void printError() {
    this.error.forEach(e -> System.out.println(e));
  }

  /**
   * 打印规则.
   */
  public void printRole() {
    System.out.println("参数要求:");
    System.out.println("任一参数均以'" + prefix + "'开始, 相邻像个参数之间以空格(' ')间隔. ");
    System.out.println("参数的KV分割以'" + kv_split + "'间隔. ");
    System.out.println("当参数值为基本数据类型集合是, 值与值之间通过'" + value_split + "'间隔.");
    System.out.println("当参数值为基本KV类型集合是, 值与值之间通过'" + value_kv_split + "'间隔, KV对之间通过'" + value_split + "'间隔.");
    System.out.println("--------------------------------------------------\n");
  }

  /**
   * 打印.
   */
  public void print() {
    options.forEach((k, v) -> System.out.println(k + " : " + v));
    System.out.println("--------------------------------------------------\n");
  }

}
