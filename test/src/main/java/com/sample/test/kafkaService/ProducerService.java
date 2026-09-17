package com.sample.test.kafkaService;

import com.sample.test.DTO.UserRegisterDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {

    KafkaTemplate<String, Object> kafkaTemplate;
    public ProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishUserRegisterEvent(UserRegisterDTO userRegisterDTO) {
        kafkaTemplate.send("userRegister-event", userRegisterDTO);
    }
}
