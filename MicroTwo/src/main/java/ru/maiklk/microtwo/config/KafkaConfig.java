package ru.maiklk.microtwo.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.maiklk.microtwo.dto.AbstractDto;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${kafka_server}")
    private String KAFKA_SERVER;

    @Bean
    public ProducerFactory<String, AbstractDto> producerFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        //Kafka сама решает когда создавать новые партиции, на основе ключа или нагрузки
        properties.put(ProducerConfig.PARTITIONER_ADPATIVE_PARTITIONING_ENABLE_CONFIG, true);
//        Можно реализовать Partitioner из пакета org.apache.kafka.clients.producer и каждая партиция
//        будет со своей логикой
//        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG)
//        по умолчанию ключ сообщения кафки является партицией
//        properties.put(ProducerConfig.PARTITIONER_IGNORE_KEYS_CONFIG)
        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean
    public KafkaTemplate<String, AbstractDto> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
