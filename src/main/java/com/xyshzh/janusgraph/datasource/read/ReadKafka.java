package com.xyshzh.janusgraph.datasource.read;

import java.util.Arrays;
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
