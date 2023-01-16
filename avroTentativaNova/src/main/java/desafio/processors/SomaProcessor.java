package desafio.processors;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

import java.text.NumberFormat;
import java.text.ParseException;

public class SomaProcessor implements StreamProcessor {
    String parcela;
    String topic;

    public SomaProcessor(String parcela, String topic) {
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
        stream.mapValues((key,value)-> {
            try {
                return String.valueOf(NumberFormat.getInstance().parse(value).floatValue() + NumberFormat.getInstance().parse(this.getParcela()).floatValue());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }).to(this.getTopic(), Produced.with(Serdes.String(),Serdes.String()));
    }
}
