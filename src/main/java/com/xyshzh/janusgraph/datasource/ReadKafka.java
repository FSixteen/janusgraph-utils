package com.xyshzh.janusgraph.datasource;

import java.util.Arrays;
import java.util.List;

import com.gl.kafka.utils.KafkaConsumerUtils;

/** 数据源为Kafka.
 * @author Shengjun Liu
 * @version 2018-07-20 */
public class ReadKafka implements Read, java.io.Serializable {

  public static void main(String[] args) {
    ReadKafka r = new ReadKafka();
    r.init();
    for (int i = 0; i < 10000; i++) {
      System.out.println(r.readLine());
    }
    r.close();
  }

  private static final long serialVersionUID = -3033449241202336811L;

  private KafkaConsumerUtils consumer = null;

  public ReadKafka() {
    consumer = new KafkaConsumerUtils("192.168.0.66:9092", "insertdata", "192.168.0.66:2181/kafka",
        Arrays.asList("insertdata"), null);
  }

  public ReadKafka(String kafkaIp, String groupId, String zkHosts, List<String> topics, String offset) {
    consumer = new KafkaConsumerUtils(kafkaIp, groupId, zkHosts, topics, offset);
  }

  @Override
  public synchronized void init() {
    consumer.init();
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
  public synchronized void commitOffset() {}

  @Override
  public synchronized void close() {
    consumer.closeKafkaConsumer();
  }

  @Override
  public synchronized boolean check() {
    return true;
  }

}
