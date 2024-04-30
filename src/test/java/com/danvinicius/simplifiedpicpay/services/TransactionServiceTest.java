package com.danvinicius.simplifiedpicpay.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.danvinicius.simplifiedpicpay.domain.user.User;
import com.danvinicius.simplifiedpicpay.domain.user.UserType;
import com.danvinicius.simplifiedpicpay.dto.TransactionDTO;
import com.danvinicius.simplifiedpicpay.exceptions.UnauthorizedTransactionException;
import com.danvinicius.simplifiedpicpay.repositories.TransactionRepository;

public class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserService userService;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    @Autowired
    private TransactionService transactionService;

    @BeforeEach
    private void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create transaction successfully when everything is OK")
    void testCreateTransactionSuccess() {
        BigDecimal oldBalance = new BigDecimal(1000);
        User sender = new User(1L, "dan", "vinic", "123456789-01", "dan@mail.com", "1234", oldBalance, UserType.COMMON);
        User receiver = new User(2L, "john", "doe", "123456789-02", "john@mail.com", "1234", oldBalance, UserType.COMMON);

        when(userService.findUserById(1L)).thenReturn(sender);
        when(userService.findUserById(2L)).thenReturn(receiver);

        when(authorizationService.authorizeTransaction(any(), any())).thenReturn(true);

        BigDecimal transactionAmount = new BigDecimal(1000);
        TransactionDTO transaction = new TransactionDTO(transactionAmount, 1L, 2L);
        transactionService.createTransaction(transaction);

        verify(transactionRepository, times(1)).save(any());
        verify(userService, times(1)).saveUser(sender);
        verify(userService, times(1)).saveUser(receiver);

        assertThat(sender.getBalance()).isEqualTo(oldBalance.subtract(transactionAmount));
        assertThat(receiver.getBalance()).isEqualTo(oldBalance.add(transactionAmount));

        String receiverNotificationMessage = "Transaction worth " + "$" + transaction.amount().divide(BigDecimal.valueOf(100)) + " received from " + sender.getFirstName() + ".";

        verify(notificationService, times(1)).sendNotification(sender, "Transaction sent sucessfully.");
        verify(notificationService, times(1)).sendNotification(receiver, receiverNotificationMessage);
    }

    @Test
    @DisplayName("Should throw exception when sender is a merchant user")
    void testCreateTransactionFailureCase1() {
        User sender = new User(1L, "dan", "vinic", "123456789-01", "dan@mail.com", "1234", new BigDecimal(1000), UserType.MERCHANT);
        User receiver = new User(2L, "john", "doe", "123456789-02", "john@mail.com", "1234", new BigDecimal(1000), UserType.COMMON);
    
        when(userService.findUserById(1L)).thenReturn(sender);
        when(userService.findUserById(2L)).thenReturn(receiver);

        when(authorizationService.authorizeTransaction(any(), any())).thenReturn(true);

        UnauthorizedTransactionException thrown = assertThrows(UnauthorizedTransactionException.class, () -> {
            TransactionDTO transaction = new TransactionDTO(new BigDecimal(1000), 1L, 2L);
            transactionService.createTransaction(transaction);
        });

        assertEquals(thrown.getMessage(), "Merchant user cannot make transactions");
    }

    @Test
    @DisplayName("Should throw exception when sender has insufficient balance")
    void testCreateTransactionFailureCase2() {
        User sender = new User(1L, "dan", "vinic", "123456789-01", "dan@mail.com", "1234", new BigDecimal(1000), UserType.COMMON);
        User receiver = new User(2L, "john", "doe", "123456789-02", "john@mail.com", "1234", new BigDecimal(1000), UserType.COMMON);
    
        when(userService.findUserById(1L)).thenReturn(sender);
        when(userService.findUserById(2L)).thenReturn(receiver);

        when(authorizationService.authorizeTransaction(any(), any())).thenReturn(true);

        UnauthorizedTransactionException thrown = assertThrows(UnauthorizedTransactionException.class, () -> {
            TransactionDTO transaction = new TransactionDTO(new BigDecimal(2000), 1L, 2L);
            transactionService.createTransaction(transaction);
        });

        assertEquals(thrown.getMessage(), "Insufficient balance");
    }

    @Test
    @DisplayName("Should throw exception when transaction is not authorized")
    void testCreateTransactionFailureCase3() {
        User sender = new User(1L, "dan", "vinic", "123456789-01", "dan@mail.com", "1234", new BigDecimal(1000), UserType.COMMON);
        User receiver = new User(2L, "john", "doe", "123456789-02", "john@mail.com", "1234", new BigDecimal(1000), UserType.COMMON);

        when(userService.findUserById(1L)).thenReturn(sender);
        when(userService.findUserById(2L)).thenReturn(receiver);

        when(authorizationService.authorizeTransaction(any(), any())).thenReturn(false);

        UnauthorizedTransactionException thrown = assertThrows(UnauthorizedTransactionException.class, () -> {
            TransactionDTO transaction = new TransactionDTO(new BigDecimal(1000), 1L, 2L);
            transactionService.createTransaction(transaction);
        });

        assertEquals(thrown.getMessage(), "Unauthorized transaction");

    }
}
