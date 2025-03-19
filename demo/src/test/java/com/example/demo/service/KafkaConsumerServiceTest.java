package com.example.demo.service;

import com.example.demo.model.Message;
import com.example.demo.repository.MessageRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerServiceTest {
    private final String KAFKA_MESSAGE = "Message";

    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;

    @Mock
    private MessageRepository messageRepository;

    @Test
    void shouldSaveMessageWhenConsumed() {
        // Given
        var record = new ConsumerRecord<>("test-topic", 0, 0L, "key", KAFKA_MESSAGE);

        // When
        kafkaConsumerService.consumeMessage(record);

        // Then
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void shouldReturnAllMessages() {
        // Given
        String secondKafkaMessage = "kafka message 2";
        given(messageRepository.findAll()).willReturn(
                Arrays.asList(new Message(KAFKA_MESSAGE), new Message(secondKafkaMessage))
        );

        // When
        List<Message> messages = kafkaConsumerService.getMessages();

        // Then
        assertNotNull(messages);
        assertEquals(2, messages.size());
        assertEquals(KAFKA_MESSAGE, messages.getFirst().getContent());
        assertEquals(secondKafkaMessage, messages.get(1).getContent());
    }
}