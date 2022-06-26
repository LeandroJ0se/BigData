package com.isec.bd.streaming;

import com.isec.bd.streaming.runnable.KafkaConsumerRunnable;
import com.isec.bd.streaming.runnable.KafkaProducerRunnable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StreamingApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamingApplication.class, args);

        System.out.println("******* Starting Kafka Consumer... *******");
        Thread kafkaConsumer = new Thread(new KafkaConsumerRunnable());
        kafkaConsumer.start();

        System.out.println("******* Starting Kafka Producer... *******");
        Thread kafkaProducer = new Thread(new KafkaProducerRunnable());
        kafkaProducer.start();
    }
}
