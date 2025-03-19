package com.example.demo.controller;

import com.example.demo.AbstractMongoDBTestContainer;
import com.example.demo.model.Message;
import com.example.demo.repository.MessageRepository;
import com.example.demo.service.KafkaConsumerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class KafkaConsumerControllerTest extends AbstractMongoDBTestContainer {
    private final String USER_ACCESS_ENDPOINT = "/consumer/user";
    private final String ADMIN_ACCESS_ENDPOINT = "/consumer/admin";

    @Autowired
    private KafkaConsumerService kafkaConsumerService;

//    @Autowired
//    private MessageRepository messageRepository;
//
//    @BeforeEach
//    public void setUp() {
//        messageRepository.insert(new Message("content-message"));
//        messageRepository.insert(new Message("content-message-1"));
//    }

    @Test
    @WithMockUser(roles = "USER")
    void givenUserWithUserRole_whenAccessingUserEndpoint_thenShouldGrantAccess() throws Exception {
        mockMvc.perform(get(USER_ACCESS_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().string("Kafka-Consumer-Service: Access granted for user!"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void givenUserWithAdminRole_whenAccessingAdminEndpoint_thenShouldGrantAccess() throws Exception {
        mockMvc.perform(get(ADMIN_ACCESS_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().string("Kafka-Consumer-Service: Access granted for admin!"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void givenUserWithAdminRole_whenAccessingUserEndpoint_thenShouldDenyAccess() throws Exception {
        mockMvc.perform(get(USER_ACCESS_ENDPOINT))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(roles = "USER")
    void givenUserWithUserRole_whenAccessingAdminEndpoint_thenShouldDenyAccess() throws Exception {
        mockMvc.perform(get(ADMIN_ACCESS_ENDPOINT))
                .andExpect(status().isForbidden());
    }

    @Test
    void givenAnonymousUser_whenAccessingAdminEndpoint_thenShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get(ADMIN_ACCESS_ENDPOINT))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void givenAnonymousUser_whenAccessingUserEndpoint_thenShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get(USER_ACCESS_ENDPOINT))
                .andExpect(status().isUnauthorized());
    }

}