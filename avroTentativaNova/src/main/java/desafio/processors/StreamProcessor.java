package desafio.processors;

import org.apache.kafka.streams.kstream.KStream;

public interface StreamProcessor {
    public void process(KStream<String, String> stream);
}
