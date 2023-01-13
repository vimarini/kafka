package desafio;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Arrays;
import java.util.Properties;

public class DesafioStreams {

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    public static void main(String[] args) {

        Properties properties = new Properties();
        // normal consumer
        properties.setProperty("bootstrap.servers","127.0.0.1:9092");
        properties.put("group.id", "customer-consumer-group-v1");
        properties.put("auto.commit.enable", "false");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        // streams
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "desafio");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        String topic = "desafio-input";

        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, String> numerosInput = streamsBuilder.stream(topic);
        KStream<String, String> removeDados = numerosInput.filter((key, value) -> isNumeric(value));
        KStream<String, String> numerosOutput = removeDados.flatMapValues(value -> Arrays.asList(String.valueOf(Integer.parseInt(value)+1),String.valueOf(Integer.parseInt(value)+2),String.valueOf(Integer.parseInt(value)+3)));
        numerosOutput.to("desafio-output", Produced.with(Serdes.String(),Serdes.String()));
        KafkaStreams streams = new KafkaStreams(streamsBuilder.build(), properties);
        streams.start();
    }
}
