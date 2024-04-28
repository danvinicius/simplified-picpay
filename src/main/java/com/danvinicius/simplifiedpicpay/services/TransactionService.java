package com.danvinicius.simplifiedpicpay.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.danvinicius.simplifiedpicpay.domain.transaction.Transaction;
import com.danvinicius.simplifiedpicpay.domain.user.User;
import com.danvinicius.simplifiedpicpay.domain.user.UserType;
import com.danvinicius.simplifiedpicpay.dto.TransactionDTO;
import com.danvinicius.simplifiedpicpay.exceptions.UnauthorizedTransaction;
import com.danvinicius.simplifiedpicpay.exceptions.UserNotFoundException;
import com.danvinicius.simplifiedpicpay.repositories.TransactionRepository;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${picpay.authorizerService.url}")
    private String authorizerServiceUrl;

    public void createTransaction(TransactionDTO transactionRequest) throws UserNotFoundException {
        User sender = userService.findUserById(transactionRequest.senderId());
        User receiver = userService.findUserById(transactionRequest.receiverId());

        this.validateTransaction(sender, transactionRequest.amount());

        if (!this.authorizeTransaction(sender, transactionRequest.amount())) {
            throw new UnauthorizedTransaction();
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionRequest.amount());
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transactionRequest.amount()));
        receiver.setBalance(receiver.getBalance().add(transactionRequest.amount()));

        this.transactionRepository.save(transaction);
        this.userService.save(sender);
        this.userService.save(receiver);
    }

    public boolean authorizeTransaction(User sender, BigDecimal amount) {

        // mocked authorizer service
        ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity(this.authorizerServiceUrl, Map.class);
        String message = (String) authorizationResponse.getBody().get("message");
        return authorizationResponse.getStatusCode() == HttpStatus.OK && message.equalsIgnoreCase("Autorizado");
    }


    public void validateTransaction(User sender, BigDecimal amount) throws UnauthorizedTransaction {
        if (sender.getType() == UserType.MERCHANT) {
            throw new UnauthorizedTransaction("Merchant user cannot make transactions");
        }
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new UnauthorizedTransaction("Insufficient balance");
        }
    }
}
