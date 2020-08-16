package com.example.transactionvalidation.service;

import model.Account;
import model.ResponseMessage;
import model.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class ValidationServiceImplTest {

    @TestConfiguration
    static class ValidationServiceImplTestContextConfiguration {

        @Bean
        public ValidationService validationService() {
            return new ValidationServiceImpl();
        }
    }

    @Autowired
    ValidationService validationService;

    @Test
    public void validateTransaction() {
        Transaction transaction = new Transaction();

        // Test null payer
        ResponseMessage message = validationService.validateTransaction(transaction);
        assertThat(400).isEqualTo(message.getCode());
        assertThat("Payer information is missing").isEqualTo(message.getMessage());
        transaction.setPayer(new Account());

        // Test null payee
        message = validationService.validateTransaction(transaction);
        assertThat(400).isEqualTo(message.getCode());
        assertThat("Payee information is missing").isEqualTo(message.getMessage());
        transaction.setPayee(new Account());

        // Test null description
        message = validationService.validateTransaction(transaction);
        assertThat(400).isEqualTo(message.getCode());
        assertThat("Transaction description is missing").isEqualTo(message.getMessage());
        transaction.setDescription("Test description");

        // Test null amount
        message = validationService.validateTransaction(transaction);
        assertThat(400).isEqualTo(message.getCode());
        assertThat("Transaction amount is missing/invalid").isEqualTo(message.getMessage());
        transaction.setAmount(14.99);

        // Test null payer CNP
        message = validationService.validateTransaction(transaction);
        assertThat(400).isEqualTo(message.getCode());
        assertThat("Invalid payer CNP").isEqualTo(message.getMessage());
        transaction.getPayer().setCnp(12345L);

        // Test invalid payer CNP (less than 13 digits)
        message = validationService.validateTransaction(transaction);
        assertThat(400).isEqualTo(message.getCode());
        assertThat("Invalid payer CNP").isEqualTo(message.getMessage());
        transaction.getPayer().setCnp(1234567890123L);

        // Test null payee CNP
        message = validationService.validateTransaction(transaction);
        assertThat(400).isEqualTo(message.getCode());
        assertThat("Invalid payee CNP").isEqualTo(message.getMessage());
        transaction.getPayee().setCnp(1234567890124L);

        // Test null payer IBAN
        message = validationService.validateTransaction(transaction);
        assertThat(400).isEqualTo(message.getCode());
        assertThat("Invalid payer IBAN").isEqualTo(message.getMessage());
        transaction.getPayer().setIban("RO012345");

        // Test invalid payer IBAN (less than 24 characters)
        message = validationService.validateTransaction(transaction);
        assertThat(400).isEqualTo(message.getCode());
        assertThat("Invalid payer IBAN").isEqualTo(message.getMessage());
        transaction.getPayer().setIban("OM0156789012345678901234");

        // Test invalid payer IBAN (not starting with RO)
        message = validationService.validateTransaction(transaction);
        assertThat(400).isEqualTo(message.getCode());
        assertThat("Invalid payer IBAN").isEqualTo(message.getMessage());
        transaction.getPayer().setIban("RO0156789012345678901234");

        // Test null payee IBAN
        message = validationService.validateTransaction(transaction);
        assertThat(400).isEqualTo(message.getCode());
        assertThat("Invalid payee IBAN").isEqualTo(message.getMessage());
        transaction.getPayee().setIban("RO012345");

        // Test invalid payee IBAN (less than 24 characters)
        message = validationService.validateTransaction(transaction);
        assertThat(400).isEqualTo(message.getCode());
        assertThat("Invalid payee IBAN").isEqualTo(message.getMessage());
        transaction.getPayee().setIban("OM0156789012345678901234");

        // Test invalid payee IBAN (not starting with RO)
        message = validationService.validateTransaction(transaction);
        assertThat(400).isEqualTo(message.getCode());
        assertThat("Invalid payee IBAN").isEqualTo(message.getMessage());
        transaction.getPayee().setIban("RO0156789012345678901235");

        // Test null payer name
        message = validationService.validateTransaction(transaction);
        assertThat(400).isEqualTo(message.getCode());
        assertThat("Invalid payer name").isEqualTo(message.getMessage());
        transaction.getPayer().setName("Test Payer");

        // Test null payee name
        message = validationService.validateTransaction(transaction);
        assertThat(400).isEqualTo(message.getCode());
        assertThat("Invalid payee name").isEqualTo(message.getMessage());
        transaction.getPayee().setName("Test Payee");

        // Test transaction is valid
        message = validationService.validateTransaction(transaction);
        assertThat(200).isEqualTo(message.getCode());
        assertThat("Transaction is valid").isEqualTo(message.getMessage());
    }
}
