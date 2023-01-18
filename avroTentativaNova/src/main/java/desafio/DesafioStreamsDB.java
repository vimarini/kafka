package desafio;

import desafio.processors.DBProcessor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;

import java.io.IOException;
import java.util.Properties;

public class DesafioStreamsDB {
    public static void main(String[] args) throws IOException {

        Properties properties = new Properties();
        // normal consumer
        properties.setProperty("bootstrap.servers","127.0.0.1:9092");
        properties.put("group.id", "customer-consumer-group-v1");
        properties.put("auto.commit.enable", "false");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest");
        // streams
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "orders-db");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        String topic = "db-input";


        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, String> orders = streamsBuilder.stream(topic);
        orders.print(Printed.toSysOut());
        DBProcessor dbProcessor = new DBProcessor();
        dbProcessor.process(orders);


        KafkaStreams streams = new KafkaStreams(streamsBuilder.build(), properties);
        streams.start();
    }
}
