package com.webc.service;

import com.opencsv.CSVWriter;
import com.webc.service.file.CsvFile;
import com.webc.service.httprequest.OkHttpRequest;
import com.webc.service.kafka.Producer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.apache.kafka.streams.KafkaStreams;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import com.webc.service.url.UrlFormatter;
@Service
public class
UrlStatusService {

    private String startUrl = "";
    private CSVWriter writer = null;
    private Map<String, String> statusCode = new HashMap<>();

    public void storePageData(String url, Map<String,String> response){

        statusCode.put(url, response.get("code"));
        String[] statusData = {url,response.get("code")};
        writer.writeNext(statusData);
    }


    public List<String> getPageLinks(String url) throws IOException {

        List<String> pageLinks = new ArrayList<>();

        if(!statusCode.containsKey(url)) {

            OkHttpRequest req = new OkHttpRequest();

            Map<String, String> response = req.get(url);

            storePageData(url, response);

            Document page = Jsoup.parseBodyFragment(response.get("body"));
            List<Element> links = page.getElementsByTag("a");

            links.stream()
                    .parallel()
                    .filter(link -> !link.attr("href").isEmpty())
                    .filter(link -> !link.attr("href").equals("/"))
                    .forEach(link -> {
                        pageLinks.add(UrlFormatter.merge(startUrl, link.attr("href")));
                    });

        }

        return pageLinks;

    }


    public void generateStatusCodes(String url) throws Exception {

        startUrl = UrlFormatter.generateBaseUrl(url);

        List<String> message = new ArrayList<>();
        message.add(startUrl);

        Producer.run(message);

        writer = CsvFile.create();

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-streams");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> inputStream = builder.stream("url-list");

        KStream<String, String> outputStream = inputStream.flatMapValues(value -> {
            try {
                return getPageLinks(value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        outputStream.to("url-list");

        KafkaStreams streams = new KafkaStreams(builder.build(), props);

        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    CsvFile.close(writer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }
}
