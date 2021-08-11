package Task2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class Main {
    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
        HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=EcvltSP05EPOgHw62w6uKv7PJ9OQgSs0TfI4EaMH");
        String urlFromData;
        String fileName;
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            NASAData data = mapper.readValue(response.getEntity().getContent(), new TypeReference<>() {});
            System.out.println(data);
            urlFromData = data.getUrl();
            fileName = urlFromData.substring(urlFromData.lastIndexOf("/") + 1);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (urlFromData.equals("")){
            return;
        }
        try (BufferedInputStream in = new BufferedInputStream(new URL(urlFromData).openStream());
             FileOutputStream fout = new FileOutputStream(fileName)) {
            final byte[] data = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
