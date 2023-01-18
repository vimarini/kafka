package desafio.processors;

import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;

import java.text.NumberFormat;
import java.text.ParseException;

public class ProcessorBasic implements Processor<String, String, Void, Void> {
    @Override
    public void init(ProcessorContext<Void, Void> context) {
    }

    @Override
    public void process(Record<String, String> record) {
        System.out.println("Processor API");
        System.out.println(record.value());
        try {
            System.out.println("Valor mais 1 = "+ (NumberFormat.getInstance().parse(record.value())).floatValue() + 1);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
    }
}
