package com.example.demo.controller;

import com.example.demo.service.KafkaConsumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/consumer")
public class KafkaConsumerController {

    private final KafkaConsumerService kafkaConsumerService;


    @GetMapping("/messages")
    public List<String> getReceivedMessages() {
        return kafkaConsumerService.getMessages();
    }

    @GetMapping("/status")
    public String getStatus() {
        return kafkaConsumerService.getStatus();
    }

}