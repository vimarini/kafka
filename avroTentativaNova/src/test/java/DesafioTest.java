import desafio.processors.SomaProcessor;
import desafio.processors.StringSomaProcessor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.errors.StreamsException;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.state.KeyValueStore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static desafio.DesafioStreams.isNumeric;
import static junit.framework.TestCase.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class DesafioTest {
    private TopologyTestDriver testDriver;
    private TestInputTopic<String, String> inputTopic;
    private TestOutputTopic<String, String> outputTopic;
    private TestOutputTopic<String, String> outputTopic2;
    private KeyValueStore<String, Long> store;
    private Serde<String> stringSerde = new Serdes.StringSerde();
    private String parcela = "1";


    @Before
    public void setup(){
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

        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream <String,String> stream = streamsBuilder.stream("desafio-input", Consumed.with(Serdes.String(),Serdes.String()).withName("somaProcessor").withOffsetResetPolicy(Topology.AutoOffsetReset.LATEST)).filter((key,value)->isNumeric(value));

        SomaProcessor somaProcessorPlusOne = new SomaProcessor(parcela,"desafio-output");
        somaProcessorPlusOne.process(stream);

        StringSomaProcessor stringSomaProcessor = new StringSomaProcessor(parcela,"desafio-output3");
        stringSomaProcessor.process(stream);

        testDriver = new TopologyTestDriver(streamsBuilder.build(),properties);
        inputTopic = testDriver.createInputTopic("desafio-input", stringSerde.serializer(),stringSerde.serializer());
        outputTopic = testDriver.createOutputTopic("desafio-output", stringSerde.deserializer(), stringSerde.deserializer());
        outputTopic2 = testDriver.createOutputTopic("desafio-output3", stringSerde.deserializer(), stringSerde.deserializer());
    }

    @After
    public void tearDown() {
        testDriver.close();
    }

    @Test
    public void shouldAddPositiveValues(){
        inputTopic.pipeInput(parcela);
        assertThat(outputTopic.readValue(),equalTo("2.0"));
        assertThat(outputTopic2.readValue(),equalTo("11"));
    }

    @Test
    public void shouldAddNegativeValues(){
        inputTopic.pipeInput("-1");
        assertThat(outputTopic.readValue(),equalTo("0.0"));
        assertThat(outputTopic2.readValue(),equalTo("-11"));
    }

    @Test
    public void shouldNotThrowExceptionWhenAddNonNumericValue(){
        inputTopic.pipeInput("ANBS");
        assertTrue(outputTopic.isEmpty());
    }

}
