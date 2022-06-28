package com.isec.bd.streaming.runnable;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.isec.bd.streaming.model.HotelEntry;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.stream.Collectors;

import static com.isec.bd.streaming.model.Constants.*;

public class KafkaProducerRunnable implements Runnable {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final CsvMapper csvMapper = new CsvMapper();

    private final Producer<String, String> producer;

    public KafkaProducerRunnable() {

        //Setup Kafka Client
        final Properties kafkaProps = new Properties();
        kafkaProps.put("bootstrap.servers", KAFKA_SERVER);

        // Configurations needed since there is no default
        kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // Create the producer using properties
        producer = new KafkaProducer<>(kafkaProps);
    }

    @Override
    public void run() {

        try {
            final Path path = Paths.get(FILE_PATH);
            final BufferedReader br = Files.newBufferedReader(path, StandardCharsets.US_ASCII);

            final String lines = br.lines().collect(Collectors.joining("\n"));

            final CsvSchema csvSchema = csvMapper
                    .typedSchemaFor(HotelEntry.class)
                    .withHeader() // If needed for headers
                    .withColumnSeparator(';');

            final MappingIterator<HotelEntry> it = csvMapper
                    .readerWithTypedSchemaFor(HotelEntry.class)
                    .with(csvSchema)
                    .readValues(lines);

            while (it.hasNextValue()) {
                final ProducerRecord<String, String> entry = new ProducerRecord<>(
                        KAFKA_PRODUCER_TOPIC,
                        "message",
                        mapper.writeValueAsString(it.nextValue())
                );

                producer.send(entry).get(); // to get more info 'RecordMetadata rmd = '
                System.out.println("\u001B[35m" + "Kafka Stream : Sending Event  : " + entry.value());

                //Sleep for 3 secs before the next row.
                Thread.sleep(10000);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
