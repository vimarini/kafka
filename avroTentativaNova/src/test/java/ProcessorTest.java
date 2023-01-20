import desafio.processors.ProcessorBasic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class ProcessorTest {
    private TopologyTestDriver testDriver;
    private TestInputTopic<String, String> inputTopic;
    private TestOutputTopic<String, String> outputTopic;
    private Serde<String> stringSerde = new Serdes.StringSerde();
    private String parcela = "1";

    @Before
    public void setup(){
        final Topology topology = new Topology();
        topology.addSource("processor-hello","hello-input");
        topology.addProcessor("Basic", ProcessorBasic::new, "processor-hello");
        topology.addSink("processor-sink","hello-output","processor-hello");

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

        testDriver = new TopologyTestDriver(topology, properties);
        inputTopic = testDriver.createInputTopic("hello-input", stringSerde.serializer(), stringSerde.serializer());
        outputTopic = testDriver.createOutputTopic("hello-output", stringSerde.deserializer(), stringSerde.deserializer());

    }

    @After
    public void tearDown() {
        testDriver.close();
    }

    @Test
    public void test(){
        inputTopic.pipeInput("1");
        assertThat(outputTopic.readValue(),equalTo("1"));
    }

}
