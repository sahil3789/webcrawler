package com.webc.service.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
public class Producer {

    private final static String TOPIC = "url-list";
    private final static String BOOTSTRAP_SERVERS =
            "localhost:9092";

    private static final Logger log = Logger.getLogger(Producer.class.getName());

    private static org.apache.kafka.clients.producer.Producer<Long, String> create() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }


    public static void run(List<String> links) throws Exception {

        final org.apache.kafka.clients.producer.Producer<Long, String> producer = create();
        long time = System.currentTimeMillis();

        try {
            for (long index = 0; index < links.size(); index++) {
                final ProducerRecord<Long, String> record =
                        new ProducerRecord<>(TOPIC, index,
                                links.get((int) index));


                RecordMetadata metadata = producer.send(record).get();
                long elapsedTime = System.currentTimeMillis() - time;
                System.out.printf("sent record(key=%s value=%s) " +
                                "meta(partition=%d, offset=%d) time=%d\n",
                        record.key(), record.value(), metadata.partition(),
                        metadata.offset(), elapsedTime);

            }
        } finally {
            producer.flush();
            producer.close();
        }
    }
}
