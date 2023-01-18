package desafio.processors;

import org.apache.kafka.streams.kstream.KStream;

import java.io.IOException;

public interface StreamProcessor {
    public void process(KStream<String, String> stream) throws IOException;
}
