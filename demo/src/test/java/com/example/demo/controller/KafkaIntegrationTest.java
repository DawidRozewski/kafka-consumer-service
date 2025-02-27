package com.example.demo.controller;

import com.example.demo.AbstractMongoDBTestContainer;
import com.example.demo.model.Message;
import com.example.demo.repository.MessageRepository;
import com.example.demo.service.KafkaConsumerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EmbeddedKafka(partitions = 1, topics = {"test-topic"})
class KafkaIntegrationTest extends AbstractMongoDBTestContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaConsumerService kafkaConsumerService;

    @Autowired
    private MessageRepository messageRepository;

    private static final String MESSAGES_ENDPOINT = "/consumer/messages";

    @BeforeEach
    void setUp() {
        messageRepository.deleteAll();
    }

    @Test
    void shouldReturnMessages_whenMessagesAreConsumed() throws Exception {
        // Given
        String testMessage = "Hello from Kafka!";
        kafkaTemplate.send("test-topic", testMessage).get();

        // When
        mockMvc.perform(get(MESSAGES_ENDPOINT))
                .andExpect(status().isOk());

        // Then
        Awaitility.await()
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    List<Message> messages = kafkaConsumerService.getMessages();
                    assertThat(messages).isNotEmpty();
                    assertThat(messages.get(0).getContent()).isEqualTo(testMessage);
                });
    }

    @Test
    void shouldReturnEmptyList_whenNoMessagesAreConsumed() throws Exception {
        // When
        mockMvc.perform(get(MESSAGES_ENDPOINT))
                .andExpect(status().isOk());

        // Then
        List<Message> messages = kafkaConsumerService.getMessages();
        assertThat(messages).isEmpty();
    }
}
