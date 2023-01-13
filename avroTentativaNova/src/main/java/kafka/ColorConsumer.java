package kafka;

import com.example.Color;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;

public class ColorConsumer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        // normal consumer
        properties.setProperty("bootstrap.servers","127.0.0.1:9092");
        properties.put("group.id", "customer-consumer-group-v1");
        properties.put("auto.commit.enable", "false");
        properties.put("auto.offset.reset", "earliest");

        // avro part (deserializer)
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", KafkaAvroDeserializer.class.getName());
        properties.setProperty("schema.registry.url", "http://127.0.0.1:8081");
        properties.setProperty("specific.avro.reader", "true");

        KafkaConsumer<String, Color> consumer = new KafkaConsumer<String, Color>(properties);
        String topic = "color-avro";
        consumer.subscribe(Collections.singleton(topic));
        System.out.println("Waiting for data in "+topic+" ...");

        while(true) {
            ConsumerRecords<String, Color> records = consumer.poll(500);
            for (ConsumerRecord<String, Color> record : records) {
                Color color = record.value();
                System.out.println(color);
            }
        }
    }
}
