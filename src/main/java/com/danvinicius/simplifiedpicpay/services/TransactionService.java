package com.danvinicius.simplifiedpicpay.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.danvinicius.simplifiedpicpay.domain.transaction.Transaction;
import com.danvinicius.simplifiedpicpay.domain.user.User;
import com.danvinicius.simplifiedpicpay.domain.user.UserType;
import com.danvinicius.simplifiedpicpay.dto.TransactionDTO;
import com.danvinicius.simplifiedpicpay.exceptions.NotificationException;
import com.danvinicius.simplifiedpicpay.exceptions.UnauthorizedTransactionException;
import com.danvinicius.simplifiedpicpay.exceptions.UserNotFoundException;
import com.danvinicius.simplifiedpicpay.repositories.TransactionRepository;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private NotificationService notificationService;

    public Transaction createTransaction(TransactionDTO transactionRequest) throws UserNotFoundException {
        User sender = userService.findUserById(transactionRequest.senderId());
        User receiver = userService.findUserById(transactionRequest.receiverId());

        this.validateAndAuthorizeTransaction(sender, transactionRequest);

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionRequest.amount());
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transactionRequest.amount()));
        receiver.setBalance(receiver.getBalance().add(transactionRequest.amount()));

        this.transactionRepository.save(transaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);

        this.handleTransactionNotification(transaction, sender, receiver);

        return transaction;
    }

    public void validateAndAuthorizeTransaction(User sender, TransactionDTO transaction) throws UnauthorizedTransactionException {
        this.validateTransaction(sender, transaction.amount());

        if (!this.authorizationService.authorizeTransaction(sender, transaction.amount())) {
            throw new UnauthorizedTransactionException();
        }
    }

    public void handleTransactionNotification(Transaction transaction, User sender, User receiver) throws NotificationException {
        String receiverNotificationMessage = "Transaction worth " + "$" + transaction.getAmount().divide(BigDecimal.valueOf(100)) + " received from " + sender.getFirstName() + ".";
        System.out.println(receiverNotificationMessage);
        
        this.notificationService.sendNotification(sender, "Transaction sent sucessfully.");
        this.notificationService.sendNotification(receiver, receiverNotificationMessage);
    }


    public void validateTransaction(User sender, BigDecimal amount) throws UnauthorizedTransactionException {
        if (sender.getType() == UserType.MERCHANT) {
            throw new UnauthorizedTransactionException("Merchant user cannot make transactions");
        }
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new UnauthorizedTransactionException("Insufficient balance");
        }
    }
}
