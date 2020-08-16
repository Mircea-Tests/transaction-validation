package com.example.transactionvalidation.controller;

import model.ResponseMessage;
import com.example.transactionvalidation.service.ValidationService;
import model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionValidationController {

    @Autowired
    ValidationService validationService;

    @Autowired
    KafkaTemplate<String, Transaction> kafkaTemplate;

    @Value(value = "${kafka.topic}")
    private String topic;

    @PostMapping(path = "/validate")
    public ResponseMessage validate(@RequestBody Transaction transaction) {
        ResponseMessage result = validationService.validateTransaction(transaction);
        if (result.getCode() == 200) {
            kafkaTemplate.send(topic, transaction);
        }
        return result;
    }
}
