package desafio.processors;

import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;

public class ProcessorBasic implements Processor<String, String, Void, Void> {
    private ProcessorContext context;
    @Override
    public void init(ProcessorContext<Void, Void> context) {
        this.context=context;
    }

    @Override
    public void process(Record<String, String> record) {
        System.out.println(record.value());
        System.out.println("Valor mais 1 = "+ (Integer.parseInt(record.value())+1));
    }

    @Override
    public void close() {
    }
}
