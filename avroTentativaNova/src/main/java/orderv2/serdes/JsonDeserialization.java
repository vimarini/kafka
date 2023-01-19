package orderv2.serdes;

import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class JsonDeserialization<T> implements Deserializer<T> {
    private Class<T> deserializeClass;
    public JsonDeserialization(Class<T> deserializeClass) {
        this.deserializeClass=deserializeClass;
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        try {
            return JsonSerialization.MAPPER.readValue(data,deserializeClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
