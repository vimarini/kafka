package serializer;

import com.example.Order;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Collections;
import java.util.Map;

public class AvroSerializer {
    public static Serde<Order> Order(String url, boolean isKey) {
        Map<String, String> serdeConfig = Collections.singletonMap("schema.registry.url", url);
        Serde<Order> serde = new Serde<Order>() {
            @Override
            public Serializer<Order> serializer() {
                return null;
            }

            @Override
            public Deserializer<Order> deserializer() {
                return null;
            }
        };
        serde.configure(serdeConfig, isKey);
        return serde;
    }
}
