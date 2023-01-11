import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;

import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;

import static java.lang.ProcessBuilder.Redirect.to;

public class WordCount {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "word-count");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> wordCountInput = builder.stream("streams-file-input");
        KTable<String, String> wordCounts = wordCountInput
                .map((key, value) -> new KeyValue<>(UUID.randomUUID().toString(), value + "_outer"))
                .peek((value, a) -> System.out.println(value + " " + a)).toTable();

        wordCounts.toStream().to( "streams-wordcount-output", Produced.with(Serdes.String(), Serdes.String()));
        wordCounts.toStream().print(Printed.toSysOut());
        KafkaStreams streams = new KafkaStreams(builder.build(), properties);
        streams.start();
    }
}
