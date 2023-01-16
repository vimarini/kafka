package desafio;

import desafio.processors.SomaProcessor;
import desafio.processors.StringSomaProcessor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

import static desafio.DesafioStreams.isNumeric;

public class DesafioStreamsV3 {
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
        String topic1 = "desafio-output";
        String topic2 = "desafio-output2";
        String topic3 = "desafio-output3";


        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, String> numerosInput = streamsBuilder.stream(topic);
        KStream<String, String> removeDados = numerosInput.filter((key, value) -> isNumeric(value));

        SomaProcessor somaProcessorPlusOne = new SomaProcessor("1",topic1);
        SomaProcessor somaProcessorPlusTwo = new SomaProcessor("2",topic2);
//        SomaProcessor somaProcessorPlusThree = new SomaProcessor("3",topic3);
        somaProcessorPlusOne.process(removeDados);
        somaProcessorPlusTwo.process(removeDados);
//        somaProcessorPlusThree.process(removeDados);

        StringSomaProcessor stringSomaProcessor = new StringSomaProcessor("3",topic3);
        stringSomaProcessor.process(removeDados);


        KafkaStreams streams = new KafkaStreams(streamsBuilder.build(), properties);
        streams.start();
    }
}
