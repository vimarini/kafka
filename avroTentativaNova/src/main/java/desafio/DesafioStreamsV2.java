package desafio;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Produced;

import java.util.*;

import static desafio.DesafioStreams.isNumeric;
import static java.lang.String.valueOf;

public class DesafioStreamsV2 {
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

        KStream<String, String>[] branch = removeDados.branch(
                (key, value) -> Integer.parseInt(value) < 1,
                (key, value) -> Integer.parseInt(value) > 1,
                (key, value) -> Integer.parseInt(value) == 1
        );

        Map<String, KStream<String, String>> branchesMap = removeDados.split(Named.as("Branch-"))
                .branch((key, value) -> Integer.parseInt(value) > 0,  /* first predicate  */
                        Branched.as("A"))
                .branch((key, value) -> Integer.parseInt(value) >0,  /* second predicate */
                        Branched.as("B"))
                .branch((key, value) -> Integer.parseInt(value) >0,  /* second predicate */
                Branched.as("C"))
                .noDefaultBranch();

        branchesMap.get("Branch-A").peek((a,b)-> System.out.println("branchA - value = "+ b)).mapValues((key,value)->String.valueOf(Integer.parseInt(value)+1)).to(topic1, Produced.with(Serdes.String(),Serdes.String()));
        branchesMap.get("Branch-B").peek((a,b)-> System.out.println("branchB - value = "+ b)).mapValues((key,value)->String.valueOf(Integer.parseInt(value)+1)).to(topic2, Produced.with(Serdes.String(),Serdes.String()));
        branchesMap.get("Branch-C").peek((a,b)-> System.out.println("branchC - value = "+ b)).mapValues((key,value)->String.valueOf(Integer.parseInt(value)+1)).to(topic3, Produced.with(Serdes.String(),Serdes.String()));

//        List<String> dados = new ArrayList<>();
//        streamSomada.foreach(
//                (key,value)->dados.add(value)
//        );
//        for (String dado : dados) {
//            System.out.println(dado);
//        }
//        KStream<String, String>[] branch = removeDados.branch(
//                (key,value)-> dados.contains(String.valueOf(Integer.parseInt(value)+1)),
//                (key,value)-> dados.contains(String.valueOf(Integer.parseInt(value)+2)),
//                (key,value)-> dados.contains(String.valueOf(Integer.parseInt(value)+3))
//
//        );
//
//        branch[0].peek((a,b)-> System.out.println("branch1 - value = "+ b));
//        branch[1].peek((a,b)-> System.out.println("branch2 - value = "+ b));
//        branch[2].peek((a,b)-> System.out.println("branch3 - value = "+ b));
//        branch[0].to(topic1, Produced.with(Serdes.String(),Serdes.String()));
//        branch[1].to(topic2, Produced.with(Serdes.String(),Serdes.String()));
//        branch[2].to(topic3, Produced.with(Serdes.String(),Serdes.String()));

        KafkaStreams streams = new KafkaStreams(streamsBuilder.build(), properties);
        streams.start();
    }
}
