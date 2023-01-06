import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;

import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Stream;

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
        KTable<String, Long> wordCounts = wordCountInput
                .mapValues(value -> value.toLowerCase())
                        .flatMapValues(value -> Arrays.asList(value.split(" ")))
                                .selectKey((key, value) -> value)
                                        .groupByKey()
                                                .count();
        wordCounts.toStream().to( "streams-wordcount-output");
        wordCounts.toStream().print(Printed.toSysOut());
        KafkaStreams streams = new KafkaStreams(builder.build(), properties);
        streams.start();
    }
}
