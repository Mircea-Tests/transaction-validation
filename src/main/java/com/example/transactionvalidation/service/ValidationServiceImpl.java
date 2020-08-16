package com.example.transactionvalidation.service;

import model.ResponseMessage;
import model.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ValidationServiceImpl implements ValidationService {
    @Override
    public ResponseMessage validateTransaction(Transaction transaction) {
        if (transaction.getPayer() == null) {
            return setError("Payer information is missing");
        }
        if (transaction.getPayee() == null) {
            return setError("Payee information is missing");
        }
        if (StringUtils.isEmpty(transaction.getDescription())) {
            return setError("Transaction description is missing");
        }
        if (transaction.getAmount() <= 0) {
            return setError("Transaction amount is missing/invalid");
        }
        if (!validateCNP(transaction.getPayer().getCnp())) {
            return setError("Invalid payer CNP");
        }
        if (!validateCNP(transaction.getPayee().getCnp())) {
            return setError("Invalid payee CNP");
        }
        if (!validateIBAN(transaction.getPayer().getIban())) {
            return setError("Invalid payer IBAN");
        }
        if (!validateIBAN(transaction.getPayee().getIban())) {
            return setError("Invalid payee IBAN");
        }
        if (!validateName(transaction.getPayer().getName())) {
            return setError("Invalid payer name");
        }
        if (!validateName(transaction.getPayee().getName())) {
            return setError("Invalid payee name");
        }
        ResponseMessage message = new ResponseMessage();
        message.setCode(200);
        message.setMessage("Transaction is valid");
        return message;
    }

    private boolean validateCNP(Long cnp) {
        return cnp != null && String.valueOf(cnp).length() == 13;
    }

    private boolean validateIBAN(String iban) {
        // Romanian IBAN check
        return !StringUtils.isEmpty(iban) && iban.length() == 24 && iban.startsWith("RO");
    }

    private boolean validateName(String name) {
        return !StringUtils.isEmpty(name);
    }
    
    private ResponseMessage setError(String message) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setCode(400);
        responseMessage.setMessage(message);
        return responseMessage;
    }

}
