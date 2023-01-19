package orderv2.serdes;

import orderv2.model.Order;
import orderv2.model.OrderKey;
import orderv2.model.OrderPattern;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

public class JsonSerdes {

    public static OrderWrapSerde Order(){
        return new OrderWrapSerde(new JsonSerialization<>(),new JsonDeserialization<>(Order.class));
    }
    public final static class OrderWrapSerde extends WrapSerde<Order>{
        private OrderWrapSerde(Serializer<Order> serializer, Deserializer<Order> deserializer) {
            super(serializer, deserializer);
        }

    }
    public static OrderKeyWrapSerde OrderKey(){
        return new OrderKeyWrapSerde(new JsonSerialization<>(),new JsonDeserialization<>(OrderKey.class));
    }
    public final static class OrderKeyWrapSerde extends WrapSerde<OrderKey>{
        private OrderKeyWrapSerde(Serializer<OrderKey> serializer, Deserializer<OrderKey> deserializer) {
            super(serializer, deserializer);
        }

    }
    public static OrderPatternWrapSerde OrderPattern(){
        return new OrderPatternWrapSerde(new JsonSerialization<>(), new JsonDeserialization<>(OrderPattern.class));
    }

    public final static class OrderPatternWrapSerde extends WrapSerde<OrderPattern>{
        private OrderPatternWrapSerde(Serializer<OrderPattern> serializer, Deserializer<OrderPattern> deserializer) {
            super(serializer, deserializer);
        }
    }
    private static class WrapSerde<T> implements Serde<T> {
        private final Serializer<T> serializer;
        private final Deserializer<T> deserializer;

        private WrapSerde(Serializer<T> serializer, Deserializer<T> deserializer){
            this.serializer = serializer;
            this.deserializer = deserializer;
        }


        @Override
        public Serializer<T> serializer() {
            return serializer;
        }

        @Override
        public Deserializer<T> deserializer() {
            return deserializer;
        }
    }
}
