package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class KafkaConsumerService {

    private final List<String> receivedMessages = new ArrayList<>();

    @KafkaListener(topics = "test-topic", groupId = "consumer-group-1")
    public void consumeMessage(ConsumerRecord<String, String> record) {
        log.info("Received message: {} from partition: {}", record.value(), record.partition());
        receivedMessages.add(record.value());
    }

    public List<String> getMessages() {
        return new ArrayList<>(receivedMessages);
    }

    public String getStatus() {
        return "Consumer Service is running!";
    }
}
