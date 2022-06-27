package com.isec.bd.streaming.runnable;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import static com.isec.bd.streaming.model.Constants.KAFKA_CONSUMER_TOPIC;
import static com.isec.bd.streaming.model.Constants.KAFKA_SERVER;

public class KafkaConsumerRunnable implements Runnable {
    private final Consumer<String, String> consumer;

    public KafkaConsumerRunnable() {

        //Setup Kafka Client
        final Properties kafkaProps = new Properties();
        kafkaProps.put("bootstrap.servers", KAFKA_SERVER);

        // Configurations needed since there is no default
        kafkaProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        kafkaProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        kafkaProps.put(CommonClientConfigs.GROUP_ID_CONFIG, "test-consumer-group");

        // Create the consumer using properties
        consumer = new KafkaConsumer<>(kafkaProps);

        // Subscribe to the topic.
        consumer.subscribe(Collections.singletonList(KAFKA_CONSUMER_TOPIC));
    }

    @Override
    public void run() {
        String color;
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    if(record.value().contains("Good"))
                        color = "\u001B[32m";
                    else
                        color = "\u001B[31m";
                    System.out.println(color + "Kafka Stream : Feedback Event : " + record.value());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
