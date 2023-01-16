package desafio.processors;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

public class StringSomaProcessor implements StreamProcessor {
    String parcela;
    String topic;

    public StringSomaProcessor(String parcela, String topic) {
        this.parcela = parcela;
        this.topic = topic;
    }

    public String getParcela() {
        return parcela;
    }

    public String getTopic() {
        return topic;
    }

    @Override
    public void process(KStream<String, String> stream) {
        stream.mapValues((key,value)-> value + this.getParcela()).to(this.getTopic(), Produced.with(Serdes.String(),Serdes.String()));
    }
}
