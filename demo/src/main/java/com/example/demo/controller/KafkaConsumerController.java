package com.example.demo.controller;

import com.example.demo.model.Message;
import com.example.demo.service.KafkaConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
        log.info("Receiving messages from Kafka");
        return kafkaConsumerService.getMessages();
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> userAccess() {
        return ResponseEntity.ok("Kafka-Consumer-Service: Access granted for user!");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminAccess() {
        return ResponseEntity.ok("Kafka-Consumer-Service: Access granted for admin!");
    }
}