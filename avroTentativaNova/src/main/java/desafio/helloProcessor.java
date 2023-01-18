package desafio;

import desafio.processors.ProcessorBasic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

import java.util.Properties;

public class helloProcessor {
    public static void main(String[] args) {
        Topology topology = new Topology();
        topology.addSource("Desafio","desafio-input");
        topology.addProcessor("Basic", ProcessorBasic::new, "Desafio");

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "dev2");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        KafkaStreams streams = new KafkaStreams(topology, config);
        System.out.println("Starting streams");
        streams.start();

    }
}
