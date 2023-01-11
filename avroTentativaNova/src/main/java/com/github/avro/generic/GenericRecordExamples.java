package com.github.avro.generic;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.*;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;

import java.io.File;
import java.io.IOException;

public class GenericRecordExamples {
    public static void main(String[] args) {
        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse(
                "{\"type\" : \"record\",\n" +
                        "\"namespace\" : \"com.example\",\n" +
                        "\"name\" : \"Order\",\n" +
                        "\"doc\" : \"Orders\",\n" +
                        "\"fields\" : [\n" +
                        "    {\"name\":\"id\",\"type\": \"int\"},\n" +
                        "    {\"name\":\"info\", \"type\":\"string\"}\n" +
                        "    ]}");
        GenericRecordBuilder orderBuilder = new GenericRecordBuilder(schema);
        orderBuilder.set("id", 2);
        orderBuilder.set("info", "calça");
        GenericData.Record order = orderBuilder.build();
        System.out.println(order);

        final DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);
        try (DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<>(datumWriter)) {
            dataFileWriter.create(order.getSchema(), new File("customer-generic.avro"));
            dataFileWriter.append(order);
            System.out.println("Written customer-generic.avro");
            dataFileWriter.close();
        } catch (IOException e) {
            System.out.println("Couldn't write file");
            e.printStackTrace();
        }

        // reading from a file
        final File file = new File("customer-generic.avro");
        final DatumReader<GenericRecord> datumReader = new GenericDatumReader<>();
        GenericRecord customerRead;
        try (DataFileReader<GenericRecord> dataFileReader = new DataFileReader<>(file, datumReader)){
            customerRead = dataFileReader.next();
            System.out.println("Successfully read avro file");
            System.out.println(customerRead.toString());

            // get the data from the generic record
            System.out.println("Info: " + customerRead.get("info"));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
