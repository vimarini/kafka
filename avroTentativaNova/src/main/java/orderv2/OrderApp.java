package orderv2;

import orderv2.model.Order;
import orderv2.model.OrderKey;
import orderv2.model.OrderPattern;
import orderv2.serdes.JsonSerdes;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class OrderApp {
    private final static Logger LOG = LoggerFactory.getLogger(OrderApp.class);
    private final static String APP_ID = "orde_app";
    private final static String BOOTSTRAP_SERVER = "localhost:9092";
    private final static String ORDER_TRANSACTION = "order.transaction";
    private final static String ORDER_PATTERN = "order.pattern";
    private final static String ORDER_PURCHASED = "order.purchased";

//{"customerName":"Pedro","customerId":"2","paymentMethod":"credit","itemPurchased":"camisa","amount":20,"purchaseDate":"2023-01-18T16:59:32.141535","cep":"31555050"}

    public static void main(String[] args) {
        Properties conf = new Properties();
        conf.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        conf.put(StreamsConfig.APPLICATION_ID_CONFIG, APP_ID);
        StreamsBuilder builder = new StreamsBuilder();
        //1. consume topic `order.transaction`
        KStream<String, Order> ks0 = builder.stream(ORDER_TRANSACTION, Consumed.with(Serdes.String(), JsonSerdes.Order())
                .withName("transaction-source")
                .withOffsetResetPolicy(Topology.AutoOffsetReset.LATEST));
        //2. Extrat pattern
        ks0.mapValues(v -> OrderPattern.builder(v).build(), Named.as("order-patern"))
                .to(ORDER_PATTERN, Produced.with(Serdes.String(), JsonSerdes.OrderPattern()));
        //3
        ks0.selectKey((k,v)->new OrderKey(v.getCustomerId(),v.getPurchaseDate()))
                .to(ORDER_PURCHASED,Produced.with(JsonSerdes.OrderKey(), JsonSerdes.Order()));
        //4
        ks0.foreach((k,v)->LOG.info("Simulate located the transaction to data lake"));
        Topology topology = builder.build();
        KafkaStreams kafkaStreams = new KafkaStreams(topology, conf);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            kafkaStreams.close();
            LOG.info("The kafka streams application is closed.");
        }));
        kafkaStreams.start();
        LOG.info("The kafka streams application start...");
    }
}
