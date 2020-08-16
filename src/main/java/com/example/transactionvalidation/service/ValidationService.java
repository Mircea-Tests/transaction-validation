package com.example.transactionvalidation.service;

import model.Account;
import model.ResponseMessage;
import model.Transaction;

public interface ValidationService {

    ResponseMessage validateTransaction(Transaction transaction);
}
