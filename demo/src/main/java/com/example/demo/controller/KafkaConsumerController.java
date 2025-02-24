package com.example.demo.controller;

import com.example.demo.model.Message;
import com.example.demo.service.KafkaConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/consumer")
@Slf4j
public class KafkaConsumerController {

    private final KafkaConsumerService kafkaConsumerService;

    @GetMapping("/messages")
    public List<Message> getReceivedMessages() {
        return kafkaConsumerService.getMessages();
    }

    @GetMapping("/user")
    public String userAccess() {
        return "Kafka-Consumer-Service: Access granted for user!";
    }

    @GetMapping("/admin")
    public String helloAdmin() {
        return "Kafka-Consumer-Service: Access granted for admin!";
    }
}