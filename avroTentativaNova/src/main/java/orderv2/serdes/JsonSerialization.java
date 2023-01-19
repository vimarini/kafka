package orderv2.serdes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonSerialization<T> implements Serializer<T> {
    final static ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    @Override
    public byte[] serialize(String topic, T data) {
        try {
            return MAPPER.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
