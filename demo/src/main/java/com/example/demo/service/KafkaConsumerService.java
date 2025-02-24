package com.example.demo.service;

import com.example.demo.model.Message;
import com.example.demo.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final MessageRepository messageRepository;

    @KafkaListener(topics = "test-topic", groupId = "consumer-group-1")
    public void consumeMessage(ConsumerRecord<String, String> record) {
        log.info("Received message: {} from partition: {}", record.value(), record.partition());

        Message message = new Message();
        message.setContent(record.value());
        messageRepository.save(message);
    }

    public List<Message> getMessages() {
        return messageRepository.findAll();
    }

}
