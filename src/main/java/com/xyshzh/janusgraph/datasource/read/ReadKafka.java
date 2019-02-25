package com.xyshzh.janusgraph.datasource.read;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.gl.kafka.utils.KafkaConsumerUtils;

/** 数据源通用读取接口,数据源为Kafka.
 * @author Shengjun Liu
 * @version 2018-07-20 */
public class ReadKafka implements Read {

  private static final long serialVersionUID = -3033449241202336811L;

  /**
   * KafkaConsumer 实例.
   */
  private KafkaConsumerUtils consumer = null;

  public ReadKafka() {
    consumer = new KafkaConsumerUtils("192.168.0.66:9092", "insertdata", "192.168.0.66:2181/kafka", Arrays.asList("insertdata"), null);
  }

  public ReadKafka(Map<String, Object> options) {
    List<String> topics = new ArrayList<>();
    Object _topics = options.getOrDefault("topics", "test");
    if (_topics instanceof String) {
      topics.add(String.class.cast(_topics));
    } else if (_topics instanceof Collection) {
      topics.addAll(ArrayList.class.cast(_topics));
    } else {
      topics.add("test");
    }
    consumer = new KafkaConsumerUtils(String.class.cast(options.getOrDefault("kafkaIp", "127.0.0.1:9092")),
        String.class.cast(options.getOrDefault("groupId", String.valueOf(System.currentTimeMillis()))),
        String.class.cast(options.getOrDefault("zkHosts", "127.0.0.1:2181")), topics,
        String.class.cast(options.getOrDefault("offset", null)));
  }

  public ReadKafka(String kafkaIp, String groupId, String zkHosts, List<String> topics, String offset) {
    consumer = new KafkaConsumerUtils(kafkaIp, groupId, zkHosts, topics, offset);
  }

  @Override
  public synchronized Read init() {
    consumer.init();
    return this;
  }

  @Override
  public synchronized String readLine() {
    String data = consumer.getKafkaData();
    if (null == data) {
      try {
        Thread.sleep(1000L);
        data = consumer.getKafkaData();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    return data;
  }

  @Override
  @SuppressWarnings("unchecked")
  public synchronized Map<Integer, Long> commitOffset() {
    return consumer.getOffset();
  }

  @Override
  public synchronized void close() {
    consumer.closeKafkaConsumer();
  }

  @Override
  public synchronized boolean check() {
    return null != consumer;
  }

}
