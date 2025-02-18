package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {

    @KafkaListener(topics = "test-topic", groupId = "consumer-group-1")
    public void consumeMessage(ConsumerRecord<String, String> record) {
        log.info("Received message: {} from partition: {}", record.value(), record.partition());
    }
}
