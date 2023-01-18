package desafio.processors;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.kafka.streams.kstream.KStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Predicates.equalTo;

public class DBProcessor implements StreamProcessor{
    @Override
    public void process(KStream<String, String> stream) {
        stream.peek((key,value)->{
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:3000/orders/");
        String json = "{ \"info\" : \""+ value+"\"}";
        StringEntity entity = null;
        try {
            entity = new StringEntity(json);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(json);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        try {
            client.execute(httpPost);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
        );
    }
}
